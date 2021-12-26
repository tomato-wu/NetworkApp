package chapter14;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private  String JDBC_DRIVER;
    private  String DB_URL;
    private  String DB_USER;
    private  String DB_PASSWORD;

    public ConnectionProvider() {
        //PropertyReader对象会读出db.txt文件中的配置信息，
        JDBC_DRIVER= PropertyReader.get("JDBC_DRIVER");
        DB_URL= PropertyReader.get("DB_URL");
        DB_USER= PropertyReader.get("DB_USER");
        DB_PASSWORD= PropertyReader.get("DB_PASSWORD");

        try{//JDBC实现
            Class jdbcDriver=Class.forName(JDBC_DRIVER);
            DriverManager.registerDriver((Driver)jdbcDriver.newInstance());
        }catch(Exception e){}
    }

    public Connection getConnection()throws SQLException {

        System.out.println("DB_URL :" + DB_URL);
        System.out.println("DB_USER:" + DB_USER);
        System.out.println("DB_PASSWORD: " + DB_PASSWORD);
        Connection con= DriverManager.getConnection( DB_URL,DB_USER,DB_PASSWORD);
        return con;
    }

}
