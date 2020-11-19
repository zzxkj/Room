package Chats;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SupServer {
    public ServerSocket server;
    //私聊用map，key为昵称，value是对应消息流，广播则遍历value
    private Map<String,OutputStream> pri_map;
    //图片标志符
    private static final String ImgFlag = "@*image*@";
    //文件标志符
    private static final String FileFlag = "@*files*@";
    //登录列表标志符
    private static final String ListsFlag = "@*lists*@";
    //好友请求标志符
    private static final String ListsShow = "@*YEORN*@";
    // 初始化服务端
    public SupServer() throws Exception {
        // 初始化的同时申请端口号
        server = new ServerSocket(8798);
        //初始化用于保存客户端输出流的Map集合
        pri_map = new HashMap<String,OutputStream>();
    }
    //将客户端发送过来的信息以键值对形式存入map集合,(昵称,对应输出流)
    private void putIn(String key,OutputStream value){
        pri_map.put(key,value);
    }
    //将给定的消息发送给私聊的客户端
    private void  sendMessageToOne(String name,String message) throws IOException {
        //将对应客户端的聊天信息取出作为私聊内容发送出
        OutputStream out=pri_map.get(name);   //此处name为键，get(name)获取其值，即目标客户端的输出流（写）
        if (out==null)
        {
            System.out.println("null");
        }
        if (out!=null){
            System.out.println(message);
            out.write(message.getBytes());        //如果目标输出流对象存在，将信息写入该流
        }
    }
    private void  sendFileToOne(String name, byte[] Fil) throws IOException {
        //将对应客户端的聊天信息取出作为私聊内容发送出
        OutputStream out=pri_map.get(name);   //此处name为键，get(name)获取其值，即目标客户端的输出流（写）
        if (out==null)
        {
            System.out.println("null");
        }
        if (out!=null){
            System.out.println(Fil.length);
            out.write(Fil);        //如果目标输出流对象存在，将信息写入该流
        }
    }
    //将指定的输出流从共享集合map中删除
    private synchronized void remove(String key) {
        pri_map.remove(key);
    }
    //服务端开始工作的方法
    public void start(){
        try {
            while(true) {
                System.out.println("等待客户端连接...");
                //侦听并接受想连接到此套接字的客户端连接
                Socket socket = server.accept();
                //获取客户端ip地址
                InetAddress address = socket.getInetAddress();
                System.out.println("客户端：" + address.getHostAddress() + "连接成功！ ");
                //创建一个实现Runnale接口的线程处理与客户端的交互
                ClientHandler handler= new ClientHandler(socket);
                Thread t = new Thread(handler);
                //启动线程
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //ClientHandler：处理与客户端的交互
    class ClientHandler implements Runnable{
        //该线程处理的客户端的Socket
        private Socket socket;
        // 客户端的地址信息，区分不同客户端(ip地址)
        private String host;
        //当前客户端用户昵称
        private String nickName;
        //消息接收者昵称
        private String targetName;
        //构造方法
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        //线程的run()方法
        public void run() {
            try {
                //服务端的输入流br读取客户端发送的消息
                InputStream in = socket.getInputStream();
                BufferedInputStream br = new BufferedInputStream(in);
                OutputStream out = socket.getOutputStream();
                /*获取通信的两个客户端名和当前登录的列表*/
                int len = br.available();
                byte[] bytes = new byte[len];
                br.read(bytes);
                String name = new String(bytes);
                String[] names = name.split(" ");
                nickName = names[0];
                targetName = names[1];
                if(nickName.equals(ListsFlag)){
                    /*当前登录的列表*/
                    System.out.println(targetName+"上线了！");
                    System.out.println(ListsFlag+targetName);
                    putIn(ListsFlag+targetName,out);
                }
                else {
                    /*获取通信的两个客户端名*/
                    System.out.println(nickName+"的消息接收者："+targetName);
                    putIn(nickName,out);
                }
                /*存放对应通信的映射*/
                /*将当前客户端输入的消息发送给消息接收者*/
                //好友请求判断
                String[] reflects = null;
                String message = null;
                //当前请求的前缀，用于判断当前请求的类型/功能
                byte[] flag = new byte[9];
                System.out.println(socket.getInputStream().available());
               while(true) {
                   len = br.available();
                  // System.out.println(len);
                    if (len > 0){
                        bytes = new byte[len];
                        br.read(bytes);
                        if(len>=9) {
                            System.arraycopy(bytes,0,flag,0,9);
                        }
                        if(new String(flag).equals(ImgFlag)){
                            System.arraycopy(bytes,9,flag,0,9);
                            sendFileToOne(targetName,bytes);
                            System.out.println("传图片");
                        }else if(new String(flag).equals(FileFlag)){
                            sendFileToOne(targetName,bytes);
                            System.out.println("传文件");
                        }else if (new String(flag).equals(ListsFlag)){
                            System.out.println("来自好友请求");
                            message = new String(bytes);
                            System.out.println(message);
                            System.out.println(ListsFlag+message.substring(10));
                            sendMessageToOne(ListsFlag+message.substring(10),ListsFlag+" "+targetName);
                        }else if (new String(flag).equals(ListsShow)){
                            System.out.println("是否同意好友请求！");
                            message = new String(bytes);
                            reflects = message.split(" ");
                            System.out.println(message);
                            System.out.println(ListsFlag+reflects[1]);
                            sendMessageToOne(ListsFlag+reflects[1],ListsShow+" "+reflects[2]+" "+reflects[3]);
                        }else{
                            System.out.println("传文字");
                            message = new String(bytes);
                            System.out.println(message);
                            message=nickName+":"+message;//发送的消息格式: 发送者昵称:消息
                            sendMessageToOne(targetName, message);
                        }
                    }
                }
            } catch (Exception e) {
            }finally {
                //处理当前客户端断开后的逻辑
                //将该客户端的输出流pw从共享集合中删除
                remove(nickName);
                try {
                    sendMessageToOne(targetName,nickName+"下线了！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        try {
            SupServer server = new SupServer();
            server.start();     //执行服务端start方法
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务端建立联系失败！");
        }
    }
}

