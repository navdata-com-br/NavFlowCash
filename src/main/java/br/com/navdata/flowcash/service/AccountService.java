package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.request.AccountRequest;
import br.com.navdata.flowcash.dto.response.AccountResponse;
import br.com.navdata.flowcash.entity.AccountEntity;
import br.com.navdata.flowcash.entity.TransactionEntity;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import br.com.navdata.flowcash.exception.ResourceNotFoundException;
import br.com.navdata.flowcash.repository.AccountRepository;
import br.com.navdata.flowcash.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<AccountResponse> listByUser(String userId) {
        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        List<TransactionEntity> paidTransactions = transactionRepository.findByUserIdAndIsPaidTrue(userId);

        return accounts.stream().map(acc -> {
            BigDecimal income = paidTransactions.stream()
                .filter(t -> acc.getId().equals(t.getAccountId()) && t.getType() == TransactionType.INCOME)
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal expense = paidTransactions.stream()
                .filter(t -> acc.getId().equals(t.getAccountId()) && t.getType() == TransactionType.EXPENSE)
                .map(TransactionEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal current = acc.getInitialBalance().add(income).subtract(expense);
            return AccountResponse.fromEntity(acc, current);
        }).toList();
    }

    @Transactional
    public AccountResponse create(String userId, AccountRequest req) {
        AccountEntity entity = AccountEntity.builder()
                .userId(userId)
                .name(req.name())
                .type(req.type())
                .bank(req.bank())
                .initialBalance(req.initialBalance())
                .color(req.color())
                .build();
        AccountEntity saved = accountRepository.save(entity);
        return AccountResponse.fromEntity(saved, saved.getInitialBalance());
    }

    @Transactional
    public void delete(String userId, String id) {
        AccountEntity acc = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        accountRepository.delete(acc);
    }
}
