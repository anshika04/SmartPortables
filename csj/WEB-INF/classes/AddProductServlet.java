import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.*;
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
import java.io.File;
import java.util.HashMap;	
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.sql.*;


public class AddProductServlet extends HttpServlet
{
	public String productType;
	public String filePath;

	public static HashMap<String, ProductCatalog> hm_prod = new HashMap<String, ProductCatalog>();
	ProductCatalog objProductCatalog = new ProductCatalog();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();

		Utilities util = new Utilities(request,out);

		util.getDefaultTemplate("header.html");
		util.getDefaultTemplate("leftNavigation.html");		
		util.getAddProductTemplate();		
		util.getDefaultTemplate("footer.html");	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException
	{
		try
		{
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();				
			MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	

			String uniqueID = UUID.randomUUID().toString();
			String imagePath = "images/loading.gif";	

			System.out.println("random number **********" + uniqueID);	


			objProductCatalog.setRetailer(request.getParameter("retailer"));
			objProductCatalog.setManufacturer(request.getParameter("manufacturer"));
			objProductCatalog.setName(request.getParameter("name"));
			objProductCatalog.setImagepath(imagePath);
			objProductCatalog.setCondition(request.getParameter("condition"));
			objProductCatalog.setPrice(Float.valueOf(request.getParameter("price")));
			objProductCatalog.setRetailerDiscount(Float.valueOf(request.getParameter("rdiscount")));
			objProductCatalog.setManufacturerRebate(Float.valueOf(request.getParameter("mrebate")));
			objProductCatalog.setOnSale(request.getParameter("onsale"));
			objProductCatalog.setQuantity(Integer.parseInt(request.getParameter("pquantity")));

			productType = request.getParameter("product_type");
			objProductCatalog.setCategory(productType);

			if(productType.equalsIgnoreCase("phone"))
			{
				objProductCatalog.setId("PH"+ uniqueID);
				WfCache.phoneCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.PHONE_XML;
			}
			else if(productType.equalsIgnoreCase("smartWatch"))
			{
				objProductCatalog.setId("SM"+ uniqueID);
				WfCache.smartWatchCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.SMARTWATCH_XML;
			}
			else if(productType.equalsIgnoreCase("speaker"))
			{
				objProductCatalog.setId("SP"+ uniqueID);
				WfCache.speakerCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.SPEAKER_XML;
			}
			if(productType.equalsIgnoreCase("laptop"))
			{
				objProductCatalog.setId("LP"+ uniqueID);
				WfCache.laptopCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.LAPTOP_XML;
			}
			else if(productType.equalsIgnoreCase("headphone"))
			{
				objProductCatalog.setId("HE"+ uniqueID);
				WfCache.headphoneCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.HEADPHONE_XML;
			}
			else if(productType.equalsIgnoreCase("externalStorage"))
			{
				objProductCatalog.setId("EX"+ uniqueID);
				WfCache.externalStorageCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.EXTERNALSTORAGE_XML;
			}
			else if(productType.equalsIgnoreCase("accessory"))
			{
				objProductCatalog.setId("AC"+ uniqueID);
				WfCache.accessoryCatalog.put(objProductCatalog.getId(),objProductCatalog);
				objSql.insertProductDetails(objProductCatalog);
				filePath = Constants.ACCESSORY_XML;
			}

			Utilities util = new Utilities(request,out);

			util.getDefaultTemplate("header.html");

			SAXProductHandler saxHandler = new SAXProductHandler();
			try
			{
				hm_prod = saxHandler.readDataFromXML(filePath);
				WriteXML();	
			}
			catch(SAXException e)
			{
				
			}
			catch(ParserConfigurationException e)
			{
				
			}
			catch(TransformerException e)
			{
				
			}

			out.println(
				"<html>"+
				"<body>"+
				"<h3>Product is successfully added</a></h3>"+
				"<a href='home'> Home Page </a>"
				);
			util.getDefaultTemplate("footer.html");
		}

		catch(SQLException ex)
		{

		}

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
			
			Element newNode = document.createElement(productType);
			
			
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
		
		Element newNode = document.createElement(productType);
		
		
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
		
		
		nodeID.setTextContent(objProductCatalog.getId());
		nodeName.setTextContent(objProductCatalog.getName());
		nodeCondition.setTextContent(objProductCatalog.getCondition());
		nodePrice.setTextContent(Float.toString(objProductCatalog.getPrice()));
		nodeImage.setTextContent(objProductCatalog.getImagepath());
		nodeRetailer.setTextContent(objProductCatalog.getRetailer());
		nodeManufacture.setTextContent(objProductCatalog.getManufacturer());
		nodeRetailerDiscount.setTextContent(Float.toString(objProductCatalog.getRetailerDiscount()));
		nodeManufactureRebate.setTextContent(Float.toString(objProductCatalog.getManufacturerRebate()));		
		nodeOnsale.setTextContent(objProductCatalog.getOnSale());
		nodeQuantity.setTextContent(Integer.toString(objProductCatalog.getQuantity()));		

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