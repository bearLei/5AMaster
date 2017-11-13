package com.puti.education.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.text.TextUtils;

public class NumberUtils {
	
	/**
	 * 格式化价格，强制保留2位小数
	 * @param price
	 * @return
	 */
	public static String formatPrice(double value,boolean hasPrefix) {
		
				String valueStr = value +"";
			    if (TextUtils.isEmpty(valueStr) && hasPrefix) {
					return  "¥ "+"0.00";
				}else if (TextUtils.isEmpty(valueStr) && !hasPrefix) {
					return "0.00";
				}
	            
	            String result = null;
		        
	            DecimalFormat decimalFormat = null;
		        if (valueStr.contains(".")) {
		        	 
					decimalFormat = new DecimalFormat("###0.00##");  
				    result = decimalFormat.format(value);  
				     
				     
				}else{
					
					BigDecimal bigDecimal = new BigDecimal(value);  
			        bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);  
			        BigDecimal newBigDecimal = bigDecimal; 
			        decimalFormat = new DecimalFormat("0.00"); 
			        decimalFormat.setRoundingMode(RoundingMode.DOWN);  
				    result = decimalFormat.format(newBigDecimal); 
					
				}
		        
		        if (hasPrefix) {
		        	return "¥ "+result;  
				}else {
					return result;  
				}
	}
	
	public static String formatPriceWithoutCoin(double price) {
		DecimalFormat df=new DecimalFormat("0.00");
		return df.format(price);
	}
	
	public static double formatDouble(double number, int decimalNum){
		BigDecimal  b = new BigDecimal(number);  
		double result = b.setScale(decimalNum, BigDecimal.ROUND_HALF_UP).doubleValue();  
		return result;
	}
	
	//保留指定的小数位数，同时作进位处理
	public static double formatUpDouble(double number, int decimalNum){
		BigDecimal  b = new BigDecimal(number);  
		double result = b.setScale(decimalNum, BigDecimal.ROUND_UP).doubleValue();  
		return result;
	}
	
	
	 public static String formatQuantity(double value){
		 DecimalFormat decimalFormat = null;
		 decimalFormat = new DecimalFormat("####.##");
		 decimalFormat.setRoundingMode(RoundingMode.DOWN);  
		 return decimalFormat.format(value);   
	 }
	 
	public static double formatDoubleReserve(double number, int decimalNum){
		BigDecimal  b = new BigDecimal(number);  
		double result = b.setScale(decimalNum, BigDecimal.ROUND_DOWN).doubleValue();  
		return result;
	}
	
	 /** 
     * double 相加 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double sum(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.add(bd2).doubleValue(); 
    } 


    /** 
     * double 相减 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double sub(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.subtract(bd2).doubleValue(); 
    } 
    
    
    /** 
     * double 乘法 
     * @param d1 
     * @param d2 
     * @return 
     */ 
    public static double mul(double d1,double d2){ 
        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.multiply(bd2).doubleValue(); 
    } 


    /** 
     * double 除法 
     * @param d1 
     * @param d2 
     * @param scale 四舍五入 小数点位数 
     * @return 
     */ 
    public static double div(double d1,double d2,int scale){ 
        //  当然在此之前，你要判断分母是否为0，   
        //  为0你可以根据实际需求做相应的处理 

        BigDecimal bd1 = new BigDecimal(Double.toString(d1)); 
        BigDecimal bd2 = new BigDecimal(Double.toString(d2)); 
        return bd1.divide 
               (bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    } 


	  
}
