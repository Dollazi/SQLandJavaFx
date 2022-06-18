package products;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private static Connection connection = null;

	public static void connect() {
		try {
			// db parameters
			File temp = new File("products.db");
			String url = "jdbc:sqlite:" + temp.getAbsolutePath().replace("\\", "\\\\");
			// create a connection to the database
			DBConnection.connection = DriverManager.getConnection(url);
			System.out.println("Conexão com SQLite estabelecida com sucesso!");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static ResultSet executeSQL(String sqlCommand) {
		// Linhas SQL tipo SELECT
		ResultSet rs = null;
		try {
			Statement statement = DBConnection.connection.createStatement();
			rs = statement.executeQuery(sqlCommand);
		} catch (SQLException e) {
			System.out.println("Erro de SQL em executeSQL(): " + e);
		} catch (Exception e) {
			System.out.println("Erro em executeSQL(): " + e);
		}
		return rs;
	}

	public static int updateSQL(String sqlCommand) {
		int result = 0;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(sqlCommand);
			statement.close();
		} catch (SQLException e) {
			System.out.println("Erro de SQL em updateSQL(): " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Erro em updateSQL(): " + e.getMessage());
		}
		return result;
	}
}
