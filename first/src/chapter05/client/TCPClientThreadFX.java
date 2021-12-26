package chapter05.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 单用户版本的TCPServer.java程序不能同时服务多用户对话
 * 解决思路就是用多线程。服务器可能面临很多客户的并发连接，这种情况的方案一般是：
 * 主线程只负责监听客户请求和接受连接请求，用一个线程专门负责和一个客户对话，即一个客户请求成功后，创建一个新线程来专门负责该客户。
 * 对于这种多用户的情况，用第三讲的方式new Thread创建线程，频繁创建大量线程需要消耗大量系统资源。
 * 对于服务器，一般是使用线程池来管理和复用线程。
 * 线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。
 * 如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。ExecutorService代表线程池，
 * 其创建方式常见的有两种：
 * ExecutorService executorService = Executors.newFixedThreadPool(n);
 * ExecutorService executorService = Executors. newCachedThreadPool( );
 * 创建后，就可以使用executorService.execute方法来取出一个线程执行，该方法的参数就是Runnable接口类型。
 * 我们可以将和客户对话部分的代码抽取到一个Runnable的实现类Handler（见附录）的run方法中，然后丢给线程池去执行。
 * 方便起见，Handler作为主程序的内部类是个不错的选择。
 */
public class TCPClientThreadFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");

    private TextField tfSend = new TextField();
    private TextArea taDisplay = new TextArea();

    private TextField tfIP = new TextField("127.0.0.1");
    private TextField tfPort = new TextField("8008");
    private Button btnConnect = new Button("连接");

    private TCPClient tcpClient;
    private Thread readThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        HBox connHbox = new HBox();
        connHbox.setAlignment(Pos.CENTER);
        connHbox.setSpacing(10);
        connHbox.getChildren().addAll(new Label("IP地址："), tfIP, new Label("端口："), tfPort, btnConnect);
        mainPane.setTop(connHbox);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 20, 10, 20));
        // 设置发送信息的文本框
        // 自动换行
        taDisplay.setWrapText(true);
        // 只读
        taDisplay.setEditable(false);
        vBox.getChildren().addAll(new Label("信息显示区： "), taDisplay, new Label("信息输入区："), tfSend);
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        // 按钮事件绑定
        btnConnect.setOnAction(event -> {
            String ip = tfIP.getText().trim();
            String port = tfPort.getText().trim();

            try {
                //tcpClient不是局部变量，是本程序定义的一个TCPClient类型的成员变量
                tcpClient = new TCPClient(ip, port);
                //成功连接服务器，接收服务器发来的第一条欢迎信息
                String firstMsg = tcpClient.receive();
                taDisplay.appendText(firstMsg + "\n");
                // 启用发送按钮
                btnSend.setDisable(false);
                // 停用连接按钮
                btnConnect.setDisable(true);
                // 启用接收信息进程
                readThread = new Thread(() -> {
                    String msg = null;
                    // 新增线程是否中断条件 解决退出时出现异常问题
                    while ((msg = tcpClient.receive()) != null) {
                        String msgTemp = msg;
                        // todo

                        Platform.runLater(() -> {
                            taDisplay.appendText(msgTemp + "\n");
                        });
                    }
                    Platform.runLater(() -> {
                        taDisplay.appendText("对话已关闭！\n");
                        // 连接断开后重新开放连接按钮
                        btnSend.setDisable(true);
                        btnConnect.setDisable(false);
                    });
                });
                readThread.start();
            } catch (Exception e) {
                taDisplay.appendText("服务器连接失败！" + e.getMessage() + "\n");
            }

        });
        btnExit.setOnAction(event -> {
            exit();
        });
        btnSend.setOnAction(event -> {
            String sendMsg = tfSend.getText();
            tcpClient.send(sendMsg);//向服务器发送一串字符
            taDisplay.appendText("客户端发送：" + sendMsg + "\n");
            tfSend.clear();
            // 发送bye后重新启用连接按钮，禁用发送按钮
            if (sendMsg.equals("bye")) {
                btnConnect.setDisable(false);
                btnSend.setDisable(true);
            }
        });


        // 未连接时禁用发送按钮
        btnSend.setDisable(true);
        hBox.getChildren().addAll(btnSend, btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 700, 400);

        // 回车响应功能
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    sendText();
                }
            }
        });

        // 响应窗体关闭
        primaryStage.setOnCloseRequest(event -> {
            exit();
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void exit() {
        if (tcpClient != null) {
            tcpClient.send("bye");
            tcpClient.close();
        }
        // 系统退出时，单独的读线程没有结束，因此会出现异常。
        // 解决方案：在这里通知线程中断，在线程循环中增加条件检测当前线程是否被中断。
        // p.s. 此处使用的thread.stop()为deprecated的函数，应使用interrupt，正确写法见chapter03/TCPClientThreadFX
        readThread.stop();
        System.exit(0);
    }

    public void sendText() {
        String sendMsg = tfSend.getText();
        tcpClient.send(sendMsg);//向服务器发送一串字符
        taDisplay.appendText("客户端发送：" + sendMsg + "\n");
        tfSend.clear();
        // 发送bye后重新启用连接按钮，禁用发送按钮
        if (sendMsg.equals("bye")) {
            btnConnect.setDisable(false);
            btnSend.setDisable(true);
        }
    }
}
