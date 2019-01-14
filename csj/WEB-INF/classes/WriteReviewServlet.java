import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import java.sql.*;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class WriteReviewServlet extends HttpServlet 
{
	public HashMap<String, ProductCatalog> hm = null;
	Review rv = null;	
	ProductCatalog pc = null;	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{

		PrintWriter out = response.getWriter();
		Utilities util = new Utilities(request,out);

			String productID = request.getParameter("productid2");
			String category = null;
			System.out.println("cate************" + productID);

			if(productID.toLowerCase().startsWith("lp")){

				category = "laptop";
				hm = WfCache.isLaptopCatalogFetched();
			}
			if(productID.toLowerCase().startsWith("ph")){

				category = "phone";
				hm = WfCache.isPhoneCatalogFetched();
			}
			if(productID.toLowerCase().startsWith("sp")){

				category = "speaker";
				hm = WfCache.isSpeakerCatalogFetched();
			}
			if(productID.toLowerCase().startsWith("sm")){

				category = "smartWatch";
				hm = WfCache.isSmartWatchCatalogFetched();
			}
			if(productID.toLowerCase().startsWith("he")){

				category = "headphone";
				hm = WfCache.isHeadphoneCatalogFetched();
			}
			if(productID.toLowerCase().startsWith("ex")){

				category = "externalStorage";
				hm = WfCache.isExternalStorageCatalogFetched();
			}

			if(productID.toLowerCase().startsWith("ac")){

				category = "accessory";
				hm = WfCache.isAccessoryCatalogFetched();
			}
			
			

			pc = hm.get(productID);
			
			

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getWriteReviewTemplate(pc,category);		
			util.getDefaultTemplate("footer.html");	
		}

			catch(SQLException ex)
		{
			System.out.println(ex.toString());
		}	
		
			
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException 
	{
		try 
		{
			rv  = new Review();
			
			
			
			rv.setProductModelName(request.getParameter("productmodelname"));
			rv.setCategory(request.getParameter("category"));
			rv.setProductPrice(request.getParameter("productprice"));
			rv.setRetailerName(request.getParameter("retailername"));
			rv.setRetailerZip(request.getParameter("retailerzip"));
			rv.setRetailerCity(request.getParameter("retailercity"));
			rv.setRetailerState(request.getParameter("retailerstate"));
			rv.setProductOnSale(request.getParameter("productonsale"));
			rv.setUserID(request.getParameter("userid"));
			rv.setManufacturerRebate(request.getParameter("manufacturerrebate"));
			rv.setUserOccupation(request.getParameter("useroccupation"));
			rv.setReviewRating(request.getParameter("reviewrating"));
			rv.setUserAge(request.getParameter("userage"));
			rv.setManufacturerName(request.getParameter("manufacturername"));
			rv.setReviewDate(request.getParameter("reviewdate"));
			rv.setReviewText(request.getParameter("reviewtext"));
			rv.setUserGender(request.getParameter("usergender"));		
			
			MongoDBDataStoreUtilities.insertReview(rv);
						
			response.setContentType("text/html");
			java.io.PrintWriter out = response.getWriter();
			Utilities util = new Utilities(request,out);
		
			util.getDefaultTemplate("header.html");
			out.println("<h2>" + "Review for " + request.getParameter("productmodelname") + " Submitted Successfully." + "</h2>");
			out.println("<br/>");
			out.println("<a href='home'> Home Page </a>");			
			out.println("</body>");
			out.println("</html>");
			out.close();
		}		
		catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}
}