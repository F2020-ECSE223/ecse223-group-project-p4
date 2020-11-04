package ca.mcgill.ecse.flexibook.features;

import org.junit.Test;

//import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Map;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse223.flexibook.controller.*;
import ca.mcgill.ecse.flexibook.application.*;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	private static FlexiBook flexiBook;
	private String error;
	private int errorCntr;
	private Integer appointmentCntr = 0;
	private Integer prevAppointmentCntr = 0;
	private String updateAppointmentSuccess = null;
	private Date SystemDateTime;
	private boolean exception;

	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Given("{string} is logged in to their account")
	public void is_logged_in_to_their_account(String string) {
		User user = findCustomerByName(string);
		FlexiBookApplication.setCurrentUser(user);
	}


//	
//	
//	
//	/**
//	 * @author Shaswata Bhattacharyya
//	 */

	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		appointmentCntr = 0;
		prevAppointmentCntr = 0;
		updateAppointmentSuccess = "";

	    error = "";
	    errorCntr = 0;

	
		error = "";
		errorCntr = 0;
	}

//	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		SystemDateTime = Date.valueOf(string.substring(0, 10));
		String dateAndTime[] = string.split("\\+");
		FlexiBookApplication.setSystemDate(dateAndTime[0]);
		FlexiBookApplication.setSystemTime(dateAndTime[1] + ":00");

	}
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param dataTable
	 */
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		Appointment appointment = null;
		
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class); 
		for(Map<String, String> columns : rows) {

			Customer customer = findCustomerByName(columns.get("customer"));
			BookableService bookableService = findServiceByName(columns.get("serviceName"));
			
			TimeSlot timeSlot = new TimeSlot(Date.valueOf(columns.get("date")), Time.valueOf(columns.get("startTime") + ":00"), Date.valueOf(columns.get("date")), Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			
			
			appointment = new Appointment(customer, bookableService, timeSlot, flexiBook);
			
			if(bookableService.getClass().equals(ServiceCombo.class)) {		//check if chosen service is a ServiceCombo
				ServiceCombo combo = (ServiceCombo) bookableService;
				
				//set main service
				int index = columns.get("serviceName").indexOf('-');
				Service main = (Service)findServiceByName(columns.get("serviceName").substring(0, index));
				ComboItem newItem = new ComboItem(true, main, combo);
				appointment.addChosenItem(newItem);
				
				//get the optional services
				String itemList = columns.get("optServices");
				String[] items = itemList.split(",");
				for(String item : items) {
					Service thisService = (Service) findServiceByName(item);
					newItem = new ComboItem(false, thisService, combo);
					appointment.addChosenItem(newItem);
				}

			}
			
		}
	    
	}
	
	

	/**
	 * @author Shaswata Bhattacharyya
	 */
	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
		Owner owner = flexiBook.getOwner();
		if (owner == null) {
			owner = new Owner("owner", "admin", flexiBook);
			flexiBook.setOwner(owner);
		}
	}

	/**
	 * @author Shaswata Bhattacharyya
	 */
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
		Business business = flexiBook.getBusiness();
		if (business == null) {
			business = new Business("aName", "anAddress", "aPhoneNo", "enEmail", flexiBook);
		}
	}


//	
///**
//	 * @author Shaswata Bhattacharyya
//	 * @param dataTable
//	 */

	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> columns : rows) {
			flexiBook.addCustomer(new Customer(columns.get("username"), columns.get("password"), flexiBook));

		}

	}





//
//
///**
//	 * @author Shaswata Bhattacharyya
//	 * @param dataTable
//	 */
	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> columns : rows) {
			ServiceCombo combo = new ServiceCombo(columns.get("name"), flexiBook);
			flexiBook.addBookableService(combo);
			Service main = (Service) findServiceByName(columns.get("mainService"));

			// extract the combo items
			String itemList = columns.get("services");
			String[] items = itemList.split(",");
			String mandatoryList = columns.get("mandatory");
			String[] mandatory = mandatoryList.split(",");

			for (int i = 0; i < items.length; i++) {

				BookableService thisService = findServiceByName(items[i]);
				boolean thisMandatory = Boolean.parseBoolean(mandatory[i]);
				if (thisService.getClass().equals(Service.class)) {
					Service serviceItem = (Service) thisService;
					if (items[i].equals(main.getName())) {
						combo.setMainService(new ComboItem(true, serviceItem, combo));
					} else {
						combo.addService(thisMandatory, serviceItem);
					}
				}

			}

		}
	}






