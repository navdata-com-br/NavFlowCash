package br.com.navdata.flowcash.entity.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMethodTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void acceptsLowercaseAndUppercaseValues() throws Exception {
        assertEquals(PaymentMethod.PIX, objectMapper.readValue("\"pix\"", PaymentMethod.class));
        assertEquals(PaymentMethod.PIX, objectMapper.readValue("\"PIX\"", PaymentMethod.class));
        assertEquals(PaymentMethod.CREDIT_CARD, objectMapper.readValue("\"credit_card\"", PaymentMethod.class));
        assertEquals(PaymentMethod.CREDIT_CARD, objectMapper.readValue("\"CREDIT_CARD\"", PaymentMethod.class));
    }

    @Test
    void serializesValuesInFrontendFormat() throws Exception {
        assertEquals("\"pix\"", objectMapper.writeValueAsString(PaymentMethod.PIX));
        assertEquals("\"credit_card\"", objectMapper.writeValueAsString(PaymentMethod.CREDIT_CARD));
    }
}
