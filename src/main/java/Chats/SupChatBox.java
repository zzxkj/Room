package Chats;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 *@auhtor zzxkj
 *@date 2020年10月20日 下午9:36:07
 *@description 
 */
public class SupChatBox extends JFrame {
	private String nickName = null;
	private String targetName = null;
	private SupClient client = null;
	private JPanel contentPane;//总容器
	private JScrollPane scrollPane_content ,srollPane_input;//内容显示容器、内容输入容器
	private JTextPane textPane_content ,textPane_input;//内容显示、内容输入组件
	private JButton button_sent, button_image, button_file, button_emoticon;//发送、照片、文件功能按钮
	private StyledDocument doc = null;  //JTextPane的关联的模型。
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
					SupChatBox frame = new SupChatBox();
					frame.setVisible(true);//可见
					frame.setResizable(false);//大小不可变
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
    /**
     * start
     */
	public void start(SupChatBox chatBox) {
		client.start(chatBox);
	}
	/**
	 * Create the frame.
	 */
	public SupChatBox() {
	}

	public SupChatBox(String nickName, String targetName) throws Exception {
		try {
			// 设置窗口边框样式
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.nickName = nickName;
		this.targetName = targetName;
		this.client = new SupClient(nickName,targetName);
		setVisible(true);//可见
		setResizable(false);//大小不可变
		setLocationRelativeTo(null);//居中
		setBounds(100, 100, 694, 605);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane_content = new JScrollPane();
		scrollPane_content.setBounds(10, 10, 616, 310);
		contentPane.add(scrollPane_content);
	
		textPane_content = new JTextPane();
		textPane_content.setEditable(false);
		scrollPane_content.setViewportView(textPane_content);
		doc = textPane_content.getStyledDocument(); // 获得JTextPane的Document
		
		button_sent = new JButton("发送");
		button_sent.setBounds(529, 330, 97, 23);
		contentPane.add(button_sent);
		this.getRootPane().setDefaultButton(button_sent); // 默认回车按钮
		button_image = new JButton("图片");

		button_image.setBounds(20, 330, 97, 23);
		contentPane.add(button_image);
		
		button_emoticon = new JButton("表情");
		button_emoticon.setBounds(141, 330, 97, 23);
		contentPane.add(button_emoticon);
		
		button_file = new JButton("文件");
		button_file.setBounds(275, 330, 97, 23);
		contentPane.add(button_file);
		
		JScrollPane scrollPane_input = new JScrollPane();
		scrollPane_input.setBounds(10, 373, 616, 150);
		contentPane.add(scrollPane_input);
		
		textPane_input = new JTextPane();
		scrollPane_input.setViewportView(textPane_input);
		
		button_sent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(()->{
					String content = textPane_input.getText();
					if(content.length()==0) JOptionPane.showMessageDialog(null, "不能为空!");
					else insert();
					try {
						client.sendMessage(content);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					textPane_input.setText("");
				}).start();
			}
		});
		/**选择图片*/
		button_image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//文件流
				InputStream inputStream = null;
				// 查找文件
			    JFileChooser dir = new JFileChooser();
			    dir.showOpenDialog(null);
			    File file = dir.getSelectedFile();
			    if(file!=null){
					try {
						inputStream = new FileInputStream(file);
						try {
							byte[] img = new byte[inputStream.available()];
							inputStream.read(img);
							client.sendImage(img);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}
					insertIcon(file,nickName); // 插入图片
				}
			}
		});
		/**选择表情*/
		button_emoticon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(()->{
					new EmoticonK().init();
				}).start();
			}
		});
		/**选择文件*/
		button_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//文件流
				InputStream inputStream = null;
				// 查找文件
				JFileChooser dir = new JFileChooser();
				dir.showOpenDialog(null);
				File file = dir.getSelectedFile();
				String path = file.getPath();
				System.out.println(path);
				if(file!=null){
					try {
						inputStream = new FileInputStream(file);
						try {
							byte[] fil = new byte[inputStream.available()];
							inputStream.read(fil);
							client.sendFile(file.getName(),fil);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
	}
	 public void insertIcon(File file,String name) {

		  ImageIcon image = new ImageIcon(file.getPath());
		  int width = image.getIconWidth();
		  int height = image.getIconWidth();
		  System.out.println("width:"+width+"\n"+"height:"+height);
          ImageIcon img = change(image,0.5);
		 try { // 插入图片
			 doc.insertString(doc.getLength(),  name+":\n",
					 new SimpleAttributeSet());
			 textPane_content.setCaretPosition(doc.getLength()); // 设置插入位置
			 textPane_content.insertIcon(img); // 插入图片
			 doc.insertString(doc.getLength(),  "\n",
					 new SimpleAttributeSet());	; //换行
		 } catch (BadLocationException e) {
			 e.printStackTrace();
		 }

		 }
	  public ImageIcon change(ImageIcon image,double i){//  i 为放缩的倍数 	 
	          int width=(int) (image.getIconWidth()*i);
	          int height=(int) (image.getIconHeight()*i);
	          Image img=image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);//第三个值可以去查api是图片转化的方式
	          ImageIcon image2=new ImageIcon(img); 
	          return image2;
	    }
	 private void insert() {
		  try { // 插入文本
		     doc.insertString(doc.getLength(), nickName+":"+textPane_input.getText() + "\n",
		     new SimpleAttributeSet());
		  } catch (BadLocationException e) {
		   e.printStackTrace();
		  }
		 }
	public void inserts(String content) {
		try { // 插入文本
			doc.insertString(doc.getLength(), content + "\n",
					new SimpleAttributeSet());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**内部表情包类*/
	public class EmoticonK extends JFrame {

		private JPanel contentPane;

		/**
		 * Create the frame.
		 */
		public void init(){
			try {
				// 设置窗口边框样式
				BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
				BeautyEyeLNFHelper.launchBeautyEyeLNF();
				UIManager.put("RootPane.setupButtonVisible", false);

			} catch (Exception e) {
				e.printStackTrace();
			}

			EmoticonK frame = new EmoticonK();
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
		}

		public EmoticonK() {
			String path1,path2,path3,path4,path5,path6,path7,path8,path9;
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setResizable(false);
			setBounds(100, 100, 550, 480);
			contentPane = new JPanel();
			contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(contentPane);
			contentPane.setLayout(null);
			path1= SupChatBox.class.getClassLoader().getResource("images/emoticons/1.jpg").getPath();
			ImageIcon image = new ImageIcon(path1);
			image.setImage(image.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton = new JButton(image);
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					   System.out.println(1);
						System.out.println(2);
						File file = new File(path1);
						try {
							InputStream inputStream = new FileInputStream(file);
							byte[] img = new byte[inputStream.available()];
							inputStream.read(img);
							client.sendImage(img);
							insertIcon(file,nickName);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
				}
			});
			btnNewButton.setFocusable(false);
			btnNewButton.setContentAreaFilled(false);
			btnNewButton.setBounds(45, 41, 55,54);
			contentPane.add(btnNewButton);
			path2 = SupChatBox.class.getClassLoader().getResource("images/emoticons/2.jpg").getPath();
			ImageIcon image1 = new ImageIcon(path2);
			image1.setImage(image1.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_1 = new JButton(image1);
			btnNewButton_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path2);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_1.setFocusable(false);
			btnNewButton_1.setContentAreaFilled(false);
			btnNewButton_1.setBounds(214, 41, 55, 54);
			contentPane.add(btnNewButton_1);
			path3 = SupChatBox.class.getClassLoader().getResource("images/emoticons/3.jpg").getPath();
			ImageIcon image2 = new ImageIcon(path3);
			image2.setImage(image2.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_2 = new JButton(image2);
			btnNewButton_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path3);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_2.setFocusable(false);
			btnNewButton_2.setContentAreaFilled(false);
			btnNewButton_2.setBounds(365, 41, 55,54);
			contentPane.add(btnNewButton_2);
			path4 = SupChatBox.class.getClassLoader().getResource("images/emoticons/4.jpg").getPath();
			ImageIcon image3 = new ImageIcon(path4);
			image3.setImage(image3.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_3 = new JButton(image3);
			btnNewButton_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path4);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_3.setFocusable(false);
			btnNewButton_3.setContentAreaFilled(false);
			btnNewButton_3.setBounds(45, 178, 55,54);
			contentPane.add(btnNewButton_3);
			path5 = SupChatBox.class.getClassLoader().getResource("images/emoticons/5.jpg").getPath();
			ImageIcon image4 = new ImageIcon(path5);
			image4.setImage(image4.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_4 = new JButton(image4);
			btnNewButton_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path5);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_4.setFocusable(false);
			btnNewButton_4.setContentAreaFilled(false);
			btnNewButton_4.setBounds(214, 178, 55,54);
			contentPane.add(btnNewButton_4);
			path6 = SupChatBox.class.getClassLoader().getResource("images/emoticons/6.jpg").getPath();
			ImageIcon image5 = new ImageIcon(path6);
			image5.setImage(image5.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_5 = new JButton(image5);
			btnNewButton_5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path6);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_5.setFocusable(false);
			btnNewButton_5.setContentAreaFilled(false);
			btnNewButton_5.setBounds(365, 178, 55,54);
			contentPane.add(btnNewButton_5);

			path7 = SupChatBox.class.getClassLoader().getResource("images/emoticons/7.jpg").getPath();
			ImageIcon image6 = new ImageIcon(path7);
			image6.setImage(image6.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_6 = new JButton(image6);
			btnNewButton_6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path7);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_6.setFocusable(false);
			btnNewButton_6.setContentAreaFilled(false);
			btnNewButton_6.setBounds(45, 317, 55,54);
			contentPane.add(btnNewButton_6);

			path8 = SupChatBox.class.getClassLoader().getResource("images/emoticons/8.jpg").getPath();
			ImageIcon image7 = new ImageIcon(path8);
			image7.setImage(image7.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_7 = new JButton(image7);
			btnNewButton_7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path8);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_7.setFocusable(false);
			btnNewButton_7.setContentAreaFilled(false);
			btnNewButton_7.setBounds(214, 317, 55,54);
			contentPane.add(btnNewButton_7);

			path9 = SupChatBox.class.getClassLoader().getResource("images/emoticons/9.jpg").getPath();
			ImageIcon image8 = new ImageIcon(path9);
			image8.setImage(image8.getImage().getScaledInstance(55,54, Image.SCALE_DEFAULT));
			JButton btnNewButton_8 = new JButton(image8);
			btnNewButton_8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					File file = new File(path9);
					try {
						InputStream inputStream = new FileInputStream(file);
						byte[] img = new byte[inputStream.available()];
						inputStream.read(img);
						client.sendImage(img);
						insertIcon(file,nickName);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			});
			btnNewButton_8.setFocusable(false);
			btnNewButton_8.setContentAreaFilled(false);
			btnNewButton_8.setBounds(365, 317, 55,54);
			contentPane.add(btnNewButton_8);

		}
	}

}
