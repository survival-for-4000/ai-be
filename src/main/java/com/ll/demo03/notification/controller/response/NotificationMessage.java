package com.ll.demo03.notification.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationMessage {
    private String type;
    private long memberId;
    private String imageUrl;
    private Long taskId;
    private String prompt;
}
