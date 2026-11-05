package br.com.navdata.flowcash.entity.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTypeTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void acceptsLowercaseAndUppercaseValues() throws Exception {
        assertEquals(TransactionType.EXPENSE, objectMapper.readValue("\"expense\"", TransactionType.class));
        assertEquals(TransactionType.EXPENSE, objectMapper.readValue("\"EXPENSE\"", TransactionType.class));
        assertEquals(TransactionType.INCOME, objectMapper.readValue("\"income\"", TransactionType.class));
        assertEquals(TransactionType.INCOME, objectMapper.readValue("\"INCOME\"", TransactionType.class));
    }

    @Test
    void serializesValuesInFrontendFormat() throws Exception {
        assertEquals("\"expense\"", objectMapper.writeValueAsString(TransactionType.EXPENSE));
        assertEquals("\"income\"", objectMapper.writeValueAsString(TransactionType.INCOME));
    }
}
