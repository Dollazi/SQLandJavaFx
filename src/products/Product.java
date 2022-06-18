package products;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
	private StringProperty id;
	private StringProperty description;
	private DoubleProperty price;

	public Product() {
		this.id = new SimpleStringProperty();
		this.description = new SimpleStringProperty();
		this.price = new SimpleDoubleProperty();
	}

	public String getId() {
		return id.get();
	}

	public StringProperty getIdProperty() {
		return this.id;
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public String getDescription() {
		return description.get();
	}

	public StringProperty getDescriptionProperty() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description.set(description);
		;
	}

	public Double getPrice() {
		return price.get();
	}

	public DoubleProperty getPriceProperty() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price.set(price);
	}
	
}
