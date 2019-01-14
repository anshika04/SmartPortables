import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;

public class MySqlDataStoreUtilities
{
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 

	Connection conn = null;
	HashMap<String, User> hmUsers = null;
	HashMap<String, ProductCatalog> hmProductCatalog = new HashMap<String, ProductCatalog>();
		
	PreparedStatement pst = null;
	ResultSet rs = null;
	ProductCatalog pc = null;
	Statement stmt = null;
	
	public MySqlDataStoreUtilities() {}
	
	public HashMap<String, User> getAllUserDetails() throws SQLException 
	{
		try {

			hmUsers = new HashMap<String, User>();
			User user = null;

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String query = "SELECT * FROM UserDetail";
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				user = new User();

				user.setUserId(rs.getInt("userId"));
				user.setFirstName(rs.getString("firstname"));
				user.setLastName(rs.getString("lastname"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("userPassword"));
				user.setPhone(rs.getString("phone"));
				user.setRole(rs.getString("userRole"));
				user.setAddress(rs.getString("address"));
				user.setCity(rs.getString("city"));
				user.setCountry(rs.getString("country"));
				user.setState(rs.getString("state"));
				user.setZipcode(rs.getInt("zipcode"));

				hmUsers.put(user.getUsername(), user);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmUsers;
	}
	
	public boolean insertUserDetails(User user) throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");

			String query = "INSERT INTO UserDetail(firstname, lastname, username, userPassword, userRole, phone, address, city, state, country, zipcode) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			pst = conn.prepareStatement(query);

			pst.setString(4, user.getPassword());
			pst.setString(3, user.getUsername());
			pst.setString(1, user.getFirstName());
			pst.setString(2, user.getLastName());
			pst.setString(5, user.getRole());
			pst.setString(6, user.getPhone());
			pst.setString(7, user.getAddress());
			pst.setString(8, user.getCity());
			pst.setString(9, user.getState());
			pst.setString(10, user.getCountry());
			pst.setString(11, Integer.toString(user.getZipcode()));

			int result = pst.executeUpdate();

			pst.close();
			conn.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return false;
	}


	public boolean insertOrderDetails(HashMap<String, ProductCatalog> orderDetails, String creditcard, String userName)throws SQLException
	{
		try
		{
			int newOrderNo = getLastOrderNo(userName) + 1;

			Date currentOrderTime = new Date();
			System.out.println(dateFormat.format(currentOrderTime));

			Calendar c = Calendar.getInstance();
        	c.setTime(currentOrderTime);

			c.add(Calendar.DATE, 14);
			Date deliveryTime = c.getTime();
        	System.out.println(dateFormat.format(deliveryTime));
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable","root","root");			
			
			pc = new ProductCatalog();
			
			String query = "INSERT INTO CustomerOrder(orderId, productId, productName, username, creditcard, orderPrice, quantity, retailer, manufacturer, currentOrderTime, deliveryTime, orderStatus) "
									+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
			
			if(orderDetails != null)
			{
				for(String key: orderDetails.keySet())
				{
					pc = orderDetails.get(key);
										
					pst = conn.prepareStatement(query);

					pst.setInt(1,newOrderNo);
					pst.setString(2,pc.getId());
					pst.setString(3,pc.getName());					
					pst.setString(4,userName);
					pst.setString(5,creditcard);
					pst.setFloat(6,pc.getPrice() * pc.getQuantity());
					pst.setInt(7,pc.getQuantity());
					pst.setString(8,pc.getRetailer());				
					pst.setString(9,pc.getManufacturer());
					pst.setString(10,dateFormat.format(currentOrderTime));
					pst.setString(11,dateFormat.format(deliveryTime));
					pst.setString(12,"Processing!");
					
					pst.execute();

					WfCache.currentOrderNo = newOrderNo;
				}
				WfCache.currentOrderDeliveryTime = dateFormat.format(deliveryTime);
			}
		}
		catch(Exception ex)
		{
			System.out.println("insertOrderDetails()- " + ex.toString());
			return false;
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		return true;
	}
	
	public HashMap<String, Order> fetchOrderDetails(String username)throws SQLException
	{
		HashMap<String, Order> userOrderDetails = new HashMap<String, Order>();
		try
		{			
			System.out.println(username);
			Order orders = null;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable","root","root");
			stmt = conn.createStatement();

			if(WfCache.role.equalsIgnoreCase("salesman") || WfCache.role.equalsIgnoreCase("storemanager")) {
				String query = "SELECT * FROM CustomerOrder order by orderId desc;";
				rs = stmt.executeQuery(query);

			} else {
				String query = "SELECT * FROM CustomerOrder WHERE username = '"+username+"'order by orderId desc;";
				rs = stmt.executeQuery(query);
			}

			
						
						
			while(rs.next())
			{
				orders = new Order();
				
				orders.setId(rs.getString("orderId"));
				orders.setProductId(rs.getString("productId"));
				orders.setProductName(rs.getString("productName"));
				orders.setUserName(rs.getString("username"));
				orders.setOrderPrice(rs.getFloat("orderPrice"));
				orders.setQuantity(rs.getInt("quantity"));
				orders.setRetailer(rs.getString("retailer"));
				orders.setManufacturer(rs.getString("manufacturer"));
				orders.setOrderTime(dateFormat.parse(rs.getString("currentOrderTime")));
				orders.setDeliveryTime(dateFormat.parse(rs.getString("deliveryTime")));
				orders.setStatus(rs.getString("orderStatus"));
				
				
				userOrderDetails.put(rs.getString("customerOrderId"), orders);
			}

			System.out.println("details length" + userOrderDetails.size());
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch(Exception ex)
		{
			System.out.println("fetchOrderDetails()- " + ex.toString());			
		}
		finally
		{			
			rs.close();
			stmt.close();
			conn.close();
		}
		return userOrderDetails;
	}

	public Order fetchUniqueOrderDetails(int customerOrderId)throws SQLException
	{
		Order orders = new Order();
		try
		{			
			System.out.println(WfCache.username);
			
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable","root","root");
			stmt = conn.createStatement();

			String query = "SELECT * FROM CustomerOrder WHERE customerOrderId = '"+customerOrderId+"' order by orderId;";
			rs = stmt.executeQuery(query);			
						
			while(rs.next())
			{
				
				
				orders.setId(rs.getString("orderId"));
				orders.setProductId(rs.getString("productId"));
				orders.setProductName(rs.getString("productName"));
				orders.setUserName(rs.getString("username"));
				orders.setOrderPrice(rs.getFloat("orderPrice"));
				orders.setQuantity(rs.getInt("quantity"));
				orders.setRetailer(rs.getString("retailer"));
				orders.setManufacturer(rs.getString("manufacturer"));
				orders.setOrderTime(dateFormat.parse(rs.getString("currentOrderTime")));
				orders.setDeliveryTime(dateFormat.parse(rs.getString("deliveryTime")));
				orders.setStatus(rs.getString("orderStatus"));
			}
			
			rs.close();
			stmt.close();
			conn.close();
		}
		catch(Exception ex)
		{
			System.out.println("fetchUniqueOrderDetails()- " + ex.toString());			
		}
		finally
		{			
			rs.close();
			stmt.close();
			conn.close();
		}
		return orders;
	}
	
	
	public int getLastOrderNo(String username)throws SQLException
	{
		int lastOrderNo = 0;

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable","root","root");
			
			String selectQuery = "SELECT MAX(orderId) as orderId FROM CustomerOrder where username='"+ username +"'";
			
			pst = conn.prepareStatement(selectQuery);
			rs = pst.executeQuery();
				
			if(!rs.next())
			{
				return lastOrderNo;
			}
			else
			{
				do
				{
					lastOrderNo = rs.getInt("orderId");
				}while(rs.next());
			}		
		}
		catch(Exception ex)
		{
			System.out.println("getLastOrderNo()- " + ex.toString());
			return lastOrderNo;
		}
		finally
		{
			rs.close();
			pst.close();
			conn.close();
		}
		return lastOrderNo;
	}

	public void updateOrderDetails(Order order, int customerOrderId) throws SQLException 
	{
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String query = "UPDATE CustomerOrder SET orderPrice = ? , quantity = ?, orderStatus = ?  "
				                  + "WHERE customerOrderId = '"+customerOrderId+"' ;";
			
			pst = conn.prepareStatement(query);

			pst.setFloat(1, order.getOrderPrice());
			pst.setInt(2, order.getQuantity());
			pst.setString(3, order.getStatus());

			pst.executeUpdate();
		}
		catch(Exception ex)
		{
			System.out.println("updateOrderDetails()- " + ex.toString());
		}
		finally
		{			
			pst.close();
			conn.close();
		}
		
	}
	
		public void deleteProductDetails(String productID) throws SQLException {
		try 
		{			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();
			
			String deleteQuery;


			
			if(productID == null)
			{
				deleteQuery = "DELETE from Product";	
			}
			else
			{
				deleteQuery = "DELETE from Product where productId = '"+ productID +"';";
				System.out.println(deleteQuery);
			}
					
			stmt.executeUpdate(deleteQuery);
						
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
	
	public boolean insertProductDetails(ProductCatalog pc) throws SQLException 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");

			String insertQuery = "INSERT INTO Product(productId, productName, manufacturer, productQuantity, productPrice, imageSrc, productCondition, retailer, retailerDiscount, manufactureRebate, productCategory, productOnSale) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

			pst = conn.prepareStatement(insertQuery);

			pst.setString(1, pc.getId());
			pst.setString(2, pc.getName());
			pst.setString(3, pc.getManufacturer());
			pst.setInt(4, pc.getQuantity());
			pst.setFloat(5, pc.getPrice());
			pst.setString(6, pc.getImagepath());
			pst.setString(7, pc.getCondition());
			pst.setString(8, pc.getRetailer());
			pst.setFloat(9, pc.getRetailerDiscount());
			pst.setFloat(10, pc.getManufacturerRebate());
			pst.setString(11, pc.getCategory());
			pst.setString(12, pc.getOnSale());

			int result = pst.executeUpdate();
			pst.close();
			conn.close();

			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return false;
	}

	public HashMap<String, ProductCatalog> selectProductDetails(String category) throws SQLException 
	{
		try {
			hmProductCatalog = new HashMap<String, ProductCatalog>();
			ProductCatalog pc = null;

			System.out.println("CHECKING VALUE OF category ************************" + category);

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();
			String selectQuery;

			if(category.equalsIgnoreCase("all"))
			{
				selectQuery = "SELECT * FROM Product";
			}
			else
			{
				selectQuery = "SELECT * FROM Product WHERE productCategory = '"+ category +"';";
			}
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {
				pc = new ProductCatalog();

				pc.setId(rs.getString("productId"));
				pc.setName(rs.getString("productName"));
				pc.setManufacturer(rs.getString("manufacturer"));
				pc.setPrice(rs.getFloat("productPrice"));
				pc.setImagepath(rs.getString("imageSrc"));
				pc.setCondition(rs.getString("productCondition"));
				pc.setRetailer(rs.getString("retailer"));
				pc.setRetailerDiscount(rs.getFloat("retailerDiscount"));
				pc.setManufacturerRebate(rs.getFloat("manufactureRebate"));
				pc.setCategory(rs.getString("productCategory"));
				pc.setOnSale(rs.getString("productOnSale"));
				pc.setQuantity(rs.getInt("productQuantity"));

				hmProductCatalog.put(pc.getId(), pc);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmProductCatalog;
	}

	public HashMap<String, ProductCatalog> selectAllProductDetails() throws SQLException 
	{
		try {
			hmProductCatalog = new HashMap<String, ProductCatalog>();
			ProductCatalog pc = null;

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT * FROM Product";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				pc = new ProductCatalog();

				pc.setId(rs.getString("productId"));
				pc.setName(rs.getString("productName"));
				pc.setManufacturer(rs.getString("manufacturer"));
				pc.setPrice(rs.getFloat("productPrice"));
				pc.setImagepath(rs.getString("imageSrc"));
				pc.setCondition(rs.getString("productCondition"));
				pc.setRetailer(rs.getString("retailer"));
				pc.setRetailerDiscount(rs.getFloat("retailerDiscount"));
				pc.setManufacturerRebate(rs.getFloat("manufactureRebate"));
				pc.setCategory(rs.getString("productCategory"));
				pc.setOnSale(rs.getString("productOnSale"));
				pc.setQuantity(rs.getInt("productQuantity"));


				hmProductCatalog.put(pc.getId(), pc);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmProductCatalog;
	}

	public ArrayList<ProductCatalog> productsAvailable() throws SQLException 
	{

		ArrayList<ProductCatalog> arrayList = new ArrayList<ProductCatalog>();

		try 
		{		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT productId, productName, productPrice, productQuantity FROM Product;";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				ProductCatalog prd = new ProductCatalog();

				prd.setName(rs.getString("productName"));
				prd.setPrice(rs.getFloat("productPrice"));
				prd.setQuantity(rs.getInt("productQuantity"));

				arrayList.add(prd);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return arrayList;
	}

	public HashMap<String, Float> productsWithManufactureRebate() throws SQLException 
	{
		HashMap<String, Float> hmTrendData = new HashMap<String, Float>();

		try 
		{			

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT productName, manufactureRebate FROM Product WHERE manufactureRebate > 0 ORDER BY manufactureRebate DESC;";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				System.out.println("*** products"+ rs.getString("productName") + "**" + rs.getFloat("manufactureRebate"));

				hmTrendData.put(rs.getString("productName"), rs.getFloat("manufactureRebate"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmTrendData;
	}

	public HashMap<String, String> productsOnSale() throws SQLException 
	{
		HashMap<String, String> hmTrendData = new HashMap<String, String>();

		try 
		{			

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT productId, productName FROM Product WHERE productOnSale LIKE 'Yes';";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				hmTrendData.put(rs.getString("productId"), rs.getString("productName"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmTrendData;
	}

	public HashMap<String, String> dailySales() throws SQLException 
	{
		HashMap<String, String> hmTrendData = new HashMap<String, String>();

		try 
		{			

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT STR_TO_DATE(currentOrderTime, '%Y/%m/%d') as Dates, SUM(orderPrice) as TotalSales FROM CustomerOrder GROUP BY STR_TO_DATE(currentOrderTime, '%Y/%m/%d');";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				hmTrendData.put(rs.getString("Dates"), rs.getString("TotalSales"));
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return hmTrendData;
	}

	public ArrayList<ProductCatalog> productsSold() throws SQLException 
	{

		ArrayList<ProductCatalog> arrayList = new ArrayList<ProductCatalog>();

		try 
		{		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smartPortable", "root", "root");
			stmt = conn.createStatement();

			String selectQuery;
			selectQuery = "SELECT co.productId, co.productName as ProductName, p.productPrice as ProductPrice, SUM(co.quantity) as NumberOfItemsSold, (p.productPrice * SUM(co.quantity)) as TotalSales FROM CustomerOrder as co JOIN Product p ON co.productId = p.productId GROUP BY co.productId;";

			
			rs = stmt.executeQuery(selectQuery);

			while (rs.next()) {

				ProductCatalog prd = new ProductCatalog();

				prd.setName(rs.getString("ProductName"));
				prd.setPrice(rs.getFloat("ProductPrice"));
				prd.setQuantity(rs.getInt("NumberOfItemsSold"));
				prd.setRetailerDiscount(rs.getFloat("TotalSales"));

				arrayList.add(prd);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException se) { // Handle errors for JDBC
			se.printStackTrace();

		} catch (Exception e) { // Handle errors for Class.forName
			e.printStackTrace();

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {

			}

			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return arrayList;
	}
	
	
	

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/* 	public HashMap<String, ProductCatalog> selectCartDetails(String query)
	{
		try
		{
			if(conn == null)
			{
				getConnection();
			}
			
			String selectQuery = null;
			
			if(query == null)
			{
				selectQuery = "SELECT * FROM CART";
			}
			else
			{
				selectQuery = "SELECT * FROM CART " + query;
			}
			
			
			pst = conn.prepareStatement(selectQuery);
			rs = pst.executeQuery();
			pc = new ProductCatalog();
			
			if(!rs.next())
			{
				return null;
			}
			else
			{
				do
				{
					pc.setId(rs.getString("id"));
					pc.setName(rs.getString("name"));
					pc.setManufacturer(rs.getString("manufacturer"));
					pc.setPrice(rs.getFloat("price"));
					pc.setRetailer(rs.getString("retailer"));
					pc.setRetailerDiscount(rs.getFloat("retailerDiscount"));
					pc.setManufacturerRebate(rs.getFloat("manufacturerRebate"));				
					pc.setCategory(rs.getString("category"));
					pc.setQuantity(rs.getInt("quantity"));
					
					hmProductCatalog.put(pc.getId(), pc);
				}while(rs.next());
			}	
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			closeConnection();
		}
		return hmProductCatalog;		
	}
	
	public boolean insertCartDetails(ProductCatalog pc)
	{
		try
		{
			if(conn == null)
			{
				getConnection();
			}
			
			String query = "Where id = "+pc.getId();
			
			HashMap<String, ProductCatalog> hmPC = selectCartDetails(query);
			
			if(hmPC == null)
			{
				//Insert Query
				String insertQuery = "INSERT INTO CART(id,name,manufacturer,price,retailer,retailerDiscount,manufacturerRebate,category,quantity) " + "VALUES (?,?,?,?,?,?,?,?,?);";
				
				pst = conn.prepareStatement(insertQuery);
			
				pst.setString(1,pc.getId());
				pst.setString(2,pc.getName());
				pst.setString(3,pc.getManufacturer());
				pst.setFloat(4,pc.getPrice());
				pst.setString(5,pc.getRetailer());
				pst.setFloat(6,pc.getRetailerDiscount());
				pst.setFloat(7,pc.getManufacturerRebate());
				pst.setString(8,pc.getCategory());
				pst.setInt(9,pc.getQuantity());

			}
			else
			{
				//Update Query
				for (ProductCatalog pCatalog : hmPC.values()) 
				{
					String updateQuery = null;
					if (pCatalog != null) 
					{
						int temp = pCatalog.getQuantity() + 1;
						updateQuery = "UPDATE CART SET quantity = "+temp+" WHERE id = "+pCatalog.getId()+";";
					}				
				pst = conn.prepareStatement(updateQuery);
				}			
				pst.execute();				
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		finally
		{
			closeConnection();
		}
		return true;
	} */
	
	/* public boolean deleteCartDetails(ProductCatalog pc)
	{
		try
		{
			if(conn == null)
			{
				getConnection();
			}		
			
			String deleteQuery = "DELETE FROM CART WHERE id = "+pc.getId()+";";			
			pst = conn.prepareStatement(deleteQuery);					
			pst.execute();			
		}
		catch(Exception ex)
		{
			return false;
		}
		finally
		{
			closeConnection();
		}
		return true;
	} */