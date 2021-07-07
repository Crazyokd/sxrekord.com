package frame;

import DBConnectionManager.DBConnectionManager;
import JDBCUtils.JDBCUtils;
import entity.Employee;
import entity.Photo;
import entity.RestrictedFields;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class  InsertInfoFrame {

    private JFrame jf=new JFrame("员工信息管理系统-插入信息");
    private Font font=new Font("黑体",Font.PLAIN,20);

    private JTextField id_tf;
    private JTextField name_tf;
    private JTextField salary_tf;
    private RestrictedFields rf=new RestrictedFields();
    private Photo photo=new Photo();
    private Employee employee;

    public InsertInfoFrame(){
        this.jf.setLayout(null);
        this.jf.setBounds(400,200,700,600);
    }

    public void showWindow(){

        //构造所有label控件
        constructLabel();
        //初始化信息区域
        initInterface();
        //收尾
        confirmSubmit();
        //显示界面
        this.jf.setVisible(true);
    }

    public void initInterface(){
        //文本区
        id_tf=new JTextField();
        id_tf.setBounds(275,80,150,30);

        name_tf=new JTextField();
        name_tf.setBounds(275,130,150,30);

        salary_tf=new JTextField();
        salary_tf.setBounds(275,380,150,30);

        this.jf.add(id_tf);
        this.jf.add(name_tf);
        this.jf.add(salary_tf);

        //初始化限制字段
        rf.initRestrictedFields(font,this.jf);
        //初始化所有字段数据
        initInfo();

        //选择照片
        JButton photo_btn=new JButton("选择文件");
        photo_btn.setFont(font);
        photo_btn.setBounds(275,430,150,30);
        photo_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser photo_fc=Photo.choosePhoto();
                if(photo_fc.showOpenDialog(jf)==0){
                    photo.setPhoto_path(photo_fc.getSelectedFile().getAbsolutePath());
                }
            }
        });
        this.jf.add(photo_btn);

        JButton check_btn=new JButton("点击查看");
        check_btn.setFont(font);
        check_btn.setBounds(435,430,150,30);
        check_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                photo.viewPhoto();
            }
        });
        this.jf.add(check_btn);

    }

    public void confirmSubmit(){
        JButton sub_btn=new JButton("提交");
        sub_btn.setFont(new Font("黑体",Font.BOLD,20));
        sub_btn.setBounds(250,490,100,30);
        sub_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkInfo())
                    if(DBConnectionManager.insertInfoToDB(new Employee(Integer.parseInt(id_tf.getText()),name_tf.getText(),
                            rf.getEdu_level(),rf.getMar_status(),rf.getJob_title(), rf.getDep_name(),
                            Double.parseDouble(salary_tf.getText()),photo))){
                        //清空信息
                        initInfo();
                    }
            }
        });
        this.jf.add(sub_btn);
    }

    public boolean checkInfo(){
        boolean result=false;
        String feedback="";
        String name=name_tf.getText();
        String salary=salary_tf.getText();
        String id=id_tf.getText();
        if(!name.equals("")&&!salary.equals("")&&!id.equals("")){
            try{
                int id_int=Integer.parseInt(id);
                double sal=Double.parseDouble(salary);
                feedback="插入成功";
                result=true;
            }catch (NumberFormatException nfe){
                feedback="ID或工资格式有误";
            }
        }else{
            feedback="信息输入不全";
        }
        if(!feedback.equals("插入成功"))
        JOptionPane.showMessageDialog(this.jf,feedback);
        return result;
    }

    public void initInfo(){
        id_tf.setText("");
        name_tf.setText("");
        salary_tf.setText("");
        rf.initInfo();
    }

    public void constructLabel(){
        //头部
        JLabel insert_lab=new JLabel("插入信息");
        insert_lab.setBounds(235,20,200,50);
        insert_lab.setFont(new Font("黑体",Font.BOLD,30));
        this.jf.add(insert_lab);

        this.jf.add(initLabel("ID",70));
        this.jf.add(initLabel("姓名",120));
        this.jf.add(initLabel("学历",170));
        this.jf.add(initLabel("婚姻状况",220));
        this.jf.add(initLabel("部门名",270));
        this.jf.add(initLabel("职称",320));
        this.jf.add(initLabel("月薪资",370));
        this.jf.add(initLabel("照片",420));
    }

    public JLabel initLabel(String name,int y){
        JLabel lab=new JLabel(name+":");
        lab.setBounds(175,y,100,50);
        lab.setFont(font);
        return lab;
    }

}
