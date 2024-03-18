package org.mock.tinybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.AccountAmountDto;
import org.mock.tinybank.dto.UnitTransferDto;
import org.mock.tinybank.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TinyBankApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    private UserDto givenUser(String username) throws Exception {
        UserDto requestUser = new UserDto(username);
        return post("/user", requestUser, UserDto.class, HttpStatus.OK.value());
    }

    @Test
    void getNonExistentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/i_dont_exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        UserDto requestUser = new UserDto("bankUser");
        UserDto postResponseUser = post("/user", requestUser, UserDto.class, HttpStatus.OK.value());
        assertThat(postResponseUser).isEqualTo(requestUser);
        UserDto getResponseUser = get("/user/"+requestUser.userName(), UserDto.class, HttpStatus.OK.value());
        assertThat(getResponseUser).isEqualTo(requestUser);
    }

    @Test
    void depositAndWithdrawal_getsCorrectBalance() throws Exception {
        UserDto givenUser = givenUser("depositor");
        AccountAmountDto deposit = new AccountAmountDto(givenUser.userName(), BigInteger.TEN);
        AccountAmountDto depositResponse = post("/deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());

        assertThat(depositResponse).isEqualTo(deposit);

        post("/deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());
        BigInteger balance = get("/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(balance).isEqualTo(20);

        AccountAmountDto withdrawal = new AccountAmountDto(givenUser.userName(), BigInteger.valueOf(5));
        AccountAmountDto withdrawalResponse = post("/withdraw", withdrawal, AccountAmountDto.class, HttpStatus.OK.value());
        assertThat(withdrawalResponse).isEqualTo(withdrawal);

        BigInteger newBalance = get("/balances/depositor", BigInteger.class, HttpStatus.OK.value());
        assertThat(newBalance).isEqualTo(15);
    }

    @Test
    void insufficientBalance_returns400() throws Exception {
        UserDto givenUser = givenUser("someGuy");
        AccountAmountDto deposit = new AccountAmountDto(givenUser.userName(), BigInteger.TEN);
        post("/deposit", deposit, AccountAmountDto.class, HttpStatus.OK.value());

        AccountAmountDto withdrawal = new AccountAmountDto(givenUser.userName(), BigInteger.valueOf(11));
        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(withdrawal)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transferMoneyToAnotherUser() throws Exception {
        // Given
        UserDto sender = givenUser("sender");
        UserDto receiver = givenUser("receiver");
        post("/deposit", new AccountAmountDto(sender.userName(), BigInteger.TEN), AccountAmountDto.class, HttpStatus.OK.value());

        UnitTransferDto transferRequest = new UnitTransferDto(sender.userName(), receiver.userName(), BigInteger.valueOf(4));
        UnitTransferDto transferResponse = post("/transfer", transferRequest, UnitTransferDto.class, HttpStatus.OK.value());
        assertThat(transferResponse).isEqualTo(transferRequest);

        BigInteger senderBalance = get("/balances/sender", BigInteger.class, HttpStatus.OK.value());
        BigInteger receiverBalance = get("/balances/receiver", BigInteger.class, HttpStatus.OK.value());
        assertThat(senderBalance).isEqualTo(6);
        assertThat(receiverBalance).isEqualTo(4);
    }
}
