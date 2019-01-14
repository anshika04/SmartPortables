
public class Review {

	private String ProductModelName;	
	private String ProductCategory;
	private String ProductPrice;
	private String RetailerName;
	private String RetailerZip;
	private String RetailerCity;
	private String RetailerState;
	private String ProductOnSale;	
	private String ManufacturerName;
	private String ManufacturerRebate;
	private String UserID;
	private String UserAge;
	private String UserGender;
	private String UserOccupation;
	private String ReviewRating;
	private String ReviewText;
	private String ReviewDate;
	
	
	public Review()
	{
		
	}
	

	public Review(String ProductModelName, String ProductCategory, String ProductPrice, String RetailerName,
			String RetailerZip, String RetailerCity, String RetailerState, String ProductOnSale,
			String ManufacturerName, String ManufacturerRebate, String UserID, String UserAge, String UserGender,
			String UserOccupation, String ReviewRating, String ReviewText, String ReviewDate) {
		
		this.ProductModelName = ProductModelName;
		this.ProductCategory = ProductCategory;
		this.ProductPrice = ProductPrice;
		this.RetailerName = RetailerName;
		this.RetailerZip = RetailerZip;
		this.RetailerCity = RetailerCity;
		this.RetailerState = RetailerState;
		this.ProductOnSale = ProductOnSale;
		this.ManufacturerName = ManufacturerName;
		this.ManufacturerRebate = ManufacturerRebate;
		this.UserID = UserID;
		this.UserAge = UserAge;
		this.UserGender = UserGender;
		this.UserOccupation = UserOccupation;
		this.ReviewRating = ReviewRating;
		this.ReviewText = ReviewText;
		this.ReviewDate = ReviewDate;
	}
	
	
	public String getProductModelName() {
		return ProductModelName;
	}
	public void setProductModelName(String productModelName) {
		this.ProductModelName = productModelName;
	}

	public String getCategory() {
		return ProductCategory;
	}

	public void setCategory(String ProductCategory) {
		this.ProductCategory = ProductCategory;
	}

	public String getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(String productPrice) {
		this.ProductPrice = productPrice;
	}

	public String getRetailerName() {
		return RetailerName;
	}

	public void setRetailerName(String retailerName) {
		this.RetailerName = retailerName;
	}

	public String getRetailerZip() {
		return RetailerZip;
	}

	public void setRetailerZip(String retailerZip) {
		this.RetailerZip = retailerZip;
	}

	public String getRetailerCity() {
		return RetailerCity;
	}

	public void setRetailerCity(String retailerCity) {
		this.RetailerCity = retailerCity;
	}

	public String getRetailerState() {
		return RetailerState;
	}

	public void setRetailerState(String retailerState) {
		this.RetailerState = retailerState;
	}

	public String getProductOnSale() {
		return ProductOnSale;
	}

	public void setProductOnSale(String productOnSale) {
		this.ProductOnSale = productOnSale;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		this.UserID = userID;
	}

	public String getManufacturerRebate() {
		return ManufacturerRebate;
	}

	public void setManufacturerRebate(String manufacturerRebate) {
		this.ManufacturerRebate = manufacturerRebate;
	}

	public String getUserOccupation() {
		return UserOccupation;
	}

	public void setUserOccupation(String userOccupation) {
		this.UserOccupation = userOccupation;
	}

	public String getReviewRating() {
		return ReviewRating;
	}

	public void setReviewRating(String reviewRating) {
		this.ReviewRating = reviewRating;
	}

	public String getUserAge() {
		return UserAge;
	}

	public void setUserAge(String userAge) {
		this.UserAge = userAge;
	}

	public String getManufacturerName() {
		return ManufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.ManufacturerName = manufacturerName;
	}

	public String getReviewText() {
		return ReviewText;
	}

	public void setReviewText(String reviewText) {
		this.ReviewText = reviewText;
	}

	public String getReviewDate() {
		return ReviewDate;
	}

	public void setReviewDate(String reviewDate) {
		ReviewDate = reviewDate;
	}

	public String getUserGender() {
		return UserGender;
	}

	public void setUserGender(String userGender) {
		this.UserGender = userGender;
	}
}
