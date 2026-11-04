package br.com.navdata.flowcash.repository;

import br.com.navdata.flowcash.entity.ClosedMonthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClosedMonthRepository extends JpaRepository<ClosedMonthEntity, String> {
    List<ClosedMonthEntity> findByUserIdOrderByMonthDesc(String userId);
    boolean existsByUserIdAndMonth(String userId, LocalDateTime month);
    boolean existsByUserIdAndMonthGreaterThanEqual(String userId, LocalDateTime month);
    void deleteByUserIdAndMonth(String userId, LocalDateTime month);
}
