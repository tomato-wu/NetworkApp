package chapter02;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    private Socket socket; //定义套接字
    //定义字符输入流和输出流
    private PrintWriter pw;
    private BufferedReader br;

    public TCPClient(String ip, String port) throws IOException {
        //主动向服务器发起连接，实现TCP的三次握手过程
        //如果不成功，则抛出错误信息，其错误信息交由调用者处理
        socket = new Socket(ip, Integer.parseInt(port));//向服务进程发起TCP三次握手连接

        //得到网络输出字节流地址，并封装成网络输出字符流
        //Socket连接成功后，通过调用GET方法，可获得字节输出流和字节输入流，输出流用于发送信息，输入流用于接收信息
        OutputStream socketOut = socket.getOutputStream();
        // 设置最后一个参数为true，表示自动flush数据
        pw = new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);

        //得到网络输入字节流地址，并封装成网络输入字符流
        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(socketIn, "utf-8"));//读取
    }

    public void send(String msg) {
        //输出字符流，由Socket调用系统底层函数，经网卡发送字节流
        //PrintWriter类的println(String x)方法和writer(Stirng x)方法都表示把输入写到输出流中，但需要注意println()方法会再文本的后面加上分隔符，如windows下为“\r\n”,不同操作系统间有区别。
        pw.println(msg);
    }

    public String receive() {
        String msg = null;
        try {
            //从网络输入字符流中读信息，每次只能接受一行信息
            //如果不够一行（无行结束符），则该语句阻塞，
            // 直到条件满足，程序才往下运行
            //readLine() 方法读取一个字符串。
            msg = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void close() {
        try {
            if (socket != null) {
                //关闭socket连接及相关的输入输出流,实现四次握手断开
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //本机模块内测试与运行，需先运行TCPServer
    public static void main(String[] args) throws IOException {
        TCPClient tcpClient = new TCPClient("127.0.0.1", "8008");
        tcpClient.send("hello");//发送一串字符
        //接收服务器返回的字符串并显示
        System.out.println(tcpClient.receive());
    }
}
