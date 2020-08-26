package by.it.academy.results;

import by.it.academy.pojo.Address;
import by.it.academy.pojo.Employee;
import by.it.academy.pojo.EmployeeDetails;
import by.it.academy.support.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Task_22 {
    public static void main(String[] args) throws Exception {
        Employee person = new Employee();
        EmployeeDetails details = new EmployeeDetails();

        person.setName("Anastasiya");
        person.setSurname("Kalach");
        person.setAge(29);
        person.setAddress(new Address("Khoruzhey", "Minsk", "220005"));
        person.setSalary((double) 1800);
        person.setCompany("EY");
        person.setEmployeeDetails(details);

        details.setHobbies("Swimming");
        details.setPensionPlan("Non-defined");
        details.setEmployee(person);

        SessionFactory factory = HibernateUtil.start();
        Session session = factory.openSession();
        Transaction transaction = null;

        EmployeeDetails ed = null;
        try {
            transaction = session.beginTransaction();
            session.save(person);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        try {
            transaction = session.beginTransaction();
            ed = session.get(Employee.class,Long.valueOf(1)).getEmployeeDetails();
            System.out.println(ed.getHobbies());
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        HibernateUtil.finish();
    }
}
