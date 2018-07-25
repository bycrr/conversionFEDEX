package br.com.claudiorenatorosa.conversionFEDEX;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.naming.spi.DirStateFactory.Result;

public class AppConversion {
	
	private final static DateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");
	private final static String FROM_CURRENCY = "EUR";  
	private final static String TO_CURRENCY = "USD";  
	private final static Float VALUE_CONVERT = 2000f;
	private final static String FOLDER = "C:\\Users\\claudiorenatorosa\\";
	private final static String EXTENSION = ".csv";
	
    public static void main( String[] args ) {
    	
    	Date today = new Date();
    	String dateQuotation = FORMAT_DATE.format(today.getTime());
    	BigDecimal result = null;
    	
    	try {
    		result = currencyQuotation(FROM_CURRENCY, TO_CURRENCY, VALUE_CONVERT, dateQuotation);
   		
            System.out.println( "Today quotation ("+ dateQuotation +"): " + 
            		VALUE_CONVERT + " " + FROM_CURRENCY + " = " +
            		result.toString() + " " + TO_CURRENCY);
            
    	} catch (Exception e) {
            System.out.println( "Erro: " + e );    		
    	}
    	
    }
    
    public static BigDecimal currencyQuotation(String from, String to, Number value, String quotation) 
		throws Exception {
    	
    	final Integer COLUMN_CURRENCY = 3; // Moeda
    	final Integer COLUMN_TAX = 4; // Taxa Venda
    	
    	Float valueFrom, valueTo;
    	
		if (value.floatValue() < 0f) {
			throw new Exception("Error: 'value' is smaller than zero."); 
		}
		
		Float quotationFrom = Float.valueOf(extractCel( COLUMN_CURRENCY, 
							  from.toUpperCase(), COLUMN_TAX, quotation));
		
		if (quotationFrom.equals("0")) {
			throw new Exception("Error: currency in 'from' not found."); 
		}
		
		valueFrom = value.floatValue() * quotationFrom;		
		
		Float quotationTo = Float.valueOf(extractCel( COLUMN_CURRENCY, 
							to.toUpperCase(), COLUMN_TAX, quotation));
		
		if (quotationTo.equals("0")) {
			throw new Exception("Error: currency in 'to' not found."); 
		}
		
		valueTo = valueFrom / quotationTo;
		
    	return BigDecimal.valueOf(valueTo).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
    
    private static String extractCel(Integer colFind, String findCurrency, Integer colReturn, String dateQuotation)
		throws Exception {

    	DecimalFormat truncFormat = new DecimalFormat("0.00");
    	Date dateTest = (Date) FORMAT_DATE.parse(dateQuotation);
    	GregorianCalendar calendar = new GregorianCalendar();
    	calendar.setTime(dateTest);
    	int dayWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
    	
    	if (dayWeek == GregorianCalendar.SUNDAY) {
    		calendar.add(GregorianCalendar.DAY_OF_MONTH, -2); ;
    	}
    	if (dayWeek == GregorianCalendar.SATURDAY) {
    		calendar.add(GregorianCalendar.DAY_OF_MONTH, -1); ;
    	}
    	
    	dateTest = calendar.getTime();
    	dateQuotation = FORMAT_DATE.format(dateTest);    	
    	String fileBCB = dateQuotation.substring(6) + dateQuotation.substring(3,5) + dateQuotation.substring(0,2);  
    	
   		BufferedReader quotations = new BufferedReader(new FileReader(FOLDER + fileBCB + EXTENSION));
		String rowBCB, valueTax = "0", separator = ";";
		String[] quotation;		
		
        while ((rowBCB = quotations.readLine()) != null) {
        	quotation = rowBCB.split(separator);
            if (quotation[colFind].equals(findCurrency)) {
            	valueTax = quotation[colReturn].replaceAll(",", ".");
            	break;
            }
        }            
    	quotations.close();
        return valueTax;
    }

}

