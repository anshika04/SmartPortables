import java.util.*;
import java.io.Serializable;


public class Order implements Serializable{

	private String id;
	private String productId;
	private String productName;
	private String userName;
	private String creditCard;
	private float orderPrice;
	private int quantity;
	private String retailer;
	private String manufacturer;
	private Date orderTime;
	private Date deliveryTime;
	private String status;
	
	public Order()
	{
		
	}
	
	public Order(String id, String productId, String productName, String userName, String creditCard, float orderPrice, int quantity, String retailer, String manufacturer, Date orderTime, Date deliveryTime, String status)
	{
		this.id = id;
		this.productId = productId;
		this.productName = productName;
		this.userName = userName;
		this.creditCard = creditCard;
		this.orderPrice = orderPrice;
		this.quantity = quantity;	
		this.retailer = retailer;
		this.manufacturer = manufacturer;
		this.orderTime = orderTime;
		this.deliveryTime = deliveryTime;
		this.status = status;
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getRetailer() {
		return retailer;
	}

	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}