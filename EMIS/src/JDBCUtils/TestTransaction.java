package JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 1.开启新事物
 * 2.编写组成事务的一组sql语句
 * 3.结束事务
 */
public class TestTransaction {
    //1.获取连接
    public void testTransaction() throws SQLException {
        Connection connection=JDBCUtils.getConnection();

        connection.setAutoCommit(false);


        //2.执行sql语句
        String sql="SELECT * FROM jobs";//sql语句字符串不需要加分号
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.executeQuery();
        connection.commit();
        connection.rollback();
        JDBCUtils.close(null,preparedStatement,connection);
    }

}
