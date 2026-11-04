package br.com.navdata.flowcash.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public record DashboardSummaryResponse(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance,
    BigDecimal pendingIncome,
    BigDecimal pendingExpense,
    BigDecimal consolidatedAccountBalance,
    Map<String, BigDecimal> expensesByCategory
) {}
