package org.data.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.data.model.visitor.Visitor;

public class Contractor extends Employee {
    private int hourlyRate;

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getFirstName()).append(getLastName()).append(getDepartmentId())
                .append(getHourlyRate()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contractor)) {
            return false;
        }

        Contractor other = (Contractor) obj;
        if (hourlyRate != other.hourlyRate) {
            return false;
        }
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
