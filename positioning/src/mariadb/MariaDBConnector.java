package mariadb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnector {
    private Connection con = null;
    private String userName = "root";
    private String password = "1111";
    private String dbName = "positioning";
    private String hostIP = "localhost";
    
    private void loadDBDriver() {
    		try {
				Class.forName("org.mariadb.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("error: " + e.getMessage());
			}
    }
    
    public void connectDB() {
        try {
        	loadDBDriver();
            String url = "jdbc:mariadb://" + hostIP + ":3306/" + dbName;
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connection successful");
        } catch(SQLException e) {
            System.err.println("error :" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnectDB() {
        try {
            if(con != null) {
                con.close();
                System.out.println("Terminate database connection");
            }
        } catch (SQLException e) {
            System.err.println("error :" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getCon() {
        return con;
    }
}
