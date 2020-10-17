package ca.mcgill.ecse.flexibook.features;

import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.flexibook.controller.*;
import ca.mcgill.ecse.flexibook.application.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.sql.Date;
import java.sql.Time;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	
	private static FlexiBook flexiBook; 
	
	
	private static String error;
	private static int errorCounter;
	
	private static void initailiseBackground() {
		
	}
	

	
	@Given ("a Flexibook system exists")
	public static void thereIsFlexibooSystem() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		error = "";
		errorCounter = 0;
	}
	
	
    @Given ("an owner account exists in the system")
    public static void thereIsAnOwner() {
    	Owner owner = flexiBook.getOwner();
    	if(owner == null) {
    		
    	}
    }
    
	
    @Given ("a business exists in the system")
    public static void thereIsABusiness() {
    	Business business = flexiBook.getBusiness();
    	if(business == null) {
    		
    	}
    }
    
    @Given ("the following customers exist in the system:")
    public static void followingCustomersExist() {
    	Customer c1 = new Customer("customer1", "12345678", flexiBook);
    	Customer c2 = new Customer("customer2", "12345678", flexiBook);
    	Customer c3 = new Customer("customer3", "12345678", flexiBook);
    	flexiBook.addCustomer(c1);
    	flexiBook.addCustomer(c2);
    	flexiBook.addCustomer(c3);
    }
    
    @Given ("the following services exist in the system:")
    public static void followingServicesExist() {
		
		Service s1 = new Service("wash", flexiBook, 10, 0, 0);
		Service s2 = new Service("color", flexiBook, 75, 30, 45);
		Service s3 = new Service("cut", flexiBook, 20, 0, 0);
		Service s4 = new Service("dry", flexiBook, 10, 0, 0);
		
		flexiBook.addBookableServiceAt(s1, 1);
		flexiBook.addBookableServiceAt(s2, 2);
		flexiBook.addBookableServiceAt(s3, 3);
		flexiBook.addBookableServiceAt(s4, 4);

    	
    }
    
    
    @Given ("the following service combos exist in the system:")
    public static void followingCombosExist() {
    	ServiceCombo combo1 = new ServiceCombo("cut-basic", flexiBook, null);
    	ComboItem item1 = new ComboItem(true, (Service)flexiBook.getBookableService(3), combo1);
    	ComboItem item2 = new ComboItem(false, (Service)flexiBook.getBookableService(1), combo1);
    	ComboItem item3 = new ComboItem(false, (Service)flexiBook.getBookableService(4), combo1);
    	combo1.setMainService(item1);
    	combo1.addService(item1);
    	combo1.addService(item2);
    	combo1.addService(item3);
    	
    	
    	ServiceCombo combo2 = new ServiceCombo("dye-basic", flexiBook, null);
    	ComboItem item4 = new ComboItem(true, (Service)flexiBook.getBookableService(2), combo2);
    	ComboItem item5 = new ComboItem(false, (Service)flexiBook.getBookableService(1), combo2);
    	ComboItem item6 = new ComboItem(false, (Service)flexiBook.getBookableService(4), combo2);
    	combo2.setMainService(item4);
    	combo2.addService(item4);
    	combo2.addService(item5);
    	combo2.addService(item6);
    	
    	flexiBook.addBookableServiceAt(combo1, 5);
    	flexiBook.addBookableServiceAt(combo2, 6);

    }
    
    @Given ("the business has the following opening hours")
    public static void hasBusinessHours() {
    	BusinessHour day1 = new BusinessHour(DayOfWeek.Monday, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"), flexiBook);
    	BusinessHour day2 = new BusinessHour(DayOfWeek.Monday, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"), flexiBook);
    	BusinessHour day3 = new BusinessHour(DayOfWeek.Monday, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"), flexiBook);
    	BusinessHour day4 = new BusinessHour(DayOfWeek.Monday, Time.valueOf("09:00:00"), Time.valueOf("17:00:00"), flexiBook);
    	BusinessHour day5 = new BusinessHour(DayOfWeek.Monday, Time.valueOf("09:00:00"), Time.valueOf("15:00:00"), flexiBook);
    	
    	flexiBook.getBusiness().addBusinessHour(day1);
    	flexiBook.getBusiness().addBusinessHour(day2);
    	flexiBook.getBusiness().addBusinessHour(day3);
    	flexiBook.getBusiness().addBusinessHour(day4);
    	flexiBook.getBusiness().addBusinessHour(day5);
    	
    }
    
    
    @Given ("the business has the following holidays")
    public static void hasHolidays() {
    	TimeSlot timeSlot = new TimeSlot(Date.valueOf("2020-12-31"), Time.valueOf("00:00"), Date.valueOf("2021-01-01"),  Time.valueOf("23:59"), flexiBook);
    	Business business = flexiBook.getBusiness();
    	business.addHoliday(timeSlot);
    }
    
    @Given ("the following appointments exist in the system")
    public static void hasAppointments() {
    	List<ComboItem> items = flexiBook.getBookableService(6)
    	Appointment a1 = new Appointment(flexiBook.getCustomer(1), flexiBook.getBookableService(6), )
    }
  
  
    @After
    public void tearDown() {
        flexiBook.delete();
    }
	
	
	

}

