package br.com.navdata.flowcash.service;

import br.com.navdata.flowcash.dto.request.TransactionCreateRequest;
import br.com.navdata.flowcash.dto.request.TransferRequest;
import br.com.navdata.flowcash.dto.response.TransactionResponse;
import br.com.navdata.flowcash.entity.TransactionEntity;
import br.com.navdata.flowcash.entity.enums.PaymentMethod;
import br.com.navdata.flowcash.entity.enums.TransactionType;
import br.com.navdata.flowcash.exception.BusinessException;
import br.com.navdata.flowcash.exception.ResourceNotFoundException;
import br.com.navdata.flowcash.repository.ClosedMonthRepository;
import br.com.navdata.flowcash.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ClosedMonthRepository closedMonthRepository;

    @Transactional(readOnly = true)
    public List<TransactionResponse> listByUserAndMonth(String userId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);
        return transactionRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    @Transactional
    public List<TransactionResponse> create(String userId, TransactionCreateRequest req) {
        validateMonthNotClosed(userId, req.date());

        List<TransactionEntity> toSave = new ArrayList<>();
        boolean isPaid = Boolean.TRUE.equals(req.isPaid());
        LocalDateTime paidAt = isPaid ? LocalDateTime.now() : null;

        if (req.installments() != null && req.installments() > 1) {
            String groupId = UUID.randomUUID().toString();
            for (int i = 1; i <= req.installments(); i++) {
                toSave.add(TransactionEntity.builder()
                        .userId(userId)
                        .description(req.description() + " (" + i + "/" + req.installments() + ")")
                        .amount(req.amount())
                        .date(req.date().plusMonths(i - 1))
                        .type(req.type())
                        .paymentMethod(req.paymentMethod())
                        .categoryId(req.categoryId())
                        .accountId(req.accountId())
                        .isPaid(i == 1 && isPaid)
                        .paidAt(i == 1 && isPaid ? paidAt : null)
                        .installmentGroup(groupId)
                        .installmentNumber(i)
                        .totalInstallments(req.installments())
                        .isFixed(false)
                        .build());
            }
        } else if (Boolean.TRUE.equals(req.isFixed())) {
            String groupId = UUID.randomUUID().toString();
            for (int i = 0; i < 12; i++) {
                toSave.add(TransactionEntity.builder()
                        .userId(userId)
                        .description(req.description())
                        .amount(req.amount())
                        .date(req.date().plusMonths(i))
                        .type(req.type())
                        .paymentMethod(req.paymentMethod())
                        .categoryId(req.categoryId())
                        .accountId(req.accountId())
                        .isPaid(i == 0 && isPaid)
                        .paidAt(i == 0 && isPaid ? paidAt : null)
                        .installmentGroup(groupId)
                        .isFixed(true)
                        .build());
            }
        } else {
            toSave.add(TransactionEntity.builder()
                    .userId(userId)
                    .description(req.description())
                    .amount(req.amount())
                    .date(req.date())
                    .type(req.type())
                    .paymentMethod(req.paymentMethod())
                    .categoryId(req.categoryId())
                    .accountId(req.accountId())
                    .isPaid(isPaid)
                    .paidAt(paidAt)
                    .isFixed(false)
                    .build());
        }

        return transactionRepository.saveAll(toSave).stream().map(TransactionResponse::fromEntity).toList();
    }

    @Transactional
    public void createTransfer(String userId, TransferRequest req) {
        validateMonthNotClosed(userId, req.date());
        String transferId = UUID.randomUUID().toString();

        TransactionEntity debit = TransactionEntity.builder()
                .userId(userId)
                .description("Transferência entre contas")
                .amount(req.amount())
                .date(req.date())
                .type(TransactionType.EXPENSE)
                .paymentMethod(PaymentMethod.TRANSFER)
                .accountId(req.sourceAccountId())
                .isTransfer(true)
                .transferId(transferId)
                .destinationAccountId(req.destinationAccountId())
                .isPaid(true)
                .paidAt(LocalDateTime.now())
                .build();

        TransactionEntity credit = TransactionEntity.builder()
                .userId(userId)
                .description("Transferência recebida")
                .amount(req.amount())
                .date(req.date())
                .type(TransactionType.INCOME)
                .paymentMethod(PaymentMethod.TRANSFER)
                .accountId(req.destinationAccountId())
                .isTransfer(true)
                .transferId(transferId)
                .destinationAccountId(req.sourceAccountId())
                .isPaid(true)
                .paidAt(LocalDateTime.now())
                .build();

        transactionRepository.saveAll(List.of(debit, credit));
    }

    @Transactional
    public TransactionResponse togglePaid(String userId, String id) {
        TransactionEntity t = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        boolean newPaidStatus = !Boolean.TRUE.equals(t.getIsPaid());
        t.setIsPaid(newPaidStatus);
        t.setPaidAt(newPaidStatus ? LocalDateTime.now() : null);

        return TransactionResponse.fromEntity(transactionRepository.save(t));
    }

    @Transactional
    public void delete(String userId, String id) {
        TransactionEntity t = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        if (Boolean.TRUE.equals(t.getIsTransfer()) && t.getTransferId() != null) {
            transactionRepository.deleteByTransferId(t.getTransferId());
        } else {
            transactionRepository.delete(t);
        }
    }

    private void validateMonthNotClosed(String userId, LocalDateTime date) {
        LocalDateTime startOfMonth = date.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        if (closedMonthRepository.existsByUserIdAndMonthGreaterThanEqual(userId, startOfMonth)) {
            throw new BusinessException("Não é possível alterar lançamentos em um mês fechado!");
        }
    }
}
