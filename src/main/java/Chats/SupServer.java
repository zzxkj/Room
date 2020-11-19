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
    //˽����map��keyΪ�ǳƣ�value�Ƕ�Ӧ��Ϣ�����㲥�����value
    private Map<String,OutputStream> pri_map;
    //ͼƬ��־��
    private static final String ImgFlag = "@*image*@";
    //�ļ���־��
    private static final String FileFlag = "@*files*@";
    //��¼�б��־��
    private static final String ListsFlag = "@*lists*@";
    //���������־��
    private static final String ListsShow = "@*YEORN*@";
    // ��ʼ�������
    public SupServer() throws Exception {
        // ��ʼ����ͬʱ����˿ں�
        server = new ServerSocket(8798);
        //��ʼ�����ڱ���ͻ����������Map����
        pri_map = new HashMap<String,OutputStream>();
    }
    //���ͻ��˷��͹�������Ϣ�Լ�ֵ����ʽ����map����,(�ǳ�,��Ӧ�����)
    private void putIn(String key,OutputStream value){
        pri_map.put(key,value);
    }
    //����������Ϣ���͸�˽�ĵĿͻ���
    private void  sendMessageToOne(String name,String message) throws IOException {
        //����Ӧ�ͻ��˵�������Ϣȡ����Ϊ˽�����ݷ��ͳ�
        OutputStream out=pri_map.get(name);   //�˴�nameΪ����get(name)��ȡ��ֵ����Ŀ��ͻ��˵��������д��
        if (out==null)
        {
            System.out.println("null");
        }
        if (out!=null){
            System.out.println(message);
            out.write(message.getBytes());        //���Ŀ�������������ڣ�����Ϣд�����
        }
    }
    private void  sendFileToOne(String name, byte[] Fil) throws IOException {
        //����Ӧ�ͻ��˵�������Ϣȡ����Ϊ˽�����ݷ��ͳ�
        OutputStream out=pri_map.get(name);   //�˴�nameΪ����get(name)��ȡ��ֵ����Ŀ��ͻ��˵��������д��
        if (out==null)
        {
            System.out.println("null");
        }
        if (out!=null){
            System.out.println(Fil.length);
            out.write(Fil);        //���Ŀ�������������ڣ�����Ϣд�����
        }
    }
    //��ָ����������ӹ�����map��ɾ��
    private synchronized void remove(String key) {
        pri_map.remove(key);
    }
    //����˿�ʼ�����ķ���
    public void start(){
        try {
            while(true) {
                System.out.println("�ȴ��ͻ�������...");
                //���������������ӵ����׽��ֵĿͻ�������
                Socket socket = server.accept();
                //��ȡ�ͻ���ip��ַ
                InetAddress address = socket.getInetAddress();
                System.out.println("�ͻ��ˣ�" + address.getHostAddress() + "���ӳɹ��� ");
                //����һ��ʵ��Runnale�ӿڵ��̴߳�����ͻ��˵Ľ���
                ClientHandler handler= new ClientHandler(socket);
                Thread t = new Thread(handler);
                //�����߳�
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //ClientHandler��������ͻ��˵Ľ���
    class ClientHandler implements Runnable{
        //���̴߳���Ŀͻ��˵�Socket
        private Socket socket;
        // �ͻ��˵ĵ�ַ��Ϣ�����ֲ�ͬ�ͻ���(ip��ַ)
        private String host;
        //��ǰ�ͻ����û��ǳ�
        private String nickName;
        //��Ϣ�������ǳ�
        private String targetName;
        //���췽��
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        //�̵߳�run()����
        public void run() {
            try {
                //����˵�������br��ȡ�ͻ��˷��͵���Ϣ
                InputStream in = socket.getInputStream();
                BufferedInputStream br = new BufferedInputStream(in);
                OutputStream out = socket.getOutputStream();
                /*��ȡͨ�ŵ������ͻ������͵�ǰ��¼���б�*/
                int len = br.available();
                byte[] bytes = new byte[len];
                br.read(bytes);
                String name = new String(bytes);
                String[] names = name.split(" ");
                nickName = names[0];
                targetName = names[1];
                if(nickName.equals(ListsFlag)){
                    /*��ǰ��¼���б�*/
                    System.out.println(targetName+"�����ˣ�");
                    System.out.println(ListsFlag+targetName);
                    putIn(ListsFlag+targetName,out);
                }
                else {
                    /*��ȡͨ�ŵ������ͻ�����*/
                    System.out.println(nickName+"����Ϣ�����ߣ�"+targetName);
                    putIn(nickName,out);
                }
                /*��Ŷ�Ӧͨ�ŵ�ӳ��*/
                /*����ǰ�ͻ����������Ϣ���͸���Ϣ������*/
                //���������ж�
                String[] reflects = null;
                String message = null;
                //��ǰ�����ǰ׺�������жϵ�ǰ���������/����
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
                            System.out.println("��ͼƬ");
                        }else if(new String(flag).equals(FileFlag)){
                            sendFileToOne(targetName,bytes);
                            System.out.println("���ļ�");
                        }else if (new String(flag).equals(ListsFlag)){
                            System.out.println("���Ժ�������");
                            message = new String(bytes);
                            System.out.println(message);
                            System.out.println(ListsFlag+message.substring(10));
                            sendMessageToOne(ListsFlag+message.substring(10),ListsFlag+" "+targetName);
                        }else if (new String(flag).equals(ListsShow)){
                            System.out.println("�Ƿ�ͬ���������");
                            message = new String(bytes);
                            reflects = message.split(" ");
                            System.out.println(message);
                            System.out.println(ListsFlag+reflects[1]);
                            sendMessageToOne(ListsFlag+reflects[1],ListsShow+" "+reflects[2]+" "+reflects[3]);
                        }else{
                            System.out.println("������");
                            message = new String(bytes);
                            System.out.println(message);
                            message=nickName+":"+message;//���͵���Ϣ��ʽ: �������ǳ�:��Ϣ
                            sendMessageToOne(targetName, message);
                        }
                    }
                }
            } catch (Exception e) {
            }finally {
                //����ǰ�ͻ��˶Ͽ�����߼�
                //���ÿͻ��˵������pw�ӹ�������ɾ��
                remove(nickName);
                try {
                    sendMessageToOne(targetName,nickName+"�����ˣ�");
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
            server.start();     //ִ�з����start����
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("����˽�����ϵʧ�ܣ�");
        }
    }
}

