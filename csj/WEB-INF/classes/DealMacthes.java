import java.util.*;
import java.io.*;

public class DealMacthes
{
	public DealMacthes()
	{
		
	}
	
	public String DealMacthesProducts()
	{
		HashMap<String, ProductCatalog> selectedProducts = new HashMap<String, ProductCatalog>();

		StringBuilder dynamicHtml = new StringBuilder();
		String line = null;
		ProductCatalog pc = null;
		String productType = null;
		try
		{			
			dynamicHtml.append("<h1 style='float: center; color:red'>Welcome to Smart Portables.</h1>");
			dynamicHtml.append("<br><h2>Price-Match Guaranteed</h2>");	

			HashMap<String, ProductCatalog> allProductsMap = WfCache.getAllProducts();

			for(String key : allProductsMap.keySet())
			{				
				pc = new ProductCatalog();
				pc = allProductsMap.get(key);

				if(selectedProducts.size() < 2 && !selectedProducts.containsKey(pc.getId()))
				{					
					BufferedReader reader = new BufferedReader(new FileReader(new File(Constants.DEAL_MATCH_FILEPATH)));
					line = reader.readLine();
					if(line == null)
					{
						dynamicHtml.append("<h3 align='center'>No Offers Found</h3>");
						break;
					}
					else
					{
						do
						{
							if(line.contains(pc.getName()))
							{
								dynamicHtml.append("<h3>"+line+"</h3>");
								dynamicHtml.append("<br>");
								selectedProducts.put(pc.getId(),pc);
								break;
							}
						}while((line= reader.readLine()) != null);
					}
					reader.close();
				}				
			}
			
			dynamicHtml.append("<article class='expanded'>");
			dynamicHtml.append("<h2>Deal Matches</h2>");
			dynamicHtml.append("<table class='table well'>");
			// dynamicHtml.append("<tr>");
			// dynamicHtml.append("<th></th>");
			// dynamicHtml.append("<th></th>");
			// dynamicHtml.append("<th></th>");
			// dynamicHtml.append("</tr>");
			
			for(String key : selectedProducts.keySet())
			{
				pc = new ProductCatalog();
				pc = selectedProducts.get(key);
				
				if(pc.getId().toLowerCase().startsWith("ph"))
				{
					productType = "phone"; 
				}
				else if(pc.getId().toLowerCase().startsWith("sp"))
				{
					productType = "speaker"; 
				}
				else if(pc.getId().toLowerCase().startsWith("lp"))
				{
					productType = "laptop"; 
				}
				else if(pc.getId().toLowerCase().startsWith("sm"))
				{
					productType = "smartWatch"; 
				}
				else if(pc.getId().toLowerCase().startsWith("he"))
				{
					productType = "headphone"; 
				}
				else if(pc.getId().toLowerCase().startsWith("ex"))
				{
					productType = "externalStorage"; 
				}
				else if(pc.getId().toLowerCase().startsWith("ac"))
				{
					productType = "accessory"; 
				}
				
				dynamicHtml.append("<tr>");
				dynamicHtml.append("<td>");				
				dynamicHtml.append("<img src = '"+ pc.getImagepath() +"' width = '250' height = '250'>");				
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td>");
				dynamicHtml.append("<p>Model: "+ pc.getName() +"</p>");
				dynamicHtml.append("<p>Sold by: "+ pc.getRetailer()+ "</p>");
				dynamicHtml.append("<p> Condition: "+ pc.getCondition()+ "</p>");
				dynamicHtml.append("<p> Price: "+ pc.getPrice()+ "</p>");
				dynamicHtml.append("</td>");
				dynamicHtml.append("<td align='center'>");

				dynamicHtml.append("<div class='row'>");
				dynamicHtml.append("<form method = 'get' action = 'viewdescription'>");
				dynamicHtml.append("<input type='hidden' name = 'productid1' value='"+key+"'/>");
				dynamicHtml.append("<input type = 'submit' name = 'View Description' class='btn btn-primary btn-sm' value = 'View Description'>");
				dynamicHtml.append("</form>");
				dynamicHtml.append("</div> <br>");

				if(WfCache.username != null) {

					if(pc.getOnSale().equalsIgnoreCase("yes") && WfCache.role.equalsIgnoreCase("customer")) {

						dynamicHtml.append("<div class='row'>");
						dynamicHtml.append("<form method = 'post' action = '"+productType+"'>");
						dynamicHtml.append("<input type='hidden' name = 'productid' value='"+key+"'/>");
						dynamicHtml.append("<input type = 'submit' name = '"+productType+"' class='btn btn-primary btn-sm' value = 'Add to Cart'>");
						dynamicHtml.append("</form>");
						dynamicHtml.append("</div> <br>");

					}					

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'readreview'>");
					dynamicHtml.append("<input type='hidden' name = 'productname' value='"+pc.getName()+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Read Review' class='btn btn-primary btn-sm' value = 'Read Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");

					dynamicHtml.append("<div class='row'>");
					dynamicHtml.append("<form method = 'get' action = 'writereview'>");
					dynamicHtml.append("<input type='hidden' name = 'productid2' value='"+key+"'/>");
					dynamicHtml.append("<input type = 'submit' name = 'Write Review' class='btn btn-primary btn-sm' value = 'Write Review'>");
					dynamicHtml.append("</form>");
					dynamicHtml.append("</div> <br>");
				}				
				dynamicHtml.append("</td>");
				dynamicHtml.append("</tr>");				
			}
			dynamicHtml.append("</table>");
			dynamicHtml.append("</article>");			
		}
		catch(Exception ex)
		{
			
		}
		return dynamicHtml.toString();		
	}
}