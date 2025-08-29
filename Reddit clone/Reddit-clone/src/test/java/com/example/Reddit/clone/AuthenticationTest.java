package com.example.Reddit.clone;

import com.example.Reddit.clone.Auth.AuthenticationRequest;
import com.example.Reddit.clone.Auth.AuthenticationResponse;
import com.example.Reddit.clone.Auth.RegisterRequest;
import com.example.Reddit.clone.Config.JwtService;
import com.example.Reddit.clone.Entity.User;
import com.example.Reddit.clone.Repository.UserRepository;
import com.example.Reddit.clone.Services.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    RegisterRequest registerRequest = RegisterRequest.builder()
                                .firstname("hans")
                                .lastname("larsen")
                                .email("hans@email.com")
                                .password("password1")
                                .username("username1")
                                .build();

    AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                                .username(registerRequest.getUsername())
                                .password(registerRequest.getPassword())
                                .build();
    @Test
    public void user_can_register_and_is_saved_to_database() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", nullValue()))
                .andDo(print());


        assertTrue(userRepository.existsByUsername(registerRequest.getUsername()));
        assertFalse(userRepository.existsByUsername("random_username"));
    }



    private User getUser(String username) {
        return userRepository.findByUsername(username) .orElseThrow(() -> ExceptionUtils.noUserWithThatName(username));
    }

    @Test
    public void user_can_authorize_after_registration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(userRepository.existsByUsername(registerRequest.getUsername()));


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", notNullValue()))
                .andDo(print());

    }

    //bruk jwtservice til å returnere bruker
    @Test
    public void user_can_have_token_which_points_back_to_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertTrue(userRepository.existsByUsername(registerRequest.getUsername()));


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        AuthenticationResponse response = objectMapper.readValue(content, AuthenticationResponse.class);

        String username = jwtService.extractUsername( response.getToken() );
        assertEquals(authenticationRequest.getUsername(), username);




    }








}
