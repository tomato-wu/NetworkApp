package chapter04.client;
//文件数据客户端程序（数据传输进程）FileDataClient.java。
//连接服务器数据端口、发送文件名、保存下载的文件，文件传输完成后关闭数据连接。
import java.io.*;
import java.net.Socket;

public class FileDataClient {
    private Socket dataSocket; //定义套接字

    FileDataClient(String ip, String port) throws IOException {
        try {
            dataSocket = new Socket(ip, Integer.parseInt(port));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    该方法主要功能是先在本地新建一个空文件，向服务器发送其文件名（基于字符串的字节流包装操作），
//    然后接收网络文件数据并保存为本地的这个文件（基于文件的字节流包装操作），关闭数据套接字。
//    可以在窗体界面的“下载”按钮中调用getFile方法，
    public void getFile(File saveFile) throws IOException {
        if (dataSocket != null) {
            FileOutputStream fileOut = new FileOutputStream(saveFile);
            byte[] buf = new byte[1024];
            InputStream socketIn = dataSocket.getInputStream();
            OutputStream socketOut = dataSocket.getOutputStream();

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketOut, "utf-8"), true);
            pw.println(saveFile.getName());

            int size = 0;
            while ((size = socketIn.read(buf)) != -1) {
                fileOut.write(buf, 0, size);
            }
            fileOut.flush();
            fileOut.close();

            if (dataSocket != null) {
                dataSocket.close();
            } else {
                System.err.println("连接ftp数据服务器失败");
            }
        }
    }
}
