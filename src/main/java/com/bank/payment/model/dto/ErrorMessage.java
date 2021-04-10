package com.bank.payment.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private int code;
    private String message;
}
