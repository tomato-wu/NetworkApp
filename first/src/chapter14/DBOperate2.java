package chapter14;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DBOperate2
{
    private ConnectionProvider provider;
    private Connection con = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    public DBOperate2(ConnectionProvider provider)
    {
        this.provider = provider;
    }

//    添加学生的方法
    public void addStudent(String sNo, String sName, int age, String sClass,String IP) throws SQLException
    {

        try
        {
            con = provider.getConnection();
            String sql = "insert into peoples2(NO,NAME,AGE,CLASS,IP) values(?,?,?,?,?)";
            //创建数据库执行对象
            stmt = con.prepareStatement(sql);
            stmt.setObject(1,sNo);
            stmt.setObject(2,sName);
            stmt.setObject(3,age);
            stmt.setObject(4,sClass);
            stmt.setObject(5,IP);

            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeStatement();
            closeConnection();
        }


    }


// 删除学生
    public void deleteStudent(String name)
    {
        try
        {
            con = provider.getConnection();
            String sql = "delete from STUDENTS where NAME=?";
            stmt = con.prepareStatement(sql);
            stmt.setObject(1, name);
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeStatement();
            closeConnection();
        }
    }

//    打印所有的学生
    public void printAllStudents() throws SQLException
    {
        try
        {
            con = provider.getConnection();
            String sql = "SELECT NO,NAME,AGE,CLASS from peoples2";
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            //输出查询结果
            while (rs.next())
            {
                System.out.print(rs.getString(1) + "\t");
                System.out.print(rs.getString(2) + "\t");
                System.out.print(rs.getInt(3) + "\t");
                System.out.print(rs.getString(4) + "\n");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeAll();
        }
    }

    public void getTable()
    {
        try
        {
            con = provider.getConnection();
            DatabaseMetaData metaData = con.getMetaData();//返回数据库的一些元信息
            /*
            以下语句调用会返回一个ResultSet结果集，该结果集含4列，其中有一列含表名（该列名称为TABLE_NAME，可以通过rsTables.getString("TABLE_NAME")获得）
            可通过遍历rsTables得到包含的表名称
            */
            ResultSet rsTables = metaData.getTables(null,null,null,new String[]{"TABLE"});
            //用于保存表名的数组列表，供之后遍历访问
            ArrayList<String> tablesName = new ArrayList<>();
            System.out.println("该数据库中包含的表：");
            List<String> list=new ArrayList<String>();
            while (rsTables.next())
            {
                //不知道字段类型的情况下，也可以用rs.getObject(…)来打印输出结果
                String mytablesName = rsTables.getString("TABLE_NAME");
                System.out.println(mytablesName);
                list.add(mytablesName);
        //                找字段名字
            }
            PreparedStatement stmt = null;
            ResultSet rs = null;
            for (String tableName : list)
            {
                //保存字段名
                ArrayList<String> filedsName = new ArrayList<>();
                String sql = "select * from " + tableName ;
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
                ResultSetMetaData fields = rs.getMetaData();//会返回该表的字段信息
                int n = fields.getColumnCount();//有多少个字段
                System.out.println(tableName + "字段：");
                for(int i=1;i<=n;i++)
                {
                    //getColumnName可以获得字段名
                    String fieldName = fields.getColumnName(i);
                    System.out.println(fieldName);

                }
                System.out.println();
                System.out.println(tableName + "数据：");
                //如果有必要，还可以循环遍历列表结果，获取有价值信息
                while (rs.next())
                {
                    System.out.print(rs.getObject(1)+"\t");
                    System.out.print(rs.getObject(2)+"\t");
                    System.out.print(rs.getObject(3)+"\t");

                }
                System.out.println("-----------------------------------");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


//关闭
    public void closeAll()
    {
        try
        {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    private void closeStatement()
    {
        try
        {
            if (stmt != null) stmt.close();
        }
        catch (SQLException e)
        {
        }
    }

    private void closeConnection()
    {
        try
        {
            if (con != null) con.close();
        }
        catch (SQLException e)
        {
        }
    }

    public static void main(String args[]) throws Exception{
        DBOperate2 tester = new DBOperate2(new ConnectionProvider());
//        tester.addStudent("20191003027", "邬坤源", 21, "软件工程1904","10.173.40.22");
//        tester.printAllStudents();
//        tester.deleteStudent("小王五");
        tester.getTable();
    }
}
