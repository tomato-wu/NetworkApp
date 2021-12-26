package chapter14;

import java.sql.*;

//该程序主要功能是在数据库插入一些记录，并实现浏览数据库。该程序要点包括
public class DBOperate1 {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        try {
            //加载MySQL驱动器，其中com.mysql.jdbc.Driver就是由下载的mysql驱动包提供
            Class jdbcDriver=Class.forName("com.mysql.jdbc.Driver");
            //注册MySQL驱动器
            java.sql.DriverManager.registerDriver((Driver)jdbcDriver.newInstance());
            //指定数据库所在位置，先用本地地址测试，访问本地的数据库
            String dbUrl = "jdbc:mysql://202.116.195.71:3306/STUDENTDB1?characterEncoding=utf8&useSSL=false";
            //指定用户名和密码
            String dbUser="student";
            String dbPwd="student";
            //创建数据库连接对象
             con = java.sql.DriverManager.getConnection(dbUrl,dbUser,dbPwd);
            //创建sql查询语句
            String sql = "select NO,NAME,AGE,CLASS from STUDENTS where name like ? and age=?";
            //创建数据库执行对象
            PreparedStatement stmt = con.prepareStatement(sql);
            //设置sql语句参数，查找名字以“小”开头，年龄23岁的记录
            stmt.setObject(1, "小%");
            stmt.setObject(2,23);
            //从数据库的返回集合中读出数据
            ResultSet rs = stmt.executeQuery();
            //循环遍历结果
            while (rs.next())
            {
                //不知道字段类型的情况下，也可以用rs.getObject(…)来打印输出结果
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.print(rs.getInt(3)+"\t");
                System.out.print(rs.getString(4)+"\n");
            }
            System.out.println("------------------------------------");
            //在本地数据库中插入一条包含自己信息的记录，并查询：
            //设置插入记录的sql语句(如何避免重复插入学号相同的信息？)
            String sql1 = "insert into STUDENTS(NO,NAME,AGE,CLASS,IP) values(?,?,?,?,?)";
            stmt = con.prepareStatement(sql1);
            stmt.setObject(1,"20191003027");
            stmt.setObject(2,"邬坤源");
            stmt.setObject(3, 21);
            stmt.setObject(4,"软件工程1904");
            stmt.setObject(5,"10.173.40.22");


            stmt.executeUpdate();
            //查询是否插入数据成功
            sql = "select NO,NAME,AGE,CLASS from STUDENTS ";
            stmt = con.prepareStatement(sql);
            //从数据库的返回集合中读出数据
            rs = stmt.executeQuery();

            //再次循环遍历结果，看是否成功
            while (rs.next())
            {
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.print(rs.getInt(3)+"\t");
                System.out.print(rs.getString(4)+"\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
    }
}
