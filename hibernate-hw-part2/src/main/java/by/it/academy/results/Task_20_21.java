package by.it.academy.results;

import by.it.academy.pojo.Address;
import by.it.academy.pojo.Employee;
import by.it.academy.pojo.Person;
import by.it.academy.pojo.Student;
import by.it.academy.support.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Task_20_21 {
    public static void main(String[] args) throws Exception {
        Person person = new Person();

        person.setName("Anastasiya");
        person.setSurname("Kalach");
        person.setAge(29);
        person.setAddress(new Address("Khoruzhey", "Minsk", "220005"));

        Student student = new Student();
        student.setFaculty("FIR");
        student.setName("Ivan");

        Employee employee = new Employee();
        employee.setSalary((double) 1800);
        employee.setName("Olga");

        SessionFactory factory = HibernateUtil.start();
        Session session = factory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(person);
            session.save(student);
            session.save(employee);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        HibernateUtil.finish();
    }
}
