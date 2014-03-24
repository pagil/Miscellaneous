package org.data.model.visitor.impl;

import org.data.model.Contractor;
import org.data.model.PermanentEmployee;
import org.data.model.visitor.Visitor;

public class HealthInsuaranceCalculator implements Visitor {

    @Override
    public void visit(PermanentEmployee employee) {
        System.out.printf("Health care insuarance payment for permanent employee [%s] is %d\n", employee,
                employee.getFamilySize() * 2000);
    }

    @Override
    public void visit(Contractor employee) {
        System.out.printf("Health care insuarance payment for contractor [%s] is 0\n", employee);
    }
}
