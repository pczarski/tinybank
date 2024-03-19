package org.mock.tinybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.account.api.dto.TransactionDto;
import org.mock.tinybank.account.domain.model.AccountAmountRequest;
import org.mock.tinybank.account.domain.model.TransactionType;
import org.mock.tinybank.account.domain.model.TransferRequest;
import org.mock.tinybank.user.domain.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigInteger;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TinyBankApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getNonExistentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/i_dont_exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        UserRecord requestUser = new UserRecord("bankUser");
        UserRecord postResponseUser = post("/users", requestUser, UserRecord.class, HttpStatus.OK.value());
        assertThat(postResponseUser).isEqualTo(requestUser);
        UserRecord getResponseUser = get("/users/" + requestUser.username(), UserRecord.class, HttpStatus.OK.value());
        assertThat(getResponseUser).isEqualTo(requestUser);
    }

    @Test
    void deactivateUser() throws Exception {
        givenUser("existing_user");
        UserRecord userToDelete = givenUser("to_delete");

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/to_delete"))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        UserRecord deletedUser = objectMapper.readValue(response, UserRecord.class);
        assertThat(deletedUser)
                .isEqualTo(userToDelete);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/" + deletedUser.username()))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositAndWithdrawal_getsCorrectBalance() throws Exception {
        UserRecord givenUser = givenUser("depositor");
        AccountAmountRequest deposit = new AccountAmountRequest(givenUser.username(), BigInteger.TEN);
        TransactionDto depositResponse = post("/accounts/deposit", deposit, TransactionDto.class, HttpStatus.OK.value());

        TransactionDto expectedDeposit = TransactionDto.builder().netUnits(BigInteger.TEN).transactionType(TransactionType.DEPOSIT).build();
        assertThat(depositResponse).isEqualTo(expectedDeposit);

        post("/accounts/deposit", deposit, AccountAmountRequest.class, HttpStatus.OK.value());
        BigInteger balance = get("/accounts/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(balance).isEqualTo(20);

        AccountAmountRequest withdrawal = new AccountAmountRequest(givenUser.username(), BigInteger.valueOf(5));
        TransactionDto withdrawalResponse = post("/accounts/withdraw", withdrawal, TransactionDto.class, HttpStatus.OK.value());
        TransactionDto expectedWithdrawal = TransactionDto.builder().netUnits(BigInteger.valueOf(-5)).transactionType(TransactionType.WITHDRAWAL).build();
        assertThat(withdrawalResponse).isEqualTo(expectedWithdrawal);

        BigInteger newBalance = get("/accounts/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(newBalance).isEqualTo(15);
    }

    @Test
    void insufficientBalance_returns400() throws Exception {
        UserRecord givenUser = givenUser("someGuy");
        AccountAmountRequest deposit = new AccountAmountRequest(givenUser.username(), BigInteger.TEN);
        post("/accounts/deposit", deposit, AccountAmountRequest.class, HttpStatus.OK.value());

        AccountAmountRequest withdrawal = new AccountAmountRequest(givenUser.username(), BigInteger.valueOf(11));
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdraw")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawal)))
                .andExpect(status().isBadRequest());
    }

    private static List<TransactionDto> getExpectedTransactionsForFirstUserTransactionHistoryGetTest() {
        return asList(
                TransactionDto.builder().transactionType(TransactionType.DEPOSIT).netUnits(BigInteger.TEN).build(),
                TransactionDto.builder().transactionType(TransactionType.DEPOSIT).netUnits(BigInteger.valueOf(11)).build(),
                TransactionDto.builder().transactionType(TransactionType.WITHDRAWAL).netUnits(BigInteger.valueOf(-2)).build(),
                TransactionDto.builder().transactionType(TransactionType.TRANSFER).netUnits(BigInteger.valueOf(-4)).receiver("otherUser").build(),
                TransactionDto.builder().transactionType(TransactionType.TRANSFER).netUnits(BigInteger.valueOf(3)).sender("otherUser").build()
        );
    }

    private static List<TransactionDto> getExpectedTransactionsForOtherUserTransactionHistoryGetTest() {
        return asList(
                TransactionDto.builder().transactionType(TransactionType.DEPOSIT).netUnits(BigInteger.TEN).build(),
                TransactionDto.builder().transactionType(TransactionType.TRANSFER).netUnits(BigInteger.valueOf(4)).sender("firstUser").build(),
                TransactionDto.builder().transactionType(TransactionType.TRANSFER).netUnits(BigInteger.valueOf(-3)).receiver("firstUser").build()
        );
    }

    @Test
    void transferMoneyToAnotherUser() throws Exception {
        UserRecord sender = givenUser("sender");
        UserRecord receiver = givenUser("receiver");
        post("/accounts/deposit", new AccountAmountRequest(sender.username(), BigInteger.TEN), AccountAmountRequest.class, HttpStatus.OK.value());

        TransferRequest transferRequest = new TransferRequest(sender.username(), receiver.username(), BigInteger.valueOf(4));
        TransactionDto transferResponse = post("/accounts/transfer", transferRequest, TransactionDto.class, HttpStatus.OK.value());
        TransactionDto expectedResponse = TransactionDto
                .builder()
                .receiver(receiver.username())
                .transactionType(TransactionType.TRANSFER)
                .netUnits(BigInteger.valueOf(-4))
                .build();
        assertThat(transferResponse).isEqualTo(expectedResponse);

        BigInteger senderBalance = get("/accounts/balances/sender", BigInteger.class, HttpStatus.OK.value());
        BigInteger receiverBalance = get("/accounts/balances/receiver", BigInteger.class, HttpStatus.OK.value());
        assertThat(senderBalance).isEqualTo(6);
        assertThat(receiverBalance).isEqualTo(4);
    }

    @Test
    void getEmptyTransactions() throws Exception {
        givenUser("firstUser");
    }

    @Test
    void getTransactionsForAUser() throws Exception {
        UserRecord firstUser = givenUser("firstUser");
        UserRecord otherUser = givenUser("otherUser");

        post("/accounts/deposit", new AccountAmountRequest(otherUser.username(), BigInteger.TEN), AccountAmountRequest.class, HttpStatus.OK.value());
        post("/accounts/deposit", new AccountAmountRequest(firstUser.username(), BigInteger.TEN), AccountAmountRequest.class, HttpStatus.OK.value());
        post("/accounts/deposit", new AccountAmountRequest(firstUser.username(), BigInteger.valueOf(11)), AccountAmountRequest.class, HttpStatus.OK.value());
        post("/accounts/withdraw", new AccountAmountRequest(firstUser.username(), BigInteger.valueOf(2)), AccountAmountRequest.class, HttpStatus.OK.value());
        post("/accounts/transfer", new TransferRequest(firstUser.username(), otherUser.username(), BigInteger.valueOf(4)), AccountAmountRequest.class, HttpStatus.OK.value());
        post("/accounts/transfer", new TransferRequest(otherUser.username(), firstUser.username(), BigInteger.valueOf(3)), AccountAmountRequest.class, HttpStatus.OK.value());

        String firstUserTransactionHistoryJson = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + firstUser.username() + "/transactions"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        String otherUserTransactionHistoryJson = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + otherUser.username() + "/transactions"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        List<TransactionDto> expectedFirstUserTransactions = getExpectedTransactionsForFirstUserTransactionHistoryGetTest();
        String expectedFirstUserJson = new ObjectMapper().writeValueAsString(expectedFirstUserTransactions);

        List<TransactionDto> expectedOtherUserTransactions = getExpectedTransactionsForOtherUserTransactionHistoryGetTest();
        String expectedOtherUserTransactionsJson = new ObjectMapper().writeValueAsString(expectedOtherUserTransactions);

        assertThat(firstUserTransactionHistoryJson).isEqualTo(expectedFirstUserJson);
        assertThat(otherUserTransactionHistoryJson).isEqualTo(expectedOtherUserTransactionsJson);
    }

    private <T> T post(String path, Object request, Class<T> resposneClass, int expectedCode) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedCode))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, resposneClass);
    }

    private <T> T get(String path, Class<T> resposneClass, int expectedCode) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(status().is(expectedCode))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, resposneClass);
    }

    private UserRecord givenUser(String username) throws Exception {
        UserRecord requestUser = new UserRecord(username);
        return post("/users", requestUser, UserRecord.class, HttpStatus.OK.value());
    }
}
