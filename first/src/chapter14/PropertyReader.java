package chapter14;

import java.io.InputStream;
import java.util.Properties;


//读配置信息类
public class PropertyReader
{
    private static Properties ps ;

    static{
        ps=new Properties();
        try{
            //该方式读取类路径下的配置文件，需要将config.txt复制到编译后的class对应位置，建议使用绝对路径
            InputStream in= PropertyReader.class.getResourceAsStream("/chapter14/db.txt");
            ps.load(in);
            in.close();
        }catch(Exception e){e.printStackTrace();}
    }

    //这种方法不能处理value为中文的情况
  /*public static String get(String key){
     return (String)ps.get(key);
  }*/

    public static String get(String key)
    {
        String value = null;
        try
        {
            value = new String(ps.getProperty(key).getBytes("ISO-8859-1"),"utf-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return value;
    }
}