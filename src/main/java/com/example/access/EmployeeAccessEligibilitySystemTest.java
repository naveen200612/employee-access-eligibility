
package com.example.access;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAccessEligibilitySystemTest {

    private Employee createEmployee(
            int age,
            String department,
            String status,
            boolean idValid,
            int clearance) {

        return new Employee(
                "EMP101",
                "Arun",
                age,
                department,
                "Permanent",
                clearance,
                idValid,
                status
        );
    }

    @Test
    void eligibleEmployee() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(25, "IT", "Active", true, 3), 2);

        assertEquals("Eligible", result.getStatus());
        assertTrue(result.getReasons().isEmpty());
    }

    @Test
    void ageExactlyTwentyOneIsAllowed() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(21, "HR", "Active", true, 2), 2);

        assertEquals("Eligible", result.getStatus());
    }

    @Test
    void employeeBelowTwentyOneIsRejected() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(20, "IT", "Active", true, 3), 1);

        assertEquals("Not Eligible", result.getStatus());
        assertTrue(result.getReasons().stream()
                .anyMatch(reason -> reason.contains("21")));
    }

    @Test
    void unauthorizedDepartmentIsRejected() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(25, "Sales", "Active", true, 3), 1);

        assertEquals("Not Eligible", result.getStatus());
        assertTrue(result.getReasons().stream()
                .anyMatch(reason -> reason.contains("Department")));
    }

    @Test
    void inactiveEmployeeIsRejected() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(25, "IT", "Inactive", true, 3), 1);

        assertEquals("Not Eligible", result.getStatus());
        assertTrue(result.getReasons().stream()
                .anyMatch(reason -> reason.contains("active")));
    }

    @Test
    void invalidEmployeeIdIsRejected() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(25, "IT", "Active", false, 3), 1);

        assertEquals("Not Eligible", result.getStatus());
        assertTrue(result.getReasons().stream()
                .anyMatch(reason -> reason.contains("invalid or expired")));
    }

    @Test
    void insufficientClearanceIsConditional() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(25, "IT", "Active", true, 1), 2);

        assertEquals("Conditionally Eligible", result.getStatus());
        assertFalse(result.getReasons().isEmpty());
    }

    @Test
    void reportsAllMultipleFailures() {
        var result = EmployeeAccessEligibilitySystem.evaluate(
                createEmployee(19, "Sales", "Inactive", false, 3), 2);

        assertEquals("Not Eligible", result.getStatus());
        assertEquals(4, result.getReasons().size());
    }

    @Test
    void invalidAccessLevelThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> EmployeeAccessEligibilitySystem.evaluate(
                        createEmployee(25, "IT", "Active", true, 3), 5));
    }

    @Test
    void nullEmployeeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> EmployeeAccessEligibilitySystem.evaluate(null, 1));
    }
}
