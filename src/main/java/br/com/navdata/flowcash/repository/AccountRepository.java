package br.com.navdata.flowcash.repository;

import br.com.navdata.flowcash.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByUserId(String userId);
    Optional<AccountEntity> findByIdAndUserId(String id, String userId);
}
