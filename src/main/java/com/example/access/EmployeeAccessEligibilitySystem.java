
package com.example.access;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class EmployeeAccessEligibilitySystem {

    private static final Set<String> AUTHORIZED_DEPARTMENTS =
            Set.of("IT", "HR", "FINANCE", "ADMINISTRATION");

    public static class EligibilityResult {
        private final String status;
        private final List<String> reasons;

        public EligibilityResult(String status, List<String> reasons) {
            this.status = status;
            this.reasons = List.copyOf(reasons);
        }

        public String getStatus() {
            return status;
        }

        public List<String> getReasons() {
            return reasons;
        }
    }

    public static EligibilityResult evaluate(
            Employee employee, int requestedAccessLevel) {

        if (employee == null) {
            throw new IllegalArgumentException(
                    "Employee cannot be null.");
        }

        if (requestedAccessLevel < 0 || requestedAccessLevel > 3) {
            throw new IllegalArgumentException(
                    "Access level must be between 0 and 3.");
        }

        List<String> failures = new ArrayList<>();

        if (employee.getEmployeeId() == null ||
                employee.getEmployeeId().isBlank()) {
            failures.add("Employee ID is missing.");
        }

        if (employee.getName() == null ||
                employee.getName().isBlank()) {
            failures.add("Employee name is missing.");
        }

        if (employee.getAge() < 21 || employee.getAge() > 120) {
            failures.add("Employee age must be between 21 and 120.");
        }

        String department = employee.getDepartment() == null
                ? ""
                : employee.getDepartment().trim()
                    .toUpperCase(Locale.ROOT);

        if (!AUTHORIZED_DEPARTMENTS.contains(department)) {
            failures.add("Department is not authorized.");
        }

        String employmentStatus =
                employee.getEmploymentStatus() == null
                ? ""
                : employee.getEmploymentStatus().trim();

        if (!employmentStatus.equalsIgnoreCase("ACTIVE")) {
            failures.add("Employment status is not active.");
        }

        if (!employee.isIdValid()) {
            failures.add("Employee ID is invalid or expired.");
        }

        int clearance = employee.getSecurityClearanceLevel();

        if (clearance < 0 || clearance > 3) {
            failures.add("Security clearance level is invalid.");
        }

        if (!failures.isEmpty()) {
            return new EligibilityResult("Not Eligible", failures);
        }

        if (clearance < requestedAccessLevel) {
            return new EligibilityResult(
                    "Conditionally Eligible",
                    List.of(
                        "Insufficient security clearance. "
                        + "Access is denied until clearance is approved."
                    )
            );
        }

        return new EligibilityResult("Eligible", List.of());
    }

    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    "Input cannot be empty: " + prompt);
        }

        return value;
    }

    private static int readInt(
            Scanner scanner, String prompt, int min, int max) {

        String value = readText(scanner, prompt);

        try {
            int number = Integer.parseInt(value);

            if (number < min || number > max) {
                throw new IllegalArgumentException(
                        "Enter a number between " + min + " and " + max + ".");
            }

            return number;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Please enter a valid integer for " + prompt);
        }
    }

    private static boolean readBoolean(
            Scanner scanner, String prompt) {

        while (true) {
            String value = readText(scanner, prompt);

            if (value.equalsIgnoreCase("yes")) {
                return true;
            }

            if (value.equalsIgnoreCase("no")) {
                return false;
            }

            System.out.println("Please enter yes or no.");
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            int count = readInt(
                    scanner, "Number of employees: ", 1, 500);

            for (int i = 1; i <= count; i++) {
                System.out.println("\n========== Employee " + i
                        + " ==========");

                String id = readText(scanner, "Employee ID: ");
                String name = readText(scanner, "Name: ");
                int age = readInt(scanner, "Age: ", 0, 120);
                String department =
                        readText(scanner, "Department: ");
                String type = readText(
                        scanner, "Employment type (Permanent/Contract): ");
                int clearance = readInt(
                        scanner, "Security clearance (0-3): ", 0, 3);
                boolean idValid = readBoolean(
                        scanner, "Is ID valid? (yes/no): ");
                String status = readText(
                        scanner, "Employment status (Active/Inactive): ");

                int requestedLevel = readInt(
                        scanner, "Requested access level (0-3): ", 0, 3);

                Employee employee = new Employee(
                        id, name, age, department, type,
                        clearance, idValid, status);

                EligibilityResult result =
                        evaluate(employee, requestedLevel);

                System.out.println("\nEmployee ID: " + id);
                System.out.println("Employee Name: " + name);
                System.out.println("Department: " + department);
                System.out.println("Requested Access Level: "
                        + requestedLevel);
                System.out.println("Eligibility: " + result.getStatus());

                if (!result.getReasons().isEmpty()) {
                    System.out.println("Reasons:");

                    for (String reason : result.getReasons()) {
                        System.out.println("- " + reason);
                    }
                }
            }

        } catch (IllegalArgumentException ex) {
            System.err.println("Input Error: " + ex.getMessage());
        }
    }
}
