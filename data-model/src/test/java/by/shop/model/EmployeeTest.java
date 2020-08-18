package by.shop.model;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@Ignore
public class EmployeeTest extends ModelTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void create() {
        //Given
        Employee employee = new Employee();
        employee.setBirthDate(new Date());
        employee.setFirstName("First Name Employee");
        employee.setLastName("Last Name Employee");

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setCity("Minsk");
        employeeDetails.setCountry(Country.BY);
        employeeDetails.setMobileNumber("375292658195");
        employeeDetails.setStreet("Khoruzhey-37-57");

        employeeDetails.setEmployee(employee);
        employee.setEmployeeDetails(employeeDetails);

        //When
        String employeeId = save(employee);
        String employeeDetailsId = employee.getEmployeeDetails().getId();

        Employee savedEmployee = get(employeeId);

        //Then
        assertNotNull(employeeId);
        assertNotNull(employeeDetailsId);
        assertNotNull(savedEmployee);
        assertNotNull(savedEmployee.getEmployeeDetails());
        assertEquals(employeeDetailsId, savedEmployee.getEmployeeDetails().getId());
    }

    public Employee get(String employeeId) {
        Session sess = factory.openSession();
        Employee employee = null;
        Transaction tx = null;

        try {
            tx = sess.beginTransaction();
            employee = sess.get(Employee.class, employeeId);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            sess.close();
        }
        return employee;
    }

    private String save(Employee employee) {
        Session sess = factory.openSession();
        Transaction tx = null;
        String employeeId;
        try {
            tx = sess.beginTransaction();
            employeeId = (String) sess.save(employee);
            sess.saveOrUpdate(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            sess.close();
        }
        return employeeId;
    }
}