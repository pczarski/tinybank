package org.mock.tinybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserEndpointTest {

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

    @Test
    void createUser() throws Exception {
        User requestUser = new User("bankUser");
        User expectedUser = post("/user", requestUser, User.class, HttpStatus.OK.value());
        assertThat(expectedUser).isEqualTo(new User("bankUser"));
    }
}