/**
	 * @author Shaswata Bhattacharyya
	 * @param dataTable
	 */
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class); 
		for(Map<String, String> columns : rows) {
			flexiBook.addBookableService(new Service(columns.get("name"), flexiBook, Integer.parseInt(columns.get("duration")), Integer.parseInt(columns.get("downtimeDuration")), Integer.parseInt(columns.get("downtimeStart"))));
			
		}
	}





/**
	 * @author Shaswata Bhattacharyya
	 */
	@After
    public void tearDown() {
        flexiBook.delete();
    }



	
	
	
	
	
	
	
	

	/**
	 * @author Shaswata Bhattacharyya
	 * @param dataTable
	 */
	@Given("the business has the following opening hours")
	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
		BusinessHour businessHour = null;
		String Day;

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> columns : rows) {
			Day = columns.get("day");
			switch (Day) {

			case "Monday":
				businessHour = new BusinessHour(DayOfWeek.Monday, Time.valueOf(columns.get("startTime") + ":00"),
						Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			case "Tuesday":
				businessHour = new BusinessHour(DayOfWeek.Tuesday, Time.valueOf(columns.get("startTime") + ":00"),
						Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			case "Wednesday":
				businessHour = new BusinessHour(DayOfWeek.Wednesday, Time.valueOf(columns.get("startTime") + ":00"),
						Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			case "Thursday":
				businessHour = new BusinessHour(DayOfWeek.Thursday, Time.valueOf(columns.get("startTime") + ":00"),
						Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			case "Friday":
				businessHour = new BusinessHour(DayOfWeek.Friday, Time.valueOf(columns.get("startTime") + ":00"),
						Time.valueOf(columns.get("endTime") + ":00"), flexiBook);

			}

			flexiBook.getBusiness().addBusinessHour(businessHour);

		}

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param dataTable
	 */
	@Given("the business has the following holidays")
	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
		TimeSlot holiday;

		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> columns : rows) {
			holiday = new TimeSlot(Date.valueOf(columns.get("startDate")),
					Time.valueOf(columns.get("startTime") + ":00"), Date.valueOf(columns.get("endDate")),
					Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
			flexiBook.getBusiness().addHoliday(holiday);
		}
	}



			
			
		
	


			
			
		
	    
	
	
	
	

//	/**
//	 * @author Shaswata Bhattacharyya
//	 * @param dataTable
//	 */
//	@Given("the following appointments exist in the system:")
//	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		Appointment appointment = null;
//		
//		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class); 
//		for(Map<String, String> columns : rows) {
//
//			Customer customer = findCustomerByName(columns.get("customer"));
//			BookableService bookableService = findServiceByName(columns.get("serviceName"));
//			
//			TimeSlot timeSlot = new TimeSlot(Date.valueOf(columns.get("date")), Time.valueOf(columns.get("startTime") + ":00"), Date.valueOf(columns.get("date")), Time.valueOf(columns.get("endTime") + ":00"), flexiBook);
//			
//			
//			appointment = new Appointment(customer, bookableService, timeSlot, flexiBook);
//			
//			if(bookableService.getClass().equals(ServiceCombo.class)) {		//check if chosen service is a ServiceCombo
//				ServiceCombo combo = (ServiceCombo) bookableService;
//				
//				//set main service
//				int index = columns.get("serviceName").indexOf('-');
//				Service main = (Service)findServiceByName(columns.get("serviceName").substring(0, index));
//				ComboItem newItem = new ComboItem(true, main, combo);
//				appointment.addChosenItem(newItem);
//				
//				//get the optional services
//				String itemList = columns.get("optServices");
//				String[] items = itemList.split(",");
//				for(String item : items) {
//					Service thisService = (Service) findServiceByName(item);
//					newItem = new ComboItem(false, thisService, combo);
//					appointment.addChosenItem(newItem);
//				}
//
//			}
//			
//		}
//	}


	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param date
	 * @param serviceName
	 * @param startTime
	 */
	@When("{string} schedules an appointment on {string} for {string} at {string}")
	public void schedules_an_appointment_on_for_at(String username, String date, String serviceName, String startTime) {

		try {
			FlexiBookController.makeAppointment(username, serviceName, null, startTime, date, flexiBook,
					SystemDateTime);
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr++;
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param date
	 * @param startTime
	 * @param endTime
	 */
	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
	public void shall_have_a_appointment_on_from_to(String username, String serviceName, String date, String startTime,
			String endTime) {

		Appointment appointment = findAppointment(username, Date.valueOf(date), Time.valueOf(startTime + ":00"));

		assertTrue(username.equals(appointment.getCustomer().getUsername()));
		assertTrue(serviceName.equals(appointment.getBookableService().getName()));
		assertTrue(Date.valueOf(date).equals(appointment.getTimeSlot().getStartDate()));
		assertTrue(Time.valueOf(startTime + ":00").equals(appointment.getTimeSlot().getStartTime()));
		assertTrue(Time.valueOf(endTime + ":00").equals(appointment.getTimeSlot().getEndTime()));

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param int1
	 */
	@Then("there shall be {int} more appointment in the system")
	public void there_shall_be_more_appointment_in_the_system(Integer int1) {

		assertEquals(appointmentCntr - prevAppointmentCntr, int1);

	}

	///////// UNCOMMENT
	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param oldDate
	 * @param oldTime
	 * @param newDate
	 * @param newTime
	 */

	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_their_appointment_on_at_to_at(String username, String serviceName, String oldDate, String oldTime, String newDate, String newTime) {
	    
		try {
			updateAppointmentSuccess = FlexiBookController.updateAppointmentTime(username, serviceName, newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), SystemDateTime, flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}
		
	}
	

//	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
//	public void attempts_to_update_their_appointment_on_at_to_at(String username, String serviceName, String oldDate, String oldTime, String newDate, String newTime) {
//	    
//		try {
//			updateAppointmentSuccess = FlexiBookController.updateAppointment(username, serviceName, null, null, newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), SystemDateTime, flexiBook);
//		} catch (InvalidInputException e) {
//			error = e.getMessage();
//			errorCntr++;
//		}
//		
//	}
//	


	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param date
	 * @param time
	 */
	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
	public void attempts_to_cancel_their_appointment_on_at(String username, String serviceName, String date,
			String time) {

		try {

			FlexiBookController.cancelAppointment(username, time + ":00", date, SystemDateTime, flexiBook);

			prevAppointmentCntr = appointmentCntr;
			appointmentCntr--;
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param date
	 * @param time
	 */
	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
	public void s_appointment_on_at_shall_be_removed_from_the_system(String username, String serviceName, String date,
			String time) {

		Appointment appointment = findAppointment(username, Date.valueOf(date), Time.valueOf(time + ":00"));
		assertNull(appointment);

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param int1
	 */
	@Then("there shall be {int} less appointment in the system")
	public void there_shall_be_less_appointment_in_the_system(Integer int1) {

		assertEquals(prevAppointmentCntr - appointmentCntr, int1);

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param date
	 * @param serviceName
	 * @param optServices
	 * @param time
	 */
	@When("{string} schedules an appointment on {string} for {string} with {string} at {string}")
	public void schedules_an_appointment_on_for_with_at(String username, String date, String serviceName,
			String optServices, String time) {

		String[] optionalServices = optServices.split(",");
		List<String> optionalServiceList = Arrays.asList(optionalServices);

		try {
			FlexiBookController.makeAppointment(username, serviceName, optionalServiceList, time, date, flexiBook,
					SystemDateTime);
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr++;
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Then("the system shall report {string}")
	public void the_system_shall_report(String errorMsg) {

		assertTrue(error.equals(errorMsg));

	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param optServices
	 * @param date
	 * @param time
	 */
	@Given("{string} has a {string} appointment with optional sevices {string} on {string} at {string}")
	public void has_a_appointment_with_optional_sevices_on_at(String username, String serviceName, String optServices,
			String date, String time) {

		Appointment appointment = null;
		Customer customer = findCustomerByName(username);
		BookableService bookableService = findServiceByName(serviceName);

		TimeSlot timeSlot = new TimeSlot(Date.valueOf(date), Time.valueOf(time + ":00"), Date.valueOf(date),
				Time.valueOf("11:35:00"), flexiBook);
		appointment = new Appointment(customer, bookableService, timeSlot, flexiBook);

		if (bookableService.getClass().equals(ServiceCombo.class)) { // check if chosen service is a ServiceCombo
			ServiceCombo combo = (ServiceCombo) bookableService;

			// set main service
			Service main = (Service) findServiceByName("color");
			ComboItem newItem = new ComboItem(true, main, combo);
			appointment.addChosenItem(newItem);

			// get the optional services
			Service thisService = (Service) findServiceByName("wash");
			newItem = new ComboItem(false, thisService, combo);
			appointment.addChosenItem(newItem);

			thisService = (Service) findServiceByName("dry");
			newItem = new ComboItem(true, thisService, combo);
			appointment.addChosenItem(newItem);

		}

	}

	//// UNCOMMENT
	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param action
	 * @param optService
	 * @param mainService
	 * @param date
	 * @param time
	 */

	@When("{string} attempts to {string} {string} from their {string} appointment on {string} at {string}")
	public void attempts_to_from_their_appointment_on_at(String username, String action, String optService, String mainService, String date, String time) {
		String[] optionalServices = optService.split(",");	
		List<String> optionalServiceList = Arrays.asList(optionalServices);
	    
	    try {
	    	if(action.equals("add")) {
	    		updateAppointmentSuccess = FlexiBookController.updateAppointmentServices(username, mainService, optionalServiceList, null, Time.valueOf(time + ":00"), Date.valueOf(date), SystemDateTime, flexiBook);
		    }
		    else if(action.equals("remove")) {
		    	updateAppointmentSuccess = FlexiBookController.updateAppointmentServices(username, mainService, null, optionalServiceList, Time.valueOf(time + ":00"), Date.valueOf(date), SystemDateTime, flexiBook);
		    }
			
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}
	    
	}

//	@When("{string} attempts to {string} {string} from their {string} appointment on {string} at {string}")
//	public void attempts_to_from_their_appointment_on_at(String username, String action, String optService, String mainService, String date, String time) {
//		String[] optionalServices = optService.split(",");	
//		List<String> optionalServiceList = Arrays.asList(optionalServices);
//	    
//	    try {
//	    	if(action.equals("add")) {
//	    		updateAppointmentSuccess = FlexiBookController.updateAppointment(username, mainService, optionalServiceList, null, null, null, Time.valueOf(time + ":00"), Date.valueOf(date), SystemDateTime, flexiBook);
//		    }
//		    else if(action.equals("remove")) {
//		    	updateAppointmentSuccess = FlexiBookController.updateAppointment(username, mainService, null, optionalServiceList, null, null, Time.valueOf(time + ":00"), Date.valueOf(date), SystemDateTime, flexiBook);
//		    }
//			
//		} catch (InvalidInputException e) {
//			error = e.getMessage();
//			errorCntr++;
//		}
//	    
//	}


	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Then("the system shall report that the update was {string}")
	public void the_system_shall_report_that_the_update_was(String string) {

		assertTrue(string.equals(updateAppointmentSuccess));

	}

	////// UNCOMMENT
	/**
	 * @author Shaswata Bhattacharyya
	 * @param username1
	 * @param username2
	 * @param serviceName
	 * @param oldDate
	 * @param oldTime
	 * @param newDate
	 * @param newTime
	 */

	@When("{string} attempts to update {string}'s {string} appointment on {string} at {string} to {string} at {string}")
	public void attempts_to_update_s_appointment_on_at_to_at(String username1, String username2, String serviceName, String oldDate, String oldTime, String newDate, String newTime) {
	    
	
		try {
			updateAppointmentSuccess = FlexiBookController.updateAppointmentTime(username1, serviceName,  newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), SystemDateTime, flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}
		
	}

//	@When("{string} attempts to update {string}'s {string} appointment on {string} at {string} to {string} at {string}")
//	public void attempts_to_update_s_appointment_on_at_to_at(String username1, String username2, String serviceName, String oldDate, String oldTime, String newDate, String newTime) {
//	    
//	
//		try {
//			updateAppointmentSuccess = FlexiBookController.updateAppointment(username1, serviceName, null, null, newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), SystemDateTime, flexiBook);
//		} catch (InvalidInputException e) {
//			error = e.getMessage();
//			errorCntr++;
//		}
//		
//	}


	/**
	 * @author Shaswata Bhattacharyya
	 * @param username1
	 * @param username2
	 * @param serviceName
	 * @param date
	 * @param time
	 */
	@When("{string} attempts to cancel {string}'s {string} appointment on {string} at {string}")
	public void attempts_to_cancel_s_appointment_on_at(String username1, String username2, String serviceName, String date, String time) {

		try {

			FlexiBookController.cancelAppointment(username1, time + ":00", date, SystemDateTime, flexiBook);

			prevAppointmentCntr = appointmentCntr;
			appointmentCntr--;
		} catch (InvalidInputException e) {
			error = e.getMessage();
			errorCntr++;
		}

	}

	// private methods

	/**
	 * @author Shaswata Bhattacharyya
	 * @param serviceName
	 * @return
	 */
	private BookableService findServiceByName(String serviceName) {
		BookableService thisService = null;

		List<BookableService> serviceList = flexiBook.getBookableServices();
		for (int i = 0; i < serviceList.size(); i++) {
			thisService = serviceList.get(i);
			if (thisService.getName().equals(serviceName)) {
				return thisService;
			}
		}

		return thisService;
	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @return
	 */
	private Customer findCustomerByName(String username) {
		Customer thisCustomer = null;

		List<Customer> customerList = flexiBook.getCustomers();
		for (int i = 0; i < customerList.size(); i++) {
			thisCustomer = customerList.get(i);
			if (thisCustomer.getUsername().equals(username)) {
				return thisCustomer;
			}
		}

		return thisCustomer;
	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param date
	 * @param startTime
	 * @return
	 */
	private Appointment findAppointment(String username, Date date, Time startTime) {
		Appointment appointment = null;

		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			String Username = thisAppointment.getCustomer().getUsername();

			
			if(date.equals(thisAppointment.getTimeSlot().getStartDate()) && startTime.equals(thisAppointment.getTimeSlot().getStartTime()) && Username.equals(username)) {

			String d = thisAppointment.getTimeSlot().getStartDate().toString();
			String t = thisAppointment.getTimeSlot().getStartTime().toString();

			if (date.equals(thisAppointment.getTimeSlot().getStartDate())
					&& startTime.equals(thisAppointment.getTimeSlot().getStartTime()) && Username.equals(username)) {

				appointment = thisAppointment;
				break;
			}
		}
		}
		

		return appointment;

		
			
	
	
	}
	
	


	
	
		

	
////////////////////////////APPOINTMENT MANAGEMENT/////////////
			
			
			
			
			
			
			@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
			public void a_time_slot_exists_with_start_time_at_and_end_time_at(String string, String string2, String string3, String string4, String string5) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Given("{string} has {int} no-show records")
			public void has_no_show_records(String string, Integer int1) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@When("{string} makes a {string} appointment for the date {string} and time {string} at {string}")
			public void makes_a_appointment_for_the_date_and_time_at(String string, String string2, String string3, String string4, String string5) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@When("{string} attempts to change the service in the appointment to {string} at {string}")
			public void attempts_to_change_the_service_in_the_appointment_to_at(String string, String string2, String string3) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the appointment shall be booked")
			public void the_appointment_shall_be_booked() {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the service in the appointment shall be {string}")
			public void the_service_in_the_appointment_shall_be(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
			public void the_appointment_shall_be_for_the_date_with_start_time_and_end_time(String string, String string2, String string3) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the username associated with the appointment shall be {string}")
			public void the_username_associated_with_the_appointment_shall_be(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the user {string} shall have {int} no-show records")
			public void the_user_shall_have_no_show_records(String string, Integer int1) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the system shall have {int} appointments")
			public void the_system_shall_have_appointments(Integer int1) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
	
			
			@When("the owner starts the appointment at {string}")
			public void the_owner_starts_the_appointment_at(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			@When("{string} attempts to cancel the appointment at {string}")
			public void attempts_to_cancel_the_appointment_at(String string, String string2) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			@Then("the appointment shall be in progress")
			public void the_appointment_shall_be_in_progress() {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}

	
			@When("{string} makes a {string} appointment without choosing optional services for the date {string} and time {string} at {string}")
			public void makes_a_appointment_without_choosing_optional_services_for_the_date_and_time_at(String string, String string2, String string3, String string4, String string5) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@When("{string} attempts to add the optional service {string} to the service combo in the appointment at {string}")
			public void attempts_to_add_the_optional_service_to_the_service_combo_in_the_appointment_at(String string, String string2, String string3) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@Then("the service combo in the appointment shall be {string}")
			public void the_service_combo_in_the_appointment_shall_be(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			@Then("the service combo shall have {string} selected services")
			public void the_service_combo_shall_have_selected_services(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@When("{string} attempts to update the date to {string} and time to {string} at {string}")
			public void attempts_to_update_the_date_to_and_time_to_at(String string, String string2, String string3, String string4) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			@When("the owner attempts to end the appointment at {string}")
			public void the_owner_attempts_to_end_the_appointment_at(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			
			
			@When("the owner attempts to register a no-show for the appointment at {string}")
			public void the_owner_attempts_to_register_a_no_show_for_the_appointment_at(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}
			

			@Then("the system shall have {int} appointment")
			public void the_system_shall_have_appointment(Integer int1) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}

			@When("the owner ends the appointment at {string}")
			public void the_owner_ends_the_appointment_at(String string) {
			    // Write code here that turns the phrase above into concrete actions
			    throw new io.cucumber.java.PendingException();
			}	


	}			

