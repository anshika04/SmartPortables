import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class SAXProductHandler extends DefaultHandler {
	
	boolean brootnode = false;
	boolean bid = false;
	boolean bname = false;
	boolean bretailer = false;
	boolean bprice = false;
	boolean bcondition = false;
	boolean bimagepath = false;
	boolean bmanufacturer = false;
	boolean bretailerdiscount = false;
	boolean bmanufacturerrebate = false;
	boolean bonsale = false;
	boolean bquantity = false;

	ProductCatalog catalog;
	
	HashMap<String, ProductCatalog> hpCatalog = new HashMap<String, ProductCatalog>();

	public HashMap<String, ProductCatalog> readDataFromXML(String fileName)	throws ParserConfigurationException, SAXException, IOException 
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		javax.xml.parsers.SAXParser parser = factory.newSAXParser();

		parser.parse(new File(fileName), this);
		
		return hpCatalog;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	{			
		
		if(qName.matches("(?i)phone|speaker|laptop|smartWatch|headphone|externalStorage|accessory"))
		{
			brootnode = true;
			catalog = new ProductCatalog();
		}
		
		if(brootnode)
		{		
			if (qName.equalsIgnoreCase("ID")) {
				bid = true;
			}

			else if (qName.equalsIgnoreCase("NAME")) {
				bname = true;
			}

			else if (qName.equalsIgnoreCase("RETAILER")) {
				bretailer = true;
			}

			else if (qName.equalsIgnoreCase("PRICE")) {
				bprice = true;
			}

			else if (qName.equalsIgnoreCase("CONDITION")) {
				bcondition = true;
			}

			else if (qName.equalsIgnoreCase("IMAGE")) {
				bimagepath = true;
			}
			
			else if (qName.equalsIgnoreCase("MANUFACTURER")) {
				bmanufacturer = true;
			}
			
			else if (qName.equalsIgnoreCase("RETAILER_DISCOUNT")) {
				bretailerdiscount = true;
			}
			
			else if (qName.equalsIgnoreCase("MANUFACTURER_REBATE")) {
				bmanufacturerrebate = true;
			}

			else if (qName.equalsIgnoreCase("ONSALE")) {
				bonsale = true;
			}

			else if (qName.equalsIgnoreCase("QUANTITY")) {
				bquantity = true;
			}
		}
	}

	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
			if(qName.matches("(?i)phone|speaker|laptop|smartWatch|headphone|externalStorage|accessory")) {			
			hpCatalog.put(catalog.getId(), catalog);
			brootnode = false;
		}
	}

	
	public void characters(char[] ch, int start, int length) throws SAXException {	
		

			if (bid) {
				catalog.setId(new String(ch, start, length));
				bid = false;
			}
			
			else if (bname) {
				catalog.setName(new String(ch, start, length));
				bname = false;
			}
			
			else if (bretailer) {
				catalog.setRetailer(new String(ch, start, length));
				bretailer = false;
			}
			
			else if (bcondition) {
				catalog.setCondition(new String(ch, start, length));
				bcondition = false;
			}
			else if (bprice) {
				catalog.setPrice(Float.parseFloat(new String(ch, start, length)));
				bprice = false;
			}
			
			else if (bimagepath) {
				catalog.setImagepath(new String(ch, start, length));
				bimagepath = false;
			}

			else if (bmanufacturer) {
				catalog.setManufacturer(new String(ch, start, length));
				bmanufacturer = false;
			}
			else if (bretailerdiscount) {
				catalog.setRetailerDiscount(Float.parseFloat(new String(ch, start, length)));
				bretailerdiscount = false;
			}

			else if (bmanufacturerrebate) {
				catalog.setManufacturerRebate(Float.parseFloat(new String(ch, start, length)));
				bmanufacturerrebate = false;
			}

			else if (bonsale) {
				catalog.setOnSale(new String(ch, start, length));
				bonsale = false;
			}	

			else if (bquantity) {
				catalog.setQuantity(Integer.parseInt(new String(ch, start, length)));
				bquantity = false;
			}			
	}
}