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
 *@date 2020��11��12�� ����8:51:16
 *@description ע�����
 */
public class Register extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JLabel avater;//ͷ���ǩ
	private String avater_path;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			// ���ô��ڱ߿���ʽ
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
		JButton button_1 = new JButton("ѡ��ͷ��");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �����ļ�
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

		JLabel label_U = new JLabel("�û�����");
		label_U.setBounds(96, 158, 58, 15);
		contentPane.add(label_U);

		JLabel label_P = new JLabel("���룺");
		label_P.setBounds(96, 218, 36, 15);
		contentPane.add(label_P);

		usernameField = new JPasswordField();
		usernameField.setBounds(164, 158, 136, 21);
		contentPane.add(usernameField);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(164, 215, 136, 21);
		contentPane.add(passwordField_1);

		JLabel label = new JLabel("ȷ�����룺");
		label.setBounds(96, 269, 67, 15);
		contentPane.add(label);
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(164, 266, 136, 21);
		contentPane.add(passwordField_2);

		JButton button = new JButton("ȷ��ע��");
		button.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

             String password1 = String.valueOf(passwordField_1.getPassword());
             String password2 = String.valueOf(passwordField_2.getPassword());
             String username = usernameField.getText();
			 User user = ImpUserMapper.CheckLogin(username);
             if(user != null){
				 JOptionPane.showMessageDialog(null, "���û����Ѵ���");
			 }else if(!password1.equals(password2)){
				 JOptionPane.showMessageDialog(null, "�������벻ͬ�����飡");
			 }else {
             	 User newUser = new User();
             	 newUser.setUsername(username);
             	 newUser.setPassword(password1);
             	 newUser.setAvatar(avater_path);
             	 ImpUserMapper.InsertUser(newUser);
				 JOptionPane.showMessageDialog(null, username+",��ӭ�㣡");
                 dispose();
			 }
			}
		});
		button.setBounds(176, 337, 97, 23);
		contentPane.add(button);
	}
	public static void init(){
		try {
			// ���ô��ڱ߿���ʽ
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
