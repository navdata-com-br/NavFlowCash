package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.entity.ClosedMonthEntity;
import br.com.navdata.flowcash.repository.ClosedMonthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClosedMonthService {

    private final ClosedMonthRepository closedMonthRepository;

    @Transactional(readOnly = true)
    public List<ClosedMonthEntity> listByUser(String userId) {
        return closedMonthRepository.findByUserIdOrderByMonthDesc(userId);
    }

    @Transactional
    public void closeMonth(String userId, int year, int month) {
        LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0, 0);
        if (!closedMonthRepository.existsByUserIdAndMonth(userId, monthStart)) {
            closedMonthRepository.save(ClosedMonthEntity.builder()
                    .userId(userId)
                    .month(monthStart)
                    .build());
        }
    }

    @Transactional
    public void reopenMonth(String userId, int year, int month) {
        LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0, 0);
        closedMonthRepository.deleteByUserIdAndMonth(userId, monthStart);
    }
}
