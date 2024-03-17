package org.mock.tinybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mock.tinybank.dto.UserDto;
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
    private <T> T get(String path, Class<T> resposneClass, int expectedCode) throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(status().is(expectedCode))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(response, resposneClass);
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

}
