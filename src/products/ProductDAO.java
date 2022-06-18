package products;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductDAO {

	private static Product getProductFromResultSet(ResultSet rs) throws SQLException {
		Product product = null;
		if (rs.next()) {
			product = new Product();
			product.setId(rs.getString("id"));
			product.setDescription(rs.getString("description"));
			product.setPrice(rs.getDouble("price"));
		}
		return product;
	}

	public static Product searchProduct(String productID) throws SQLException {
		String selectStmt = "SELECT * FROM products WHERE id='" + productID + "'";
		try {
			// Get resultset
			ResultSet rsProducts = DBConnection.executeSQL(selectStmt);
			// Send ResultSet to the getEmployeeFromResultSet method and get employee object
			Product product = getProductFromResultSet(rsProducts);
			// Return product
			return product;
		} catch (SQLException e) {
			System.out.println("Erro de SQL em searchProduct() " + e);
			// Return exception
			throw e;
		}
	}

	private static ObservableList<Product> getProductList(ResultSet rs) throws SQLException {
		ObservableList<Product> productList = FXCollections.observableArrayList(new ArrayList<Product>());
		while (rs.next()) {
			Product product = new Product();
			System.out.println(rs.getString("id"));
			product.setId(rs.getString("id"));
			product.setDescription(rs.getString("description"));
			product.setPrice(rs.getDouble("price"));
			productList.add(product);
		}
		return productList;
	}

	public static ObservableList<Product> searchProducts() throws SQLException {
		String selectStmt = "SELECT * FROM products";
		// Execute SELECT statement
		try {
			// Get ResultSet from dbExecuteQuery method
			ResultSet rsProducts = DBConnection.executeSQL(selectStmt);

			// Send ResultSet to the getEmployeeList method and get employee object
			ObservableList<Product> productList = getProductList(rsProducts);

			// Return employee object
			return productList;
		} catch (SQLException e) {
			System.out.println("Erro de SQL em searchProducts(): " + e);
			// Return exception
			throw e;
		}

	}

	public static int insertProduct(Product product) {
		String insertStmt = "INSERT INTO products VALUES ('" +
							product.getId() + "','" +
							product.getDescription() + "'," +
							product.getPrice() + ")";
		System.out.println(insertStmt);
		return DBConnection.updateSQL(insertStmt);
	}
	
	public static int updateProduct(Product product) {
		String updateStmt = "UPDATE products " + 
							"SET description='" + product.getDescription() + 
							"', price="	+ product.getPrice() + 
							" WHERE id='" + product.getId() + "'";
		System.out.println(updateStmt);
		return DBConnection.updateSQL(updateStmt);
	}
	
	public static int deleteProduct(String id) {
		String deleteStmt = "DELETE FROM products WHERE id='" + 
							id + "'";
		System.out.println(deleteStmt);
		return DBConnection.updateSQL(deleteStmt);
	}

}
