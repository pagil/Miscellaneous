package org.data.model.visitor.impl;

import org.data.model.Contractor;
import org.data.model.PermanentEmployee;
import org.data.model.visitor.Visitor;

public class AnnualSalaryCalculator implements Visitor {

    @Override
    public void visit(PermanentEmployee employee) {
        System.out.printf("Annual salary of permanent employee [%s] is %d\n", employee, employee.getAnnualSalary());
    }

    @Override
    public void visit(Contractor employee) {
        System.out.printf("Annual salary of contractor [%s] is %d\n", employee, employee.getHourlyRate() * 8 * 5 * 4
                * 12);
    }

}
