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
        //����һ���׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�
        socket = new Socket("localhost",8798);
        this.nickName = nickName;
        this.targetName = targetName;
        out = socket.getOutputStream();
        in = socket.getInputStream();
        out.write((nickName+" "+targetName).getBytes());
    }

    //�ͻ��˿�ʼ�����ķ���
    public void start(SupChatBox chatBox) {
        this.chatBox = chatBox;
        //����"��ȡ����˷��͹�������Ϣ"���߳�,һֱ������ͨ�ŵĻش���Ϣ
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
          chatBox.inserts(nickName+":�����㷢����һ���ļ������ȥ���Ĭ���ļ�Ŀ¼�㿴���ɣ�");
          sendMessage("�����㷢����һ���ļ������ȥ���Ĭ���ļ�Ŀ¼�㿴���ɣ�");
    }
    //���߳�������ȡͨ���������˷�����������
    class ServerHandler implements Runnable {
        //�߳�run����
        public void run() {
            try {
                BufferedInputStream br = new BufferedInputStream(in);
                //������Ϣ������˴���
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
                            System.out.println("��ͼ��ϣ�");
                        }else if(new String(flag).equals(new String(FileFlag))&&len>9){
                            getFile(bytes);
                            System.out.println("�����ļ���ϣ�");
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

