import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Date;

public class CheckoutServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();
	ProductCatalog pc = null;
	String username = null;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getCheckoutTemplate();		
		util.getDefaultTemplate("footer.html");		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{			
			String custName = request.getParameter("custName");
			String custAddr = request.getParameter("custAddr");
			String creditCard = request.getParameter("cardnumber");		

			System.out.println("hey in checkout");
						
			
			hm = WfCache.showCart();
			

			if(sqlData.insertOrderDetails(hm, creditCard, WfCache.username))
			{
				WfCache.clearCart();
				WfCache.cartCount = 0;
								
				response.setContentType("text/html");
				java.io.PrintWriter out = response.getWriter();
				Utilities util = new Utilities(request,out);
		
				util.getDefaultTemplate("header.html");
				out.println("<h2>Order Successfully placed.</h2>");
				out.println("<h2>Confirmation Number : "+ WfCache.currentOrderNo +" .</h2>");
				out.println("<h4>Delivery Date: "+ WfCache.currentOrderDeliveryTime +".</h4>");
				out.println("<br/>");
				out.println("<a href='home'> Go to Home Page </a>");
				out.println("</body>");
				out.println("</html>");
				out.close();
			}
			else
			{
				response.setContentType("text/html");
				java.io.PrintWriter out = response.getWriter();
				out.println("<html>");
				Utilities util = new Utilities(request,out);
		
				util.getDefaultTemplate("header.html");
				out.println("<h2>Order UnSuccessful.</h2>");
				out.println("<h2>" + "Order not placed." + "</h2>");
				out.println("<br/>");
				out.println("<a href='cart'> Go to cart </a>");
				out.println("</body>");
				out.println("</html>");
				out.close();
			}

		}		
		catch(Exception ex)
		{
			
		}
	}
}