package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RegisterOption extends JFrame{
    private JButton manager_register_btn;
    private JButton user_register_btn;
    private JPasswordField manager_password_pf;
    public RegisterOption(){
        initInterface();
        initLogic();
    }

    public void initInterface(){
        this.setTitle("员工信息管理系统-注册选项");
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

        //
        JLabel l1 = new JLabel("REGISTER OPTION");
        Font f = new Font("黑体", Font.BOLD, 30);
        l1.setFont(f);
        //
        l1.setForeground(Color.blue);
        setComponet(gbc, 0, 0, 4, 1, l1);


        //
        JLabel l2 = new JLabel("管理员注册:");
        Font f1 = new Font("宋体", Font.BOLD, 25);
        l2.setFont(f1);
        l2.setForeground(Color.red);
        setComponet(gbc, 0, 1, 1, 1, l2);


        //
        manager_password_pf = new  JPasswordField(20);
        setComponet(gbc, 1, 1, 2, 1, manager_password_pf);

        //
        manager_register_btn = new JButton("注册");
        manager_register_btn.setToolTipText("点击注册");
        setComponet(gbc, 3, 1, 1, 1, manager_register_btn);


        //
        JLabel l3 = new JLabel("用户注册:");
        l3.setFont(f1);
        l3.setForeground(Color.red);
        setComponet(gbc, 0, 2, 1, 1, l3);

        //
        user_register_btn = new JButton("注册");
        user_register_btn.setToolTipText("点击注册");
        setComponet(gbc, 1, 2, 3, 1, user_register_btn);
    }
    public void initLogic(){
        //管理员注册
        manager_register_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String adminpwd=manager_password_pf.getText();
                if(!adminpwd.equals("123456")){
                    //
                    JOptionPane.showMessageDialog(null, "密码错误");

                }else{
                    dispose();
                    new Register(true).setVisible(true);
                }

            }
        });
        //用户注册
        user_register_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                dispose();
                new Register(false).setVisible(true);
            }
        });
    }

    private void setComponet(GridBagConstraints gbc,int x,int y,int w,int h,JComponent comp) {
        gbc.gridx=x;
        gbc.gridy=y;
        gbc.gridwidth=w;
        gbc.gridheight=h;
        this.add(comp, gbc);
    }
}
