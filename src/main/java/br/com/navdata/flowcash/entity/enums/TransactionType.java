package br.com.navdata.flowcash.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum TransactionType {
    INCOME,
    EXPENSE;

    @JsonCreator
    public static TransactionType fromJson(String value) {
        if (value == null) {
            return null;
        }
        return TransactionType.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    @JsonValue
    public String toJson() {
        return name().toLowerCase(Locale.ROOT);
    }
}
