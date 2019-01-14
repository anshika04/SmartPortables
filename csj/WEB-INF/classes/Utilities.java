import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;


public class Utilities {

	private HttpServletRequest request = null;
	private PrintWriter out = null;
	private String pHtml = null;
	public String username = null;
	public String cartcount = "0";
	public String role = null;
	public  HashMap<String, ProductCatalog> hm = null;
	public  HashMap<String, ProductCatalog> accHm = null;
	ProductCatalog pc = null;
	private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
	
	
	public Utilities(HttpServletRequest request, PrintWriter out)
	{		
		this.request = request;
		this.out = out;
	}

	public String fetchHtmlContent(String filePath)
	{
		String htmlContent = null;
		String htmlLine;		
		StringBuffer strBuffer = new StringBuffer();
		
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			while((htmlLine = br.readLine()) != null) {
				strBuffer.append(htmlLine);
			}
			htmlContent = strBuffer.toString();
		} catch (IOException e) {
			htmlContent = "Error in reading file " + filePath;
		}		
		return htmlContent;		
	}
	
	public String modifyHtml(String oldHtml, String newHtml, String toChange) {		
		oldHtml = oldHtml.replaceAll(toChange, newHtml);
		return oldHtml;
	}
		
	public void getDefaultTemplate(String fileName) throws IOException
	{		
				
		if(fileName.equalsIgnoreCase("header.html"))
		{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_HEADER_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();

			try 
			{	
				if(WfCache.isNewApp) {
					WfCache.isNewApp = false;
					WfCache.resetDB();
				}

				if(WfCache.phoneCatalog == null)
				{
					hm = WfCache.isPhoneCatalogFetched();
				}
				if(WfCache.smartWatchCatalog == null)
				{
					hm = WfCache.isSmartWatchCatalogFetched();
				}
				if(WfCache.speakerCatalog == null)
				{
					hm = WfCache.isSpeakerCatalogFetched();
				}
				if(WfCache.laptopCatalog == null)
				{
					hm = WfCache.isLaptopCatalogFetched();
				}
				if(WfCache.headphoneCatalog == null)
				{
					hm = WfCache.isHeadphoneCatalogFetched();
				}
				if(WfCache.externalStorageCatalog == null)
				{
					hm = WfCache.isExternalStorageCatalogFetched();
				}
				if(WfCache.accessoryCatalog == null)
				{
					hm = WfCache.isAccessoryCatalogFetched();
				}

			}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}

			
			if(WfCache.username == null) {

				dynamicHtml.append("<li><a href='/csj/signUp'>Sign Up </a></li>");
				dynamicHtml.append("<li><a href='/csj/login'>Login </a></li>");	

			} 
			else if(WfCache.role.equalsIgnoreCase("customer")) {

				dynamicHtml.append("<li><a href='home'><b>Welcome, " + WfCache.username.substring(0,1).toUpperCase() + WfCache.username.substring(1).toLowerCase() + "</b></a></li>");

				if(cartcount != null) {								
					dynamicHtml.append("<li><a href='cart'> <span><i class='glyphicon glyphicon-shopping-cart'></i></span> Cart(" +WfCache.cartCount+ ")</a></li>");			
				} 
				else {						
					dynamicHtml.append("<li><a href='cart'><span><i class='glyphicon glyphicon-shopping-cart'></i></span> Cart</a></li>");
				}

				dynamicHtml.append("<li><a href='/csj/logout'>Logout </a></li>");

			}
			else {
				
				dynamicHtml.append("<li><a href='home'><b>Welcome, " + WfCache.username.substring(0,1).toUpperCase() + WfCache.username.substring(1).toLowerCase() + "</b></a></li>");

				dynamicHtml.append("<li><a href='/csj/logout'>Logout </a></li>");	
			}		
					
			
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#headerList");
			out.append(pHtml);
		}
		else if(fileName.equalsIgnoreCase("leftNavigation.html"))
		{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_LEFTNAVIGATION_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();
			
			if(WfCache.username != null)
			{
				if(WfCache.role.equalsIgnoreCase("storemanager"))
				{
					dynamicHtml.append("<li><a href='addproduct'>Add Product</a></li>");
					dynamicHtml.append("<li><a href='updateproduct'>Update Product</a></li>");
					dynamicHtml.append("<li><a href='deleteproduct'>Delete Product</a></li>");
					dynamicHtml.append("<li><a href='inventoryreport'>Inventory Report</a></li>");
					dynamicHtml.append("<li><a href='salesreport'>Sales Report</a></li>");
				}
				else if(WfCache.role.equalsIgnoreCase("salesman"))
				{
					dynamicHtml.append("<li><a href='addcustomer'>Create new Customer</a></li>");
					dynamicHtml.append("<li><a href='orders'>Update / Delete Order</a></li>");
				}
				else
				{
					if(WfCache.role.equalsIgnoreCase("customer"))
					{
						dynamicHtml.append("<li><a href='orders'>My Orders</a></li>");
						dynamicHtml.append("<li><a href='trending'>Trending</a></li>");
					}
					
					dynamicHtml.append("<li><a href='smartWatch'>Smart Watch</a></li>");
					dynamicHtml.append("<li><a href='speaker'>Speaker</a></li>");
					dynamicHtml.append("<li><a href='headphone'>Headphones</a></li>");
					dynamicHtml.append("<li><a href='phone'>Phone</a></li>");	
					dynamicHtml.append("<li><a href='laptop'>Laptop</a></li>");
					dynamicHtml.append("<li><a href='externalStorage'>External Storage</a></li>");
				}			
			}
			else
			{
				
				dynamicHtml.append("<li><a href='smartWatch'>Smart Watch</a></li>");
				dynamicHtml.append("<li><a href='speaker'>Speaker</a></li>");
				dynamicHtml.append("<li><a href='headphone'>Headphones</a></li>");
				dynamicHtml.append("<li><a href='phone'>Phone</a></li>");	
				dynamicHtml.append("<li><a href='laptop'>Laptop</a></li>");
				dynamicHtml.append("<li><a href='externalStorage'>External Storage</a></li>");
			}
			
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#navigationList");
			out.append(pHtml);			
		}
		else
		{					
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_FOOTER_TEMPLATE));
			out.append(defaultTemplate.toString());
		}			
	}
	
	public void getHomeTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.DEFAULT_CONTENT_TEMPLATE));
		// StringBuilder dynamicHtml = new StringBuilder();
		
		// dynamicHtml.append("<h1 align='center'>Welcome to Smart Portables</h1><br>"); 
		// dynamicHtml.append("<img src = 'images/sm.jpg' width = '700' height = '300' alt = 'home'>");
		
		
		// pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Content");
		// out.append(pHtml);	

		String dynamicHtml = null;
		String oldHtml = defaultTemplate.toString();
		
		DealMacthes dm  = new DealMacthes();
		dynamicHtml = dm.DealMacthesProducts(); 
		
		
		pHtml = oldHtml.replace("#Content", dynamicHtml);
		
		
		out.append(pHtml);	
	}

	public void getLoginTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_LOGIN_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getChartTemplate(String productArray, String type)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_CHART_TEMPLATE));	

		StringBuilder dynamicHtml = new StringBuilder();

		dynamicHtml.append("<input type='hidden' id='idchk' name = 'idchk' value='"+productArray+"'/>");
		dynamicHtml.append("<input type='hidden' id='reporttype' name = 'reporttype' value='"+type+"'/>");


		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Chart");
		out.append(pHtml);	
	}

	public void getAddCustomerTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_ADDCUSTOMER_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}
	
	public void getSignUpTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_SIGNUP_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getAddProductTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_ADDPRODUCT_TEMPLATE));				
		pHtml = defaultTemplate.toString();
		out.append(pHtml);		
	}

	public void getDeleteProductTemplate()	throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_DELETEPRODUCT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		
		hm = WfCache.getAllProducts();
		pc = new ProductCatalog();

		dynamicHtml.append("<div align='center' style='overflow: auto;'>");

		dynamicHtml.append("<table class='table table-striped' align='center'>");
		
		for(String key: hm.keySet())
		{
			pc = hm.get(key);
			
			dynamicHtml.append("<tr>");			
			dynamicHtml.append("<td>");
			dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
			dynamicHtml.append("</td>");
			dynamicHtml.append("<td align='center'>");
			dynamicHtml.append("<form method = 'post' action = 'deleteproduct'>");
			dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
			dynamicHtml.append("<input type = 'submit' name = 'Delete' value = 'Delete' class='btn btn-primary btn-sm'>");
			dynamicHtml.append("</form>");			
			dynamicHtml.append("</td>");
			dynamicHtml.append("</tr>");
		}
		dynamicHtml.append("</table>");
		dynamicHtml.append("</div>");
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#ProductsList");			
		out.append(pHtml);		
	}
	
	public void getUpdateProductTemplate()	throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_UPDATEPRODUCT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		
		hm = WfCache.getAllProducts();
		pc = new ProductCatalog();

		dynamicHtml.append("<table class='table table-striped' align='center' style='overflow: auto;'>");
		
		for(String key: hm.keySet())
		{
			pc = hm.get(key);
			
			dynamicHtml.append("<tr>");			
			dynamicHtml.append("<td>");
			dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
			dynamicHtml.append("</td>");
			dynamicHtml.append("<td align='center'>");
			dynamicHtml.append("<form method = 'post' action = 'updateproduct'>");
			dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
			dynamicHtml.append("<input type = 'submit' name = 'Update' value = 'Update' class='btn btn-primary btn-sm'>");
			dynamicHtml.append("</form>");			
			dynamicHtml.append("</td>");
			dynamicHtml.append("</tr>");
		}

		dynamicHtml.append("</table>");

		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#ProductsList");			
		out.append(pHtml);		
	}
	
	public void getUpdateProductDetailsTemplate(String productId)throws IOException, SQLException
	{		
		StringBuilder dynamicHtml = new StringBuilder();
		
		hm = WfCache.getAllProducts();
		pc = new ProductCatalog();		
		pc = hm.get(productId);
		
		dynamicHtml.append("<div class='col-md-9' align='justify'>");
		dynamicHtml.append("<section id='content'>");
		dynamicHtml.append("<table class='table table-striped' align='center' style='overflow: auto;'>");

		dynamicHtml.append("<form method='post' action='updateproductdetails'>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Product ID:</td><td><input type='text' class='form-control' name='productid'	value='"+pc.getId()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Category:</td><td><input type='text' class='form-control' name='category' value='"+pc.getCategory()+"' readonly></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Product Name:</td><td><input type='text' class='form-control' name='productname' value='"+pc.getName()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Product Quantity:</td><td><input type='number' class='form-control' name='pquantity' value='"+pc.getQuantity()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Manufacturer:</td><td><input type='text' class='form-control' class='form-control' name='manufacturer' value='"+pc.getManufacturer()+"' ></td>");
		dynamicHtml.append("</tr>");
		// dynamicHtml.append("<tr>");
		// dynamicHtml.append("<td>Image Path:</td> <td><input type='text' class='form-control' name='imagepath' value='"+pc.getImagepath()+"' ></td>");
		// dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Condition:</td><td><input type='text' class='form-control' name='condition'	value='"+pc.getCondition()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Retailer:</td><td><input type='text' class='form-control' name='retailer' value='"+pc.getRetailer()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Retailer Discount:</td><td><input type='text' class='form-control' name='rdiscount' value='"+pc.getRetailerDiscount()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Manufacture Rebate:</td><td><input type='text' class='form-control' name='mrebate' value='"+pc.getManufacturerRebate()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Product On Sale:</td><td><input type='text' class='form-control' name='onsale' value='"+pc.getOnSale()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td>Product Price:</td><td><input type='text' class='form-control' name='pprice' value='"+pc.getPrice()+"' ></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<td align='center' colspan='2'><input type='submit' value='Update Product' class='btn btn-warning btn-sm'></td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("</form>");
		dynamicHtml.append("</table>");
		dynamicHtml.append("</section>");
		dynamicHtml.append("</div");
		dynamicHtml.append("</div");
		
		pHtml = dynamicHtml.toString();			
		out.append(pHtml);		
	}
	
	
	
	public void getProductContentTemplate(String productType, String productId)	throws IOException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_PRODUCT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();



		
		try 
		{
		
			if(productType.equalsIgnoreCase("phone"))
			{
				hm = WfCache.isPhoneCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("smartWatch"))
			{
				hm = WfCache.isSmartWatchCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("speaker"))
			{
				hm = WfCache.isSpeakerCatalogFetched();
			}
			if(productType.equalsIgnoreCase("laptop"))
			{
				hm = WfCache.isLaptopCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("headphone"))
			{
				hm = WfCache.isHeadphoneCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("externalStorage"))
			{
				hm = WfCache.isExternalStorageCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("accessory"))
			{
				hm = WfCache.isAccessoryCatalogFetched();
			}

		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}
		

		dynamicHtml.append("<table class='table table-striped'>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<th>#</th>");
		dynamicHtml.append("<th>Name</th>");
		dynamicHtml.append("<th>Action</th>");
		pc = new ProductCatalog();

		if(productId != null) {

				productId = productId.toUpperCase();


				for(String key: hm.keySet())
				{
					if(key.equalsIgnoreCase(productId)){

						pc = hm.get(key);
						System.out.println("check list: " + key);
						System.out.println("## check product" + pc.getImagepath());
						System.out.println("## check product" + pc.getName());
						System.out.println("## check product" + pc.getPrice());
						System.out.println("## check product" + pc.getOnSale());
						System.out.println("## check product" + pc.getOnSale().equalsIgnoreCase("yes"));
						break;
					}
					
				}
				System.out.println("## In Product Content TEmplate" + productType + " $$$" + productId + "%%" + WfCache.username);
				
				dynamicHtml.append("<tr>");
				dynamicHtml.append("<td>");				
				dynamicHtml.append("<img src = '"+ pc.getImagepath() +"' width = '150' height = '150' alt = 'phone'>");				
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
				dynamicHtml.append("<p> Price: "+ pc.getPrice()+ "</p>");
				dynamicHtml.append("<p> Product on Sale: "+ pc.getOnSale()+ "</p>");
				dynamicHtml.append("</td>");

				dynamicHtml.append("<td align='center'>");

				dynamicHtml.append("<div class='row'>");
				dynamicHtml.append("<form method = 'get' action = 'viewdescription'>");
				dynamicHtml.append("<input type='hidden' name = 'productid1' value='"+productId+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'View Description' class='btn btn-primary btn-sm' value = 'View Description'>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</div> <br>");

				if(WfCache.username != null) {

					if(pc.getOnSale().equalsIgnoreCase("yes") && WfCache.role.equalsIgnoreCase("customer")) {

						dynamicHtml.append("<div class='row'>");
						dynamicHtml.append("<form method = 'post' action = '"+productType+"'>");
						dynamicHtml.append("<input type='hidden' name = 'productid' value='"+productId+"'/>");
						dynamicHtml.append("<input type = 'submit' name = '"+productType+"' class='btn btn-primary btn-sm' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</div> <br>");

					}					

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'readreview'>");
					dynamicHtml.append("<input type='hidden' name = 'productname' value='"+pc.getName()+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Read Review' class='btn btn-primary btn-sm' value = 'Read Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'writereview'>");
					dynamicHtml.append("<input type='hidden' name = 'productid2' value='"+productId+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Write Review' class='btn btn-primary btn-sm' value = 'Write Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");
				}				
				
				

				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");

		}
		else {

			for(String key: hm.keySet())
			{
				pc = hm.get(key);
				
				dynamicHtml.append("<tr>");
				dynamicHtml.append("<td>");				
				dynamicHtml.append("<img src = '"+ pc.getImagepath() +"' width = '150' height = '150' alt = 'phone'>");				
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
				dynamicHtml.append("<p> Price: "+ pc.getPrice()+ "</p>");
				dynamicHtml.append("<p> Product on Sale: "+ pc.getOnSale()+ "</p>");
				dynamicHtml.append("</td>");

				dynamicHtml.append("<td align='center'>");

				dynamicHtml.append("<div class='row'>");
				dynamicHtml.append("<form method = 'get' action = 'viewdescription'>");
				dynamicHtml.append("<input type='hidden' name = 'productid1' value='"+key+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'View Description' class='btn btn-primary btn-sm' value = 'View Description'>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</div> <br>");

				if(WfCache.username != null) {

					if(pc.getOnSale().equalsIgnoreCase("yes")) {

						dynamicHtml.append("<div class='row'>");
						dynamicHtml.append("<form method = 'post' action = '"+productType+"'>");
						dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
						dynamicHtml.append("<input type = 'submit' name = '"+productType+"' class='btn btn-primary btn-sm' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</div> <br>");

					}					

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'readreview'>");
					dynamicHtml.append("<input type='hidden' name = 'productname' value='"+pc.getName()+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Read Review' class='btn btn-primary btn-sm' value = 'Read Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'writereview'>");
					dynamicHtml.append("<input type='hidden' name = 'productid2' value='"+key+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Write Review' class='btn btn-primary btn-sm' value = 'Write Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");
				}				
				
				

				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");
			}

		}
						
		

			dynamicHtml.append("</table>");
			pHtml = modifyHtml(defaultTemplate.toString(),productType,"#productType");
			pHtml = modifyHtml(pHtml,dynamicHtml.toString(),"#ProductsList");
			System.out.println(pHtml);		
			out.append(pHtml);		
	}

	public void getCartTemplate() throws IOException, SQLException
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_CART_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		
		hm = WfCache.showCart();
		accHm = WfCache.isAccessoryCatalogFetched();
		int length = accHm.size();		

		dynamicHtml.append("<table class='table table-striped' align='center'>");
		dynamicHtml.append("<tr>");
		dynamicHtml.append("<th>Name</th>");
		dynamicHtml.append("<th>Retailer</th>");
		dynamicHtml.append("<th>Price</th>");
		dynamicHtml.append("<th>Quantity</th>");
		dynamicHtml.append("<th></th>");
		dynamicHtml.append("</tr>");
		
		pc = new ProductCatalog();
		
		if(hm != null)
		{			
			for(String key: hm.keySet())
			{
				pc = hm.get(key);
								
				dynamicHtml.append("<tr>");				
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Sold by: "+ pc.getRetailer()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p> Price: "+ pc.getPrice()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p> Quantity: "+ pc.getQuantity()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<form method = 'post' action = 'cart'>");
				dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
				dynamicHtml.append("<input type='hidden' name = 'opr' value='delete'/>");
				dynamicHtml.append("<input type = 'submit' name = 'Delete' value = 'Delete Item' class='btn btn-primary btn-sm'>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");
			}
			dynamicHtml.append("<tr>");
			dynamicHtml.append("<td align='center' colspan='5'>");
			dynamicHtml.append("<form method = 'get' action = 'checkout'>");
			dynamicHtml.append("<input type = 'submit' name = 'Checkout' value = 'Checkout' class='btn btn-warning btn-sm'>");
			dynamicHtml.append("</form>");
			dynamicHtml.append("</td>");
			dynamicHtml.append("</tr>");
		}

			dynamicHtml.append("</table>");

			
  			dynamicHtml.append("<div id='myCarousel' class='carousel slide' data-ride='carousel' align='center'>"); 
  			dynamicHtml.append("<div class='carousel-inner'>");
  			

  			int i=1;

  			for(String key: accHm.keySet())
			{
				pc = accHm.get(key);
				System.out.println("key******************" + key);

				if(i == 1) {

					dynamicHtml.append("<div class='item active '>");
					dynamicHtml.append("<div class='card col-sm-6'>");
                	dynamicHtml.append("<img class='card-img-top' src='"+ pc.getImagepath() +"' width = '250' height = '250' alt = 'image'>");
                	dynamicHtml.append("</div>");


                	dynamicHtml.append("<div class='card-block col-sm-6'>");      

                	if(pc.getOnSale().equalsIgnoreCase("yes")) {
                		
                		dynamicHtml.append("<div class='row '>");
						dynamicHtml.append("<form method = 'post' action = 'cart'>");
						dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
						dynamicHtml.append("<input type='hidden' name = 'opr' value='add'/>");
						dynamicHtml.append("<input type = 'submit' name = 'accessory' class='btn btn-primary btn-sm' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</div> <br>");
                	}            
                	

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'readreview'>");
					dynamicHtml.append("<input type='hidden' name = 'productname' value='"+pc.getName()+"'/>");
					dynamicHtml.append("<input type='hidden' name = 'opr' value='read'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Read Review' class='btn btn-primary btn-sm' value = 'Read Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'writereview'>");
					dynamicHtml.append("<input type='hidden' name = 'productid2' value='"+key+"'/>");
					dynamicHtml.append("<input type='hidden' name = 'opr' value='write'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Write Review' class='btn btn-primary btn-sm' value = 'Write Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");


					dynamicHtml.append("</div>");
					dynamicHtml.append("</div>");

					i++;

				} else {

					dynamicHtml.append("<div class='item '>");
					dynamicHtml.append("<div class='card col-sm-6'>");
                	dynamicHtml.append("<img class='card-img-top' src='"+ pc.getImagepath() +"' width = '250' height = '250' alt = 'image'>");
                	dynamicHtml.append("</div>");


                	dynamicHtml.append("<div class='card-block col-sm-6'>");                  
                	dynamicHtml.append("<div class='row '>");

					dynamicHtml.append("<form method = 'post' action = 'cart'>");
					dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
					dynamicHtml.append("<input type='hidden' name = 'opr' value='add'/>");
					dynamicHtml.append("<input type = 'submit' name = 'accessory' class='btn btn-primary btn-sm' value = 'Add to Cart'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>"); 

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'post' action = 'readreview'>");
					dynamicHtml.append("<input type='hidden' name = 'productname' value='"+pc.getName()+"'/>");
					dynamicHtml.append("<input type='hidden' name = 'opr' value='read'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Read Review' class='btn btn-primary btn-sm' value = 'Read Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'post' action = 'writereview'>");
					dynamicHtml.append("<input type='hidden' name = 'productid2' value='"+key+"'/>");
					dynamicHtml.append("<input type='hidden' name = 'opr' value='write'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Write Review' class='btn btn-primary btn-sm' value = 'Write Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("</div>");
					dynamicHtml.append("</div>");
				}		
    				
				
			}

			

			dynamicHtml.append("<a style='height: 250px;' class='left carousel-control' href='#myCarousel' data-slide='prev'>");
			dynamicHtml.append("<span class='glyphicon glyphicon-chevron-left'></span>");
			dynamicHtml.append("<span class='sr-only'>Previous</span>");
			dynamicHtml.append("</a>");
			dynamicHtml.append("<a style='height: 250px;' class='right carousel-control' href='#myCarousel' data-slide='next'>");
			dynamicHtml.append("<span class='glyphicon glyphicon-chevron-right'></span>");
			dynamicHtml.append("<span class='sr-only'>Next</span>");
		  	dynamicHtml.append("</a>");
		
			dynamicHtml.append("</div>");
			dynamicHtml.append("</div>");	


		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#CartList");				
		out.append(pHtml);		
	}
	
	public void getCheckoutTemplate()
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_CHECKOUT_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		float totalCartPrice = 0;
		float totalProductPrice = 0;
		
		if(WfCache.username != null && WfCache.role.equalsIgnoreCase("customer"))
		{ 
			hm = WfCache.showCart();

			dynamicHtml.append("<table class='table table-striped' align='center'>");
			dynamicHtml.append("<tr>");
			dynamicHtml.append("<th>Name</th>");
			dynamicHtml.append("<th>Retailer</th>");
			dynamicHtml.append("<th>Total Price</th>");
			dynamicHtml.append("<th>Quantity</th>");
			dynamicHtml.append("</tr>");
			pc = new ProductCatalog();
			
			if(hm != null)
			{			
				for(String key: hm.keySet())
				{
					pc = hm.get(key);
					totalProductPrice = pc.getPrice() * pc.getQuantity();
					totalCartPrice = totalCartPrice + totalProductPrice;				
									
					dynamicHtml.append("<tr>");				
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>Sold by: "+ pc.getRetailer()+ "</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");				
					dynamicHtml.append("<p>Price: "+ totalProductPrice + "</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>Quantity: "+ pc.getQuantity()+ "</p>");
					dynamicHtml.append("</td>");
					dynamicHtml.append("</tr>");
				}

				dynamicHtml.append("</table>");
				
				dynamicHtml.append("<div class='row' align='center'>");
				dynamicHtml.append("<p>Subtotal: "+ totalCartPrice +"</p>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='row' align='center'>");
				dynamicHtml.append("<form method = 'post' action = 'checkout'>");

				dynamicHtml.append("<div class='col-sm-4'>");					
				dynamicHtml.append("<p>Customer Name: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='custName' required>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='col-sm-4'>");
				dynamicHtml.append("<p>Address: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='custAddr' required>");
				dynamicHtml.append("</div>");

				dynamicHtml.append("<div class='col-sm-4'>");
				dynamicHtml.append("<p>CardNumber: </p>");
				dynamicHtml.append("<input type='text' class='form-control' name='cardnumber' required>");
				dynamicHtml.append("</div>");

				
				dynamicHtml.append("</div> <br> <br>");
				
				dynamicHtml.append("<div class='row' align='center'>");			
				dynamicHtml.append("<input type = 'submit' name = 'Buy' value = 'Buy' class='btn btn-warning btn-sm'>");			
				dynamicHtml.append("</div>");
				dynamicHtml.append("</form>");
				
			}
			
		}
		else
		{
			dynamicHtml.append("<h1 style='float: center; color:red'>Please Login.</h1>");
			dynamicHtml.append("<a href='login'> Login Page </a>");
			dynamicHtml.append("<br/>");
			dynamicHtml.append("<a href='signUp'> Signup Page </a>");			
		}
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Checkout");				
		out.append(pHtml);
	}
	
	
	public void getOrderTemplate(HashMap<String, Order> oDetails)
	{
		
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_MYORDER_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		Order order = new Order();		
		username = WfCache.username;
		role = WfCache.role;

		if(oDetails != null)
		{
			dynamicHtml.append("<table class='table table-striped' align='center'>");
			dynamicHtml.append("<tr>");

			if(role.equals("salesman")) {
				dynamicHtml.append("<th>Customer</th>");
			}
			dynamicHtml.append("<th>Order ID</th>");
			dynamicHtml.append("<th>Product Name</th>");
			dynamicHtml.append("<th>Order Date</th>");
			dynamicHtml.append("<th>Delivery Date</th>");
			dynamicHtml.append("<th>Quantity</th>");
			dynamicHtml.append("<th>New Quantity</th>");
			dynamicHtml.append("<th>Price</th>");
			dynamicHtml.append("<th>Status</th>");
			dynamicHtml.append("<th>Update/Cancel</th>");
			dynamicHtml.append("</tr>");
			
			for(String key: oDetails.keySet())
			{					
					order = oDetails.get(key);	

					dynamicHtml.append("<tr>");
					if(role.equals("salesman")) {
						dynamicHtml.append("<td>"+ order.getUserName().substring(0,1).toUpperCase() + order.getUserName().substring(1).toLowerCase() +"</td>");
					}
					dynamicHtml.append("<td>"+ order.getId() +"</td>");
					dynamicHtml.append("<td>"+ order.getProductName() +"</td>");
					dynamicHtml.append("<td>"+ order.getOrderTime() +"</td>");
					dynamicHtml.append("<td>"+ order.getDeliveryTime() +"</td>");
					dynamicHtml.append("<td>"+ order.getOrderPrice() +"</td>");
					dynamicHtml.append("<td>"+ order.getStatus() +"</td>");
					dynamicHtml.append("<form method = 'post' action = 'orders'>");	
					dynamicHtml.append("<td>"+ order.getQuantity() +"</td>");
					dynamicHtml.append("<td><input type='text' size='3' value='0' name='quantity'></td>");				
					dynamicHtml.append("<td>");								
					dynamicHtml.append("<input type='hidden' name = 'customerOrderId' value='"+key+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'action' value = 'Update Order' class='btn btn-primary btn-sm'><br>");
					dynamicHtml.append("<input type = 'submit' name = 'action' value = 'Cancel Order' class='btn btn-primary btn-sm'>");
					dynamicHtml.append("</form>");				
					dynamicHtml.append("</td>");
					dynamicHtml.append("</tr>");
								
			}
			
			dynamicHtml.append("</table>");
			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#UserOrder");				
			out.append(pHtml);
		}

		
	}
	
	public void getProductDescription(String productType, String productID) throws IOException, SQLException
	{
			StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_DESCRIPTION_TEMPLATE));
			StringBuilder dynamicHtml = new StringBuilder();

			pc = new ProductCatalog();

			if(productType.equalsIgnoreCase("phone"))
			{
				hm = WfCache.isPhoneCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("smartWatch"))
			{
				hm = WfCache.isSmartWatchCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("speaker"))
			{
				hm = WfCache.isSpeakerCatalogFetched();
			}
			if(productType.equalsIgnoreCase("laptop"))
			{
				hm = WfCache.isLaptopCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("headphone"))
			{
				hm = WfCache.isHeadphoneCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("externalStorage"))
			{
				hm = WfCache.isExternalStorageCatalogFetched();
			}
			else if(productType.equalsIgnoreCase("accessory"))
			{
				hm = WfCache.isAccessoryCatalogFetched();
			}

			//accHm = WfCache.isAccessoryCatalogFetched();

			dynamicHtml.append("<table class='table table-striped' align='center'>");
			dynamicHtml.append("<tr>");
			dynamicHtml.append("<th></th>");
			dynamicHtml.append("<th>Details</th>");

			for(String key: hm.keySet())
			{
				pc = hm.get(key);

				if(pc.getId().equalsIgnoreCase(productID)) {
				
					dynamicHtml.append("<tr>");
					dynamicHtml.append("<td>");				
					dynamicHtml.append("<img src = '"+ pc.getImagepath() +"' width = '250' height = '250' alt = 'phone'>");				
					dynamicHtml.append("</td>");
					dynamicHtml.append("<td>");
					dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
					dynamicHtml.append("<p>Sold by: "+ pc.getRetailer()+ "</p>");
					dynamicHtml.append("<p> Condition: "+ pc.getCondition()+ "</p>");
					dynamicHtml.append("<p> Price: "+ pc.getPrice()+ "</p>");
					dynamicHtml.append("<p> Retailer Discount: "+ pc.getRetailerDiscount()+ "</p>");
					dynamicHtml.append("<p> Manufacture Rebate: "+ pc.getManufacturerRebate()+ "</p>");
					dynamicHtml.append("<p> Product On Sale: "+ pc.getOnSale()+ "</p>");
					dynamicHtml.append("</td>");
					
					dynamicHtml.append("</tr>");
				}
				
				
			}
			



			pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#Description");				
			out.append(pHtml);
	}

	public void getWriteReviewTemplate(ProductCatalog pc, String category)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_WRITEREVIEW_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();

		String ndate = dateFormat.format(new Date());
			
		dynamicHtml.append("<form method = 'post' action = 'writereview'>");
		dynamicHtml.append("<table class='table table-striped' align='center'>");				
		
		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Product Model Name: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='productmodelname' class='form-control' value='"+pc.getName()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Category: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='category' class='form-control' value='"+category+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Product Price: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='productprice' class='form-control' value='"+pc.getPrice()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Retailer Name: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='retailername' class='form-control' value='"+pc.getRetailer()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Retailer Zip: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='retailerzip' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Retailer City: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='retailercity' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Retailer State: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='retailerstate' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Product on Sale: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='productonsale' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Manufacturer Name: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='manufacturername' class='form-control' value='"+pc.getManufacturer()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Manufacturer Rebate: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='manufacturerrebate' class='form-control' value='"+pc.getManufacturerRebate()+"' readonly>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("UserID: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='userid' class='form-control' value='"+WfCache.username+"' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("UserAge: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='userage' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("UserGender: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='usergender' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("UserOccupation: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='useroccupation' class='form-control' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Review Rating: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<select name='reviewrating' class='form-control'>");
		dynamicHtml.append("<option value='1'>1</option>");
		dynamicHtml.append("<option value='2'>2</option>");
		dynamicHtml.append("<option value='3'>3</option>");
		dynamicHtml.append("<option value='4'>4</option>");
		dynamicHtml.append("<option value='5'>5</option>");
		dynamicHtml.append("</select>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");

		dynamicHtml.append("<tr>");				
		dynamicHtml.append("<td>");
		dynamicHtml.append("Review Date: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<input type='text' name='reviewdate' class='form-control' value='"+ ndate +"' >");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");	
		
		dynamicHtml.append("<tr>");	
		dynamicHtml.append("<td>");
		dynamicHtml.append("Review Text: ");
		dynamicHtml.append("</td>");
		dynamicHtml.append("<td>");
		dynamicHtml.append("<textarea type='text' name='reviewtext' class='form-control' ></textarea>");
		dynamicHtml.append("</td>");
		dynamicHtml.append("</tr>");
		dynamicHtml.append("</table>");										
	
		dynamicHtml.append("<div class='row' align='center'>");			
		dynamicHtml.append("<input type = 'submit' name = 'Submit Review' value = 'Submit Review' class='btn btn-warning btn-sm'>");			
		dynamicHtml.append("</div>");
		dynamicHtml.append("</form>");		
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#WriteReview");				
		out.append(pHtml);
	}

	public void getReadReviewTemplate(HashMap<Integer, Review> hmReview)
	{
		StringBuilder defaultTemplate = new StringBuilder(fetchHtmlContent(Constants.CONTENT_READREVIEW_TEMPLATE));
		StringBuilder dynamicHtml = new StringBuilder();
		Review rv = null;

		dynamicHtml.append("<table class='table table-striped' align='center'>");
		
		if(hmReview != null)
			{			
				for(Integer key: hmReview.keySet())
				{
					rv = hmReview.get(key);		
		
					dynamicHtml.append("<tr>");				
					dynamicHtml.append("<td>");
					dynamicHtml.append("Product Model Name: ");
					dynamicHtml.append("<label>"+rv.getProductModelName()+"</label><br>");
					dynamicHtml.append("UserID: ");
					dynamicHtml.append("<label >"+rv.getUserID()+"</label><br>");
					dynamicHtml.append("UserAge: ");
					dynamicHtml.append("<label >"+rv.getUserAge()+"</label><br>");
					dynamicHtml.append("UserGender: ");
					dynamicHtml.append("<label >"+rv.getUserGender()+"</label><br>");
					dynamicHtml.append("UserOccupation: ");
					dynamicHtml.append("<label >"+rv.getUserOccupation()+"</label><br>");
					dynamicHtml.append("Review Rating: ");
					dynamicHtml.append("<label >"+rv.getReviewRating()+"</label><br>");
					dynamicHtml.append("Review Text: ");
					dynamicHtml.append("<label>"+rv.getReviewText()+"</label><br>");
					dynamicHtml.append("</td>");					
					dynamicHtml.append("</tr>");	

				}
			}
			dynamicHtml.append("</table>");				
		
		pHtml = modifyHtml(defaultTemplate.toString(),dynamicHtml.toString(),"#ReadReview");				
		out.append(pHtml);
	}
}