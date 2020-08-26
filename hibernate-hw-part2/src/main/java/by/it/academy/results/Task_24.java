package by.it.academy.results;

import by.it.academy.pojo.Employee;
import by.it.academy.pojo.Meeting;
import by.it.academy.pojo.MeetingUtil;
import by.it.academy.support.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Set;

public class Task_24 {
    public static void main(String[] args) throws Exception {
        Employee person1 = new Employee();
        person1.setName("Person1");
        Employee person2 = new Employee();
        person2.setName("Person2");
        Employee person3 = new Employee();
        person3.setName("Person3");

        Meeting meeting1 = new Meeting();
        meeting1.setSubject("meeting1");
        Meeting meeting2 = new Meeting();
        meeting2.setSubject("meeting2");
        Meeting meeting3 = new Meeting();
        meeting3.setSubject("meeting3");

        MeetingUtil.add(person1, Set.of(meeting1, meeting2));
        MeetingUtil.add(person2, Set.of(meeting2, meeting3));
        MeetingUtil.add(person3, Set.of(meeting1, meeting2, meeting3));

        SessionFactory factory = HibernateUtil.start();
        Session session = factory.openSession();
        Transaction transaction = null;

        Meeting meetingR1 = null;
        Meeting meetingR2 = null;
        Meeting meetingR3 = null;
        Employee employeeR1 = null;
        Employee employeeR2 = null;
        Employee employeeR3 = null;

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(meeting1);
            session.saveOrUpdate(meeting2);
            session.saveOrUpdate(meeting3);

            session.saveOrUpdate(person1);
            session.saveOrUpdate(person2);
            session.saveOrUpdate(person3);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        try {
            transaction = session.beginTransaction();
            meetingR1 = session.get(Meeting.class, meeting1.getId());
            meetingR2 = session.get(Meeting.class, meeting2.getId());
            meetingR3 = session.get(Meeting.class, meeting3.getId());

            employeeR1 = session.get(Employee.class, person1.getId());
            employeeR2 = session.get(Employee.class, person2.getId());
            employeeR3 = session.get(Employee.class, person3.getId());

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        printEmployees(meetingR1);
        printEmployees(meetingR2);
        printEmployees(meetingR3);
        printMeetings(employeeR1);
        printMeetings(employeeR2);
        printMeetings(employeeR3);

        HibernateUtil.finish();
    }

    public static void printEmployees(Meeting meeting) {
        System.out.println("Meeting: " + meeting.getSubject());
        Set<Employee> employeeSet = meeting.getEmployees();
        for (Employee employee : employeeSet) {
            System.out.println(employee.getName());
        }
    }

    public static void printMeetings(Employee employee) {
        System.out.println("Employee: " + employee.getName());
        Set<Meeting> meetingSet = employee.getMeetings();
        for (Meeting meeting : meetingSet) {
            System.out.println(meeting.getSubject());
        }
    }
}
