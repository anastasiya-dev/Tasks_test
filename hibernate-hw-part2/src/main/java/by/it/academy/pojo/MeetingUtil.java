package by.it.academy.pojo;

import java.util.Set;

public class MeetingUtil {
    public static void add(Set<Employee> employees, Set<Meeting> meetings) {
        for (Employee employee : employees) {
            employee.getMeetings().addAll(meetings);
        }
        for (Meeting meeting : meetings) {
            meeting.getEmployees().addAll(employees);
        }
    }

    public static void add(Employee employee, Set<Meeting> meetings) {
        employee.getMeetings().addAll(meetings);
        for (Meeting meeting : meetings) {
            meeting.getEmployees().add(employee);
        }
    }

    public static void add(Set<Employee> employees, Meeting meeting) {
        meeting.getEmployees().addAll(employees);
        for (Employee employee : employees) {
            employee.getMeetings().add(meeting);
        }
    }
}
