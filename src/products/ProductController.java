package products;

import java.sql.SQLException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class ProductController {
	@FXML
	private TableColumn<Product, String> colId;

	@FXML
	private TableColumn<Product, String> colDescription;

	@FXML
	private TableColumn<Product, Double> colPrice;

	@FXML
	private TableView<Product> tableProducts;

	// controlador do construtor
	public ProductController() {

	}

	// Initializing the controller class.
	@FXML
	private void initialize() {
		// Aqui ocorre a inicialização
		System.out.println("Inicialização");
		try {
			// Mapeamento das colunas da tabela
			colId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty());
			colDescription.setCellValueFactory(cellData -> cellData.getValue().getDescriptionProperty());
			colPrice.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());

			tableProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			// Inicializa o banco de dados
			DBConnection.connect();
			this.populateProducts();
		} catch (Exception e) {
			error("Em initialize(): " + e);
		}
	}

	@FXML
	private void populateProducts() throws SQLException {
		try {
			// Get all products information
			ObservableList<Product> productData = ProductDAO.searchProducts();
			// Set items to the employeeTable
			tableProducts.setItems(productData);
		} catch (SQLException e) {
			System.out.println("Erro de SQL em populateProducts(): " + e);
			throw e;
		}
	}

	@FXML
	private void exitApp() {
		// Sair da aplicação
		Platform.exit();
		System.exit(0);
	}

	private Product showProductDialog(boolean insert, Product product) {
		Dialog<Product> dialog = new Dialog<>();
		String title = (insert ? "Inserir novo" : "Atualzar") + " produto";
		dialog.setTitle(title);
		dialog.setHeaderText("Digite os dados do produto");
		dialog.setResizable(true);
		Label labelID = new Label("ID: ");
		Label labelDesc = new Label("Descrição: ");
		Label labelPrice = new Label("Preço: ");
		TextField textFieldID = new TextField();
		TextField textFieldDescription = new TextField();
		TextField textFieldPrice = new TextField();
		if (!insert) {
			textFieldDescription.setText(product.getDescription());
			textFieldPrice.setText(product.getPrice().toString());
		}
		GridPane grid = new GridPane();
		if (insert) {
			grid.add(labelID, 1, 1);
			grid.add(textFieldID, 2, 1);
			grid.add(labelDesc, 1, 2);
			grid.add(textFieldDescription, 2, 2);
			grid.add(labelPrice, 1, 3);
			grid.add(textFieldPrice, 2, 3);
		} else {
			grid.add(labelDesc, 1, 1);
			grid.add(textFieldDescription, 2, 1);
			grid.add(labelPrice, 1, 2);
			grid.add(textFieldPrice, 2, 2);
		}
		dialog.getDialogPane().setContent(grid);
		ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
		dialog.setResultConverter(new Callback<ButtonType, Product>() {
			@Override
			public Product call(ButtonType b) {
				if (b == buttonTypeOk) {
					Product product = new Product();
					if (insert) {
						product.setId(textFieldID.getText());
					}
					product.setDescription(textFieldDescription.getText());
					product.setPrice(Double.parseDouble(textFieldPrice.getText()));
					return product;
				}
				return null;
			}
		});
		Optional<Product> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	@FXML
	private void insertRecord() {
		Product newProduct = new Product();
		newProduct.setId("código");
		newProduct.setDescription("descrição");
		newProduct.setPrice(0.0);
		newProduct = showProductDialog(true, newProduct);
		ProductDAO.insertProduct(newProduct);
		try {
			this.populateProducts();
		} catch (SQLException e) {
			error(e.getMessage());
		}
	}

	@FXML
	private void updateRecord() {
		Product product = this.tableProducts.getSelectionModel().getSelectedItem();
		if (product != null) {
			Product newProduct = showProductDialog(false, product);
			if (newProduct != null) {
				newProduct.setId(product.getId());
				ProductDAO.updateProduct(newProduct);
				try {
					this.populateProducts();
				} catch (SQLException e) {
					error(e.getMessage());
				}
			}
		}
	}

	@FXML
	private void removeRecord() {
		Product product = this.tableProducts.getSelectionModel().getSelectedItem();
		if (product != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Remover produto");
			String s = "Você deseja remover o registro?";
			alert.setContentText(s);
			Optional<ButtonType> result = alert.showAndWait();
			if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
				ProductDAO.deleteProduct(product.getId());
				try {
					this.populateProducts();
				} catch (SQLException e) {
					error(e.getMessage());
				}
			}
		}
	}

	@FXML
	private void searchRecord() {
		Dialog<String> dialog = new TextInputDialog("");
		dialog.setTitle("Pesquisar produto");
		dialog.setHeaderText("Digite o código do produto");
		Optional<String> result = dialog.showAndWait();
		String id = null;
		if (result.isPresent()) {
			id = result.get();
		}
		if (id != null) {
			System.out.println("Pesquisar " + id);
			try {
				Product product = ProductDAO.searchProduct(id);
				if (product != null) {
					// this.tableProducts.getSelectionModel().clearSelection();
					// this.tableProducts.getSelectionModel().select(product);
					ObservableList<Product> products = tableProducts.getItems();
					for (int index = 0; index < products.size(); index++) {
						if (products.get(index).getId().equals(product.getId())) {
							this.tableProducts.getSelectionModel().select(index);
							break;
						}
					}
					System.out.println(product.getDescription());
				} else {
					error("Produto não encontrado!");
				}
			} catch (SQLException e) {
				error(e.getMessage());
			}
		}
	}

	@FXML
	private void about() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Sobre Produtos...");
		alert.setHeaderText("Produtos");
		alert.setContentText("Produtos - 2019");
		alert.showAndWait();
	}

	private void error(String msg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Erro");
		alert.setContentText(msg);
		alert.showAndWait();
	}

}
