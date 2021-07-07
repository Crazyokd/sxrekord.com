package frame;

import DBConnectionManager.DBConnectionManager;
import entity.Photo;
import entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class MainFrame{


    private JFrame jf=new JFrame("员工信息管理系统-主界面");

    public MainFrame(){
        this.jf.setLayout(null);
        this.jf.setBounds(200,200,530,400);
        this.jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void showWindow(){

        JButton insert_btn=new JButton("插入信息");
        JButton sud_btn=new JButton("查询、修改、删除信息");
        JButton census_btn=new JButton("统计信息");

        insert_btn.setBounds(150,50,200,50);
        insert_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(DBConnectionManager.getCur_user().getType().equals("admin")){
                    new InsertInfoFrame().showWindow();
                }else{
                    System.out.println("您没有权限插入信息哦");
                }

            }
        });
        sud_btn.setBounds(150,150,200,50);
        sud_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RUDFrame().frame.setVisible(true);
            }
        });
        census_btn.setBounds(150,250,200,50);
        census_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CensusFrame().showWindow();
            }
        });

        this.jf.add(insert_btn);
        this.jf.add(sud_btn);
        this.jf.add(census_btn);

        this.jf.setVisible(true);

        this.jf.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                DBConnectionManager.closeConnection();
                System.out.println("关闭数据库连接");
                //清空图片缓存
                File[] files=new File(Photo.path_prefix).listFiles();
                for(int i=0;i<files.length;i++)
                    files[i].delete();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

}
