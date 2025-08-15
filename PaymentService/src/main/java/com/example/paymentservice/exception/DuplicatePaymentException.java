package com.example.paymentservice.exception;


import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatePaymentException extends RuntimeException {
    private final Payment existing;
    public DuplicatePaymentException(Payment existing) {
        super("Payment already processed for this order");
        this.existing = existing;
    }
    public Payment getExisting() { return existing; }




}
