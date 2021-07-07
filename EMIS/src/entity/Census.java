package entity;

public class Census {
    private int cnt_employees=0;
    private int cnt_dep=0;
    private int cnt_edu_z=0;
    private int cnt_edu_b=0;
    private int cnt_edu_s=0;
    private int cnt_edu_doctor=0;
    private double cnt_edu_z_rate;
    private double cnt_edu_b_rate;
    private double cnt_edu_s_rate;
    private double cnt_edu_doctor_rate;
    private int cnt_mar;
    private int cnt_unmar;
    private double cnt_mar_rate;
    private double cnt_unmar_rate;
    private double max_salary=0;
    private double min_salary=0;
    private double avg_salary=0;

    public Census(){}
    public Census(int cnt_employees,double max_salary,double avg_salary,double min_salary){
            this.cnt_employees=cnt_employees;
            this.max_salary=max_salary;
            this.avg_salary=avg_salary;
            this.min_salary=min_salary;
    }


    public void setCnt_dep(int cnt_dep) {
        this.cnt_dep = cnt_dep;
    }

    public void setCnt_edu_z(int cnt_edu_z) {
        this.cnt_edu_z = cnt_edu_z;
    }

    public void setCnt_edu_b(int cnt_edu_b) {
        this.cnt_edu_b = cnt_edu_b;
    }

    public void setCnt_edu_s(int cnt_edu_s) {
        this.cnt_edu_s = cnt_edu_s;
    }

    public void setCnt_edu_doctor() {
        this.cnt_edu_doctor = this.cnt_employees-cnt_edu_z-cnt_edu_b-cnt_edu_s;
    }

    public void setCnt_mar(int cnt_mar) {
        this.cnt_mar = cnt_mar;
    }

    public void setCnt_unmar() {
        this.cnt_unmar = cnt_employees-cnt_mar;
    }

    public int getCnt_employees() {
        return cnt_employees;
    }

    public int getCnt_dep() {
        return cnt_dep;
    }

    public String getCnt_edu_z() {
        cnt_edu_z_rate=(double)cnt_edu_z/(double)cnt_employees;
        return String.format("%.1f",cnt_edu_z_rate*100);
    }

    public String getCnt_edu_b() {
        cnt_edu_b_rate=(double)cnt_edu_b/(double)cnt_employees;
        return String.format("%.1f",cnt_edu_b_rate*100);
    }

    public String getCnt_edu_s() {
        cnt_edu_s_rate=(double)cnt_edu_s/(double)cnt_employees;
        return String.format("%.1f",cnt_edu_s_rate*100);
    }

    public String getCnt_edu_doctor() {
        return String.format("%.1f",(1-cnt_edu_s_rate-cnt_edu_b_rate-cnt_edu_z_rate)*100);
    }

    public String getCnt_mar() {
        cnt_mar_rate=(double)cnt_mar/(double)cnt_employees;
        return String.format("%.1f",cnt_mar_rate*100);
    }

    public String getCnt_unmar() {
        return String.format("%.1f",(1-cnt_mar_rate)*100);
    }

    public String getMax_salary() {
        return String.format("%.1f",max_salary);
    }

    public String getMin_salary() {
        return String.format("%.1f",min_salary);
    }

    public String getAvg_salary() {
        return String.format("%.1f",avg_salary);
    }
}
