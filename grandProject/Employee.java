public class Employee {
    private int employeeId;
    private String name;
    private String position;
    private String department;

    public Employee(int employeeId, String name, String position, String department) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.department = department;

    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployee(int employeeId) {
        this.employeeId = employeeId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosition() {
        return position;
    }
public void setPosition(String position) {
    this.position = position;
}
public String getDepartment() {
    return department;
}
public void setDepartment(String department) {
    this.department = department;
}

@Override 
public String toString() {
    return "Employee # " + employeeId + 
    ": " + name +
    " (" + position + " - " + department + ")";
}

}
