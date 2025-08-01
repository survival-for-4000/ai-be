package com.ll.demo03.notification.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BatchNotificationMessage {
    private String type;
    private long memberId;
    private List<String> imageUrl;
    private Long taskId;
    private String prompt;
}
