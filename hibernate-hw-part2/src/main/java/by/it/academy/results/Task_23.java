package by.it.academy.results;

import by.it.academy.pojo.Address;
import by.it.academy.pojo.Department;
import by.it.academy.pojo.Employee;
import by.it.academy.pojo.EmployeeDetails;
import by.it.academy.support.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class Task_23 {
    public static void main(String[] args) throws Exception {
        Employee person = new Employee();
        Set<Employee> employeeSet = new HashSet<>();
        EmployeeDetails details = new EmployeeDetails();

        Department department = new Department();
        department.setDepartmentName("Finance department");

        person.setName("Anastasiya");
        person.setSurname("Kalach");
        person.setAge(29);
        person.setAddress(new Address("Khoruzhey", "Minsk", "220005"));
        person.setSalary((double) 1800);
        person.setCompany("EY");
        person.setEmployeeDetails(details);
        person.setDepartment(department);

        details.setHobbies("Swimming");
        details.setPensionPlan("Non-defined");
        details.setEmployee(person);

        employeeSet.add(person);
        department.setEmployees(employeeSet);

        SessionFactory factory = HibernateUtil.start();
        Session session = factory.openSession();
        Transaction transaction = null;

        EmployeeDetails employeeDetailsReceived = null;
        Employee employeeReceived = null;
        Department departmentReceived = null;
        try {
            transaction = session.beginTransaction();
            session.save(department);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        try {
            transaction = session.beginTransaction();
            departmentReceived = session.get(Department.class, department.getDepartmentId());
            System.out.println(departmentReceived.getDepartmentName());
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        Object[] employeeArrayList = departmentReceived.getEmployees().toArray();
        employeeReceived = Employee.class.cast(employeeArrayList[0]);
        System.out.println(employeeReceived.getCompany());
        System.out.println(employeeReceived.getEmployeeDetails().getPensionPlan());

        HibernateUtil.finish();
    }
}
