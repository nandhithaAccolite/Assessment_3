package com.accolite.Payment.dto;

public class OfflinePaymentCodeResponseDTO {
    private String code;

    // Constructors, getters, setters

    public OfflinePaymentCodeResponseDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
