package com.ll.demo03.domain.videoTask.service;

import com.ll.demo03.domain.member.entity.Member;
import com.ll.demo03.domain.member.repository.MemberRepository;
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
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class VideoTaskService {

    private final VideoTaskRepository videoTaskRepository;
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

    public VideoTask saveVideoTask(String taskId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found: " + memberId));

        VideoTask videoTask = new VideoTask();
        videoTask.setTaskId(taskId);
        videoTask.setMember(member);

        videoTaskRepository.save(videoTask);
        log.info("VideoTask 저장 완료: taskId={}, memberId={}", taskId, memberId);
        return videoTask;
    }
}
