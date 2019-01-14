import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class CartServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	ProductCatalog pc = null;
	ProductCatalog pc1 = null;
	public HashMap<String, ProductCatalog> hm1 = null;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try 
		{

		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);
		
		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getCartTemplate();		
		util.getDefaultTemplate("footer.html");	

		}catch(SQLException e){
				System.out.println("Utilities SQL EXCEPTION");
			}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{
			String productID = request.getParameter("productid");
			String operation = request.getParameter("opr");

			if(operation.equalsIgnoreCase("add")) {

				if(Servlet_UpdateCart(productID))
				{			
					WfCache.cartCount = WfCache.cartCount + 1;									
				}	

			} else {

				hm = WfCache.deleteCart(productID);
					
				WfCache.cartCount = WfCache.cartCount - 1;		
			}					
			
			response.sendRedirect("cart");
		}		
		catch(Exception ex)
		{
			
		}
	}

	public boolean Servlet_UpdateCart(String productID)throws IOException, SQLException 
	{
		hm = WfCache.isAccessoryCatalogFetched();
		pc = hm.get(productID);
		
		hm1 = WfCache.showCart();
		
		if(hm1 != null)
		{
			if(hm1.containsKey(productID))
			{
				pc1 = hm1.get(productID);
				if(pc1 != null)
				{
					int qty = pc1.getQuantity() + 1;
					pc1.setQuantity(qty);
					WfCache.addToCart(pc1);
					return false;
				}
			}
			else
			{
				int qty = 1;
				pc.setQuantity(qty);
				WfCache.addToCart(pc);								
			}
		}
		else
		{
			int qty = 1;
			pc.setQuantity(qty);
			WfCache.addToCart(pc);			
		}
		return true;
	}
}