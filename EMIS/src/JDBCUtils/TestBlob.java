package JDBCUtils;

import java.io.*;
import java.sql.*;

public class TestBlob {
    public void testWrite() throws SQLException, FileNotFoundException {
        Connection connection=JDBCUtils.getConnection();

        PreparedStatement statement=connection.prepareStatement("sql");

        statement.setBlob(1,new FileInputStream("path"));
        int update=statement.executeUpdate();
    }
    public void testRead() throws SQLException, IOException {
        Connection connection=JDBCUtils.getConnection();

        PreparedStatement statement=connection.prepareStatement("sql");

        ResultSet resultSet=statement.executeQuery();
        if(resultSet.next()){
            //方式一：
            Blob blob=resultSet.getBlob("photo");
            InputStream is=blob.getBinaryStream();
            //方式二：
            InputStream inputStream=resultSet.getBinaryStream("photo");
            FileOutputStream fos=new FileOutputStream("C:\\Users\\86182\\Desktop\\src\\src");

            int len;
            byte[] b=new byte[1024];
            while((len=inputStream.read())!=-1){
                fos.write(b,0,len);
            }
            fos.close();
            inputStream.close();
        }
    }
}
