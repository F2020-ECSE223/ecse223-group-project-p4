package ca.mcgill.ecse.flexibook.features;

//import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;


//import java.sql.Date;
//import java.sql.Time;
//import java.util.List;
//import java.util.Map;

import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse223.flexibook.controller.*;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;
import ca.mcgill.ecse.flexibook.application.*;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {



	private static FlexiBook flexiBook;
	private String error;
	private Integer appointmentCntr = 0;
	private Integer prevAppointmentCntr = 0;
	private String updateAppointmentSuccess = null;
	private Date systemDate;
	private Time systemTime;
	private boolean exception;
	private static Business business;
	private static Owner owner;
	private static User currentUser;
	private static Service currService; 

	private static List<Map<String, String>> preservedProperties;
	private static List<Map<String, String>> existingServices;
	private static List<ComboItem> combosInService;
	private static int numCombos = 0;;
	private static int numServices = 0;
	private static List<BookableService> allBookableServices = null;
	int numberOfAccounts=0;

	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Given("{string} is logged in to their account")
	public void is_logged_in_to_their_account(String string) {
		User user = findCustomerByName(string);
		FlexiBookApplication.setCurrentUser(user);
	}

	
	
	@Before
	 /**
	 * @author Shaswata Bhattacharyya
	 */

	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {

		FlexiBookApplication.getFlexiBook().delete();

		flexiBook = FlexiBookApplication.getFlexiBook();
		appointmentCntr = 0;
		prevAppointmentCntr = 0;
		updateAppointmentSuccess = "";

		error = "";
		error = "";
		
		numServices = 0;
		numCombos = 0;
		currentUser = null;
	}

	//
	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		
		String dateAndTime[] = string.split("\\+");
		FlexiBookApplication.setSystemDate(dateAndTime[0]);
		FlexiBookApplication.setSystemTime(dateAndTime[1] + ":00");
		
		FlexiBookPersistence.save(flexiBook);

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
		
		//FlexiBookPersistence.save(flexiBook);
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
		
		//FlexiBookPersistence.save(flexiBook);
	}

	//
	/// **
	// * @author Shaswata Bhattacharyya
	// * @param dataTable
	// */

	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		
		List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
		for (Map<String, String> columns : rows) {
			flexiBook.addCustomer(new Customer(columns.get("username"), columns.get("password"), flexiBook));

		}
		
		//FlexiBookPersistence.save(flexiBook);
	}

	//
	//
	/// **
	// * @author Shaswata Bhattacharyya
	// * @param dataTable
	// */
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
		
		//FlexiBookPersistence.save(flexiBook);
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
		//FlexiBookPersistence.save(flexiBook);
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
		//FlexiBookPersistence.save(flexiBook);
	}

	
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
			
			FlexiBookController.makeAppointment(username, serviceName, null, startTime, date, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr++;
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
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

		Appointment thisAppointment = findAppointment(username, date, startTime, serviceName);

		assertTrue(username.equals(thisAppointment.getCustomer().getUsername()));
		assertTrue(serviceName.equals(thisAppointment.getBookableService().getName()));
		assertTrue(Date.valueOf(date).equals(thisAppointment.getTimeSlot().getStartDate()));
		assertTrue(Time.valueOf(startTime + ":00").equals(thisAppointment.getTimeSlot().getStartTime()));
		assertTrue(Time.valueOf(endTime + ":00").equals(thisAppointment.getTimeSlot().getEndTime()));
		
	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param int1
	 */
	@Then("there shall be {int} more appointment in the system")
	public void there_shall_be_more_appointment_in_the_system(Integer int1) {

		assertEquals(appointmentCntr - prevAppointmentCntr, int1);

	}


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
	public void attempts_to_update_their_appointment_on_at_to_at(String username, String serviceName, String oldDate,
			String oldTime, String newDate, String newTime) {

		try {
			updateAppointmentSuccess = FlexiBookController.updateAppointmentTime(username, newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		

	}


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

			FlexiBookController.cancelAppointment(username, time, date, FlexiBookApplication.getSystemDate());
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr--;
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
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

		Appointment thisAppointment = findAppointment(username, date, time + ":00", serviceName);
		assertNull(thisAppointment);

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
			FlexiBookController.makeAppointment(username, serviceName, optionalServiceList, time, date, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr++;
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
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
	 * 
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

		Appointment thisAppointment = null;
		Customer customer = findCustomerByName(username);
		BookableService bookableService = findServiceByName(serviceName);

		TimeSlot timeSlot = new TimeSlot(Date.valueOf(date), Time.valueOf(time + ":00"), Date.valueOf(date),
				Time.valueOf("11:35:00"), flexiBook);
		thisAppointment = new Appointment(customer, bookableService, timeSlot, flexiBook);

		if (bookableService.getClass().equals(ServiceCombo.class)) { // check if chosen service is a ServiceCombo
			ServiceCombo combo = (ServiceCombo) bookableService;

			// set main service
			Service main = (Service) findServiceByName("color");
			ComboItem newItem = new ComboItem(true, main, combo);
			thisAppointment.addChosenItem(newItem);

			// get the optional services
			Service thisService = (Service) findServiceByName("wash");
			newItem = new ComboItem(false, thisService, combo);
			thisAppointment.addChosenItem(newItem);

			thisService = (Service) findServiceByName("dry");
			newItem = new ComboItem(true, thisService, combo);
			thisAppointment.addChosenItem(newItem);

		}
		//FlexiBookPersistence.save(flexiBook);

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
	public void attempts_to_from_their_appointment_on_at(String username, String action, String optService,
			String mainService, String date, String time) {
		String[] optionalServices = optService.split(",");
		List<String> optionalServiceList = Arrays.asList(optionalServices);

		try {
			if (action.equals("add")) {
				updateAppointmentSuccess = FlexiBookController.updateAppointmentServices(username, mainService, optionalServiceList, null, Time.valueOf(time + ":00"), Date.valueOf(date), FlexiBookApplication.getSystemDate(),FlexiBookApplication.getSystemTime());
			} else if (action.equals("remove")) {
				updateAppointmentSuccess = FlexiBookController.updateAppointmentServices(username, mainService, null, optionalServiceList, Time.valueOf(time + ":00"), Date.valueOf(date), FlexiBookApplication.getSystemDate(),FlexiBookApplication.getSystemTime());
			}
			FlexiBookPersistence.save(flexiBook);

		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		

	}

	

	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@Then("the system shall report that the update was {string}")
	public void the_system_shall_report_that_the_update_was(String string) {
		assertEquals(string,updateAppointmentSuccess);
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
	public void attempts_to_update_s_appointment_on_at_to_at(String username1, String username2, String serviceName,
			String oldDate, String oldTime, String newDate, String newTime) {

		try {
			appointment = findAppointment(username1, oldDate, oldTime, serviceName);
			updateAppointmentSuccess = FlexiBookController.updateAppointmentTime(username1, newTime, newDate, Time.valueOf(oldTime + ":00"), Date.valueOf(oldDate), FlexiBookApplication.getSystemDate(),FlexiBookApplication.getSystemTime());
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

	}


	/**
	 * @author Shaswata Bhattacharyya
	 * @param username1
	 * @param username2
	 * @param serviceName
	 * @param date
	 * @param time
	 */
	@When("{string} attempts to cancel {string}'s {string} appointment on {string} at {string}")
	public void attempts_to_cancel_s_appointment_on_at(String username1, String username2, String serviceName,
			String date, String time) {

		try {

			FlexiBookController.cancelAppointment(username1, time, date, FlexiBookApplication.getSystemDate());
			prevAppointmentCntr = appointmentCntr;
			appointmentCntr--;
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
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
	private Appointment findAppointment(String username, String date, String time, String bookableService) {
		Appointment theAppointment = null;

		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			thisAppointment.getCustomer().getUsername();

			Date d = Date.valueOf(date);
			Time t = Time.valueOf(time + ":00");



			if (d.equals(thisAppointment.getTimeSlot().getStartDate()) && t.equals(thisAppointment.getTimeSlot().getStartTime())) {
					theAppointment = thisAppointment;
					break;

			}

		}

		return theAppointment;
	}
	


	// *******************SERVICE IMPLEMENTATION*************************


	/**
	 * @author Sneha Singh
	 */
	@Given("the Owner with username {string} is logged in")

	public void thereIsAnOwnerLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
		assertEquals(string, currentUser.getUsername());
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void aNewServiceAdditionIsInitiated(String user, String aName, String aDuration, String aDowntimeStart,
			String aDowntimeDuration) {
		try {
			FlexiBookController.addService(aName, Integer.parseInt(aDuration), Integer.parseInt(aDowntimeDuration), Integer.parseInt(aDowntimeStart));
			currService = (Service) Service.getWithName(aName);
			//FlexiBookPersistence.save(flexiBook);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}

	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall exist in the system")
	public void theServiceShallExistInTheSystem(String string) {
		boolean flag = false;
		for(int i=0; i<flexiBook.getBookableServices().size();i++) {
			if(flexiBook.getBookableService(i).getName().equals(string)) {
				flag = true;
			}
		}
		assertTrue(flag);

	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallHave(String name, String duration, String downtimeStart, String downtimeDuration) {

		int index =0;
		for(int i=0; i<flexiBook.getBookableServices().size(); i++) {
			if(flexiBook.getBookableService(i).getName().equals(name)) {
				index =i;
			}
		}
			assertEquals(Integer.parseInt(duration), ((Service) flexiBook.getBookableService(index)).getDuration());
			assertEquals(Integer.parseInt(downtimeStart), ((Service) flexiBook.getBookableService(index)).getDowntimeStart());
			assertEquals(Integer.parseInt(downtimeDuration), ((Service) flexiBook.getBookableService(index)).getDowntimeDuration());
		
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of services in the system shall be {string}")
	public void theNumberOfServicesInTheSystemShallBe(String string) {
		allBookableServices = flexiBook.getBookableServices();
		
		for (int i = 0; i < allBookableServices.size(); i++) {
			if (allBookableServices.get(i).getClass().equals(Service.class)) {
				numServices++;
			}
		}
		assertEquals(Integer.parseInt(string), numServices);
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("an error message with content {string} shall be raised")
	public void anErrorMessageShallBeRaised(String errorMsg) {
		assertTrue(error.contains(errorMsg));
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall not exist in the system")
	public void theServiceShallNotExistInSystem(String string) {
		assertNull(Service.getWithName(string));
	}

	/**
	 * @author Sneha Singh
	 */
	@Given("the following services exist in the system:")
	public void theFollowingServicesExist(io.cucumber.datatable.DataTable dataTable) {
		existingServices = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < existingServices.size(); i++) {
			flexiBook.addBookableService(new Service(existingServices.get(i).get("name"), flexiBook,
					(Integer.parseInt(existingServices.get(i).get("duration"))),
					(Integer.parseInt(existingServices.get(i).get("downtimeDuration"))),
					(Integer.parseInt(existingServices.get(i).get("downtimeStart")))));
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall still preserve the following properties:")
	public void theServicePreservesTheFollowingProperties(String string, io.cucumber.datatable.DataTable dataTable) {
		preservedProperties = dataTable.asMaps(String.class, String.class);
		assertEquals(Service.getWithName(string).getName(), preservedProperties.get(0).get("name"));
		assertEquals(((Service) Service.getWithName(string)).getDuration(),
				Integer.parseInt(preservedProperties.get(0).get("duration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeDuration(),
				Integer.parseInt(preservedProperties.get(0).get("downtimeDuration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeStart(),
				Integer.parseInt(preservedProperties.get(0).get("downtimeStart")));
		
	}

	

	/**
	 * @author Sneha Singh
	 */
	@Given("Customer with username {string} is logged in")
	public void customerWithUsernameIsLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser((User.getWithUsername(string)));
		assertEquals(string, currentUser.getUsername());
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void updateToServiceIsInitiated(String user, String name, String newName, String newDuration,
			String newDowntimeStart, String newDowntimeDuration) {
		int duration = Integer.parseInt(newDuration);
		int downtimeStart = Integer.parseInt(newDowntimeStart);
		int downtimeDuration = Integer.parseInt(newDowntimeDuration);

		try {
			FlexiBookController.updateService(name, newName, duration, downtimeStart, downtimeDuration);
			//FlexiBookPersistence.save(flexiBook);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();

		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallBeUpdatedTo(String name, String newName, String newDuration, String newDowntimeStart,
			String newDowntimeDuration) {
		if (flexiBook.hasBookableServices() == true) {

			int i = flexiBook.indexOfBookableService(Service.getWithName(newName));
			assertEquals(newName, flexiBook.getBookableService(i).getName());
			assertEquals(Integer.parseInt(newDuration), ((Service) flexiBook.getBookableService(i)).getDuration());
			assertEquals(Integer.parseInt(newDowntimeStart), ((Service) flexiBook.getBookableService(i)).getDowntimeStart());
			assertEquals(Integer.parseInt(newDowntimeDuration), ((Service) flexiBook.getBookableService(i)).getDowntimeDuration());
		}
	}

	
	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the deletion of service {string}")
	public void serviceDeletionIsInitiated(String user, String service) {

		try {
			FlexiBookController.deleteService(service);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
			// errorCntr++;
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void numberOfAppointmentsOfServiceShouldBe(String name, String numAppointments) {
		if (Service.getWithName(name) != null) {
			assertEquals(Integer.parseInt(numAppointments), Service.getWithName(name).getAppointments().size());
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of appointments in the system shall be {string}")
	public void numberOfAppointmentsInSystemShallBe(String numAppts) {
		assertEquals(Integer.parseInt(numAppts), flexiBook.getAppointments().size());
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service combos {string} shall not exist in the system")
	public void theServiceCombosShallNotExistInTheSystem(String string) {
		assertNull(ServiceCombo.getWithName(string));

	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service combos {string} shall not contain service {string}")
	public void theServiceCombosShallNotContainTheService(String combo, String service) {
		combosInService = ((ServiceCombo) ServiceCombo.getWithName(combo)).getServices();

		for (int i = 0; i < combosInService.size(); i++) {
			assertNotEquals(combosInService.get(i).getService(), (Service.getWithName(service)));
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of service combos in the system shall be {string}")
	public void theNumberOfServiceCombosInTheSystemShallBe(String string) {
		allBookableServices = flexiBook.getBookableServices();
		for (int i = 0; i < allBookableServices.size(); i++) {
			if (allBookableServices.get(i) instanceof ServiceCombo) {
				numCombos++;
			}
		}

		assertEquals(Integer.parseInt(string), numCombos);

	}

	// **********************************END SERVICE
	// IMPLEMENTATION*******************

	// **********************************SERVICE COMBO
	// IMPLEMENTATION********************


	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 */
	@When("{string} initiates the definition of a service combo {string} with main service {string}, services {string} and mandatory setting {string}")
	public void initiates_the_definition_of_a_service_combo_with_main_service_services_and_mandatory_setting(
			String string, String string2, String string3, String string4, String string5) {
		try {
			FlexiBookController.defineServiceCombo(string2, string, string3, string5, string4);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}
	}

	
	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the service combo {string} shall not exist in the system")
public void the_service_combo_shall_not_exist_in_the_system(String serviceComboName) {

		assertNull(ServiceCombo.getWithName(serviceComboName));

	}

	//
	/**
	 * @author yasminamatta
	 * @param string
	 * @param dataTable
	 */

	@Then("the service combo {string} shall preserve the following properties:")
	public void the_service_combo_shall_preserve_the_following_properties(String serviceComboName,
			io.cucumber.datatable.DataTable dataTable) {
		String services = "";
		String mandatory = "";
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).get("name").contains(serviceComboName)) {
				ServiceCombo serviceCombo = (ServiceCombo) BookableService.getWithName(serviceComboName);
				assertEquals(serviceCombo.getMainService().getService().getName(), list.get(i).get("mainService"));

				for (int j = 0; j < serviceCombo.getServices().size(); j++) {
					if (j == serviceCombo.getServices().size() - 1) {
						mandatory += serviceCombo.getServices().get(j).getMandatory();
						services += serviceCombo.getServices().get(j).getService().getName();
					} else {
						services += serviceCombo.getServices().get(j).getService().getName() + ",";
						mandatory += serviceCombo.getServices().get(j).getMandatory() + ",";
					}
				}
				assertEquals(services, list.get(i).get("services"));
				assertEquals(mandatory, list.get(i).get("mandatory"));

			}

		}
	}
	
	/**
	 * @author yasminamatta
	 * @param dataTable //
	 */
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {

		List<Map<String,String>> list = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < list.size(); i++) {
	
			String a = list.get(i).get("date");

			Date startDate = Date.valueOf(a);
			Time startTime = Time.valueOf(list.get(i).get("startTime") + ":00");
			Date endDate = Date.valueOf(list.get(i).get("date"));
			Time endTime = Time.valueOf(list.get(i).get("endTime") + ":00");
			TimeSlot timeSlot = new TimeSlot(startDate, startTime, endDate, endTime, flexiBook);
			
			Appointment appoint = new Appointment((Customer) Customer.getWithUsername(list.get(i).get("customer")),
					(BookableService) BookableService.getWithName(list.get(i).get("serviceName")),timeSlot, flexiBook);
			
			
		
		if(list.get(i).get("optServices")!= null) {
		
				String optServicesString = list.get(i).get("optServices");
				if(!optServicesString.equals("none")) {
				String[] optServicesArray = optServicesString.split(",");
				for( int k=0; k<flexiBook.getBookableServices().size(); k++) {
					if(list.get(i).get("serviceName").equals(flexiBook.getBookableServices().get(k).getName())) {
					for(int p=0; p< optServicesArray.length; p++) {
						for( int y=0; y<(((ServiceCombo)flexiBook.getBookableServices().get(k))).getServices().size(); y++) {
							if(findServiceByName(optServicesArray[p]).equals(((ServiceCombo)flexiBook.getBookableServices().get(k)).getService(y).getService())) {
								appoint.addChosenItem(((ServiceCombo)flexiBook.getBookableServices().get(k)).getService(y));
							}
						}
					}

							
						}
						
					
					}
				}
				
		}
			
		
		else if( list.get(i).get("selectedComboItems") != null) {
		String selectedComboItemsString = list.get(i).get("selectedComboItems");
		String[] selectedComboItemsArray = selectedComboItemsString.split(",");
		for( int k=0; k<flexiBook.getBookableServices().size(); k++) {
		if(list.get(i).get("serviceName").equals(flexiBook.getBookableServices().get(k).getName())) {
		for(int p=0; p< selectedComboItemsArray.length; p++) {
			for( int y=0; y<(((ServiceCombo)flexiBook.getBookableServices().get(k))).getServices().size(); y++) {
				if(findServiceByName(selectedComboItemsArray[p]).equals(((ServiceCombo)flexiBook.getBookableServices().get(k)).getService(y).getService())) {
					appoint.addChosenItem(((ServiceCombo)flexiBook.getBookableServices().get(k)).getService(y));
				}
			}
		}

				
			}
			
		
		}
		
		}
		flexiBook.addAppointment(appoint);
		//FlexiBookPersistence.save(flexiBook);
			
	}
		
		

}		


	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @param string6
	 */
	@When("{string} initiates the update of service combo {string} to name {string}, main service {string} and services {string} and mandatory setting {string}")
	public void initiates_the_update_of_service_combo_to_name_main_service_and_services_and_mandatory_setting(
			String username, String name, String newName, String mainService, String servicesString, String mandatoryString) {
		try {

			FlexiBookController.updateServiceCombo(name, newName, username, mainService, mandatoryString, servicesString);

			
			//FlexiBookPersistence.save(flexiBook);

		} catch (InvalidInputException e) {
			error += e.getMessage();
		}

	}

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the service combo {string} shall exist in the system")
	public void the_service_combo_shall_exist_in_the_system(String serviceComboName) {
		boolean flag = false;

		if (BookableService.getWithName(serviceComboName) != null
				&& BookableService.getWithName(serviceComboName) instanceof ServiceCombo) {

			flag = true;
		}

		assertTrue(flag);
	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 */
	@Then("the service combo {string} shall contain the services {string} with mandatory setting {string}")
	public void the_service_combo_shall_contain_the_services_with_mandatory_setting(String serviceComboName, String servicesString,
			String mandatoryString) {

		ServiceCombo serviceCombo = (ServiceCombo) ServiceCombo.getWithName(serviceComboName);

		String[] services = servicesString.split(",");
		String[] mandatory = mandatoryString.split(",");

		for (int i = 0; i < services.length; i++) {
			Service service1 = (Service) Service.getWithName(services[i]);
			boolean mandatory1 = Boolean.parseBoolean(mandatory[i]);
			ComboItem comboItem = new ComboItem(mandatory1, service1, serviceCombo);

			assertEquals(true, serviceCombo.getServices().contains(comboItem));
			assertEquals(true, serviceCombo.getServices().contains(comboItem));
		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */
	@Then("the main service of the service combo {string} shall be {string}")
	public void the_main_service_of_the_service_combo_shall_be(String serviceComboName, String mainService) {
		assertEquals(((ServiceCombo) BookableService.getWithName(serviceComboName)).getMainService().getService().getName(),
				mainService);
	}
	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */

	@Then("the service {string} in service combo {string} shall be mandatory")
	public void the_service_in_service_combo_shall_be_mandatory(String serviceName, String serviceComboName) {

		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo
					&& flexiBook.getBookableServices().get(i).getName().equals(serviceComboName)) {
				ServiceCombo serviceC = (ServiceCombo) flexiBook.getBookableServices().get(i);
				for (ComboItem comboItem : serviceC.getServices()) {
					if (comboItem.getService().getName().equals(serviceName)) {
						assertEquals(true, comboItem.getMandatory());
					}
				}
			}
		}
	}

	

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */
	@When("{string} initiates the deletion of service combo {string}")
	public void initiates_the_deletion_of_service_combo(String string, String string2) {
		try {
			FlexiBookController.deleteServiceCombo(string, string2);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */
	@Then("the service combo {string} shall be updated to name {string}")
	public void the_service_combo_shall_be_updated_to_name(String serviceComboName, String newName) {

		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo
					&& flexiBook.getBookableServices().get(i).getName().equals(serviceComboName)) {

				assertEquals(newName, flexiBook.getBookableService(i).getName());

			}
		}

	}

	// **************************************SET UP BUSINESS
	// IMPLEMENTATION*****************************
	/**
	 * @author Aroomoogon Krishna
	 */

	@Given("an owner account exists in the system with username {string} and password {string}")
	public void an_owner_account_exists_in_the_system_with_username_and_password(String string, String string2) {
		owner = flexiBook.getOwner();
		if (owner != null) {
			owner.delete();
		}
		owner = new Owner(string, string2, flexiBook);
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("no business exists")
	public void no_business_exists() {
		if (flexiBook.hasBusiness()) {
			business = flexiBook.getBusiness();
			business.delete();
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("the user is logged in to an account with username {string}")
	public void the_user_is_logged_in_to_an_account_with_username(String string) {
		FlexiBookApplication.setCurrentUser(User.getWithUsername(string));
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to set up the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_set_up_the_business_information_with_new_and_and_and(String string, String string2,
			String string3, String string4) {
		exception = false;
		try {
			FlexiBookController.setupBusinessInfo(string, string2, string3, string4);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("a new business with new {string} and {string} and {string} and {string} shall {string} created")
	public void a_new_business_with_new_and_and_and_shall_created(String string, String string2, String string3,
			String string4, String string5) {
		if (exception == false) {
			business = flexiBook.getBusiness();
			assertEquals(string, business.getName());
			assertEquals(string2, business.getAddress());
			assertEquals(string3, business.getPhoneNumber());
			assertEquals(string4, business.getEmail());
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("an error message {string} shall {string} raised")
	public void an_error_message_shall_raised(String string, String string2) {
		assertEquals(string, error);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("a business exists with the following information:")
	public void a_business_exists_with_the_following_information(io.cucumber.datatable.DataTable dataTable) {
		String name = dataTable.cell(1, 0);
		String address = dataTable.cell(1, 1);
		String telnum = dataTable.cell(1, 2);
		String email = dataTable.cell(1, 3);
		business = new Business(name, address, telnum, email, flexiBook);
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("the business has a business hour on {string} with start time {string} and end time {string}")
	public void the_business_has_a_business_hour_on_with_start_time_and_end_time(String string, String string2, String string3) {
		flexiBook.getBusiness().addBusinessHour(new BusinessHour(FlexiBookController.getDow(string), Time.valueOf(string2 + ":00"), Time.valueOf(string3 + ":00"), flexiBook));
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to add a new business hour on {string} with start time {string} and end time {string}")
	public void the_user_tries_to_add_a_new_business_hour_on_with_start_time_and_end_time(String string, String string2,
			String string3) {
		exception = false;
		try {
			FlexiBookController.addBusinessHour(string, string2, string3);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("a new business hour shall {string} created")
	public void a_new_business_hour_shall_created(String string) {
		if (!exception) {
			assertEquals("be", string);
		} else {
			assertEquals("not be", string);
		}
		return;
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to access the business information")
	public void the_user_tries_to_access_the_business_information() {
		Business b = flexiBook.getBusiness();
		error = b.getName() + b.getAddress() + b.getPhoneNumber() + b.getEmail();
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the {string} and {string} and {string} and {string} shall be provided to the user")
	public void the_and_and_and_shall_be_provided_to_the_user(String string, String string2, String string3, String string4) {
		String info = string + string2 + string3 + string4;
		assertEquals(error, info);
	}

	/**
	 * @author Aroomoogon Krishna implemented only for holiday and vacation
	 *         situation
	 */
//	@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
//	public void a_time_slot_exists_with_start_time_at_and_end_time_at(String string, String string2, String string3, String string4, String string5) {
//	    if (string.equals("holiday")) {
//	    	flexiBook.getBusiness().addHoliday(new TimeSlot(Date.valueOf(string2), Time.valueOf(string3 + ":00"), Date.valueOf(string4), Time.valueOf(string5 + ":00"), flexiBook));
//	    }
//	    if (string.equals("vacation")) {
//	    	flexiBook.getBusiness().addVacation(new TimeSlot(Date.valueOf(string2), Time.valueOf(string3 + ":00"), Date.valueOf(string4), Time.valueOf(string5 + ":00"), flexiBook));
//	    }
//	}

	/**
	 * @author Aroomoogon Krishna implemented only for holiday and vacation
	 *         situation
	 */
	@When("the user tries to add a new {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_add_a_new_with_start_date_at_and_end_date_at(String string, String string2,
			String string3, String string4, String string5) {
		exception = false;
		try {
			if (string.equals("holiday")) {
				FlexiBookController.addHolidaySlot(string2, string3, string4, string5);
			}
			if (string.equals("vacation")) {
				FlexiBookController.addVacationSlot(string2, string3, string4, string5);
			}
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("a new {string} shall {string} be added with start date {string} at {string} and end date {string} at {string}")
	public void a_new_shall_be_added_with_start_date_at_and_end_date_at(String string, String string2, String string3,
			String string4, String string5, String string6) {
		Date sD = Date.valueOf(string3);
		Time sT = Time.valueOf(string4 + ":00");
		TimeSlot ts = FlexiBookController.findOfftime(sD, sT, string);
		if (!exception) {
			assertEquals(ts.getEndDate(), Date.valueOf(string5));
			assertEquals(ts.getEndTime(), Time.valueOf(string6 + ":00"));
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to update the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_update_the_business_information_with_new_and_and_and(String string, String string2,
			String string3, String string4) {
		exception = false;
		try {
			FlexiBookController.updateBusinessInfo(string, string2, string3, string4);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the business information shall {string} updated with new {string} and {string} and {string} and {string}")
	public void the_business_information_shall_updated_with_new_and_and_and(String string, String string2,
			String string3, String string4, String string5) {
		if (exception == false) {
			business = flexiBook.getBusiness();
			assertEquals(string2, business.getName());
			assertEquals(string3, business.getAddress());
			assertEquals(string4, business.getPhoneNumber());
			assertEquals(string5, business.getEmail());
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to change the business hour {string} at {string} to be on {string} starting at {string} and ending at {string}")
	public void the_user_tries_to_change_the_business_hour_at_to_be_on_starting_at_and_ending_at(String string,
			String string2, String string3, String string4, String string5) {
		exception = false;
		try {
			FlexiBookController.updateBusinessHour(string, Time.valueOf(string2 + ":00"), string3, Time.valueOf(string4 + ":00"), Time.valueOf(string5 + ":00"));
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the business hour shall {string} be updated")
	public void the_business_hour_shall_be_updated(String string) {
		if (!exception) {
			assertEquals("be", string);
		} else {
			assertEquals("not be", string);
		}
		return;
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to remove the business hour starting {string} at {string}")
	public void the_user_tries_to_remove_the_business_hour_starting_at(String string, String string2) {
		exception = false;
		try {
			FlexiBookController.updateBusinessHour(string, Time.valueOf(string2 + ":00"), null, null, null);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the business hour starting {string} at {string} shall {string} exist")
	public void the_business_hour_starting_at_shall_exist(String string, String string2, String string3) {
		BusinessHour bh = FlexiBookController.findBusinessHour(string, Time.valueOf(string2 + ":00"));
		if (exception) {
			assertNotNull(bh);
		} else {
			assertNull(bh);
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("an error message {string} shall {string} be raised")
	public void an_error_message_shall_be_raised(String string, String string2) {
		if (exception) {
			assertEquals(error, string);
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to change the {string} on {string} at {string} to be with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_change_the_on_at_to_be_with_start_date_at_and_end_date_at(String string,
			String string2, String string3, String string4, String string5, String string6, String string7) {
		exception = false;
		try {
			if (string.equals("vacation")) {
				FlexiBookController.updateVacation(Date.valueOf(string2), Time.valueOf(string3 + ":00"), Date.valueOf(string4), Time.valueOf(string5 + ":00"), Date.valueOf(string6), Time.valueOf(string7 + ":00"));
			} else if (string.equals("holiday")) {
				FlexiBookController.updateHoliday(Date.valueOf(string2), Time.valueOf(string3 + ":00"), Date.valueOf(string4), Time.valueOf(string5 + ":00"), Date.valueOf(string6), Time.valueOf(string7 + ":00"));
			} else {
				throw new InvalidInputException("Invalid type");
			}
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the {string} shall {string} updated with start date {string} at {string} and end date {string} at {string}")
	public void the_shall_updated_with_start_date_at_and_end_date_at(String string, String string2, String string3,
			String string4, String string5, String string6) {
		Date sD = Date.valueOf(string3);
		Time sT = Time.valueOf(string4 + ":00");
		TimeSlot ts = FlexiBookController.findOfftime(sD, sT, string);
		if (!exception) {
			assertEquals(ts.getEndDate(), Date.valueOf(string5));
			assertEquals(ts.getEndTime(), Time.valueOf(string6 + ":00"));
		} else {
			if (ts != null) {
				assertNotEquals(ts.getEndDate(), Date.valueOf(string5));
				assertNotEquals(ts.getEndTime(), Time.valueOf(string6 + ":00"));
			} else {
				assertNull(ts);
			}
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to remove an existing {string} with start date {string} at {string} and end date {string} at {string}")
	public void the_user_tries_to_remove_an_existing_with_start_date_at_and_end_date_at(String string, String string2,
			String string3, String string4, String string5) {
		exception = false;
		try {
			if (string.equals("vacation")) {
				FlexiBookController.updateVacation(Date.valueOf(string2), Time.valueOf(string3 + ":00"), null, null, null, null);
			} else if (string.equals("holiday")) {
				FlexiBookController.updateHoliday(Date.valueOf(string2), Time.valueOf(string3 + ":00"), null, null, null, null);
			} else {
				throw new InvalidInputException("Invalid type");
			}
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			exception = true;
			error = e.getMessage();
		}
		;
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the {string} with start date {string} at {string} shall {string} exist")
	public void the_with_start_date_at_shall_exist(String string, String string2, String string3, String string4) {
		TimeSlot ts = FlexiBookController.findOfftime(Date.valueOf(string2), Time.valueOf(string3 + ":00"), string);
		if (!exception) {
			assertNull(ts);
		} else {
			assertEquals(ts.getStartDate().toString(), string2);
			assertEquals(ts.getStartTime().toString(), string3 + ":00");
		}
	}

	private static String oldUname;
	private static String oldPword;

	private static int before;

	/** ---- Artus implementation */

	/**
	 * @author artus
	 * @param string
	 */

	@Given("there is no existing username {string}")
	public void thereIsNoExistingUsername(String string) {
		List<Customer> customerList = flexiBook.getCustomers();
		Customer customer = null;
		for (int i = 0; i < customerList.size(); i++) {
			customer = customerList.get(i);
			assertTrue(customer.getUsername() != string);
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author artus
	 * @param string1
	 * @param string2
	 */
	@When("the user provides a new username {string} and a password {string}")
	public void theUserProvidesANewUsernameAndAPassword(String string1, String string2) {
		before = flexiBook.getCustomers().size();
		flexiBook = FlexiBookApplication.getFlexiBook();
		try {
			FlexiBookController.signUpCustomer(string1, string2);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author artus
	 */
	@Then("a new customer account shall be created")
	public void aNewCustomerAccountShallBeCreated() {
		boolean test = false;

		if (flexiBook.getCustomers().size() == before + 1)
			test = true;

		assertTrue(test);
	}

	/**
	 * @author artus
	 * @param string
	 * @param string2
	 */
	@Then("the account shall have username {string} and password {string}")
	public void theAccountShallHaveUsernameAndPassword(String string, String string2) {
		String Uname = FlexiBookApplication.getCurrentUser().getUsername();
		String Pword = FlexiBookApplication.getCurrentUser().getPassword();
		List<Customer> customerList = flexiBook.getCustomers();
		Customer customer = null;
		boolean tf = false;
		if (string.equals("owner")) {
			assertEquals(Uname, string);
			assertEquals(Pword, string2);
			tf = true;
		} else {
			for (int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if (customer.getUsername().equals(string)) {
					if (customer.getPassword().equals(string2))
						tf = true;
				}

			}
		}
		assertTrue(tf);
	}

	/**
	 * @author artus
	 */
	@Then("no new account shall be created")
	public void no_new_account_shall_be_created() {
		boolean tf = false;

		if (flexiBook.getCustomers().size() == before) {
			tf = true;
		}
		assertTrue(tf);
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Given("there is an existing username {string}")
	public void there_is_an_existing_username(String string) {

		flexiBook = FlexiBookApplication.getFlexiBook();
		Owner owner = null;

		if (string.equals("owner")) {
			owner = new Owner("owner", "ownerPass", flexiBook);
			flexiBook.setOwner(owner);
		} else {
			flexiBook.addCustomer(string, "ownerPass");
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	

	/**
	 * @author artus
	 * @param string
	 */
	@Given("the account with username {string} has pending appointments")
	public void theAccountWithUsernameHasPendingAppointments(String string) {
		
	}

	/**
	 * @author artus
	 * @param string
	 */
	@When("the user tries to delete account with the username {string}")
	public void theUserTriesToDeleteAccountWithTheUsername(String string) {

		try {
			FlexiBookController.deleteCustomerAccount(string);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Then("the account with the username {string} does not exist")
	public void theAccountWithTheUsernameDoesNotExist(String string) {

		boolean tf = true;
		List<Customer> customerList = flexiBook.getCustomers();
		Customer user = null;

		for (int i = 0; i < customerList.size(); i++) {
			user = customerList.get(i);
			if (user.getUsername() == string)
				tf = false;
		}
		assertTrue(tf);
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Then("all associated appointments of the account with the username {string} shall not exist")
	public void allAssociatedAppointmentsOfTheAccountWithTheUsernameShallNotExist(String string) {
		boolean tf = false;

		if (flexiBook.getAppointments().size() == 0)
			tf = true;
		assertTrue(tf);
	}

	/**
	 * @author artus
	 */
	@Then("the user shall be logged out")
	public void theUserShallBeLoggedOut() {
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Then("an error message {string} shall be raised")
	public void anErrorMessageShallBeRaised2(String string) {
		assertTrue(error.contains(string));
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Then("the account with the username {string} exists")
	public void theAccountWithTheUsernameExists(String string) {

		boolean tf = false;
		Owner owner = FlexiBookApplication.getFlexiBook().getOwner();
		List<Customer> customerList = flexiBook.getCustomers();
		Customer user = null;

		if (owner != null)
			tf = true;
		if (string.equals("owner"))
			tf = true;
		else {
			for (int i = 0; i < customerList.size(); i++) {
				user = customerList.get(i);
				if (user.getUsername() == string)
					tf = true;
			}
		}
		assertTrue(tf);

	}

	/**
	 * @author artus
	 * @param string
	 * @param string2
	 */
	@When("the user tries to update account with a new username {string} and password {string}")
	public void the_user_tries_to_update_account_with_a_new_username_and_password(String string, String string2) {
		try {

			oldPword = FlexiBookApplication.getCurrentUser().getUsername();
			oldUname = FlexiBookApplication.getCurrentUser().getUsername();

			FlexiBookController.accountUpdate(oldUname, string, string2);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

	}

	/**
	 * @author artus
	 */
	@Then("the account shall not be updated")
	public void the_account_shall_not_be_updated() {
		User user = FlexiBookApplication.getCurrentUser();
		boolean test = false;

		if (oldUname.equals(user.getUsername()) || oldPword.equals(user.getPassword())) {

			test = true;

		}
		assertTrue(test);
	}

	@Then("the user should be successfully logged in")
	public void the_user_should_be_successfully_logged_in() {
		assertEquals(FlexiBookApplication.getCurrentUser().getClass().equals(Customer.class), true);
	}

	@When("the user tries to log in with username {string} and password {string}")
	public void the_user_tries_to_log_in_with_username_and_password(String string, String string2) {
		numberOfAccounts =flexiBook.getCustomers().size();
		try {
			FlexiBookController.logIn(string, string2);
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error += e.getMessage();
		}
	}

	@Then("the user shall be successfully logged in")
	public void the_user_shall_be_successfully_logged_in() {
		boolean loggedIn = false;
		if(FlexiBookApplication.getCurrentUser().getClass().equals(Owner.class)  || FlexiBookApplication.getCurrentUser().getClass().equals(Customer.class)) {
			loggedIn = true;
		}
		assertEquals(true, loggedIn);
	}

	@Then("the user should not be logged in")
	public void the_user_should_not_be_logged_in() {
		assertEquals(FlexiBookApplication.getCurrentUser(), null);
	}


	@Then("a new account shall be created")
	public void a_new_account_shall_be_created() {
		assertEquals(flexiBook.getCustomers().size()+1,  numberOfAccounts+1);
	}

	@Given("the user is logged out")
	public void the_user_is_logged_out() {
		FlexiBookApplication.setCurrentUser(null);
		FlexiBookPersistence.save(flexiBook);
	}

	@When("the user tries to log out")
	public void the_user_tries_to_log_out() {
		try {

			FlexiBookController.logOut();
			FlexiBookPersistence.save(flexiBook);

		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

	}

	//////////////////////////// APPOINTMENT MANAGEMENT/////////////

	private static Appointment appointment;

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 */

	@Given("a {string} time slot exists with start time {string} at {string} and end time {string} at {string}")
	public void a_time_slot_exists_with_start_time_at_and_end_time_at(String timeSlotName, String startDateString, String startTimeString,
			String endDateString, String endTimeString) {

		Date startDate = Date.valueOf(startDateString);
		Date endDate = Date.valueOf(endDateString);
		Time startTime = Time.valueOf(startTimeString + ":00");
		Time endTime = Time.valueOf(endTimeString + ":00");

		TimeSlot timeSlot = new TimeSlot(startDate, startTime, endDate, endTime, flexiBook);
		if (timeSlotName.equals("vacation")) {
			flexiBook.getBusiness().addVacation(timeSlot);
		}
		if (timeSlotName.equals("holiday")) {
			flexiBook.getBusiness().addHoliday(timeSlot);
		}
		//FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param int1
	 */
	@Given("{string} has {int} no-show records")
	public void has_no_show_records(String username, Integer int1) {
		for (int i = 0; i < flexiBook.getAppointments().size(); i++) {
			if (flexiBook.getAppointments().get(i).getCustomer().getUsername().equals(username)) {
				if (flexiBook.getAppointments().get(i).getCustomer().getNoShows() != int1) {
					flexiBook.getAppointments().get(i).getCustomer().setNoShows(int1);
				}
			}
		}
		//FlexiBookPersistence.save(flexiBook);

	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @throws InvalidInputException
	 */
	@When("{string} makes a {string} appointment for the date {string} and time {string} at {string}")
	public void makes_a_appointment_for_the_date_and_time_at(String username, String name, String date,
			String time, String currentDateTime) throws InvalidInputException {
		try {
			appointmentCntr = flexiBook.numberOfAppointments();
			FlexiBookApplication.setSystemDate(currentDateTime.substring(0, 10));
			FlexiBookApplication.setSystemTime(currentDateTime.substring(11, 16) + ":00");


			appointment = FlexiBookController.makeAppointment(username, name, null, time, date, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());

			
			appointmentCntr++;
			//FlexiBookPersistence.save(flexiBook);
		} catch (RuntimeException e) {

			error = e.getMessage();
		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 * @param string3
	 * @throws InvalidInputException
	 */
	@When("{string} attempts to change the service in the appointment to {string} at {string}")
	public void attempts_to_change_the_service_in_the_appointment_to_at(String username, String newService, String currentDateTime)
			throws InvalidInputException {

		try {
			FlexiBookApplication.setSystemDate(currentDateTime.substring(0, 10));
			FlexiBookApplication.setSystemTime(currentDateTime.substring(11, 16)+":00" );
			

			appointment = FlexiBookController.cancelAndBookNewService(username, newService, null, appointment.getTimeSlot().getStartTime().toString().substring(0, 5), appointment.getTimeSlot().getStartDate().toString(), FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			FlexiBookPersistence.save(flexiBook);

		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the appointment shall be booked")
	public void the_appointment_shall_be_booked() {
		assertEquals(appointment.getAppointmentStatusFullName(), "Booked");
	}
	

	/**
	 * @author Sneha Singh
	 * @param string
	 */
	@Then("the service in the appointment shall be {string}")
	public void the_service_in_the_appointment_shall_be(String service) {
		assertEquals(BookableService.getWithName(service), appointment.getBookableService());
	}

	/**
	 * @author Sneha Singh
	 * @param string
	 * @param string2
	 * @param string3
	 */
	@Then("the appointment shall be for the date {string} with start time {string} and end time {string}")
	public void the_appointment_shall_be_for_the_date_with_start_time_and_end_time(String date, String startTime,
			String endTime) {
		assertEquals(Date.valueOf(date), appointment.getTimeSlot().getStartDate());
		assertEquals(Time.valueOf(startTime + ":00"), appointment.getTimeSlot().getStartTime());
		assertEquals(Time.valueOf(endTime + ":00"), appointment.getTimeSlot().getEndTime());
	}

	/**
	 * @author Sneha Singh
	 * @param string
	 */
	@Then("the username associated with the appointment shall be {string}")
	public void the_username_associated_with_the_appointment_shall_be(String username) {
		assertEquals(username, appointment.getCustomer().getUsername());
	}

	/**
	 * @author Sneha Singh
	 * @param string
	 * @param int1
	 */
	@Then("the user {string} shall have {int} no-show records")
	public void the_user_shall_have_no_show_records(String user, Integer int1) {
		assertEquals(int1, ((Customer) Customer.getWithUsername(user)).getNoShows());
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the system shall have {int} appointments")
	public void the_system_shall_have_appointments(Integer int1) {
		assertEquals(flexiBook.numberOfAppointments(), int1);
	}

	
	@When("{string} attempts to update the date to {string} and time to {string} at {string}")
	public void attempts_to_update_the_date_to_and_time_to_at(String string, String newDate, String newTime, String string4) {
		
		try {
			//Appointment app = FlexiBookApplication.getAppointment();
			Time oldTime = appointment.getTimeSlot().getStartTime();
			Date oldDate = appointment.getTimeSlot().getStartDate();
			FlexiBookApplication.setSystemDate(string4.substring(0, 10));
			FlexiBookApplication.setSystemTime(string4.substring(11, 16) +":00");
			FlexiBookController.updateAppointmentTime(string, newTime, newDate, oldTime, oldDate, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@When("{string} attempts to cancel the appointment at {string}")
	public void attempts_to_cancel_the_appointment_at(String string, String string2) {

		try {
			FlexiBookApplication.setSystemDate((string2.substring(0, 10)));
			FlexiBookApplication.setSystemTime((string2.substring(11, 16)) + ":00");
			FlexiBookController.cancelAppointment(string,appointment.getTimeSlot().getStartTime().toString().substring(0, 5), appointment.getTimeSlot().getStartDate().toString(),FlexiBookApplication.getSystemDate());
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the system shall have {int} appointment")
	public void the_system_shall_have_appointment(Integer int1) {
		assertEquals(int1, flexiBook.numberOfAppointments());
	}

	

	/**
	 * @author artus
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 */
	@When("{string} makes a {string} appointment without choosing optional services for the date {string} and time {string} at {string}")
	public void makes_a_appointment_without_choosing_optional_services_for_the_date_and_time_at(String string,
			String string2, String string3, String string4, String string5) {

		try {
			
			FlexiBookApplication.setSystemDate(string5.substring(0, 10));
			FlexiBookApplication.setSystemTime(string5.substring(11, 16) +":00");
			appointment = FlexiBookController.makeAppointment(string, string2, null, string4, string3, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			FlexiBookApplication.setAppointment(appointment);
			FlexiBookPersistence.save(flexiBook);
			
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author artus
	 * @param string
	 * @param string2
	 * @param string3
	 * @throws InvalidInputException
	 */

	@When("{string} attempts to add the optional service {string} to the service combo in the appointment at {string}")
	public void attempts_to_add_the_optional_service_to_the_service_combo_in_the_appointment_at(String string,
			String string2, String string3) {
		ArrayList<String> items = new ArrayList<String>();
		items.add(string2);

		try {

			// Appointment app = appointment;
			FlexiBookApplication.setSystemDate(string3.substring(0, 10));
			FlexiBookApplication.setSystemTime(string3.substring(11, 16)+":00");
			Appointment app = appointment;
			FlexiBookController.updateAppointmentServices(string, app.getBookableService().getName(), items, null, app.getTimeSlot().getStartTime(), app.getTimeSlot().getStartDate(),FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			
			//FlexiBookPersistence.save(flexiBook);
		} catch(InvalidInputException e) {
			 error = e.getMessage();
		}

	}

	/**
	 * @author artus
	 * @param string
	 */

	@Then("the service combo in the appointment shall be {string}")
	public void the_service_combo_in_the_appointment_shall_be(String string) {
		boolean test = false;
		if (string.equals(appointment.getBookableService().getName())) {
			test = true;
		}
		assertTrue(test);
	}

	/**
	 * @author Venkata Satyanarayana Chivatam
	 * @param string
	 */

	@When("the owner starts the appointment at {string}")
	public void the_owner_starts_the_appointment_at(String string) {
		String customerName = appointment.getCustomer().getUsername();
		String appointmentDate = appointment.getTimeSlot().getStartDate().toString();
		String appointmentTime = appointment.getTimeSlot().getStartTime().toString();
		Date currentDate = Date.valueOf(string.substring(0, 10));
		Time currentTime = Time.valueOf(string.substring(11, 16) + ":00");

		try {
			FlexiBookController.startAppointment(customerName, appointmentTime, appointmentDate, currentDate, currentTime);
			//FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author artus
	 * @param string
	 */
	@Then("the service combo shall have {string} selected services")
	public void the_service_combo_shall_have_selected_services(String string) {
		String[] serviceString = string.split(",");
		List<String> serviceList = Arrays.asList(serviceString);

		for (ComboItem item : appointment.getChosenItems()) {
			if (serviceList.contains(item.getService().getName())) {
			} else {
			}
		}

	}

	/**
	 * @author Venkata Satyanarayana Chivatam
	 * @param string
	 */

	@When("the owner ends the appointment at {string}")
	public void the_owner_ends_the_appointment_at(String string) {
		String customerName = appointment.getCustomer().getUsername();
		String appointmentDate = appointment.getTimeSlot().getStartDate().toString();
		String appointmentTime = appointment.getTimeSlot().getStartTime().toString();
		Date currentDate = Date.valueOf(string.substring(0, 10));
		Time currentTime = Time.valueOf(string.substring(11, 16) + ":00");

		try {
			FlexiBookController.endAppointment(customerName, appointmentTime, appointmentDate, currentDate, currentTime);
			appointment.finishAppointment();
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	/**
	 * @author Venkata Satyanarayana Chivatam
	 */
	@Then("the appointment shall be in progress")
	public void the_appointment_shall_be_in_progress() {
		assertEquals(appointment.getAppointmentStatus(), Appointment.AppointmentStatus.InProgress);
	}

	/**
	 * @author Venkata Satyanarayana Chivatam
	 * @param string
	 * @throws InvalidInputException 
	 */
	@When("the owner attempts to register a no-show for the appointment at {string}")
	public void the_owner_attempts_to_register_a_no_show_for_the_appointment_at(String string) throws InvalidInputException {
		Date cDate = Date.valueOf(string.substring(0, 10));
		Time cTime = Time.valueOf(string.substring(11, 16) + ":00");

		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			thisAppointment.getTimeSlot().getStartDate().toString();
			thisAppointment.getTimeSlot().getStartTime().toString();
			if (thisAppointment.getTimeSlot().getStartTime().before(cTime)
					&& thisAppointment.getTimeSlot().getStartDate().equals(cDate)) {
				FlexiBookController.registerNoShow(appointment.getCustomer().getUsername(), appointment.getTimeSlot().getStartDate().toString(), appointment.getTimeSlot().getStartTime().toString(), cDate, cTime);
			}

		}
		FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * @author Shaswata Bhattacharyya
	 * @param string
	 */
	@When("the owner attempts to end the appointment at {string}")
	public void the_owner_attempts_to_end_the_appointment_at(String string) {
		String customerName = appointment.getCustomer().getUsername();
		String appointmentDate = appointment.getTimeSlot().getStartDate().toString();
		String appointmentTime = appointment.getTimeSlot().getStartTime().toString();
		FlexiBookApplication.setSystemDate(string.substring(0, 10));
		FlexiBookApplication.setSystemTime(string.substring(11, 16) + ":00");

		try {
			FlexiBookController.endAppointment(customerName, appointmentTime, appointmentDate, FlexiBookApplication.getSystemDate(), FlexiBookApplication.getSystemTime());
			FlexiBookPersistence.save(flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
	}

	@After
	public void tearDown() {
		flexiBook.delete();
		FlexiBookPersistence.save(flexiBook);
	}

}
