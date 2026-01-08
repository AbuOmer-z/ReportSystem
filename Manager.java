import java.util.ArrayList;
import java.util.List;

public class Manager extends Employee{
    private List<Supervisor> supervisingTeam;

    public Manager(int employeeId, String name , String position, String department ) {
        super(employeeId, name, position, department);
        this.supervisingTeam = new ArrayList<>();
    }
    public void addSupervisorToTeam(Supervisor supervisor) {
        this.supervisingTeam.add(supervisor);

    }
    public List<Supervisor> getSupervisingTeam() {
        return this.supervisingTeam;
    }
    @Override
    public String toString() {
        return super.toString() + "[Manages" + supervisingTeam.size() + "supervisors]";
    }
    
}
