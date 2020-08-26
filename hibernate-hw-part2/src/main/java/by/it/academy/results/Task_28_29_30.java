package by.it.academy.results;

import by.it.academy.pojo.Employee;
import by.it.academy.support.HibernateUtil;
import by.it.academy.support.QueryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class Task_28_29_30 {
    static SessionFactory factory = QueryUtil.factory;

    public static void main(String[] args) throws Exception {
        Session session = factory.openSession();
        int count = 15;
        ArrayList<Employee> employees = QueryUtil.createEmployees(count);
        QueryUtil.saveEmployees(employees);
        CriteriaBuilder cb = session.getCriteriaBuilder();

        int pageNumber = 1;
        int pageSize = 3;
        CriteriaQuery<Employee> criteriaQuery1 = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot1 = criteriaQuery1.from(Employee.class);
        criteriaQuery1.select(employeeRoot1);
        TypedQuery<Employee> typedQuery = session.createQuery(criteriaQuery1);
        typedQuery.setFirstResult(pageSize * (pageNumber - 1));
        typedQuery.setMaxResults(pageSize);
        List<Employee> employees1 = typedQuery.getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employees1);

        CriteriaQuery<Employee> criteriaQuery2 = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot2 = criteriaQuery2.from(Employee.class);
        criteriaQuery2.select(employeeRoot2).where(cb.equal(employeeRoot2.get("name"), "employee2"));
        List<Employee> employees2 = session.createQuery(criteriaQuery2).getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employees2);

        CriteriaQuery<Employee> criteriaQuery3 = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot3 = criteriaQuery3.from(Employee.class);
        criteriaQuery3.select(employeeRoot3).where(cb.gt(employeeRoot3.get("salary"), Double.valueOf(1500)));
        List<Employee> employees3 = session.createQuery(criteriaQuery3).getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employees3);

        CriteriaQuery<Employee> criteriaQuery4 = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot4 = criteriaQuery4.from(Employee.class);
        criteriaQuery4.select(employeeRoot4).where(cb.like(employeeRoot4.get("name"), "%1%"));
        List<Employee> employees4 = session.createQuery(criteriaQuery4).getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employees4);

        CriteriaQuery<Employee> criteriaQuery5 = cb.createQuery(Employee.class);
        Root<Employee> employeeRoot5 = criteriaQuery5.from(Employee.class);
        Predicate predicate = cb.and(cb.gt(employeeRoot5.get("salary"), 1500), cb.lt(employeeRoot5.get("age"), 30));
        criteriaQuery5.select(employeeRoot5).where(predicate).orderBy(cb.desc(employeeRoot5.get("salary")));
        List<Employee> employees5 = session.createQuery(criteriaQuery5).getResultList();
        QueryUtil.printEmployees((ArrayList<Employee>) employees5);

        CriteriaQuery<Double> criteriaQuery6 = cb.createQuery(Double.class);
        criteriaQuery6.select(cb.avg(criteriaQuery6.from(Employee.class).get("salary")));
        double avSalary = session.createQuery(criteriaQuery6).getSingleResult();
        System.out.println(avSalary);

        CriteriaQuery<Long> criteriaQuery7 = cb.createQuery(Long.class);
        criteriaQuery7.select(cb.countDistinct(criteriaQuery7.from(Employee.class).get("age")));
        long countD = session.createQuery(criteriaQuery7).getSingleResult();
        System.out.println(countD);

//        Department department = new Department();
//        department.setDepartmentName("IT D");
//        employees.get(0).setDepartment(department);
//        employees.get(2).setDepartment(department);
//        employees.get(4).setDepartment(department);
//        department.setEmployees(Set.of(employees.get(0), employees.get(2), employees.get(4)));
//
//        CriteriaQuery<Employee> criteriaQuery8 = cb.createQuery(Employee.class);
//        Root<Employee> employeeRoot8 = criteriaQuery8.from(Employee.class);
//        Join<Employee, Department> employeeJoin = employeeRoot8.join("department");
//        criteriaQuery8.where(cb.equal(employeeJoin.get("deprtmentName"), "IT D"));
//        List<Employee> employeeList8 = session.createQuery(criteriaQuery8).getResultList();
//        QueryUtil.printEmployees((ArrayList<Employee>) employeeList8);

        HibernateUtil.finish();
    }
}
