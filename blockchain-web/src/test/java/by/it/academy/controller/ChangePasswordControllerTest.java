package by.it.academy.controller;

import by.it.academy.ApplicationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class ChangePasswordControllerTest {

    @Autowired
    ChangePasswordController changePasswordController;
    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void changePasswordView() throws Exception {

//        mockMvc.perform(post("/{userId}/change-password", "testUserId"))
//
//
//        RequestBuilder request = post("/{userId}/change-password", "testUserId")
//                .param("username", rob.getUsername())
//                .param("password", rob.getPassword())
//                .param("firstName",rob.getFirstName())
//                .param("lastName",rob.getLastName())
//                .param("email", rob.getEmail())
//                .with(csrf());
//
//
//        );

    }

    @Test
    public void updatePassword() {


    }
}