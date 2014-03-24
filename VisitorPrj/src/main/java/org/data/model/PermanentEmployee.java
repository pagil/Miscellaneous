package org.data.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.data.model.visitor.Visitor;

public class PermanentEmployee extends Employee {
    private int annualSalary;
    private int familySize;

    public int getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(int annualSalary) {
        this.annualSalary = annualSalary;
    }

    public int getFamilySize() {
        return familySize;
    }

    public void setFamilySize(int familySize) {
        this.familySize = familySize;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirstName()).append(getLastName()).append(getDepartmentId())
                .append(getAnnualSalary()).append(getFamilySize()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PermanentEmployee)) {
            return false;
        }

        PermanentEmployee other = (PermanentEmployee) obj;
        if (annualSalary != other.annualSalary)
            return false;
        if (familySize != other.familySize)
            return false;
        if (getDepartmentId() != other.getDepartmentId())
            return false;

        if (getFirstName() == null) {
            if (other.getFirstName() != null)
                return false;
        } else if (!getFirstName().equals(other.getFirstName()))
            return false;

        if (getLastName() == null) {
            if (other.getLastName() != null)
                return false;
        } else if (!getLastName().equals(other.getLastName()))
            return false;
        return true;
    }

}
