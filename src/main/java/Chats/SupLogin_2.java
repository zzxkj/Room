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
    private JLabel label; //ͷ��
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
		
		
		JLabel lblNewLabel = new JLabel("�˻�");
		lblNewLabel.setBounds(140, 167, 37, 18);
		
		contentPane.add(lblNewLabel);	
		JLabel lblNewLabel_1 = new JLabel("����");
		lblNewLabel_1.setBounds(140, 240, 37, 18);
		contentPane.add(lblNewLabel_1);
		/*��ȡ����·��*/
		path = SupLogin_2.class.getClassLoader().getResource("images/gzh.jpg").getPath();
		//0System.out.println(path);
		image=new ImageIcon(path);
		image.setImage(image.getImage().getScaledInstance(160,140,Image.SCALE_DEFAULT));
		label = new JLabel("New label");
		label.setBounds(210, 14, 160, 140);
		label.setIcon(image);
		contentPane.add(label);


		btnNewButton = new JButton("��¼");
		btnNewButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		btnNewButton.setForeground(Color.white);
		btnNewButton.setFont(MyFont.Static);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				username=textField.getText();
				password=String.valueOf(passwordField.getPassword());
				System.out.println("�û�����"+username+"\n"+"���룺"+password);
				ImpUserMapper impUserMapper = new ImpUserMapper();
				User user = impUserMapper.CheckLogin(username);
				if(username.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "�û�������Ϊ��");
				}
				else if(passwordField.getPassword().length==0) {
					JOptionPane.showMessageDialog(null, "���벻��Ϊ��");
				}
				else if(user == null) {
					JOptionPane.showMessageDialog(null, "�û���������");
				}
				else {
					if(user.getPassword().equals(password)) {
						// Main mm=new Main();
						System.out.println("��¼�ɹ�!");
						SupLists lists = null;
						try {
							lists = new SupLists(username);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						closeJFrame();
					}
					else JOptionPane.showMessageDialog(null, "�������");
				}


			}
		});
		btnNewButton.setBounds(191, 340, 62, 27);
		contentPane.add(btnNewButton);


		btnNewButton_1 = new JButton("ע��");
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
			// ��ȡ����ʱִ�д˷���
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
			// ʧȥ����ʱִ�д˷���
		}
	}

	/**
	 * �ر��������
	 */
	public void closeJFrame() {
	this.dispose();	
	}
	
}
