package org.mock.tinybank.account.api;

import org.mock.tinybank.account.api.dto.TransactionDto;
import org.mock.tinybank.account.domain.model.*;

// todo test
class Mapper {
    static TransactionDto toDto(AccountTransaction accountTransaction) {
        TransactionDto.TransactionDtoBuilder builder = TransactionDto.builder();
        builder.netUnits(accountTransaction.netUnits());
        if (accountTransaction instanceof Deposit) {
            return builder.transactionType(TransactionType.DEPOSIT).build();
        }
        if (accountTransaction instanceof Withdrawal) {
            return builder.transactionType(TransactionType.WITHDRAWAL).build();
        }
        builder.transactionType(TransactionType.TRANSFER);
        if (accountTransaction instanceof IncomingTransfer) {
            return builder.sender(((IncomingTransfer) accountTransaction).sender()).build();
        }
        if (accountTransaction instanceof OutgoingTransfer) {
            return builder.receiver(((OutgoingTransfer) accountTransaction).receiver()).build();
        }
        throw new UnsupportedAccountTransactionException();
    }
}
