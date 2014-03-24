package org.data;

import java.util.ArrayList;
import java.util.List;

import org.data.model.Contractor;
import org.data.model.Employee;
import org.data.model.PermanentEmployee;
import org.data.model.visitor.Visitor;
import org.data.model.visitor.impl.AnnualSalaryCalculator;
import org.data.model.visitor.impl.HealthInsuaranceCalculator;

public class Client {

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>();

        PermanentEmployee emp1 = new PermanentEmployee();
        emp1.setFirstName("Permanent");
        emp1.setLastName("Employee");
        emp1.setDepartmentId(5);
        emp1.setAnnualSalary(80000);
        emp1.setFamilySize(4);
        employees.add(emp1);

        Contractor emp2 = new Contractor();
        emp2.setFirstName("Contact");
        emp2.setLastName("Employee");
        emp2.setDepartmentId(3);
        emp2.setHourlyRate(40);
        employees.add(emp2);

        List<Visitor> visitors = new ArrayList<>();
        visitors.add(new AnnualSalaryCalculator());
        visitors.add(new HealthInsuaranceCalculator());

        for (Employee employee : employees) {
            for (Visitor visitor : visitors) {
                employee.accept(visitor);
            }
        }
    }
}
