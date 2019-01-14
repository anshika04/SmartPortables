import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
import javax.xml.*;
import java.io.File;
import java.util.HashMap;	
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.IOException;
import java.sql.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;



public class DeleteProductServlet extends HttpServlet
{
	public String producttype;
	public String filePath;

	public static HashMap<String, ProductCatalog> hm_prod = new HashMap<String, ProductCatalog>();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		try
		{
			PrintWriter out = response.getWriter();

			Utilities util = new Utilities(request,out);

			util.getDefaultTemplate("header.html");
			util.getDefaultTemplate("leftNavigation.html");		
			util.getDeleteProductTemplate();		
			util.getDefaultTemplate("footer.html");
		}
		catch(Exception ex)
		{

		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
	{
		try
		{
			String productId = request.getParameter("productid");
			MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();

			PrintWriter out = response.getWriter();	
			ProductCatalog pc = new ProductCatalog();


			if(productId.toLowerCase().startsWith("ph"))
			{
				WfCache.phoneCatalog.remove(productId);
				filePath = Constants.PHONE_XML;
				hm_prod = WfCache.phoneCatalog;
				producttype = "phone";
				
				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("sp"))
			{
				WfCache.speakerCatalog.remove(productId);
				filePath = Constants.SPEAKER_XML;
				hm_prod = WfCache.speakerCatalog;
				producttype = "speaker";
				
				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("lp"))
			{
				WfCache.laptopCatalog.remove(productId);
				filePath = Constants.LAPTOP_XML;
				hm_prod = WfCache.laptopCatalog;
				producttype = "laptop";

				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("sm"))
			{
				WfCache.smartWatchCatalog.remove(productId);
				filePath = Constants.SMARTWATCH_XML;
				hm_prod = WfCache.smartWatchCatalog;
				producttype = "smartWatch";
				
				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("he"))
			{
				WfCache.headphoneCatalog.remove(productId);
				filePath = Constants.HEADPHONE_XML;
				hm_prod = WfCache.headphoneCatalog;
				producttype = "headphone";
				
				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("ex"))
			{
				WfCache.externalStorageCatalog.remove(productId);
				filePath = Constants.EXTERNALSTORAGE_XML;
				hm_prod = WfCache.externalStorageCatalog;
				producttype = "externalStorage";
				
				WriteXML();
			}
			else if(productId.toLowerCase().startsWith("ac"))
			{
				WfCache.accessoryCatalog.remove(productId);
				filePath = Constants.ACCESSORY_XML;
				hm_prod = WfCache.accessoryCatalog;
				producttype = "accessory";
				
				WriteXML();
			}

			WfCache.deleteProducts(productId);

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Product Deleted</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h2>" + "Product Deleted Successfully." + "</h2>");
			out.println("<br/>");
			out.println("<a href='home'> Home Page </a>");			
			out.println("</body>");
			out.println("</html>");	

		}catch(Exception ex)
		{

		}finally{}
	}

	public void WriteXML() throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException
	{
		DocumentBuilder builder = null;
		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.newDocument();

		Element root = document.createElement("ProductCatalog");

		for(String key : hm_prod.keySet())
		{				 
			ProductCatalog value = hm_prod.get(key);				

			Element newNode = document.createElement(producttype);


			Element nodeID = document.createElement("id");					
			Element nodeImage = document.createElement("image");
			Element nodeName = document.createElement("name");
			Element nodeCondition = document.createElement("condition");
			Element nodePrice = document.createElement("price");
			Element nodeRetailer = document.createElement("retailer");
			Element nodeManufacture = document.createElement("manufacturer");
			Element nodeRetailerDiscount = document.createElement("RETAILER_DISCOUNT");
			Element nodeManufactureRebate = document.createElement("MANUFACTURER_REBATE");
			Element nodeOnsale = document.createElement("onsale");
			Element nodeQuantity = document.createElement("quantity");


			nodeID.setTextContent(value.getId());
			nodeName.setTextContent(value.getName());
			nodeCondition.setTextContent(value.getCondition());
			nodePrice.setTextContent(Float.toString(value.getPrice()));
			nodeImage.setTextContent(value.getImagepath());
			nodeRetailer.setTextContent(value.getRetailer());
			nodeManufacture.setTextContent(value.getManufacturer());
			nodeRetailerDiscount.setTextContent(Float.toString(value.getRetailerDiscount()));
			nodeManufactureRebate.setTextContent(Float.toString(value.getManufacturerRebate()));		
			nodeOnsale.setTextContent(value.getOnSale());
			nodeQuantity.setTextContent(Integer.toString(value.getQuantity()));	

			newNode.appendChild(nodeID);
			newNode.appendChild(nodeImage);
			newNode.appendChild(nodeName);
			newNode.appendChild(nodeCondition);
			newNode.appendChild(nodePrice);
			newNode.appendChild(nodeRetailer);
			newNode.appendChild(nodeManufacture);
			newNode.appendChild(nodeRetailerDiscount);
			newNode.appendChild(nodeManufactureRebate);
			newNode.appendChild(nodeOnsale);
			newNode.appendChild(nodeQuantity);
			

			root.appendChild(newNode);

		}

		document.appendChild(root);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Source source = new DOMSource(document);
		File file = new File(filePath);
		Result result = new StreamResult(file);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, result);				
	}

	


}