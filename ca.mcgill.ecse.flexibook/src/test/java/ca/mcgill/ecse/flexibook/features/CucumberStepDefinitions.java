package ca.mcgill.ecse.flexibook.features;

import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.controller.*;
import ca.mcgill.ecse.flexibook.application.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	
	private static FlexiBook flexiBook; 
	private static Owner owner;
	private static Business business;
	
	private static Customer c1; 
	private static Customer c2;
	private static Customer c3;
	
	private static Service s1;
	private static Service s2;
	private static Service s3;
	private static Service s4;
	
	private static ComboItem item1;
	private static ComboItem item2;
	private static ComboItem item3;
	private static ComboItem item4;
	private static ComboItem item5;
	private static ComboItem item6;

	
	private static ServiceCombo combo1;
	private static ServiceCombo combo2;
	
	private static String error;
	private static int errorCounter;
	

	// TODO you can add step implementations here
	@Given ("a Flexibook system exists")
	public static void thereIsFlexibooSystem() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		error = "";
		errorCounter = 0;
	}
	
	
    @Given ("an owner account exists in the system")
    public static void thereIsAnOwner() {
    	owner = flexiBook.getOwner();
    	if(owner == null) {
    		
    	}
    }
    
	
    @Given ("a business exists in the system")
    public static void thereIsABusiness() {
    	business = flexiBook.getBusiness();
    	if(business == null) {
    		
    	}
    }
    
    @Given ("the following customers exist in the system:")
    public static void followingCustomersExist() {
    	c1 = new Customer("customer1", "12345678", flexiBook);
    	c2 = new Customer("customer1", "12345678", flexiBook);
    	c2 = new Customer("customer1", "12345678", flexiBook);
    	flexiBook.addCustomer(c1);
    	flexiBook.addCustomer(c2);
    	flexiBook.addCustomer(c3);
    }
    
    @Given ("the following services exist in the system:")
    public static void followingServicesExist() {
    	s1 = new Service("wash", flexiBook, 10, 0, 0);
    	s2 = new Service("color", flexiBook, 75, 30, 45);
    	s3 = new Service("cut", flexiBook, 20, 0, 0);
    	s4 = new Service("dry", flexiBook, 10, 0, 0);
    	
    	flexiBook.addBookableService(s1);
    	flexiBook.addBookableService(s2);
    	flexiBook.addBookableService(s3);
    	flexiBook.addBookableService(s4);
    	
    }
    
    
    @Given ("the following service combos exist in the system:")
    public static void followingCombosExist() {
    	combo1 = new ServiceCombo("cut-basic", flexiBook, null);
    	item1 = new ComboItem(true, s3, combo1);
    	item2 = new ComboItem(false, s1, combo1);
    	item3 = new ComboItem(false, s4, combo1);
    	combo1.setMainService(item1);
    	
    	combo2 = new ServiceCombo("dye-basic", flexiBook, null);
    	item4 = new ComboItem(true, s2, combo2);
    	item5 = new ComboItem(false, s1, combo2);
    	item6 = new ComboItem(false, s4, combo2);
    	combo2.setMainService(item4);

    }
    
    @Given ("the business has the following opening hours")
    public static void hasBusinessHours() {
    	
    }
  
  
    @After
    public void tearDown() {
        flexiBook.delete();
    }
	
	
	

}

