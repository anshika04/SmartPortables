import com.mongodb.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.util.*;
import com.mongodb.AggregationOutput;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MongoDBDataStoreUtilities  {
	
	private static final long serialVersionUID = 1L;
	private static MongoClient mongo;
	
	
	public void init() throws ServletException{
      	// Connect to Mongo DB
		mongo = new MongoClient("localhost", 27017);
	}


	public static void insertReview(Review rv)
	{
		try
		{
			if(rv != null)
			{

				System.out.println("true rv");

				mongo = new MongoClient("localhost", 27017);
				System.out.println("true client");

				@SuppressWarnings("deprecation")
				DB db = mongo.getDB("CustomerReviews");
				DBCollection myReviews = db.getCollection("myReviews");
				System.out.println("Collection selected successfully");
				
				BasicDBObject doc = new BasicDBObject("title", "myReviews")
						.append("productmodelname", rv.getProductModelName())
						.append("category", rv.getCategory())
						.append("productprice", rv.getProductPrice())
						.append("retailername", rv.getRetailerName())
						.append("retailerzip", rv.getRetailerZip())
						.append("retailercity", rv.getRetailerCity())
						.append("retailerstate", rv.getRetailerState())
						.append("productonsale", rv.getProductOnSale())
						.append("userid", rv.getUserID())
						.append("manufacturerrebate", rv.getManufacturerRebate())
						.append("useroccupation", rv.getUserOccupation())
						.append("reviewrating", rv.getReviewRating())
						.append("userage", rv.getUserAge())
						.append("manufacturername", rv.getManufacturerName())
						.append("reviewdate", rv.getReviewDate())
						.append("reviewtext", rv.getReviewText())
						.append("usergender", rv.getUserGender());

				myReviews.insert(doc);
			}
		}
		catch(Exception ex)
		{
			 ex.printStackTrace();
		}
	}
	
	public static HashMap<Integer, Review> readReviews(String productName)
	{
		HashMap<Integer, Review> hmReview = new HashMap<Integer, Review>();		
		try
		{			
			int i = 0;
			MongoClient mango = new MongoClient("localhost", 27017);	

			DB db = mango.getDB("CustomerReviews");
			DBCollection myReviews = db.getCollection("myReviews");
			
			BasicDBObject searchQuery = new BasicDBObject();
			searchQuery.put("productmodelname", productName);

			DBCursor cursor = myReviews.find(searchQuery);
			
			while(cursor.hasNext()!=false){
				DBObject dob = cursor.next();
				i = i + 1;
				
				//System.out.println(Integer.parseInt((dob.get("_id")).toString()));
				
				Review rv1 = new Review((String)dob.get("productmodelname"),(String)dob.get("category"),(String)dob.get("productprice"),
				(String)dob.get("retailername"),(String)dob.get("retailerzip"),(String)dob.get("retailercity"),(String)dob.get("retailerstate"),
				(String)dob.get("productonsale"),(String)dob.get("manufacturername"),(String)dob.get("manufacturerrebate"),(String)dob.get("userid"),
				(String)dob.get("userage"),(String)dob.get("usergender"),(String)dob.get("useroccupation"),(String)dob.get("reviewrating"),
				(String)dob.get("reviewtext"),(String)dob.get("reviewdate"));
								
				hmReview.put(i,rv1);				
			}
		}
		catch(MongoException ex)
		{
			 ex.printStackTrace();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return hmReview;
	}
	
	public static HashMap<String, Integer> topZipCodes()
	{
		MongoClient mango;
		mango = new MongoClient("localhost", 27017);
			
		DB db = mango.getDB("CustomerReviews");
		DBCollection myReviews = db.getCollection("myReviews");
		
		HashMap<String, Integer> hmReview=new HashMap<String, Integer>();
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("retailerzip", "$_id");
		projectFields.put("reviewCount", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$retailerzip");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewCount",-1);
		
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(group, project, orderby, limit);
		
		String reviewCount = null;
		int reviewCountCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("retailerzip"));
			reviewCount = bobj.getString("reviewCount");
			if(reviewCount == null || reviewCount.isEmpty()) {
				reviewCountCount=0;
			} else {
				reviewCountCount=Integer.parseInt(reviewCount);
			}
			System.out.println(reviewCountCount);
			hmReview.put(bobj.getString("retailerzip"), reviewCountCount);

		}		
		return hmReview;
	}
	
	public static HashMap<String, Integer> topProducts()
	{
		MongoClient mango;
		mango = new MongoClient("localhost", 27017);
			
		DB db = mango.getDB("CustomerReviews");
		DBCollection myReviews = db.getCollection("myReviews");
		
		HashMap<String, Integer> hmReview=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productmodelname", "$_id");
		projectFields.put("reviewCount", "$count");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productmodelname");
		groupFields.put("count", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		// Specify the field that you want to sort on, and the direction of the sort
		sort.put("reviewCount",-1);
		
		
		//Adding sort object in DbObject
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(group, project, orderby, limit);
		
		String reviewCount = null;
		int reviewCountCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productmodelname"));
			reviewCount = bobj.getString("reviewCount");
			if(reviewCount == null || reviewCount.isEmpty()) {
				reviewCountCount=0;
			} else {
				reviewCountCount=Integer.parseInt(reviewCount);
			}
			System.out.println(reviewCountCount);
			hmReview.put(bobj.getString("productmodelname"), reviewCountCount);
		}		
		return hmReview;
	}

	public static HashMap<String, Integer> topLikedProducts()
	{
		MongoClient mango;
		mango = new MongoClient("localhost", 27017);
			
		DB db = mango.getDB("CustomerReviews");
		DBCollection myReviews = db.getCollection("myReviews");
		
		HashMap<String, Integer> hmReview=new HashMap<String, Integer>();
		
		DBObject projectFields = new BasicDBObject("_id", 0);
		projectFields.put("productmodelname", "$_id");
		projectFields.put("reviewLikes", "$likes");
		DBObject project = new BasicDBObject("$project", projectFields);

		DBObject groupFields = new BasicDBObject("_id", 0);
		groupFields.put("_id", "$productmodelname");
		groupFields.put("likes", new BasicDBObject("$sum", 1));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject limit=new BasicDBObject();
		DBObject orderby=new BasicDBObject();
		
		DBObject sort = new BasicDBObject();
		
		sort.put("reviewLikes",-1);
		
		DBObject match = new BasicDBObject("$match", new BasicDBObject("reviewrating", '5'));
				
		orderby=new BasicDBObject("$sort",sort);
		limit=new BasicDBObject("$limit",5);
		
		AggregationOutput aggregate = myReviews.aggregate(group, project, orderby, limit);
		
		String reviewLikes = null;
		int reviewLikesCount;
		for (DBObject result : aggregate.results()) {
			BasicDBObject bobj = (BasicDBObject) result;
			System.out.println(bobj.getString("productmodelname"));
			reviewLikes = bobj.getString("reviewLikes");
			if(reviewLikes == null || reviewLikes.isEmpty()) {
				reviewLikesCount=0;
			} else {
				reviewLikesCount=Integer.parseInt(bobj.getString("reviewLikes"));
			}
			System.out.println(reviewLikesCount);
			hmReview.put(bobj.getString("productmodelname"), reviewLikesCount);
		}
		
		return hmReview;
	}	

	
}