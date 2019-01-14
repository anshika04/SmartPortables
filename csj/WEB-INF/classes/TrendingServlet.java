import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.AggregationOutput;

public class TrendingServlet extends HttpServlet 
{
	DBCollection myReviews;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{			
			HashMap<String, Integer> hmp1 = MongoDBDataStoreUtilities.topZipCodes();
			HashMap<String, Integer> hmp2 = MongoDBDataStoreUtilities.topProducts();
			HashMap<String, Integer> hmp3 = MongoDBDataStoreUtilities.topLikedProducts();
			
			if(hmp3.isEmpty())
			{
				System.out.println("Review is empty");
			}
			
			
			
			PrintWriter writer = response.getWriter();

			Utilities util = new Utilities(request,writer);
		
			util.getDefaultTemplate("header.html");

			writer.println("<div class='container' style='height: 300px;'>");
				
			writer.println("<div class='row' align='center'>");
			writer.println("<div class='col-sm-4'>");
			
			writer.println("<table border='1'>");
			writer.println("<tr>");
			writer.println("<th colspan='2' align='center'>Top Most Liked Products</th>");
			writer.println("</tr>");
			writer.println("<tr></tr>");
			writer.println("<tr></tr>");			
			
			for (String key: hmp3.keySet()){					
				
				String value = hmp3.get(key).toString();  
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			} 
			writer.println("</table>");
			writer.println("");
			writer.println("");

			writer.println("</div>");
			writer.println("<div class='col-sm-4'>");
			
			
			writer.println("<table border='1'>");
			writer.println("<tr>");
			writer.println("<th colspan='2' align='center'>Top Zip Code By Products Sold</th>");
			writer.println("</tr>");
			writer.println("<tr></tr>");
			writer.println("<tr></tr>");			
			
			for (String key: hmp1.keySet()){					
				
				String value = hmp1.get(key).toString();  
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			} 
			writer.println("</table>");
			writer.println("");
			writer.println("");

			writer.println("</div >");

			writer.println("<div class='col-sm-4'>");
			
			writer.println("<table border='1'>");
			writer.println("<tr>");
			writer.println("<th colspan='2' align='center'>Top Products Sold</th>");
			writer.println("</tr>");
			writer.println("<tr></tr>");
			writer.println("<tr></tr>");
			for (String key: hmp2.keySet()){
				String value = hmp2.get(key).toString();
						
				writer.println("<tr><td>"+key+"</td> <td>"+value+"</td></tr>");
			}
			writer.println("</table>");
			writer.println("");
			writer.println("");

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
		catch(MongoException ex)
		{
			 ex.printStackTrace();
		}
	}
	
}