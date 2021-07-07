package JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestBatch {
    public void testBatch() throws SQLException {
        Connection connection=JDBCUtils.getConnection();

        PreparedStatement statement=connection.prepareStatement("sql");
        for(int i=0;i<50000;i++){
//            statement.setString("sd");
//            statement.setString("fd");
            statement.addBatch();
            if(i%1000==0){
                statement.executeBatch();
                statement.clearBatch();
            }
        }
    }
}
