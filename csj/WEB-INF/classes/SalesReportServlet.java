import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SalesReportServlet extends HttpServlet 
{
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{	
			MySqlDataStoreUtilities sqlData  = new MySqlDataStoreUtilities();

			ArrayList<ProductCatalog> arr1 = sqlData.productsSold();
			HashMap<String, String> hmp2 = sqlData.dailySales();			
			
			
			PrintWriter writer = response.getWriter();

			Utilities util = new Utilities(request,writer);
		
			util.getDefaultTemplate("header.html");

			writer.println("<div class='container'>");
				
			writer.println("<div class='row' align='center'>");
			writer.println("<div class='col-sm-4'>");

			writer.println("<h4><b><u> Daily Sales </u></b></h4>");
			
			writer.println("<table class='table table-striped' align='center'>");
			writer.println("<tr>");
			writer.println("<th>Day-Date</th>");
			writer.println("<th>Total Sales per Day ($)</th>");
			writer.println("</tr>");			
			
			for (String key: hmp2.keySet()){					
				
				String value = hmp2.get(key).toString();  
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			} 
			writer.println("</table>");
			writer.println("</div>");

			writer.println("<div class='col-sm-2'>");
			writer.println("</div>");

			writer.println("<div class='col-sm-6'>");

			writer.println("<h4><b><u> Product Transactions </u></b></h4>");
			
			writer.println("<table class='table table-striped' align='center'>");
			writer.println("<tr>");
			writer.println("<th>Product Name</th>");
			writer.println("<th>Product Price ($)</th>");
			writer.println("<th>Number of Items Sold</th>");
			writer.println("<th>Total Sales ($)</th>");
			writer.println("</tr>");				
			
			for (ProductCatalog pc : arr1) {	

				writer.println("<tr><td> "+ pc.getName() +" </td> <td> "+ pc.getPrice() +" </td> <td> "+ pc.getQuantity() +" </td> <td> "+ pc.getRetailerDiscount() +" </td></tr> ");
			} 
			writer.println("</table>");
			writer.println("<br>");
			writer.println("<br>");

			writer.println("<form method='get' action='datavisualization'>");
			writer.println("<input type='hidden' name = 'chartData' value='sales'/>");
			writer.println("<input type = submit value ='Generate Chart' class='btn btn-warning btn-sm'>");
			writer.println("</form>");

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