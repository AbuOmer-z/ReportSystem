import java.util.Date;

public class Report {
    private int reportId;
    private int processId;
    private String title;
    private String content;
    private Date creationDate;
    private int authorId;
    private String department;

    private String reportType;


    public Report(int reportId,int prcessId,  String title, String content, int authorId, String department, String reportType) {
        this.reportId = reportId;
        this.processId = processId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.department = department;
        this.reportType = reportType;
        this.creationDate = new Date();
    }
    public int getProcessId() { return processId; }

    public int getReportId() {
        return reportId;
    }
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    
    }
    public void setContent(String content) {
        this.content = content;
    
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getAuthorId() {
        return authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;

    }

    public String getReportType() {
        return reportType;
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "Report #" + reportId+ " [Process #" + processId +
        "[" + department + " - " + reportType + "]"+
        "\nTitle: " + title +
        "\nBy: EmploteeID " + authorId +
        "\nDate: " + creationDate +
        "\nContent: " + content + "\n" ;
    }
   
}
