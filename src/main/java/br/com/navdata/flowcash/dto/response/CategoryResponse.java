package br.com.navdata.flowcash.dto.response;

import br.com.navdata.flowcash.entity.CategoryEntity;
import br.com.navdata.flowcash.entity.enums.TransactionType;

public record CategoryResponse(
    String id,
    String name,
    TransactionType type,
    String icon,
    String color
) {
    public static CategoryResponse fromEntity(CategoryEntity entity) {
        return new CategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getType(),
            entity.getIcon(),
            entity.getColor()
        );
    }
}
