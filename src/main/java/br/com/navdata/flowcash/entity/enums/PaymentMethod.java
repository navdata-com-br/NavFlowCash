package br.com.navdata.flowcash.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum PaymentMethod {
    CASH,
    PIX,
    CREDIT_CARD,
    DEBIT_CARD,
    TRANSFER,
    PREVIOUS_BALANCE;

    @JsonCreator
    public static PaymentMethod fromJson(String value) {
        if (value == null) {
            return null;
        }
        return PaymentMethod.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }
}
