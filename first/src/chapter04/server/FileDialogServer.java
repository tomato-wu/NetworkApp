package chapter04.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
// 文件对话服务器程序FileDialogServer.java，开启2021端口；
//   主要功能:身份验证、文件目录传送。

public class FileDialogServer extends Thread{
    private Socket clientSocket;

    public FileDialogServer(Socket clientSocket) throws IOException {
        super();
        this.clientSocket = clientSocket;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        //获得输出流缓冲区的地址
        OutputStream socketOut = socket.getOutputStream();
        //网络流写出需要使用flush，这里在PrintWriter构造方法中直接设置为自动flush
        return new PrintWriter(
                new OutputStreamWriter(socketOut, "utf-8"), true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        //获得输入流缓冲区的地址
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(
                new InputStreamReader(socketIn, "utf-8"));
    }

    @Override
    public void run() {
        try {
            //此处程序阻塞，监听并等待客户发起连接，有连接请求就生成一个套接字。
            //本地服务器控制台显示客户端连接的用户信息
            System.out.println("New connection accepted： " + this.clientSocket.getInetAddress());
            BufferedReader br = getReader(clientSocket);//定义字符串输入流
            PrintWriter pw = getWriter(clientSocket);//定义字符串输出流
            //客户端正常连接成功，则发送服务器的欢迎信息，然后等待客户发送信息
            pw.println("From 服务器：欢迎使用本服务！");

            String msg = null;
            //此处程序阻塞，每次从输入流中读入一行字符串
            while ((msg = br.readLine()) != null) {
                //如果客户发送的消息为"bye"，就结束通信
                if (msg.equals("bye")) {
                    //向输出流中输出一行字符串,远程客户端可以读取该字符串
                    pw.println("From服务器：服务器断开连接，结束服务！");
                    System.out.println("客户端离开");
                    break; //结束循环
                }
                //向输出流中输出一行字符串,远程客户端可以读取该字符串
//                msg = msg.replaceAll("[吗?？]", "")+"!";
                pw.println("From服务器：" + msg);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(clientSocket != null)
                    clientSocket.close(); //关闭socket连接及相关的输入输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    //单客户版本，即每一次只能与一个客户建立通信连接
//    public void Service() {
//        while (true) {
//            Socket socket = null;
//            try {
//                //此处程序阻塞，监听并等待客户发起连接，有连接请求就生成一个套接字。
//                socket = serverSocket.accept();
//                //本地服务器控制台显示客户端连接的用户信息
//                System.out.println("New connection accepted： " + socket.getInetAddress());
//                BufferedReader br = getReader(socket);//定义字符串输入流
//                PrintWriter pw = getWriter(socket);//定义字符串输出流
//                //客户端正常连接成功，则发送服务器的欢迎信息，然后等待客户发送信息
//                pw.println("From 服务器：欢迎使用本服务！");
//
//                String msg = null;
//                //此处程序阻塞，每次从输入流中读入一行字符串
//                while ((msg = br.readLine()) != null) {
//                    //如果客户发送的消息为"bye"，就结束通信
//                    if (msg.equals("bye")) {
//                        //向输出流中输出一行字符串,远程客户端可以读取该字符串
//                        pw.println("From服务器：服务器断开连接，结束服务！");
//                        System.out.println("客户端离开");
//                        break; //结束循环
//                    }
//                    //向输出流中输出一行字符串,远程客户端可以读取该字符串
//                    msg = msg.replaceAll("[吗?？]", "")+"!";
//                    pw.println("From服务器：" + msg);
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if(socket != null)
//                        socket.close(); //关闭socket连接及相关的输入输出流
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public static void main(String[] args) throws IOException{
        ServerSocket FileDialogServer = new ServerSocket(2021);
        // 此处多线程实现并非最佳实践，参考chapter05/server下几个server的实现
        while(true) {
            Socket clientSocket = FileDialogServer.accept();
            new FileDialogServer(clientSocket).start();
        }
    }
}
