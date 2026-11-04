package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.response.DashboardSummaryResponse;
import br.com.navdata.flowcash.entity.TransactionEntity;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import br.com.navdata.flowcash.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getMonthlySummary(String userId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        List<TransactionEntity> transactions = transactionRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal pendingIncome = BigDecimal.ZERO;
        BigDecimal pendingExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> byCategory = new HashMap<>();

        for (TransactionEntity t : transactions) {
            boolean isPaid = Boolean.TRUE.equals(t.getIsPaid());
            if (t.getType() == TransactionType.INCOME) {
                if (isPaid) totalIncome = totalIncome.add(t.getAmount());
                else pendingIncome = pendingIncome.add(t.getAmount());
            } else if (t.getType() == TransactionType.EXPENSE) {
                if (isPaid) {
                    totalExpense = totalExpense.add(t.getAmount());
                    String cat = t.getCategoryId() != null ? t.getCategoryId() : "Outros";
                    byCategory.put(cat, byCategory.getOrDefault(cat, BigDecimal.ZERO).add(t.getAmount()));
                } else {
                    pendingExpense = pendingExpense.add(t.getAmount());
                }
            }
        }

        BigDecimal balance = totalIncome.subtract(totalExpense);
        BigDecimal consolidatedBalance = accountService.listByUser(userId).stream()
                .map(acc -> acc.currentBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummaryResponse(
            totalIncome,
            totalExpense,
            balance,
            pendingIncome,
            pendingExpense,
            consolidatedBalance,
            byCategory
        );
    }
}
