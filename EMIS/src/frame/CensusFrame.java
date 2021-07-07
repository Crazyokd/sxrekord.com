package frame;

import DBConnectionManager.DBConnectionManager;
import JDBCUtils.JDBCUtils;
import entity.Census;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CensusFrame {
    private JFrame jf=new JFrame("员工信息管理系统-统计");
    private Census census;

    public CensusFrame(){
        this.jf.setLayout(null);
        this.jf.setBounds(500,200,850,500);
    }
    public void showWindow(){

        JLabel tit_lab=new JLabel("统计信息");
        tit_lab.setFont(new Font("黑体",Font.BOLD,30));
        tit_lab.setBounds(300,30,150,30);
        tit_lab.setHorizontalAlignment(SwingConstants.CENTER);
        this.jf.add(tit_lab);

        initLabel();

        this.jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.jf.setVisible(true);
    }
    public void initLabel(){
        constructLabel1("员工总人数",100);
        constructLabel1("部长人数",160);
        constructLabel1("学历情况",220);
        constructLabel1("婚姻状况",280);
        constructLabel1("薪资水平",340);

        this.census=DBConnectionManager.censusInfoFromDB();
        constructLabel2("目前入库总人数为"+census.getCnt_employees()+"人",100);
        constructLabel2("目前部长人数为"+census.getCnt_dep()+"人",160);
        constructLabel2(census.getCnt_edu_z()+"%专科 "+census.getCnt_edu_b()+"%本科 "+
                census.getCnt_edu_s()+"%硕士 "+census.getCnt_edu_doctor()+"%博士",220);
        constructLabel2(census.getCnt_mar()+"%已婚 "+census.getCnt_unmar()+"%未婚",280);
        constructLabel2("Max:"+census.getMax_salary()+"元;Avg:"+census.getAvg_salary()+
                "元;Min:"+census.getMin_salary()+"元",340);

    }

    public void constructLabel1(String name_label,int y){
        JLabel lab=new JLabel(name_label+":");
        lab.setFont(new Font("宋体",Font.PLAIN,24));
        lab.setBounds(100,y,150,30);
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        this.jf.add(lab);
    }
    public void constructLabel2(String name_label,int y){
        JLabel lab=new JLabel(name_label);
        lab.setFont(new Font("宋体",Font.PLAIN,24));
        lab.setBounds(250,y,500,30);
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        this.jf.add(lab);
    }

}
