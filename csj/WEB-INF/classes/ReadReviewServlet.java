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

public class ReadReviewServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	public HashMap<String, Review> hmReview = null;
	Review rv = null;	
	ProductCatalog pc = null;
	DBCollection myReviews;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{
			String productName = request.getParameter("productname");
			System.out.println(productName);
			
			HashMap<Integer, Review> hmReview = MongoDBDataStoreUtilities.readReviews(productName);	
			Review pc = null;
			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);
			
			
			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getReadReviewTemplate(hmReview);
			util.getDefaultTemplate("footer.html");
		
		}		
		catch(MongoException ex)
		{
			 ex.printStackTrace();
		}
	}
}