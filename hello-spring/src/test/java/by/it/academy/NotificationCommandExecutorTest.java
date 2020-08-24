package by.it.academy;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class NotificationCommandExecutorTest {

    //to set test context
    @Autowired
    NotificationCommandExecutor executor;

    @org.junit.Test
    public void execute() {
        assertNotNull(executor);
    }
}