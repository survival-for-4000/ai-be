package com.ll.demo03.payment.exception;

public class SyncPaymentException extends RuntimeException {
    public SyncPaymentException() {
        super("결제 동기화 실패");
    }
}
