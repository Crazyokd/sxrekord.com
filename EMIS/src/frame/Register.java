package frame;

import DBConnectionManager.DBConnectionManager;
import JDBCUtils.JDBCUtils;
import entity.User;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

public class Register extends JFrame{

    private User user=new User();
    private boolean ifAdmin=false;
    private JTextField username_tf;
    private JPasswordField password_o_pf;
    private JPasswordField password_t_pf;
    private JButton register_btn;

    public Register(boolean ifAdmin) {
        this.ifAdmin=ifAdmin;
        initInterface();
        initLogic();
    }

    public void initInterface(){
        this.setTitle("员工信息管理系统-注册");
        this.setSize(800, 600);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension sc = tk.getScreenSize();
        double width = sc.getWidth();
        double height = sc.getHeight();
        int x = (int)(width-800)/2;
        int y = (int)(height-600)/2;

        this.setLocation(x, y);
        this.getContentPane().setBackground(Color.pink);

//		Image img = tk.createImage("img\\label.png");
//		this.setIconImage(img);

        //
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        //
        GridBagLayout gbl = new GridBagLayout();
        //
        this.setLayout(gbl);
        //
        GridBagConstraints gbc = new GridBagConstraints();
        //
        gbc.insets = new Insets(10, 10, 20, 20);

        String admin="ADMIN";
        String title="REGISTER";
        if(ifAdmin){
            title=admin+" "+title;
            user.setType("admin");
        }


        //
        JLabel l1 = new JLabel(title);
        Font f = new Font("黑体", Font.BOLD, 30);
        l1.setFont(f);
        //
        l1.setForeground(Color.blue);
        setComponet(gbc, 0, 0, 4, 1, l1);


        //
        JLabel l2 = new JLabel("账号:");
        Font f1 = new Font("宋体", Font.BOLD, 25);
        l2.setFont(f1);
        l2.setForeground(Color.red);
        setComponet(gbc, 0, 1, 1, 1, l2);


        //
        username_tf = new  JTextField(20);
        setComponet(gbc, 1, 1, 3, 1, username_tf);


        //
        JLabel l3 = new JLabel("密码:");
        l3.setFont(f1);
        l3.setForeground(Color.red);
        setComponet(gbc, 0, 2, 1, 1, l3);


        //
        password_o_pf = new JPasswordField(20);
        setComponet(gbc, 1, 2, 3, 1, password_o_pf);

        //
        JLabel l4 = new JLabel("密码:");
        l4.setFont(f1);
        l4.setForeground(Color.red);
        setComponet(gbc, 0, 3, 1, 1, l4);


        //
        password_t_pf = new JPasswordField(20);
        setComponet(gbc, 1, 3, 3, 1, password_t_pf);

        //
        register_btn = new JButton("注册");
        register_btn.setToolTipText("点击注册");
        setComponet(gbc, 0, 4, 4, 1, register_btn);
    }

    public void initLogic(){
        register_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String username=username_tf.getText();
                String pwd1=password_o_pf.getText();
                String pwd2=password_t_pf.getText();
                String feedback_msg;
                if(!username.equals("")&&username.length()<11){
                    if(!pwd1.equals("")&&pwd1.length()<17){
                        if(pwd1.equals(pwd2)){
                            //查找用户名是否重复
                            if(!DBConnectionManager.selectUserName(username,pwd1)){
                                user.setUsername(username);
                                user.setPassword(pwd1);
                                //将用户信息更新至数据库
                                DBConnectionManager.register(user);
                                initInfo();
                                feedback_msg="注册成功";
                            }else{
                                feedback_msg="用户名已存在";
                            }
                        }else{
                            //
                            feedback_msg="两次密码不一致";
                        }
                    }else{
                        feedback_msg="密码为空或超过十六个字符";
                    }
                }else{
                    feedback_msg="用户名为空或超过十个字符";
                }
                JOptionPane.showMessageDialog(null, feedback_msg);
            }
        });
    }

    public void initInfo(){
        username_tf.setText("");
        password_o_pf.setText("");
        password_t_pf.setText("");
    }
    private void setComponet(GridBagConstraints gbc,int x,int y,int w,int h,JComponent comp){
        gbc.gridx=x;
        gbc.gridy=y;
        gbc.gridwidth=w;
        gbc.gridheight=h;
        this.add(comp, gbc);
    }

}
