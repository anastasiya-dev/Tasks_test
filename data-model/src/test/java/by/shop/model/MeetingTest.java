package by.shop.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Ignore
public class MeetingTest extends ModelTest {

    Session session;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        session = super.factory.openSession();
    }

    @Test
    public void create() {
        //Given
        Employee manager = createEmployee("E0");
        Employee employee1 = createEmployee("E1");
        Employee employee2 = createEmployee("E2");
        Employee employee3 = createEmployee("E3");

        Meeting meeting1 = createMeeting("M1");
        Meeting meeting2 = createMeeting("M2");
        Meeting meeting3 = createMeeting("M3");

        MeetingUtil.add(manager, Set.of(meeting1, meeting2, meeting3));
        MeetingUtil.add(employee1, Set.of(meeting1, meeting2));
        MeetingUtil.add(employee2, Set.of(meeting1, meeting3));
        MeetingUtil.add(employee3, Set.of(meeting2, meeting3));

        //When
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(manager);
            session.saveOrUpdate(employee1);
            session.saveOrUpdate(employee2);
            session.saveOrUpdate(employee3);
            session.saveOrUpdate(meeting1);
            session.saveOrUpdate(meeting2);
            session.saveOrUpdate(meeting3);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        //Then
        String managerId = manager.getId();
        Employee savedManager = session.get(Employee.class, managerId);
        assertEquals(3, savedManager.getMeetings().size());
    }

    private Meeting createMeeting(String m1) {
        Meeting meeting = new Meeting();
        meeting.setSubject(m1);
        Date startTime = new Date();
        meeting.setStartTime(startTime);
        meeting.setEndTime(new Date(startTime.getTime() + 1000 * 60 * 30));
        return meeting;
    }

    private Employee createEmployee(String e1) {
        Employee employee = new Employee();
        employee.setFirstName(e1);
        employee.setLastName(e1);
        employee.setBirthDate(new Date(new Date().getTime() - 25 * 12 * 30 * 24 * 60 * 60 * 60 * 1000));
        return employee;
    }

    @After
    public void tearDown() throws Exception {
        if (session.isOpen())
            session.close();
        super.tearDown();
    }
}