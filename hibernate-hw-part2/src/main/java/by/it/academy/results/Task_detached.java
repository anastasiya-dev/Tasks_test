package by.it.academy.results;

import by.it.academy.pojo.Employee;
import by.it.academy.pojo.Task;
import by.it.academy.support.HibernateUtil;
import by.it.academy.support.QueryUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class Task_detached {
    static SessionFactory factory = QueryUtil.factory;

    public static void main(String[] args) throws Exception {
        Session session = factory.openSession();

        int count = 15;
        ArrayList<Employee> employees = QueryUtil.createEmployees(count);
        QueryUtil.saveEmployees(employees);
        ArrayList<Task> taskArrayList = new ArrayList<>();
        Task task = new Task();
        task.setName("footing");
        taskArrayList.add(task);
        employees.get(0).setTasks(taskArrayList);
        employees.get(2).setTasks(taskArrayList);
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();

        Employee employee = getEmployeeByTaskName("footing");
        System.out.println(employee.getName());

        HibernateUtil.finish();
    }

    public static Employee getEmployeeByTaskName(String taskName) {
        Session session = factory.openSession();
        DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
        DetachedCriteria taskCriteria = criteria.createCriteria("tasks");
        taskCriteria.add(Restrictions.eq("name", taskName));
        taskCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        Transaction transaction = session.beginTransaction();
        List<Employee> employeeList = criteria.getExecutableCriteria(session).list();
        transaction.commit();

        return employeeList.get(0);
    }
}
