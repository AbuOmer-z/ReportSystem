import java.util.ArrayList;
import java.util.List;


public class Supervisor extends Employee {
    private List<Employee> team;

    public Supervisor(int employeeId, String name , String position , String department) {
        super(employeeId, name , position , department);
        this.team = new ArrayList<>();
    }
    public void addEmployeeToTeam(Employee employee) {
        this.team.add(employee);
    }
    public List<Employee> getTeam() {
        return this.team;
    }
    public int getTeamSize() {
        return this.team.size();
    }

    @Override
    public String toString() {
        return super.toString() + "[Manages " + getTeamSize() + " employees]";
    }


}
