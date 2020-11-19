package Chats;


import org.junit.Test;

import java.io.*;
import java.net.Socket;

public class SupClient {
    private String nickName = null;
    private String targetName = null;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private SupChatBox chatBox;
    private static final byte[] ImgFlag = "@*image*@".getBytes();
    private static final byte[] FileFlag = "@*files*@".getBytes();
    public SupClient()  {}
    public SupClient(String nickName, String targetName) throws Exception {
        //创建一个套接字并将其连接到指定主机上的指定端口号
        socket = new Socket("localhost",8798);
        this.nickName = nickName;
        this.targetName = targetName;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        out.write((nickName+" "+targetName).getBytes());
    }

    //客户端开始工作的方法
    public void start(SupChatBox chatBox) {
        this.chatBox = chatBox;
        //启动"读取服务端发送过来的消息"的线程,一直监听该通信的回传信息
        ServerHandler handler = new ServerHandler();
        Thread t = new Thread(handler);
        t.start();

    }
    private void printMessage(byte[] message){
        System.out.println(new String(message));
    }
    public void sendMessage(String message) throws IOException {
        out.write(message.getBytes());
    }
    public File getImage(byte[] Image) throws IOException {
          byte[] realResult = new byte[Image.length];
          System.arraycopy(Image,9,realResult,0,Image.length-9);
          File file = new File("C:\\Users\\zzxkj\\IdeaProjects\\Room\\src\\main\\resources\\files" + "\\aaa.jpg");
          OutputStream os = new FileOutputStream(file);
          os.write(realResult);
/*          BufferedOutputStream bos = new BufferedOutputStream(os);
          bos.write(realResult);*/
          return file;
    }
    public void sendImage(byte[] Image) throws IOException {
          byte[] ImagesData = new byte[Image.length+ImgFlag.length];
          System.arraycopy(ImgFlag,0,ImagesData,0,ImgFlag.length);
          System.arraycopy(Image,0,ImagesData,ImgFlag.length,Image.length);
          out.write(ImagesData);
    }
    private void getFile(byte[] Fil) throws IOException {
        byte[] NameBytes = new byte[100];
        byte[] realResult = new byte[Fil.length-9];
        if(Fil.length > 100)
        System.arraycopy(Fil,0,NameBytes,0,100);
        else {
            System.arraycopy(Fil,0,NameBytes,0,Fil.length);
        }
        String[] Names = new String(NameBytes).split(" ");
        System.arraycopy(Fil,9+1+Names[1].length()+1,realResult,0,Fil.length-(9+1+Names[1].length()+1));
        File file = new File("C:\\Users\\zzxkj\\IdeaProjects\\Room\\src\\main\\resources\\files" + "\\"+Names[1]);
        OutputStream os = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        bos.write(realResult);
    }
    public void sendFile(String name , byte[] Fil) throws  IOException{
          byte[] FileName = (" "+name+" ").getBytes();
          byte[] FileData = new byte[Fil.length+FileFlag.length+FileName.length];
          System.arraycopy(FileFlag,0,FileData,0,FileFlag.length);
          System.arraycopy(FileName,0,FileData,FileFlag.length,FileName.length);
          System.arraycopy(Fil,0,FileData,FileFlag.length+FileName.length,Fil.length);
          out.write(FileData);
          chatBox.inserts(nickName+":我向你发送了一个文件，你快去你的默认文件目录你看看吧！");
          sendMessage("我向你发送了一个文件，你快去你的默认文件目录你看看吧！");
    }
    //该线程用来读取通过服务器端发送来的数据
    class ServerHandler implements Runnable {
        //线程run方法
        public void run() {
            try {
                BufferedInputStream br = new BufferedInputStream(in);
                //发送信息到服务端处理
                while(true) {
                    String message = null;
                    byte[] bytes;
                    byte[] flag = new byte[9];
                    int len = br.available();
                    if(len > 0){
                        System.out.println(len);
                        bytes = new byte[len];
                        br.read(bytes);
                        if(len > 9) System.arraycopy(bytes,0,flag,0,9);
                        if(new String(flag).equals(new String(ImgFlag))&&len > 9){
                            chatBox.insertIcon(getImage(bytes),targetName);
                            System.out.println("插图完毕！");
                        }else if(new String(flag).equals(new String(FileFlag))&&len>9){
                            getFile(bytes);
                            System.out.println("接收文件完毕！");
                        }else {
                            message = new String(bytes);
                            chatBox.inserts(message);
                            System.out.println(message);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

