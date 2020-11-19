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
 *@date 2020年11月8日 下午4:48:07
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
		//创建一个套接字并将其连接到指定主机上的指定端口号
		socket = new Socket("localhost",8798);
		out = socket.getOutputStream();
		in = socket.getInputStream();
		out.write(("@*lists*@ "+nickName).getBytes());
		try {
			// 设置窗口边框样式
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
		/*获取绝对路径*/
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

		JButton btnNewButton_3 = new JButton("添加好友");
		btnNewButton_3.setBounds(196, 72, 97, 23);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String U_N = textField.getText();
					Boolean flag = ImpUserMapper.checkUsername(U_N);
					if(U_N.equals("###")){
						JOptionPane.showMessageDialog(null,"该输入名称不合法!","警告",JOptionPane.ERROR_MESSAGE);
					} else if(!flag){
						JOptionPane.showMessageDialog(null,"该用户不存在!","警告",JOptionPane.ERROR_MESSAGE);
					}else if(U_N.equals(nickName)){
						JOptionPane.showMessageDialog(null,"不能添加自己为好友!","警告",JOptionPane.ERROR_MESSAGE);
					}else if (friends.contains(U_N)){
						JOptionPane.showMessageDialog(null,U_N+"已在你的好友列表中，不能重复添加为好友!","警告",JOptionPane.ERROR_MESSAGE);
					} else{
						int K_F = JOptionPane.showConfirmDialog(null,"是否确定向"+U_N+"发送添加好友请求？","提示",JOptionPane.YES_NO_OPTION);//YES为0，No为1
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
	//该线程用来读取通过服务器端发送来的消息
	class AddFriend implements Runnable {
		//线程run方法
		public void run() {
			try {
				//创建Scanner类,用于获取用户输入
				System.out.println(nickName+"登录成功！！！");
				String[] message = null;
				byte[] bytes;
				int len=0;
				System.out.println(len);
				//发送信息到服务端处理
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
						/*是否同意好友请求的回判*/
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
							/*添加好友*/
							panel.add(button);
							panel.revalidate();
							indexY += 80;
						}
						/*是否同意好友请求的接收和判断*/
						else if(len>9 && message[0].equals(ListsFlag)) {
							int K_F = JOptionPane.showConfirmDialog(null,"是否同意"+message[1]+"的添加好友请求？","提示",JOptionPane.YES_NO_OPTION);//YES为0，No为1
							if (K_F == 0) {
								/*同意则数据库添加好友列表信息*/
								ImpUserMapper.InsertFriend(new Friend(nickName,message[1],"Y"));
								ImpUserMapper.InsertFriend(new Friend(message[1],nickName,"Y"));
								/*同意则发送同意信息，以供发送请求方的回判*/
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
