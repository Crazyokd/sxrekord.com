package entity;


public class Employee {
    private int employee_id=-1;
    private String name;
    private int edu_id;
    private String edu_level;
    private String marital_status;
    private int job_id;
    private String job_title;
    private String department_name;
    private double salary=-1.0;
    private Photo photo;

    private double max_salary;
    private double min_salary;
    private String islead;
    private int department_id;
    private int dep_manager;
    private int manager_id=-1;

    public Employee(){}
    public Employee(int employee_id, String name, String edu_level, String marital_status,
                    String job_title, String department_name, double salary, Photo photo) {
        this(employee_id,name,edu_level,marital_status,salary,photo);
        this.job_title = job_title;
        this.department_name = department_name;
    }
    public Employee(int employee_id,String name,String edu_level,String marital_status,
                    String job_title,String department_name,int manager_id,double salary){
        this.employee_id=employee_id;
        this.name=name;
        this.edu_level=edu_level;
        this.marital_status=marital_status;
        this.job_title=job_title;
        this.department_name=department_name;
        this.manager_id=manager_id;
        this.salary=salary;
    }

    public Employee(int employee_id, String name, String edu_level, String marital_status,double salary,Photo photo){
        this.employee_id=employee_id;
        this.name=name;
        this.edu_level=edu_level;
        this.marital_status=marital_status;
        this.salary=salary;
        this.photo=photo;
    }

    public int getManager_id() {
        return manager_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public int getJob_id() {
        return job_id;
    }

    public double getMax_salary() {
        return max_salary;
    }

    public double getMin_salary() {
        return min_salary;
    }

    public void setMax_salary(double max_salary) {
        this.max_salary = max_salary;
    }

    public void setMin_salary(double min_salary) {
        this.min_salary = min_salary;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }


    public int getDepartment_id() {
        return department_id;
    }

    public String getIslead() {
        return islead;
    }

    public void setIslead(String islead) {
        this.islead = islead;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setEdu_id(int edu_id) {
        this.edu_id = edu_id;
    }

    public String getEdu_level() {
        return edu_level;
    }

    public int getDep_manager() {
        return dep_manager;
    }

    public void setDep_manager(int dep_manager) {
        this.dep_manager = dep_manager;
    }


    public double getSalary() {
        return salary;
    }
    public int getEdu_id(){
        return this.edu_id;
    }
    public String getMarital_status(){
        return  this.marital_status;
    }
    public String getName(){
        return this.name;
    }
}
