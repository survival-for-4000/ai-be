package com.ll.demo03.imageTask.domain;

import com.ll.demo03.global.domain.Status;
import com.ll.demo03.global.domain.ResolutionProfile;
import com.ll.demo03.imageTask.controller.request.ImageQueueRequest;
import com.ll.demo03.global.port.Network;
import com.ll.demo03.imageTask.controller.request.ImageTaskRequest;
import com.ll.demo03.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageTask {

    private final Long id;
    private final String prompt;
    private final String lora;
    private final String runpodId;
    private final Status status;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Member creator;
    private final ResolutionProfile resolutionProfile;

    @Builder
    public ImageTask(Long id, String prompt, String lora, String runpodId, Status status, LocalDateTime createdAt, LocalDateTime modifiedAt, Member creator, ResolutionProfile resolutionProfile) {
        this.id = id;
        this.prompt = prompt;
        this.lora = lora;
        this.runpodId = runpodId;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.creator = creator;
        this.resolutionProfile = resolutionProfile;
    }

    public static ImageTask from(Member creator, ImageQueueRequest queueRequest) {
        ResolutionProfile profile = ResolutionProfile.fromDimensions(queueRequest.getWidth(), queueRequest.getHeight());
        return ImageTask.builder()
                .prompt(queueRequest.getPrompt())
                .lora(queueRequest.getLora())
                .resolutionProfile(profile)
                .creator(creator)
                .build();
    }

    public ImageTask updateStatus(Status status, String runpodId){
        return ImageTask.builder()
                .id(id)
                .prompt(prompt)
                .lora(lora)
                .runpodId(runpodId)
                .status(status)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .creator(creator)
                .resolutionProfile(resolutionProfile)
                .build();
    }

    public static ImageQueueRequest toImageQueueRequest(ImageTaskRequest imageTaskRequest, String lora, String newPrompt, Member creator) {
        return new ImageQueueRequest(lora, newPrompt, imageTaskRequest.getResolutionProfile().getWidth(), imageTaskRequest.getResolutionProfile().getHeight(), creator.getId());
    }
}
