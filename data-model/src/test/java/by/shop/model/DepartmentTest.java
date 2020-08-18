package by.shop.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@Ignore
public class DepartmentTest extends ModelTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void createDepartmentWithoutEmployees() {
        //Given
        String departmentName = "Department1";
        Department department = createDepartment(departmentName);

        //When
        String departmentId = save(department);

        //Then
        assertNotNull(departmentId);
        Department savedDepartment = get(departmentId);
        assertEquals(departmentName, savedDepartment.getName());
        assertEquals(0, savedDepartment.getEmployees().size());
    }

    @Test
    public void createDepartmentWithEmployees() {
        //Given
        String departmentName = "Department2";
        Department department = createDepartment(departmentName);
        int employeesCount = 2;
        List<Employee> employeeList = createEmployees(employeesCount, department);
        department.setEmployees(employeeList);

        //When
        String departmentId = save(department);
        department.setEmployees(employeeList);

        //Then
        assertNotNull(departmentId);
        Department savedDepartment = get(departmentId);
        assertEquals(departmentName, savedDepartment.getName());
        assertEquals(employeesCount, savedDepartment.getEmployees().size());
    }

    private List<Employee> createEmployees(int employeesCount, Department department) {
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < employeesCount; i++) {
            Employee employee = new Employee();
            employee.setLastName("Last Name " + i);
            employee.setFirstName("First Name " + i);
            employee.setBirthDate(new Date());
            EmployeeDetails employeeDetails = new EmployeeDetails();
            employeeDetails.setEmployee(employee);
            employee.setEmployeeDetails(employeeDetails);
            employee.setDepartment(department);
            list.add(employee);
        }
        return list;
    }

    private Department get(String departmentId) {
        Transaction transaction = null;
        Department department = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            department = session.get(Department.class, departmentId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return department;
    }

    private String save(Department department) {
        String id = null;
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(department);
            id = (String) session.save(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return id;
    }

    private Department createDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        return department;
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}