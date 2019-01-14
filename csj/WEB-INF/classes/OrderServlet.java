import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class OrderServlet extends HttpServlet 
{
	String username = null;
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
					
			MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();		
			HashMap<String, Order> hm = sqlData.fetchOrderDetails(WfCache.username);
				
			PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);
			
			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getOrderTemplate(hm);		
			util.getDefaultTemplate("footer.html");		
		
		}
		catch(Exception ex)
		{
			System.out.println("OrderServlet- " + ex.toString());
		}
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();

		Date currentCancelOrderTime = new Date();
		System.out.println(dateFormat.format(currentCancelOrderTime));
		
		Calendar c = Calendar.getInstance();
        c.setTime(currentCancelOrderTime);

		c.add(Calendar.DATE, 14);
		Date newDeliveryTime = c.getTime();
        System.out.println(dateFormat.format(newDeliveryTime));

		
		Order order = new Order();
		int newQuantity = 0;


		try 
		{
			int customerOrderId = Integer.parseInt(request.getParameter("customerOrderId"));
			String[] values = request.getParameterValues("quantity");

			
			System.out.println("order.PRDID-->"+ customerOrderId);


			for (int i=0; i < values.length; i++)
	        {
	        	System.out.println("order.para quantity-->"+ values[i]);
	          	if (Integer.parseInt(values[i]) > 0) {
	          		newQuantity = Integer.parseInt(values[i]);
	          	}
	        }
	        
			
			order = sqlData.fetchUniqueOrderDetails(customerOrderId);

			if(order != null)
			{
				
				if(request.getParameter("action").trim().equalsIgnoreCase("Update Order"))
				{
					System.out.println(request.getParameter("action"));
					if(request.getParameter("customerOrderId") != null)
					{
						System.out.println("order.getprod-->"+ order.getProductName());

						int oldQuantity = order.getQuantity();
						

						float oldPrice = order.getOrderPrice();						

						float newPrice = (oldPrice/oldQuantity) * newQuantity;	

						order.setQuantity(newQuantity);
						order.setOrderPrice(newPrice);

						sqlData.updateOrderDetails(order, customerOrderId);
						response.sendRedirect("orders"); 
					}
				}
				else if(request.getParameter("action").trim().equalsIgnoreCase("Cancel Order"))
				{
					System.out.println(request.getParameter("action"));

					c.setTime(order.getDeliveryTime());
					c.add(Calendar.DATE, -5);
					Date cancelDate = c.getTime();


					if ((new Date()).before(cancelDate)) 
					{					
						order.setStatus("Cancelled");
						sqlData.updateOrderDetails(order, customerOrderId);
						response.sendRedirect("orders");
					} 
					else 
					{
						response.setContentType("text/html");
						java.io.PrintWriter out = response.getWriter();
						out.println("<html>");
						out.println("<head>");
						out.println("<title>Order Updation Failed</title>");
						out.println("</head>");
						out.println("<body>");
						out.println("<h2>" + "Order is on it's way for delivery. Cannot be cancelled" + "</h2>");
						out.println("<br/>");
						out.println("<a href='home'> Go Back to Home </a>");
						out.println("</body>");
						out.println("</html>");
						out.close();
					}
				}
			}			
		}		
		catch(Exception ex)
		{
			
		}
	}
}