package frame;

import DBConnectionManager.DBConnectionManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.*;

public class Login extends JFrame{
	private JButton login_btn;
	private JButton register_btn;
	private JTextField username_tf;
	private JPasswordField password_pf;

	public Login(){
		initInterface();
		initLogic();
		this.setVisible(true);
		DBConnectionManager.connectDB();
	}

	public void loginDB(String username,String password){
		//确保连接数据库
		while(!DBConnectionManager.isOpened()){
			System.out.println("数据库连接异常......");
		}

		//登录
		if(DBConnectionManager.selectUserName(username,password)){
			new MainFrame().showWindow();
			dispose();
			System.out.println(DBConnectionManager.getCur_user().getType()+":"+
					DBConnectionManager.getCur_user().getUsername()+"登录成功");
		}else{
			JOptionPane.showMessageDialog(this,"账号或密码不正确");
		}

	}

	public static void main(String[] args) {
		new Login();
	}

	public void initInterface(){
		this.setTitle("员工信息管理系统-登录");
		this.setSize(800, 600);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension sc = tk.getScreenSize();
		double width = sc.getWidth();
		double height = sc.getHeight();
		int x = (int)(width-800)/2;
		int y = (int)(height-600)/2;

		this.setLocation(x, y);
		this.getContentPane().setBackground(Color.pink);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//
		GridBagLayout gbl = new GridBagLayout();
		//
		this.setLayout(gbl);
		//
		GridBagConstraints gbc = new GridBagConstraints();
		//
		gbc.insets = new Insets(10, 10, 20, 20);

		//
		JLabel l1 = new JLabel("LOGIN");
		Font f = new Font("宋体", Font.BOLD, 30);
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
		password_pf = new JPasswordField(20);
		setComponet(gbc, 1, 2, 3, 1, password_pf);


		//
		login_btn = new JButton("登录");
		login_btn.setToolTipText("点击登录");
		setComponet(gbc, 1, 3, 1, 1, login_btn);

		//
		register_btn = new JButton("注册");
		register_btn.setToolTipText("点击注册");
		setComponet(gbc, 3, 3, 1, 1, register_btn);
	}
	public void initLogic(){
		//登录按钮的点击事件
		login_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loginDB(username_tf.getText(),password_pf.getText());
			}
		});
		//注册按钮的点击事件
		register_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("进入RegisterOption");
				new RegisterOption().setVisible(true);
			}
		});
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				DBConnectionManager.closeConnection();
				System.out.println("关闭数据库连接");
			}

			@Override
			public void windowClosed(WindowEvent e) {
//				JDBCUtils.close(resultSet,preparedStatement,connection);
				System.out.println("进入另一个窗口，login窗口销毁");
			}

			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
	}

	private void setComponet(GridBagConstraints gbc,int x,int y,int w,int h,JComponent comp){
		gbc.gridx=x;
		gbc.gridy=y;
		gbc.gridwidth=w;
		gbc.gridheight=h;
		this.add(comp, gbc);
	}

}
