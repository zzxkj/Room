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
import java.io.File;

/**
 *@auhtor zzxkj
 *@date 2020年11月12日 下午8:51:16
 *@description 注册界面
 */
public class Register extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JLabel avater;//头像标签
	private String avater_path;

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
            e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public Register() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 460, 470);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		avater = new JLabel("          avater");
		avater.setBounds(144, 10, 156, 88);
		contentPane.add(avater);
		JButton button_1 = new JButton("选择头像");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 查找文件
				JFileChooser dir = new JFileChooser();
				dir.showOpenDialog(null);
				File file = dir.getSelectedFile();
				String path = file.getPath();
				if(file!= null)
				{
					avater_path = path;
				}
				else {
					avater_path = "C:\\Users\\zzxkj\\IdeaProjects\\Room\\src\\main\\resources\\images\\gzh.jpg";
				}
				ImageIcon image=new ImageIcon(path);
				image.setImage(image.getImage().getScaledInstance(156,88,Image.SCALE_DEFAULT));
				avater.setIcon(image);
				System.out.println(avater_path);
			}
		});
		button_1.setBounds(176, 108, 97, 23);
		contentPane.add(button_1);

		JLabel label_U = new JLabel("用户名：");
		label_U.setBounds(96, 158, 58, 15);
		contentPane.add(label_U);

		JLabel label_P = new JLabel("密码：");
		label_P.setBounds(96, 218, 36, 15);
		contentPane.add(label_P);

		usernameField = new JPasswordField();
		usernameField.setBounds(164, 158, 136, 21);
		contentPane.add(usernameField);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(164, 215, 136, 21);
		contentPane.add(passwordField_1);

		JLabel label = new JLabel("确认密码：");
		label.setBounds(96, 269, 67, 15);
		contentPane.add(label);
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(164, 266, 136, 21);
		contentPane.add(passwordField_2);

		JButton button = new JButton("确认注册");
		button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

             String password1 = String.valueOf(passwordField_1.getPassword());
             String password2 = String.valueOf(passwordField_2.getPassword());
             String username = usernameField.getText();
			 User user = ImpUserMapper.CheckLogin(username);
             if(user != null){
				 JOptionPane.showMessageDialog(null, "该用户名已存在");
			 }else if(!password1.equals(password2)){
				 JOptionPane.showMessageDialog(null, "两次密码不同，请检查！");
			 }else {
             	 User newUser = new User();
             	 newUser.setUsername(username);
             	 newUser.setPassword(password1);
             	 newUser.setAvatar(avater_path);
             	 ImpUserMapper.InsertUser(newUser);
				 JOptionPane.showMessageDialog(null, username+",欢迎你！");
                 dispose();
			 }
			}
		});
		button.setBounds(176, 337, 97, 23);
		contentPane.add(button);
	}
	public static void init(){
		try {
			// 设置窗口边框样式
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register frame = new Register();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
