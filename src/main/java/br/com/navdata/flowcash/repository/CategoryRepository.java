package br.com.navdata.flowcash.repository;

import br.com.navdata.flowcash.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findByUserIdOrderByNameAsc(String userId);
    Optional<CategoryEntity> findByIdAndUserId(String id, String userId);
}
