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

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


@Service
@Slf4j
@RequiredArgsConstructor
public class VideoTaskService {

    private final VideoTaskRepository videoTaskRepository;
    private final MemberRepository memberRepository;

    @Value("${piapi.api.key}")
    private String piApiKey;

    public String createVideo(String imageUrl, String prompt, String webhook) {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = (HttpResponse<String>) Unirest.post("https://api.piapi.ai/api/v1/task")
                    .header("x-api-key", piApiKey)
                    .header("Content-Type", "application/json")
                    .body("{\n" +
                            "    \"model\": \"kling\",\n" +
                            "    \"task_type\": \"video_generation\",\n" +
                            "    \"input\": {\n" +
                            "        \"prompt\": \"" + prompt + "\",\n" +
                            "        \"negative_prompt\": \"\",\n" +
                            "        \"cfg_scale\": 0.5,\n" +
                            "        \"duration\": 5,\n" +
                            "        \"aspect_ratio\": \"1:1\",\n" +
                            "        \"image_url\": \"" + imageUrl + "\",\n" +
                            "        \"camera_control\": {\n" +
                            "            \"type\": \"simple\",\n" +
                            "            \"config\": {\n" +
                            "                \"horizontal\": 0,\n" +
                            "                \"vertical\": 0,\n" +
                            "                \"pan\": -10,\n" +
                            "                \"tilt\": 0,\n" +
                            "                \"roll\": 0,\n" +
                            "                \"zoom\": 0\n" +
                            "            }\n" +
                            "        },\n" +
                            "        \"mode\": \"std\"\n" +
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
