package by.it.academy;

import by.it.academy.service.NotificationCommandExecutor;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
public class NotificationCommandExecutorTest {

    //to set test context
    @Autowired
    NotificationCommandExecutor executor;

    @org.junit.Test
    public void execute() {
        assertNotNull(executor);
    }
}