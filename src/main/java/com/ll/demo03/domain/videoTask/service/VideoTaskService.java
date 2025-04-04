package com.ll.demo03.domain.videoTask.service;

import com.ll.demo03.domain.image.entity.Image;
import com.ll.demo03.domain.image.repository.ImageRepository;
import com.ll.demo03.domain.member.entity.Member;
import com.ll.demo03.domain.member.repository.MemberRepository;
import com.ll.demo03.domain.sse.repository.SseEmitterRepository;
import com.ll.demo03.domain.task.dto.AckInfo;
import com.ll.demo03.domain.upscaledTask.dto.UpscaleImageUrlResponse;
import com.ll.demo03.domain.videoTask.dto.VideoWebhookEvent;
import com.ll.demo03.domain.videoTask.entity.VideoTask;
import com.ll.demo03.domain.videoTask.repository.VideoTaskRepository;
import com.ll.demo03.global.error.ErrorCode;
import com.ll.demo03.global.exception.CustomException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Service
@Slf4j
@RequiredArgsConstructor
public class VideoTaskService {

    private final VideoTaskRepository videoTaskRepository;
    private final ImageRepository imageRepository;
    private final SseEmitterRepository sseEmitterRepository;
    private final MemberRepository memberRepository;

    @Value("${piapi.api.key}")
    private String piApiKey;

    public String createVideo( String imageUrl, String prompt,  String webhook) {
        try {

            Unirest.setTimeouts(0, 0);
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = (HttpResponse<String>) Unirest.post("https://api.piapi.ai/api/v1/task")
                    .header("x-api-key", piApiKey)
                    .header("Content-Type", "application/json")
                    .body("{\n" +
                            "    \"model\": \"hailuo\",\n" +
                            "    \"task_type\": \"video_generation\",\n" +
                            "    \"input\": {\n" +
                            "        \"prompt\": \"" + prompt + "\",\n" +
                            "        \"model\": \"i2v-01\",\n" +
                            "        \"image_url\": \"" + imageUrl + "\",\n" +
                            "        \"expand_prompt\": true\n" +
                            "    },\n" +
                            "    \"config\": {\n" +
                            "        \"service_mode\": \"public\",\n" +
                            "        \"webhook_config\": {\n" +
                            "            \"endpoint\": \"" + webhook + "\",\n" +
                            "            \"secret\": \"123456\"\n" +
                            "        }\n" +
                            "    }\n" +
                            "}")
                    .asString();

            return response.getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveVideoTask(String taskId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + memberId));

        VideoTask videoTask = new VideoTask();
        videoTask.setTaskId(taskId);
        videoTask.setMember(member);

        videoTaskRepository.save(videoTask);
        log.info("VideoTask 저장 완료: taskId={}, memberId={}", taskId, memberId);
    }

    public void processWebhookEvent(VideoWebhookEvent event) {
        log.info("동영상 웹훅 이벤트 수신: {}", event.getData().getTaskId());

        try {
            if (!"completed".equals(event.getData().getStatus())) {
                log.info("Task not yet completed, status: {}", event.getData().getStatus());
                return;
            }

            String taskId = event.getData().getTaskId();
            String videoUrl = event.getData().getOutput().getDownloadUrl();

            if (videoUrl == null || videoUrl.isEmpty()) {
                log.info("동영상 URL이 아직 생성되지 않았습니다: {}", taskId);
                return;
            }

            // 1️⃣ DB 저장 (비동기 처리)
            saveVideoToDatabase(taskId, videoUrl);

            // 2️⃣ SSE 전송 (비동기 처리)
            notifyClient(taskId, videoUrl);

        } catch (Exception e) {
            log.error("동영상 웹훅 이벤트 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Async
    public void saveVideoToDatabase(String taskId, String videoUrl) {
        try {
            VideoTask videoTask = videoTaskRepository.findByTaskId(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Video task not found"));

            Image image = Image.ofVideo(videoUrl, videoTask);
            image.setImgIndex(0);
            imageRepository.save(image);
            log.info("✅ DB 저장 완료: taskId={}, videoUrl={}", taskId, videoUrl);
        } catch (Exception e) {
            log.error("DB 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Async
    public void notifyClient(String taskId, String videoUrl) {
        try {
            VideoTask videoTask = videoTaskRepository.findByTaskId(taskId)
                    .orElseThrow(() -> new EntityNotFoundException("Video task not found"));

            Long memberId = videoTask.getMember().getId();
            String memberIdStr = String.valueOf(memberId);

            SseEmitter emitter = sseEmitterRepository.get(memberIdStr);

            if (emitter != null) {
                UpscaleImageUrlResponse response = new UpscaleImageUrlResponse(videoUrl, taskId);

                try {
                    emitter.send(SseEmitter.event().name("result").data(response));
                    log.info("✅ 클라이언트 SSE 전송 완료: {}, memberId: {}", taskId, memberId);

                    emitter.complete();
                    sseEmitterRepository.removeTaskMapping(taskId);
                } catch (Exception e) {
                    log.error("SSE 이벤트 전송 중 오류: {}", e.getMessage(), e);
                    sseEmitterRepository.remove(memberIdStr);
                }
            } else {
                log.warn("❗ SSE 연결 없음: memberId={}, taskId={}", memberId, taskId);
            }
        } catch (Exception e) {
            log.error("SSE 알림 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
