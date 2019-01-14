import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class InventoryReportServlet extends HttpServlet 
{
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{	
			MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();

			ArrayList<ProductCatalog> arr1 = sqlData.productsAvailable();
			HashMap<String, String> hmp2 = sqlData.productsOnSale();
			HashMap<String, Float> hmp3 = sqlData.productsWithManufactureRebate();			
			
			
			PrintWriter writer = response.getWriter();

			Utilities util = new Utilities(request,writer);
		
			util.getDefaultTemplate("header.html");

			writer.println("<div class='container'>");
				
			writer.println("<div class='row' align='center'>");
			writer.println("<div class='col-sm-4'>");

			writer.println("<h4><b><u> Product Status </u></b></h4>");
			
			writer.println("<table class='table table-striped' align='center'>");
			writer.println("<tr>");
			writer.println("<th>Product Name</th>");
			writer.println("<th>Product Price ($)</th>");
			writer.println("<th>Number of available Product</th>");
			writer.println("</tr>");	

			for (ProductCatalog pc : arr1) {	

				writer.println("<tr><td>"+ pc.getName() +"</td> <td>"+ pc.getPrice() +"</td> <td>"+ pc.getQuantity() +"</td></tr>");
			} 			
			
			
			writer.println("</table>");

			writer.println("<form method='get' action='datavisualization'>");
			writer.println("<input type='hidden' name = 'chartData' value='inventory'/>");
			writer.println("<input type = submit value ='Generate Chart' class='btn btn-warning btn-sm'>");
			writer.println("</form>");

			writer.println("</div>");

			writer.println("<div class='col-sm-4'>");

			writer.println("<h4><b><u> Products on Sale </u></b></h4>");
			
			writer.println("<table class='table table-striped' align='center'>");
			writer.println("<tr>");
			writer.println("<th>Product ID</th>");
			writer.println("<th>Product Name</th>");
			writer.println("</tr>");

			for (String key: hmp2.keySet()){
				String value = hmp2.get(key).toString();
						
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			}		

			writer.println("</table>");		

			writer.println("</div >");

			writer.println("<div class='col-sm-4'>");

			writer.println("<h4><b><u> Products With Manufacture Rebates </u></b></h4>");
			
			writer.println("<table class='table table-striped' align='center'>");
			writer.println("<tr>");
			writer.println("<th>Product Name</th>");
			writer.println("<th>Rebate ($)</th>");
			writer.println("</tr>");

			for (String key: hmp3.keySet()){					
				
				String value = hmp3.get(key).toString();  
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			} 

			
			writer.println("</table>");

			writer.println("</div >");
			writer.println("</div ><b><br><br><br>");

			writer.println("<div class='row' align='center'>");
			
			writer.println("<form method='get' action='home'>");
			writer.println("<input type = submit value ='Home Page' class='btn btn-warning btn-sm'>");
			writer.println("</form>");
			writer.println("</div>");
			writer.println("</div>");
			util.getDefaultTemplate("footer.html");				
			
		}		
		catch(Exception ex)
		{
			 ex.printStackTrace();
		}
	}
	
}