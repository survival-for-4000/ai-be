package com.ll.demo03.domain.taskProcessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.demo03.domain.member.entity.Member;
import com.ll.demo03.domain.member.repository.MemberRepository;
import com.ll.demo03.domain.notification.repository.NotificationRepository;
import com.ll.demo03.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@RequiredArgsConstructor
public class TaskProcessingService {

    private final MemberRepository memberRepository;

    private final StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

//    public void sendImageSseEvent(Long memberId, ImageTask imageTask) {
//        try {
//
//               Notification notification = new Notification();
//                notification.setType(NotificationType.IMAGE); // 예시
//                notification.setMessage("이미지 생성 중입니다.");
//                notification.setStatus(NotificationStatus.PENDING);
//                notification.setRead(false);
//
//            Map<String, Object> payloadMap = new HashMap<>();
//                payloadMap.put("requestId", imageTask.getId());
//                payloadMap.put("imageUrls", new String[]{});
//                payloadMap.put("prompt", imageTask.getRawPrompt());
//                payloadMap.put("ratio", imageTask.getRatio());
//                payloadMap.put("taskId", imageTask.getTaskId());
//                payloadMap.put("progress", 0);
//
//            try {
//                String memberIdStr = String.valueOf(memberId);
//                String payloadJson = objectMapper.writeValueAsString(payloadMap);
//                notification.setPayload(payloadJson);
//                notificationRepository.save(notification);
//
//                String redisKey = "notification:image:" + memberIdStr;
//                String notificationJson = objectMapper.writeValueAsString(notification);
//                redisTemplate.opsForValue().set(redisKey, notificationJson);
//
//                notificationService.publishNotificationToOtherServers(memberIdStr, notificationJson);
//
//            } catch (JsonProcessingException e) {
//                log.error("payload 직렬화 실패", e);
//            }
//        } catch (Exception e) {
//            log.error("알림 저장 중 에러 발생 : {}", e.getMessage(), e);
//        }
//    }

//    public void sendVideoSseEvent(Long memberId, VideoTask task) {
//        try {
//            Notification notification = new Notification();
//            notification.setType(NotificationType.VIDEO); // 예시
//            notification.setMessage("영상 생성 중입니다.");
//            notification.setStatus(NotificationStatus.PENDING);
//            notification.setRead(false);
//
//            Map<String, Object> payloadMap = new HashMap<>();
//            payloadMap.put("requestId", task.getId());
//            payloadMap.put("imageUrls", new String[]{});
//            payloadMap.put("prompt", task.getPrompt());
//            payloadMap.put("taskId", task.getTaskId());
//            payloadMap.put("progress", 0);
//
//            try {
//                String memberIdStr = String.valueOf(memberId);
//                String payloadJson = objectMapper.writeValueAsString(payloadMap);
//                notification.setPayload(payloadJson);
//                notificationRepository.save(notification);
//
//                String redisKey = "notification:video:" + memberIdStr;
//                String notificationJson = objectMapper.writeValueAsString(notification);
//                redisTemplate.opsForValue().set(redisKey, notificationJson);
//
//                notificationService.publishNotificationToOtherServers(memberIdStr, notificationJson);
//
//            } catch (JsonProcessingException e) {
//                log.error("payload 직렬화 실패", e);
//            }
//        } catch (Exception e) {
//            log.error("알림 저장 중 에러 발생 : {}", e.getMessage(), e);
//        }
//    }

//    public void sendUpscaleSseEvent(Long memberId, UpscaleTask task) {
//        try {
//            Notification notification = new Notification();
//            notification.setType(NotificationType.UPSCALE);
//            notification.setMessage("업스케일 중입니다.");
//            notification.setStatus(NotificationStatus.PENDING);
//            notification.setRead(false);
//
//            Map<String, Object> payloadMap = new HashMap<>();
//            payloadMap.put("requestId", task.getId());
//            payloadMap.put("imageUrls", new String[]{});
//            payloadMap.put("prompt", task.getImageTask().getRawPrompt());
//            payloadMap.put("ratio", task.getImageTask().getRatio());
//            payloadMap.put("taskId", task.getNewTaskId());
//            payloadMap.put("progress", 0);
//
//            try {
//                String memberIdStr = String.valueOf(memberId);
//                String payloadJson = objectMapper.writeValueAsString(payloadMap);
//                notification.setPayload(payloadJson);
//                notificationRepository.save(notification);
//
//                String redisKey = "notification:upscale:" + memberIdStr;
//                String notificationJson = objectMapper.writeValueAsString(notification);
//                redisTemplate.opsForValue().set(redisKey, notificationJson);
//
//                notificationService.publishNotificationToOtherServers(memberIdStr, notificationJson);
//
//            } catch (JsonProcessingException e) {
//                log.error("payload 직렬화 실패", e);
//            }
//        } catch (Exception e) {
//            log.error("알림 저장 중 에러 발생 : {}", e.getMessage(), e);
//        }
//    }


    public String extractTaskIdFromResponse(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("data").path("task_id").asText();
        } catch (Exception e) {
            log.error("응답에서 task_id 추출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Task ID 추출 실패", e);
        }
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("멤버를 찾지 못했습니다.: " + memberId));
    }
}
