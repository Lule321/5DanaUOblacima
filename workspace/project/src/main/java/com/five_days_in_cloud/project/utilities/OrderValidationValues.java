package com.five_days_in_cloud.project.utilities;

public enum OrderValidationValues {
	INVALID_ID, INVALID_QUANTITY, INVALID_PRICE, INVALID_CURRENCY, INVALID_TYPE;
	
	public String getError()
	{
		if(this.equals(INVALID_CURRENCY)) return "Currency not equal to BTCUSD!";
		if(this.equals(INVALID_PRICE)) return "Price must be positive!";
		if(this.equals(INVALID_QUANTITY)) return "Quantity must be positive!";
		if(this.equals(INVALID_ID)) return "Id negative or already exists!";
		return "";
 	}
}
