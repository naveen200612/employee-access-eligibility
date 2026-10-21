
package com.example.access;

public class Employee {
    private final String employeeId;
    private final String name;
    private final int age;
    private final String department;
    private final String employmentType;
    private final int securityClearanceLevel;
    private final boolean idValid;
    private final String employmentStatus;

    public Employee(String employeeId, String name, int age,
                    String department, String employmentType,
                    int securityClearanceLevel, boolean idValid,
                    String employmentStatus) {
        this.employeeId = employeeId;
        this.name = name;
        this.age = age;
        this.department = department;
        this.employmentType = employmentType;
        this.securityClearanceLevel = securityClearanceLevel;
        this.idValid = idValid;
        this.employmentStatus = employmentStatus;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public int getSecurityClearanceLevel() {
        return securityClearanceLevel;
    }

    public boolean isIdValid() {
        return idValid;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }
}
