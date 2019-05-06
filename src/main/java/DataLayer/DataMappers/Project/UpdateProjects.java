package DataLayer.DataMappers.Project;

import DataLayer.DBCPDBConnectionPool;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateProjects implements Runnable {

    @Override
    public void run() {
        // Do your quarterly job here.
        System.out.println("hellooooooooooooooooooooooooooooooooooooooooooooo");
        try {
            Connection conn = DBCPDBConnectionPool.getConnection();
            ProjectMapper.fillTable(conn,false);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}