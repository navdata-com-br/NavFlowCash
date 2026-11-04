package br.com.navdata.flowcash.repository;

import br.com.navdata.flowcash.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByUserIdOrderByDateAsc(String userId);
    List<TransactionEntity> findByUserIdAndIsPaidTrue(String userId);
    List<TransactionEntity> findByUserIdAndDateBetweenOrderByDateAsc(String userId, LocalDateTime start, LocalDateTime end);
    Optional<TransactionEntity> findByIdAndUserId(String id, String userId);
    List<TransactionEntity> findByInstallmentGroup(String installmentGroup);
    void deleteByTransferId(String transferId);
}
