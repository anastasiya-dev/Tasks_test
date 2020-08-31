package myproject.hibernate;

import myproject.ApplicationConfigurationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfigurationTest.class)
public class TransferObjectTest implements ApplicationContextAware {
    //very important to implement this interface - otherwise the context is null
    @Autowired
    TransferObject transferObject;

    @Autowired
    BaseDao daoEngine;

    ApplicationContext context;

    @Test
    public void create() {
        String idCreated = daoEngine.create(transferObject);
        assertNotNull(idCreated);
        assertEquals(transferObject.getId(), idCreated);
    }

    @Test
    public void read() {
        daoEngine.create(transferObject);
        TransferObject trObjRead = (TransferObject) daoEngine.read(TransferObject.class, transferObject.getId());
        assertNotNull(trObjRead);
        assertEquals(transferObject.getId(), trObjRead.getId());
        assertEquals(transferObject.getToCard(), trObjRead.getToCard());
        assertEquals(transferObject.getFromCard(), trObjRead.getFromCard());
        assertEquals(transferObject.getAmount(), trObjRead.getAmount());
    }

    @Test
    public void update() {
        String idCreated = daoEngine.create(transferObject);
        daoEngine.update(transferObject.getToCard());
        TransferObject trObjRead2 = (TransferObject) daoEngine.read(TransferObject.class, transferObject.getId());
        assertEquals("thiefCard", trObjRead2.getToCard());
    }

    @Test(expected = NoSuchElementException.class)
    public void delete() {
        TransferObject trObjForDel = (TransferObject) daoEngine.read(TransferObject.class, transferObject.getId());
        daoEngine.delete(transferObject);
        TransferObject trObjDeleted = (TransferObject) daoEngine.read(TransferObject.class, trObjForDel.getId());
    }

    @BeforeTestClass
    public void setUp() throws Exception {
        BaseDao daoEngine = (BaseDao) context.getBean("daoEngine");
    }

    @Before
    public void setUp2() throws Exception {
        TransferObject transferObject = (TransferObject) context.getBean("transferObject");
        transferObject.setId(String.valueOf(1));
        transferObject.setFromCard("fromCardTest" + 1);
        transferObject.setToCard("toCardTest" + 1);
        transferObject.setAmount(100 * 1);
    }

    @After
    public void tearDown() throws Exception {
        transferObject = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}