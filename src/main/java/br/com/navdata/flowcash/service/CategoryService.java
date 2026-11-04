package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.request.CategoryRequest;
import br.com.navdata.flowcash.dto.response.CategoryResponse;
import br.com.navdata.flowcash.entity.CategoryEntity;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import br.com.navdata.flowcash.exception.ResourceNotFoundException;
import br.com.navdata.flowcash.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> listByUser(String userId) {
        return categoryRepository.findByUserIdOrderByNameAsc(userId)
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional
    public CategoryResponse create(String userId, CategoryRequest req) {
        CategoryEntity entity = CategoryEntity.builder()
                .userId(userId)
                .name(req.name())
                .type(req.type())
                .icon(req.icon())
                .color(req.color())
                .build();
        return CategoryResponse.fromEntity(categoryRepository.save(entity));
    }

    @Transactional
    public void delete(String userId, String id) {
        CategoryEntity entity = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        categoryRepository.delete(entity);
    }

    @Transactional
    public void seedDefaultCategories(String userId) {
        if (!categoryRepository.findByUserIdOrderByNameAsc(userId).isEmpty()) {
            return;
        }

        List<CategoryEntity> defaults = List.of(
            CategoryEntity.builder().userId(userId).name("Alimentação").type(TransactionType.EXPENSE).icon("Utensils").color("#ef4444").build(),
            CategoryEntity.builder().userId(userId).name("Moradia").type(TransactionType.EXPENSE).icon("Home").color("#f97316").build(),
            CategoryEntity.builder().userId(userId).name("Transporte").type(TransactionType.EXPENSE).icon("Car").color("#f59e0b").build(),
            CategoryEntity.builder().userId(userId).name("Saúde").type(TransactionType.EXPENSE).icon("Activity").color("#10b981").build(),
            CategoryEntity.builder().userId(userId).name("Lazer").type(TransactionType.EXPENSE).icon("Smile").color("#06b6d4").build(),
            CategoryEntity.builder().userId(userId).name("Educação").type(TransactionType.EXPENSE).icon("GraduationCap").color("#6366f1").build(),
            CategoryEntity.builder().userId(userId).name("Salário").type(TransactionType.INCOME).icon("Briefcase").color("#10b981").build(),
            CategoryEntity.builder().userId(userId).name("Investimentos").type(TransactionType.INCOME).icon("TrendingUp").color("#3b82f6").build(),
            CategoryEntity.builder().userId(userId).name("Freelance").type(TransactionType.INCOME).icon("Laptop").color("#8b5cf6").build(),
            CategoryEntity.builder().userId(userId).name("Outros").type(TransactionType.INCOME).icon("PlusCircle").color("#64748b").build()
        );

        categoryRepository.saveAll(defaults);
    }
}
