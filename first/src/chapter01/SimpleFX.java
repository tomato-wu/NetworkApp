package chapter01;

import javafx.application.Application;
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

import java.time.LocalDateTime;

public class SimpleFX extends Application {

    TextFileIO textFileIO = new TextFileIO();


    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnOpen = new Button("加载");
    private Button btnSave = new Button("保存");



    //待发送信息的文本框
    private TextField tfSend = new TextField();
    //显示信息的文本区域
    private TextArea taDisplay = new TextArea();





    @Override
    public void start(Stage primaryStage) {

        BorderPane mainPane = new BorderPane();
        //内容显示区域
        VBox vBox = new VBox();

        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域

        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(new Label("信息显示区："),
                taDisplay,new Label("信息输入区："), tfSend);

        //设置显示信息区的文本区域可以纵向自动扩充范围
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend,btnSave,btnOpen,btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane,700,400);

//        退出
        btnExit.setOnAction(event -> {System.exit(0);});
//        点击发送提交
        btnSend.setOnAction(event -> {
            String msg = tfSend.getText();
            taDisplay.appendText(msg + "\n");
            tfSend.clear();
        });
        //回车提交
        tfSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER) {
                    String msg = tfSend.getText();
                    taDisplay.appendText(msg + "\n");
                    tfSend.clear();
                }
            }
        });
        //SHIFT + ENTER
        tfSend.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume(); // otherwise a new line will be added to the textArea after the sendFunction() call
                if (event.isShiftDown()) {
                    String msg = tfSend.getText();
                    taDisplay.appendText("echo:  "+msg + "\n");
                    tfSend.clear();
                } else {
                    String msg = tfSend.getText();
                    taDisplay.appendText(msg + "\n");
                    tfSend.clear();
                }
            }
        });
        taDisplay.setEditable(false);//设置显示文本框只读
//        “保存”按钮的响应事件代码中添加相应功能，代码段如下：
        btnSave.setOnAction(event -> {
            //添加当前时间信息进行保存
            textFileIO.append(
                    LocalDateTime.now().withNano(0) +" "+ taDisplay.getText());
        });
//        在“加载”按钮的响应事件代码中添加相应功能，代码段如下：
        btnOpen.setOnAction(event -> {
            String msg = textFileIO.load();
            if(msg != null){
                taDisplay.clear();
                taDisplay.setText(msg);
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}