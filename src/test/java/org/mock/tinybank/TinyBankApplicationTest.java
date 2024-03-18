package org.mock.tinybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.domain.*;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.UnitTransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mock.tinybank.domain.TransactionType.*;
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
        AccountAmountDto deposit = new AccountAmountDto(givenUser.username(), BigInteger.TEN);
        AccountAmountDto depositResponse = post("/accounts//deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());

        assertThat(depositResponse).isEqualTo(deposit);

        post("/accounts/deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());
        BigInteger balance = get("/accounts/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(balance).isEqualTo(20);

        AccountAmountDto withdrawal = new AccountAmountDto(givenUser.username(), BigInteger.valueOf(5));
        AccountAmountDto withdrawalResponse = post("/accounts/withdraw", withdrawal, AccountAmountDto.class, HttpStatus.OK.value());
        assertThat(withdrawalResponse).isEqualTo(withdrawal);

        BigInteger newBalance = get("/accounts/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(newBalance).isEqualTo(15);
    }

    @Test
    void insufficientBalance_returns400() throws Exception {
        UserRecord givenUser = givenUser("someGuy");
        AccountAmountDto deposit = new AccountAmountDto(givenUser.username(), BigInteger.TEN);
        post("/accounts/deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());

        AccountAmountDto withdrawal = new AccountAmountDto(givenUser.username(), BigInteger.valueOf(11));
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/withdraw")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferMoneyToAnotherUser() throws Exception {
        UserRecord sender = givenUser("sender");
        UserRecord receiver = givenUser("receiver");
        post("/accounts/deposit", new AccountAmountDto(sender.username(), BigInteger.TEN), AccountAmountDto.class, HttpStatus.OK.value());

        UnitTransferDto transferRequest = new UnitTransferDto(sender.username(), receiver.username(), BigInteger.valueOf(4));
        UnitTransferDto transferResponse = post("/accounts/transfer", transferRequest, UnitTransferDto.class, HttpStatus.OK.value());
        assertThat(transferResponse).isEqualTo(transferRequest);

        BigInteger senderBalance = get("/accounts/balances/sender", BigInteger.class, HttpStatus.OK.value());
        BigInteger receiverBalance = get("/accounts/balances/receiver", BigInteger.class, HttpStatus.OK.value());
        assertThat(senderBalance).isEqualTo(6);
        assertThat(receiverBalance).isEqualTo(4);
    }

    private static List<AccountTransaction> getExpectedTransactionsForFirstUserTransactionHistoryGetTest() {
        List<AccountTransaction> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new AccountTransactionWithdrawalOrDeposit(BigInteger.TEN, DEPOSIT));
        expectedTransactions.add(new AccountTransactionWithdrawalOrDeposit(BigInteger.valueOf(11), DEPOSIT));
        expectedTransactions.add(new AccountTransactionWithdrawalOrDeposit(BigInteger.valueOf(-2), WITHDRAWAL));
        expectedTransactions.add(new AccountTransactionOutgoingTransfer(BigInteger.valueOf(-4), TRANSFER, "otherUser"));
        expectedTransactions.add(new AccountTransactionIncomingTransfer(BigInteger.valueOf(3), TRANSFER, "otherUser"));
        return expectedTransactions;
    }

    private static List<AccountTransaction> getExpectedTransactionsForOtherUserTransactionHistoryGetTest() {
        List<AccountTransaction> expectedTransactions = new ArrayList<>();
        expectedTransactions.add(new AccountTransactionWithdrawalOrDeposit(BigInteger.TEN, DEPOSIT));
        expectedTransactions.add(new AccountTransactionIncomingTransfer(BigInteger.valueOf(4), TRANSFER, "firstUser"));
        expectedTransactions.add(new AccountTransactionOutgoingTransfer(BigInteger.valueOf(-3), TRANSFER, "firstUser"));
        return expectedTransactions;
    }

    @Test
    void getEmptyTransactions() throws Exception {
        givenUser("firstUser");
    }

    @Test
    void getTransactionsForAUser() throws Exception {
        UserRecord firstUser = givenUser("firstUser");
        UserRecord otherUser = givenUser("otherUser");

        post("/accounts/deposit", new AccountAmountDto(otherUser.username(), BigInteger.TEN), AccountAmountDto.class, HttpStatus.OK.value());
        post("/accounts/deposit", new AccountAmountDto(firstUser.username(), BigInteger.TEN), AccountAmountDto.class, HttpStatus.OK.value());
        post("/accounts/deposit", new AccountAmountDto(firstUser.username(), BigInteger.valueOf(11)), AccountAmountDto.class, HttpStatus.OK.value());
        post("/accounts/withdraw", new AccountAmountDto(firstUser.username(), BigInteger.valueOf(2)), AccountAmountDto.class, HttpStatus.OK.value());
        post("/accounts/transfer", new UnitTransferDto(firstUser.username(), otherUser.username(), BigInteger.valueOf(4)), AccountAmountDto.class, HttpStatus.OK.value());
        post("/accounts/transfer", new UnitTransferDto(otherUser.username(), firstUser.username(), BigInteger.valueOf(3)), AccountAmountDto.class, HttpStatus.OK.value());

        String firstUserTransactionHistoryJson = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + firstUser.username() + "/transactions"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        String otherUserTransactionHistoryJson = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + otherUser.username() + "/transactions"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        List<AccountTransaction> expectedFirstUserTransactions = getExpectedTransactionsForFirstUserTransactionHistoryGetTest();
        String expectedFirstUserJson = new ObjectMapper().writeValueAsString(expectedFirstUserTransactions);

        List<AccountTransaction> expectedOtherUserTransactions = getExpectedTransactionsForOtherUserTransactionHistoryGetTest();
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
