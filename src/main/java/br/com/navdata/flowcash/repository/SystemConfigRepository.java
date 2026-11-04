package br.com.navdata.flowcash.repository;

import br.com.navdata.flowcash.entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, String> {
}
