package Chats;

import entity.Friend;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import server.ImpUserMapper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 *@auhtor zzxkj
 *@date 2020��11��8�� ����4:48:07
 *@description
 */
public class SupLists extends JFrame {
	private String nickName = null;
	private String targetName = null;
	private JPanel contentPane;
	private JTextField textField;
	private Integer indexY = 0;
	private List<String> friends;
	private JScrollPane scrollPane;
	private JPanel panel;
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private static final String ListsFlag = "@*lists*@";
	private static final String ListsShow = "@*YEORN*@";
	/**
	 * Create the frame.
	 */
	public SupLists() {}
	public SupLists(String nickName) throws IOException {
		//����һ���׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�
		socket = new Socket("localhost",8798);
		out = socket.getOutputStream();
		in = socket.getInputStream();
		out.write(("@*lists*@ "+nickName).getBytes());
		try {
			// ���ô��ڱ߿���ʽ
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nickName = nickName;
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 359, 704);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		List<String> Avaters = ImpUserMapper.FindAvater(nickName);
		/*��ȡ����·��*/
		String path = Avaters.get(0);
		//System.out.println(path);
		ImageIcon image=new ImageIcon(path);
		image.setImage(image.getImage().getScaledInstance(60,42,Image.SCALE_DEFAULT));
		JLabel lblNewLabel = new JLabel("image");
		lblNewLabel.setBounds(112, 10, 60, 42);
		lblNewLabel.setIcon(image);
		contentPane.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(65, 73, 110, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		JButton btnNewButton_3 = new JButton("��Ӻ���");
		btnNewButton_3.setBounds(196, 72, 97, 23);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String U_N = textField.getText();
					Boolean flag = ImpUserMapper.checkUsername(U_N);
					if(U_N.equals("###")){
						JOptionPane.showMessageDialog(null,"���������Ʋ��Ϸ�!","����",JOptionPane.ERROR_MESSAGE);
					} else if(!flag){
						JOptionPane.showMessageDialog(null,"���û�������!","����",JOptionPane.ERROR_MESSAGE);
					}else if(U_N.equals(nickName)){
						JOptionPane.showMessageDialog(null,"��������Լ�Ϊ����!","����",JOptionPane.ERROR_MESSAGE);
					}else if (friends.contains(U_N)){
						JOptionPane.showMessageDialog(null,U_N+"������ĺ����б��У������ظ����Ϊ����!","����",JOptionPane.ERROR_MESSAGE);
					} else{
						int K_F = JOptionPane.showConfirmDialog(null,"�Ƿ�ȷ����"+U_N+"������Ӻ�������","��ʾ",JOptionPane.YES_NO_OPTION);//YESΪ0��NoΪ1
						if(K_F == 0){
							try {
								out.write((ListsFlag+" "+U_N).getBytes());
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							System.out.println(ListsFlag+" "+U_N);
							//ImpUserMapper.InsertFriend(new Friend(nickName,U_N,"N"));
						}
					}
			}
		});
		contentPane.add(btnNewButton_3);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 115, 283, 508);
		contentPane.add(scrollPane);

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setPreferredSize(new Dimension(250,1016));
		panel.setLayout(null);
		friends = ImpUserMapper.FindFriends(nickName);
		for (String friend : friends) {
			JButton button = new JButton(friend);
			button.setBounds(0, indexY, 250, 60);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Thread(()->{
						try {
							SupChatBox chatBox = new SupChatBox(nickName,button.getText());
							chatBox.start(chatBox);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}).start();
				}
			});
			panel.add(button);
			panel.revalidate();
			indexY+= 80;
		}
		AddFriend addFriend = new AddFriend();
		new Thread(addFriend).start();
	}
	public void ListenerAddFriend() throws IOException {
		AddFriend addFriend = new AddFriend();
		new Thread(addFriend).start();
	}
	//���߳�������ȡͨ���������˷���������Ϣ
	class AddFriend implements Runnable {
		//�߳�run����
		public void run() {
			try {
				//����Scanner��,���ڻ�ȡ�û�����
				System.out.println(nickName+"��¼�ɹ�������");
				String[] message = null;
				byte[] bytes;
				int len=0;
				System.out.println(len);
				//������Ϣ������˴���
				while(true) {
					len = in.available();
					//System.out.println(len);
					if(len > 0){
						System.out.println(len);
						bytes = new byte[len];
						in.read(bytes);
						message = new String(bytes).split(" ");
/*						System.out.println(message.length);
						for (String s : message){
							System.out.println(s);
						}*/
						/*�Ƿ�ͬ���������Ļ���*/
						if (message[0].equals(ListsShow) && message[2].equals("YES")){
							JButton button = new JButton(message[1]);
							button.setBounds(0, indexY, 250, 60);
							button.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									new Thread(() -> {
										try {
											SupChatBox chatBox = new SupChatBox(nickName, button.getText());
											chatBox.start(chatBox);
										} catch (Exception ex) {
											ex.printStackTrace();
										}
									}).start();
								}
							});
							/*��Ӻ���*/
							panel.add(button);
							panel.revalidate();
							indexY += 80;
						}
						/*�Ƿ�ͬ���������Ľ��պ��ж�*/
						else if(len>9 && message[0].equals(ListsFlag)) {
							int K_F = JOptionPane.showConfirmDialog(null,"�Ƿ�ͬ��"+message[1]+"����Ӻ�������","��ʾ",JOptionPane.YES_NO_OPTION);//YESΪ0��NoΪ1
							if (K_F == 0) {
								/*ͬ�������ݿ���Ӻ����б���Ϣ*/
								ImpUserMapper.InsertFriend(new Friend(nickName,message[1],"Y"));
								ImpUserMapper.InsertFriend(new Friend(message[1],nickName,"Y"));
								/*ͬ������ͬ����Ϣ���Թ��������󷽵Ļ���*/
								out.write((ListsShow+" "+message[1]+" "+nickName+" "+"YES").getBytes());
								JButton button = new JButton(message[1]);
								button.setBounds(0, indexY, 250, 60);
								button.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										new Thread(() -> {
											try {
												SupChatBox chatBox = new SupChatBox(nickName, button.getText());
												chatBox.start(chatBox);
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}).start();
									}
								});
								panel.add(button);
								panel.revalidate();
								indexY += 80;
							}else {
								out.write((ListsShow+" "+message[1]+" "+nickName+" "+"NO").getBytes());
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
