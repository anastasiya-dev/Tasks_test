package by.it.academy.service;

import by.it.academy.ApplicationConfiguration;
import by.it.academy.pojo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveUser() {

    }

    @Test
    public void findUserById() {
    }

    @Test
    public void findUserByName() {
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void updatePassword() {
    }

    @Test
    public void delete() {
    }
}