package JDBCUtils;

import java.io.*;
import java.sql.*;

public class MySQLDemo {
    public static void main(String[] args) throws SQLException,Exception{

        //2.获取连接
        Connection connection=JDBCUtils.getConnection();

        //3.执行增删改查

        //3-1.编写sql语句
        String sql1="SELECT photo FROM employee\n" +
                "where name='郭登'";//sql语句字符串不需要加分号
        String sql2="SELECT * FROM test where test_name=?";
        String sql3="update test\n" +
                "set photo=?\n" +
                "where test_id=2";
        String sql4="select photo\n" +
                "from test\n" +
                "where test_id=2";


        //3-2.获取执行sql语句的命令对象
        PreparedStatement preparedStatement=connection.prepareStatement(sql4);
//        preparedStatement.setString(1,"易操");
        //设置占位符
//        preparedStatement.setString(1,"username");
//        preparedStatement.setString(2,"password");

//        Statement statement=connection.createStatement();



        //3-3.使用命令对象指向sql语句
//        statement.executeUpdate(sql1);//执行增删改语句，返回受影响的行数
        ResultSet rs=preparedStatement.executeQuery();
//        preparedStatement.setBlob(1,new FileInputStream("C:/Users/86182/Desktop/src/src/"+"test1"+".jpg"));
//        System.out.println(preparedStatement.executeUpdate()>0?"更新成功":"更新失败");
//        ResultSet resultSet=statement.executeQuery(sql1);//执行查询语句，返回结果集
        System.out.println(rs.getRow());
        while (rs.next()){
            Blob b = rs.getBlob("photo") ;
            File f = new File("C:/Users/86182/Desktop/src/src/"+"test2"+".jpg") ;    // 图片文件
            OutputStream out = null ;
            out = new FileOutputStream(f) ;
            out.write(b.getBytes(1,(int)b.length())) ;
            out.close() ;
            System.out.println("查询成功");
        }


        //关闭连接
        rs.close();
        preparedStatement.close();
        connection.close();

        //加载数据库驱动程序
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        }catch (ClassNotFoundException cne){
//            cne.printStackTrace();
//        }
//        String dburl = "jdbc:mysql://127.0.0.1:3306/myemployees";
//        String sql = "SELECT * FROM job";
//        try(
//            Connection conn = DriverManager.getConnection(dburl,"root","011010");
//            Statement stmt = conn.createStatement();
//            ResultSet rst = stmt.executeQuery(sql))
//        {
//            while (rst.next()){
//                System.out.println(rst.getString(1)+"\t"+
//                        rst.getString(2)+"\t"+rst.getInt(3)+
//                        "\t" + rst.getInt(4)
//                );
//            }
//        }catch (SQLException se){
//            se.printStackTrace();
//        }

    }
    public static void readPhoto(ResultSet rs,String fileName) throws SQLException{
        InputStream is=null;
        FileOutputStream fos=null;
        try{
            is=rs.getBinaryStream("photo");
            fos=new FileOutputStream("C:/Users/86182/Desktop/src/src/"+fileName+".jpg");
            int len;
            byte[] b=new byte[1024];
            while((len=is.read())!=-1){
                fos.write(b,0,len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null){
                    fos.close();
                }
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
