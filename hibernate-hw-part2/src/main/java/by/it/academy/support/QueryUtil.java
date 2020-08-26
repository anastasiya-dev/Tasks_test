package by.it.academy.support;

import by.it.academy.pojo.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Random;

import static by.it.academy.support.HibernateUtil.start;

public class QueryUtil {
    public static SessionFactory factory;

    static {
        try {
            factory = start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Employee> createEmployees(int count) {
        ArrayList<Employee> employees = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Employee employee = new Employee();
            employee.setName("employee" + (i + 1));
            employee.setAge(20 + random.nextInt(20));
            employee.setSalary(Double.valueOf(Math.round(random.nextDouble() * 3000)));
            employees.add(employee);
        }
        return employees;
    }

    public static void saveEmployees(ArrayList<Employee> employees) throws Exception {
        factory = start();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (Employee employee : employees) {
                session.save(employee);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public static void printEmployees(ArrayList<Employee> employees) throws Exception {
        for (Employee employee : employees) {
            System.out.println("Name: " + employee.getName()
                    + " Age: " + employee.getAge()
                    + " Salary: " + employee.getSalary());
        }

    }
}
