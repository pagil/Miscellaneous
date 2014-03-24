package org.data.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmployeeTest {

    @Test
    public void testEqualEmployees() {
        Employee emp1 = new PermanentEmployee();
        Employee emp2 = new PermanentEmployee();
        assertTrue(emp1.equals(emp1));
        assertTrue(emp1.equals(emp2));
        assertTrue(emp1.hashCode() == emp2.hashCode());
    }

}
