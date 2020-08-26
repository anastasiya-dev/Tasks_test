package by.it.academy.results;

import by.it.academy.pojo.Employee;
import by.it.academy.support.HibernateUtil;
import by.it.academy.support.QueryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class Task_25_26_27 {
    static SessionFactory factory = QueryUtil.factory;

    public static void main(String[] args) throws Exception {
        int count = 15;
        ArrayList<Employee> employees = QueryUtil.createEmployees(count);
        QueryUtil.saveEmployees(employees);
        Session session = factory.openSession();

        String queryText1 = "Select E from Employee E where age>30 order by salary desc";
        System.out.println(queryText1 + ":");
        Query query1 = session.createQuery(queryText1);
        List<Employee> employeeList1 = query1.list();
        QueryUtil.printEmployees((ArrayList<Employee>) employeeList1);

        String queryText2 = "select count(E) from Employee E where age<30";
        System.out.println(queryText2 + ":");
        Query query2 = session.createQuery(queryText2);
        Long countResult = (Long) query2.uniqueResult();
        System.out.println(countResult);

        String queryText3 = "Select E from Employee E where E.id=:id";
        System.out.println(queryText3 + ":");
        Query query3 = session.createQuery(queryText3);
        query3.setParameter("id", Long.valueOf(3));
        List<Employee> employeeList3 = query3.list();
        QueryUtil.printEmployees((ArrayList<Employee>) employeeList3);

        String queryText4 = "select max(E.salary) from Employee E";
        System.out.println(queryText4 + ":");
        Query query4 = session.createQuery(queryText4);
        Double maxSalary = (Double) query4.uniqueResult();
        System.out.println(maxSalary);

        String queryText5 = "select avg(E.salary) from Employee E where id>3 and id<6";
        System.out.println(queryText5 + ":");
        Query query5 = session.createQuery(queryText5);
        Double avSalary = (Double) query5.uniqueResult();
        System.out.println(avSalary);

        String queryText6 = "from Employee";
        System.out.println(queryText6 + ":");
        Query query6 = session.createQuery(queryText6);
        query6.setFirstResult(4);
        query6.setMaxResults(6);
        List<Employee> employeeList6 = query6.list();
        QueryUtil.printEmployees((ArrayList<Employee>) employeeList6);

        Transaction tx = session.beginTransaction();
        String queryText7 = "update Employee set age=100 where id=:id";
        System.out.println(queryText7 + ":");
        Query query7 = session.createQuery(queryText7);
        query7.setParameter("id", Long.valueOf(3));
        int result = query7.executeUpdate();
        tx.commit();
        System.out.println(result);

        Query query8 = session.createNamedQuery("get_employee_by_id");
        List<Employee> employeeList8 = query8.setParameter("id", Long.valueOf(5)).getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employeeList8);

        HibernateUtil.finish();
    }
}
