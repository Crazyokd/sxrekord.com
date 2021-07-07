package DBConnectionManager;

import JDBCUtils.JDBCUtils;
import entity.Census;
import entity.Employee;
import entity.Photo;
import entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class DBConnectionManager {
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    private static User cur_user;

    private DBConnectionManager(){}

    static {
        connection= JDBCUtils.getConnection();
    }
    public static void connectDB(){
        System.out.println("连接成功");
    }
    public static void closeConnection(){
        JDBCUtils.close(resultSet,preparedStatement,connection);
    }
    public static boolean isOpened(){
        return !(connection==null);
    }


    /**
     * 将新注册的用户信息更新至数据库
     * @param user
     * @return 更新所影响的行数
     */
    public static int register(User user){
        int rtnUpdate=0;
        String sql="insert into users values(?,?,?)";
        try{
            preparedStatement=connection.prepareStatement(sql);
            //
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getType());
            rtnUpdate=preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println("sql语句执行错误");
        }finally {
            return  rtnUpdate;
        }
    }

    /**
     * 在数据库中查询指定的用户信息，若查询得到，则同步当前用户信息
     * @param username
     * @param password
     * @return
     */
    public static boolean selectUserName(String username,String password){
        boolean res=false;
        String sql="select type from users where username=? and password=?";
        try{
            preparedStatement=connection.prepareStatement(sql);

            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                DBConnectionManager.setCur_user(new User(username,password,resultSet.getString(1)));
                res=true;
            }
        }catch (SQLException e){
            System.out.println("sql语句执行错误");
        }finally {
            return res;
        }
    }

    public static void setCur_user(User user){
        cur_user=user;
    }
    public static User getCur_user() {
        return cur_user;
    }

    /**
     * 统计信息
     * @return 将所有统计信息封装至一个Census对象并返回
     */
    public static Census censusInfoFromDB(){

        String sql_cnt_employees="select * from view_cnt_employees";
        String sql_cnt_edu_z="select * from view_cnt_edu_z";
        String sql_cnt_edu_b="select * from view_cnt_edu_b";
        String sql_cnt_edu_s="select * from view_cnt_edu_s";

        String sql_cnt_dep="select count(*) as cnt_dep from department where manager_id is not null";
        String sql_cnt_mar="select count(*) as cnt_mar from employee where marital_status='yes'";
        Census census=null;

        try{
            preparedStatement=connection.prepareStatement(sql_cnt_employees);
            resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                census=new Census(resultSet.getInt(1),resultSet.getDouble(2),
                        resultSet.getDouble(3),resultSet.getDouble(4));
            }

            //查询部门数
            preparedStatement=connection.prepareStatement(sql_cnt_dep);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                census.setCnt_dep(resultSet.getInt(1));
            }

            //查询各学历人数
            preparedStatement=connection.prepareStatement(sql_cnt_edu_z);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                census.setCnt_edu_z(resultSet.getInt(1));
            }
            preparedStatement=connection.prepareStatement(sql_cnt_edu_b);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                census.setCnt_edu_b(resultSet.getInt(1));
            }
            preparedStatement=connection.prepareStatement(sql_cnt_edu_s);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                census.setCnt_edu_s(resultSet.getInt(1));
            }
            census.setCnt_edu_doctor();

            //查询已婚人数
            preparedStatement=connection.prepareStatement(sql_cnt_mar);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                census.setCnt_mar(resultSet.getInt(1));
            }
            census.setCnt_unmar();

        }catch (SQLException e){
            System.out.println("sql语句执行错误");
        }finally {
            //
            return census;
        }
    }

    /**
     * 插入信息
     * @param employee
     * @return
     */
    public static boolean insertInfoToDB(Employee employee){
        boolean res=false;
        try{
            //开启事务
            connection.setAutoCommit(false);
            //check ID
            if(checkEmployeeID(employee.getEmployee_id())){
                //检查是否有部门冲突
                if(!checkDepartmentalConflict(employee)) {
                    //薪资之争
                    if (employee.getSalary() >= employee.getMin_salary() &&
                            employee.getSalary() <= employee.getMax_salary()) {
                        //及时更新部门部长id
                        if (employee.getIslead().equals("yes")) {
                            employee.setDep_manager(employee.getEmployee_id());
                        }
                        updateEmployee(employee);
                        if (employee.getIslead().equals("yes")) {
                            //更新其他员工表
                            updateOtherEmployee(employee);
                            //更新部门表
                            updateDepartment(employee);
                        }
                        connection.commit();
                        res=true;
                    }else{
                        connection.rollback();
                        //薪资有误
                        System.out.println("薪资不符合标准");
                    }
                }
            }
        }catch (SQLException e){
            System.out.println("sql语句执行错误");
            try {
                connection.rollback();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        catch (FileNotFoundException e){
            System.out.println("文件路径错误");
        }
        finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            return res;
        }
    }

    public static boolean checkEmployeeID(int employee_id) throws SQLException{
        boolean res=true;
        String sql_checkID="select employee_id from employee where employee_id=?";
        //check ID
        preparedStatement=connection.prepareStatement(sql_checkID);
        preparedStatement.setInt(1,employee_id);
        resultSet =preparedStatement.executeQuery();
        if(resultSet.next()){
            connection.rollback();
            res=false;
            System.out.println("该员工ID已存在");
        }
        return res;
    }

    public static boolean checkDepartmentalConflict(Employee employee) throws SQLException{
        boolean res=false;

        //查询job表的相应信息
        String sql_job="select job_id,max_salary,min_salary from job where job_title=?";
        preparedStatement=connection.prepareStatement(sql_job);
        preparedStatement.setString(1,employee.getJob_title());
        resultSet= preparedStatement.executeQuery();
        while(resultSet.next()){
            employee.setJob_id(resultSet.getInt("job_id"));
            employee.setMax_salary(resultSet.getDouble("max_salary"));
            employee.setMin_salary(resultSet.getDouble("min_salary"));
        }

        //查询department表的相应信息
        String sql_dep="select department_id,ifnull(manager_id,-1) manager_id\n" +
                "from department\n" +
                "where department_name=?";
        preparedStatement=connection.prepareStatement(sql_dep);
        preparedStatement.setString(1,employee.getDepartment_name());
        resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            employee.setDepartment_id(resultSet.getInt("department_id"));
            employee.setDep_manager(resultSet.getInt("manager_id"));
        }

        //查询插入员工是否为领导岗位
        String sql_islead="select islead\n" +
                "from subjection\n" +
                "where job_id =? and department_id=?";
        preparedStatement=connection.prepareStatement(sql_islead);
        preparedStatement.setInt(1,employee.getJob_id());
        preparedStatement.setInt(2,employee.getDepartment_id());
        resultSet=preparedStatement.executeQuery();
        if(resultSet.next()){
            employee.setIslead(resultSet.getString("islead"));
        }

        if(employee.getIslead().equals("yes")&&employee.getDep_manager()!=-1){
            connection.rollback();
            res=true;
            System.out.println("该部门已有部长");
        }
        return res;
    }

    public static int updateEmployee(Employee employee) throws SQLException, FileNotFoundException {
        //查询edu_id
        String sql_edu="select edu_id\n" +
                "from education\n" +
                "where edu_level=?";
        preparedStatement=connection.prepareStatement(sql_edu);
        preparedStatement.setString(1,employee.getEdu_level());
        resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            employee.setEdu_id(resultSet.getInt("edu_id"));
        }

        //更新数据库
        String sql_ins="insert into employee values(\n" +
                "?,?,?,?,?,?,?,?\n" +
                ")";
        preparedStatement=connection.prepareStatement(sql_ins);
        preparedStatement.setInt(1,employee.getEmployee_id());
        preparedStatement.setInt(2,employee.getEdu_id());
        preparedStatement.setInt(3,employee.getJob_id());
        preparedStatement.setDouble(4,employee.getSalary());
        preparedStatement.setString(5,employee.getMarital_status());
        if(employee.getPhoto().getPhoto_path()==null){
            preparedStatement.setNull(6, Types.BLOB);
        }else{
            preparedStatement.setBlob(6,new FileInputStream(employee.getPhoto().getPhoto_path()));
        }
        preparedStatement.setString(7,employee.getName());
        if(employee.getDep_manager()!=-1&&employee.getDep_manager()!=employee.getEmployee_id()){
            preparedStatement.setInt(8,employee.getDep_manager());
        }else{
            preparedStatement.setNull(8, Types.INTEGER);
        }
        int ins_result=preparedStatement.executeUpdate();
        System.out.println(ins_result>0?"插入成功":"插入失败");
        return ins_result;
    }

    public static int updateOtherEmployee(Employee employee) throws SQLException{
        //更新员工表
        String sql_upd_emp="update employee\n" +
                "set manager_id=?\n" +
                "where job_id between (select min(job_id) as min_id\n" +
                "from subjection\n" +
                "where department_id=? and islead ='no') and\n" +
                "(select max(job_id) as max_id\n" +
                "from subjection\n" +
                "where department_id=? and islead ='no') ";
        preparedStatement=connection.prepareStatement(sql_upd_emp);
        preparedStatement.setInt(1,employee.getEmployee_id());
        preparedStatement.setInt(2,employee.getDepartment_id());
        preparedStatement.setInt(3,employee.getDepartment_id());
        int upd_res=preparedStatement.executeUpdate();
        System.out.println("员工表"+(upd_res>0?"更新成功":"更新失败"));
        return upd_res;
    }

    public static int updateDepartment(Employee employee) throws SQLException{
        //更新department表
        String sql_upd_dep="update department\n" +
                "set manager_id=?\n" +
                "where department_id=?";
        preparedStatement=connection.prepareStatement(sql_upd_dep);
        preparedStatement.setInt(1,employee.getEmployee_id());
        preparedStatement.setInt(2,employee.getDepartment_id());

        int upd_res=preparedStatement.executeUpdate();
        System.out.println("部门表"+(upd_res>0?"更新成功":"更新失败"));
        return upd_res;
    }

    public static boolean checkSalary(int id,double salary){
        boolean res=false;
        String sql="select max_salary,min_salary\n" +
                "from employee,job\n" +
                "where employee_id=? and employee.job_id=job.job_id";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                double max_salary=resultSet.getDouble(1);
                double min_salary=resultSet.getDouble(2);
                if(salary>=min_salary&&salary<=max_salary) res=true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            return res;
        }
    }

    public static int modifyInfoToDB(Employee e){
        int rst_upd=0;
        String sql_sel="select edu_id\n" +
                "from education\n" +
                "where edu_level=?";
        String sql="update employee\n" +
                "set name=?,edu_id=?,marital_status=?,salary=?,photo=?\n" +
                "where employee_id=?";
        try {
            //查询edu_id
            preparedStatement = connection.prepareStatement(sql_sel);
            preparedStatement.setString(1,e.getEdu_level());
            resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                e.setEdu_id(resultSet.getInt(1));
            }
            //更新数据库
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,e.getName());
            preparedStatement.setInt(2,e.getEdu_id());
            preparedStatement.setString(3,e.getMarital_status());
            preparedStatement.setDouble(4,e.getSalary());
            preparedStatement.setBlob(5,new FileInputStream(e.getPhoto().getPhoto_path()));
            preparedStatement.setInt(6,e.getEmployee_id());
            rst_upd=preparedStatement.executeUpdate();
            System.out.println(rst_upd>0?"更新成功":"更新失败");

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } finally {
            return rst_upd;
        }
    }

    public static ArrayList<Object[]> getTableList() {
        ArrayList<Object[]> als=new ArrayList<>();
        try {
            resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                Object[] objs=new Object[9];
                objs[0]=resultSet.getInt(1);
                for(int i=1;i<7;i++){
                    objs[i]=resultSet.getString(i+1);
                }
                objs[7]=resultSet.getDouble(8);
                //将照片缓存至C:\Users\86182\Desktop\src\src\imgcache文件夹
                objs[8]=new Photo().readPhoto(resultSet,objs[0].toString()+objs[1]);

                als.add(objs);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }finally {
            return als;
        }
    }
    public static void getPS(String sql) throws SQLException{
        preparedStatement=connection.prepareStatement(sql);
    }
    public static void getPS(String sql,int id) throws SQLException{
        preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
    }
    public static void getPS(String sql,Employee e) throws SQLException{
        preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setString(1,e.getEdu_level());
        preparedStatement.setString(2,e.getMarital_status());
        preparedStatement.setString(3,e.getJob_title());
        preparedStatement.setString(4,e.getDepartment_name());
    }

    public static boolean deleteInfoFromDB(int id){
        boolean res=false;
        String sql_del="delete \n" +
                "from employee\n" +
                "where employee_id=?";
        try{
            preparedStatement=connection.prepareStatement(sql_del);
            preparedStatement.setInt(1,id);
            int res_del=preparedStatement.executeUpdate();
            if(res_del>0){
                System.out.println("删除成功");
                res=true;
            }
        } catch (SQLException sqlException) {
            System.out.println("无法删除部长");
        }finally {
            return res;
        }
    }
}

