import java.io.*;
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

public class WfCache
{
	public static HashMap<String, ProductCatalog> phoneCatalog = null;
	public static HashMap<String, ProductCatalog> speakerCatalog = null;
	public static HashMap<String, ProductCatalog> laptopCatalog = null;
	public static HashMap<String, ProductCatalog> headphoneCatalog = null;
	public static HashMap<String, ProductCatalog> smartWatchCatalog = null;
	public static HashMap<String, ProductCatalog> externalStorageCatalog = null;
	public static HashMap<String, ProductCatalog> accessoryCatalog = null;
	public static HashMap<String, ProductCatalog> cartData = null;
	public static HashMap<String, User> wfUserData = null;

	public static HashMap<String, ProductCatalog> allProductsCatalog = null;
	public static int cartCount = 0;
	public static int currentOrderNo = 0;
	public static String currentOrderDeliveryTime = null;	

	public static String username = null;
	public static String role = null;

	public static boolean isNewApp = true;
	
	
	public WfCache()
	{
		
	}
		
	public static HashMap<String, ProductCatalog> isPhoneCatalogFetched()throws IOException, SQLException
	{		
		System.out.println("hey in phone*********");

		phoneCatalog = SAXInterface(Constants.PHONE_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : phoneCatalog.keySet()) 
			{
				ProductCatalog pc = phoneCatalog.get(key);	
				pc.setCategory("phone");				
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("phone");
		return apc;
	}
	
	public static HashMap<String, ProductCatalog> isSmartWatchCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");
		smartWatchCatalog = SAXInterface(Constants.SMARTWATCH_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : smartWatchCatalog.keySet()) 
			{
				ProductCatalog pc = smartWatchCatalog.get(key);	
				pc.setCategory("smartWatch");				
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("smartWatch");
		return apc;
	}
	
	public static HashMap<String, ProductCatalog> isLaptopCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");
		laptopCatalog = SAXInterface(Constants.LAPTOP_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : laptopCatalog.keySet()) 
			{
				ProductCatalog pc = laptopCatalog.get(key);	
				pc.setCategory("laptop");				
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("laptop");
		return apc;
	}

	public static HashMap<String, ProductCatalog> isSpeakerCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");
		speakerCatalog = SAXInterface(Constants.SPEAKER_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : speakerCatalog.keySet()) 
			{
				ProductCatalog pc = speakerCatalog.get(key);
				pc.setCategory("speaker");					
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("speaker");
		return apc;
	}

	public static HashMap<String, ProductCatalog> isHeadphoneCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");

		headphoneCatalog = SAXInterface(Constants.HEADPHONE_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : headphoneCatalog.keySet()) 
			{
				ProductCatalog pc = headphoneCatalog.get(key);
				pc.setCategory("headphone");					
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("headphone");
		return apc;
	}
	
	public static HashMap<String, ProductCatalog> isExternalStorageCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");
		externalStorageCatalog = SAXInterface(Constants.EXTERNALSTORAGE_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : externalStorageCatalog.keySet()) 
			{
				ProductCatalog pc = externalStorageCatalog.get(key);
				pc.setCategory("externalStorage");					
				objSql.insertProductDetails(pc);				
			}
		
		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("externalStorage");
		return apc;
	}
	
	public static HashMap<String, ProductCatalog> isAccessoryCatalogFetched()throws IOException, SQLException
	{
		System.out.println("hey in phone*********");
		accessoryCatalog = SAXInterface(Constants.ACCESSORY_XML);
		MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();	
			for (String key : accessoryCatalog.keySet()) 
			{
				ProductCatalog pc = accessoryCatalog.get(key);	
				pc.setCategory("accessory");				
				objSql.insertProductDetails(pc);				
			}

		HashMap<String, ProductCatalog> apc  = objSql.selectProductDetails("accessory");
		return apc;
	}
	
	public static HashMap<String, ProductCatalog> getAllProducts()throws IOException, SQLException
	{
		
			MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
			HashMap<String, ProductCatalog> allProductsCatalog  = sqlData.selectAllProductDetails();
		return allProductsCatalog;
	}
	
	public static void deleteProducts(String productID)throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.deleteProductDetails(productID);
	}

	public static void resetDB()throws IOException, SQLException
	{		
		MySqlDataStoreUtilities sqlData = new MySqlDataStoreUtilities();			
		sqlData.deleteProductDetails(null);
	}

	public static void addToCart(ProductCatalog objPC)
	{
		if(cartData == null)
		{
			cartData = new HashMap<String, ProductCatalog>();			
		}
		cartData.put(objPC.getId(), objPC);
	}
	
	public static HashMap<String, ProductCatalog> showCart()
	{		
		return cartData;
	}
	
	public static HashMap<String, ProductCatalog> deleteCart(String ID)
	{
		if(cartData != null)
		{			
			cartData.remove(ID);			
		}
		return cartData;
	}	
	
	public static void clearCart()
	{
		if(cartData != null)
		{			
			cartData.clear();			
		}
	}

	public static HashMap<String, User> UserDetails() throws SQLException {

		if (wfUserData == null) {
			
			MySqlDataStoreUtilities objSql = new MySqlDataStoreUtilities();
			wfUserData = objSql.getAllUserDetails();
		}
		return wfUserData;
	}
	
	public static HashMap<String, ProductCatalog> SAXInterface(String filePath)throws IOException
	{
		SAXProductHandler saxHandler = new SAXProductHandler();
		HashMap<String, ProductCatalog> pc = new HashMap<String, ProductCatalog>();
		try
		{
			pc = saxHandler.readDataFromXML(filePath);
		}
		catch(SAXException e)
		{
			System.out.println("Error - SAXException");
		}
		catch(ParserConfigurationException e)
		{
			System.out.println("Error - ParserConfigurationException");
		}
		return pc;
	}
}

