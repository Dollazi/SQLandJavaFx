/*
 * Baixar o ScenBuilder
 * Baixar o SDK OpenFX
 * Em Project|Properties|Java Build Path|Libraries|ClassPath criar User Library e adicionar lá os Jars do SDK
 * Em Project|Properties| Run|Debug Settings na configuraçaõ de execução adicionar em VM path:
 *  --module-path=/home/marco/local/lib/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml
 *  https://code.makery.ch/blog/javafx-dialogs-official/
 *  
 *  create table products(
 *      id char(10) not null primary key,
 *      description char(30) not null,
 *      price decimal(10,2) not null
 *  )
 *  
 *  insert into products values ('pro0001', 'Produto 1', 33.45);
 *  insert into products values ('pro0002', 'Produto 2', 12.23);
 *  insert into products values ('pro0003', 'Produto 3', 5.12);
 *  
 *  
 */

package products;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		// Criar objeto para carregar FXML
		FXMLLoader loader = new FXMLLoader();
		// Nome do arquivo FXML
		String fxmlDocPath = "./src/fxml/products.fxml";
		// Para abrir o arquivo
		FileInputStream fxmlStream;
		try {
			// Abre o arquivo
			System.out.println();
			fxmlStream = new FileInputStream(fxmlDocPath);
			// No arquivo FXML AnchorPane é a raiz - obter seu objeto
			VBox root = (VBox) loader.load(fxmlStream);
			// Cria uma cena com o objeto-raiz
			Scene scene = new Scene(root);
			
			// Apresenta a cena
			stage.setScene(scene);
			stage.setTitle("Products");
			stage.show();
		} catch (IOException e) {
			System.err.println("Exceção em start(): " + e);
			e.printStackTrace();
		}
	}

}
