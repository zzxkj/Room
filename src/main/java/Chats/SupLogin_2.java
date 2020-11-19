package Chats;

import entity.User;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import server.ImpUserMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.List;

public class SupLogin_2 extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private ImageIcon image;
    private String username=null;
    private String password=null;
    private String path;
    private JLabel label; //头像
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// 设置窗口边框样式
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {

		}

		SupLogin_2 frame = new SupLogin_2();
		frame.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public SupLogin_2() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 615, 523);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textField = new JTextField();
		textField.setToolTipText("");
		textField.setBounds(191, 164, 201, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		textField.addFocusListener(new myFocusListener());
		
		passwordField = new JPasswordField();
		passwordField.setBounds(191, 237, 201, 24);
		contentPane.add(passwordField);
		
		
		JLabel lblNewLabel = new JLabel("账户");
		lblNewLabel.setBounds(140, 167, 37, 18);
		
		contentPane.add(lblNewLabel);	
		JLabel lblNewLabel_1 = new JLabel("密码");
		lblNewLabel_1.setBounds(140, 240, 37, 18);
		contentPane.add(lblNewLabel_1);
		/*获取绝对路径*/
		path = SupLogin_2.class.getClassLoader().getResource("images/gzh.jpg").getPath();
		//0System.out.println(path);
		image=new ImageIcon(path);
		image.setImage(image.getImage().getScaledInstance(160,140,Image.SCALE_DEFAULT));
		label = new JLabel("New label");
		label.setBounds(210, 14, 160, 140);
		label.setIcon(image);
		contentPane.add(label);


		btnNewButton = new JButton("登录");
		btnNewButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		btnNewButton.setForeground(Color.white);
		btnNewButton.setFont(MyFont.Static);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username=textField.getText();
				password=String.valueOf(passwordField.getPassword());
				System.out.println("用户名："+username+"\n"+"密码："+password);
				ImpUserMapper impUserMapper = new ImpUserMapper();
				User user = impUserMapper.CheckLogin(username);
				if(username.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "用户名不能为空");
				}
				else if(passwordField.getPassword().length==0) {
					JOptionPane.showMessageDialog(null, "密码不能为空");
				}
				else if(user == null) {
					JOptionPane.showMessageDialog(null, "用户名不存在");
				}
				else {
					if(user.getPassword().equals(password)) {
						// Main mm=new Main();
						System.out.println("登录成功!");
						SupLists lists = null;
						try {
							lists = new SupLists(username);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						closeJFrame();
					}
					else JOptionPane.showMessageDialog(null, "密码错误");
				}


			}
		});
		btnNewButton.setBounds(191, 340, 62, 27);
		contentPane.add(btnNewButton);


		btnNewButton_1 = new JButton("注册");
		btnNewButton_1.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		btnNewButton_1.setForeground(Color.white);
		btnNewButton_1.setFont(MyFont.Static);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Register().init();
			}
		});
		btnNewButton_1.setBounds(330, 340, 62, 27);
		contentPane.add(btnNewButton_1);
		
	}
	class myFocusListener implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent arg0)
		{
			// 获取焦点时执行此方法
		}
		@Override
		public void focusLost(FocusEvent arg0)
		{
			new Thread(()->{
				List<String> lists = ImpUserMapper.FindAvater(textField.getText());
				System.out.println(lists.size());
				if(lists.size() > 0){
					path = lists.get(0);
					image = new ImageIcon(path);
					image.setImage(image.getImage().getScaledInstance(156,88,Image.SCALE_DEFAULT));
					label.setIcon(image);
				}
			}).start();
			// 失去焦点时执行此方法
		}
	}

	/**
	 * 关闭这个窗口
	 */
	public void closeJFrame() {
	this.dispose();	
	}
	
}
