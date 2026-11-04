package br.com.navdata.flowcash.dto.response;

import br.com.navdata.flowcash.entity.AccountEntity;
import java.math.BigDecimal;

public record AccountResponse(
    String id,
    String name,
    String type,
    String bank,
    BigDecimal initialBalance,
    BigDecimal currentBalance,
    String color
) {
    public static AccountResponse fromEntity(AccountEntity entity, BigDecimal currentBalance) {
        return new AccountResponse(
            entity.getId(),
            entity.getName(),
            entity.getType(),
            entity.getBank(),
            entity.getInitialBalance(),
            currentBalance,
            entity.getColor()
        );
    }
}
