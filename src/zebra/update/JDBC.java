package zebra.update;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import jpa.entities.Zebracrossing;

public class JDBC {

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/testdb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";

    public Connection getDBConnection() {

        System.out.println("Connecting..");
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
            e.printStackTrace();
            return null;

        }

        System.out.println("PostgreSQL JDBC Driver Registered!");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return null;

        }

        if (connection != null) {
            System.out.println("Connected..");
            return connection;
        } else {
            System.out.println("Failed to connect");
            return null;
        }
    }

    private  void deleteRecord(Zebracrossing zebra) throws SQLException {

		Connection dbConnection = null;
		Statement statement = null;

		String updateTableSQL = "DELETE FROM zebrastreifen"
				+ " WHERE Zebracrossingid = "+zebra.getZebracrossingId();

		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();

			System.out.println(updateTableSQL);

			statement.execute(updateTableSQL);

			System.out.println("Updated!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}
}
