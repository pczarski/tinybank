package org.mock.tinybank.api;

import org.mock.tinybank.api.dto.TransactionDto;
import org.mock.tinybank.domain.*;

import static org.mock.tinybank.domain.TransactionType.*;

// todo test
class Mapper {
    static TransactionDto toDto(AccountTransaction accountTransaction) {
        TransactionDto.TransactionDtoBuilder builder = TransactionDto.builder();
        builder.netUnits(accountTransaction.netUnits());
        if (accountTransaction instanceof Deposit) {
            return builder.transactionType(DEPOSIT).build();
        }
        if (accountTransaction instanceof Withdrawal) {
            return builder.transactionType(WITHDRAWAL).build();
        }
        builder.transactionType(TRANSFER);
        if (accountTransaction instanceof IncomingTransfer) {
            return builder.sender(((IncomingTransfer) accountTransaction).sender()).build();
        }
        if (accountTransaction instanceof OutgoingTransfer) {
            return builder.receiver(((OutgoingTransfer) accountTransaction).receiver()).build();
        }
        throw new UnsupportedAccountTransactionException();
    }
}
