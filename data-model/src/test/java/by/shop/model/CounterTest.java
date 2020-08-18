package by.shop.model;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class CounterTest extends ModelTest {

    private static final Logger log = LoggerFactory.getLogger(Counter.class);

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test

    public void testCounter() throws InterruptedException {
        //Given
        int initialCount = 0;
        int maxCount = 1000;
        createInitialCount(initialCount);


        //When
        for (int i = 0; i < maxCount; i++) {
            new Thread(this::incrementCounter).start();
        }

        //Then
        Thread.sleep(10_000);
        Counter savedCounter = read();
        assertEquals(maxCount, savedCounter.getCount());
    }

    private Counter read() {
        Transaction tx = null;
        Counter counter = null;
        try (final Session session = factory.openSession()) {
            tx = session.beginTransaction();
            counter = session.get(Counter.class, 1);
            tx.commit();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) tx.rollback();
        }
        return counter;
    }

    private void incrementCounter() {
        Transaction tx = null;
        try (final Session session = factory.openSession()) {

            tx = session.beginTransaction();
            Counter counter = session.get(Counter.class, 1, LockMode.PESSIMISTIC_WRITE);
            long newCount = counter.getCount() + 1;
            counter.setCount(newCount);
            session.saveOrUpdate(counter);
            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            if (tx != null) tx.rollback();
        }
    }

    private void createInitialCount(int i) {
        Transaction tx = null;
        try (final Session session = factory.openSession()) {
            tx = session.beginTransaction();
            Counter counter = new Counter();
            counter.setId(1);
            counter.setCount(i);
            session.save(counter);
            tx.commit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (tx != null) tx.rollback();
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}