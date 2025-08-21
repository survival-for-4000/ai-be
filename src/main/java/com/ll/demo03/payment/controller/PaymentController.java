package com.ll.demo03.payment.controller;

import com.ll.demo03.payment.config.PortOneSecretProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PortOneSecretProperties secret;

    public PaymentController(PortOneSecretProperties secret) {
        this.secret = secret;
        // PaymentClient, WebhookVerifier 초기화 등
    }

    @GetMapping("/item")
    public String getItem() {
        // 샘플 아이템 리턴
        return "shoes";
    }

    @PostMapping("/complete")
    public String completePayment(@RequestBody CompletePaymentRequest request) {
        // 결제 검증 로직
        return "결제 처리됨: " + request.getPaymentId();
    }

    // 내부 클래스 예시
    public static class CompletePaymentRequest {
        private String paymentId;

        public String getPaymentId() {
            return paymentId;
        }
        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }
    }
}