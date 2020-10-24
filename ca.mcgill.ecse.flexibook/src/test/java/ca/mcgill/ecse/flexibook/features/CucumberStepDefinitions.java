
package ca.mcgill.ecse.flexibook.features;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;
=======
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.controller.*;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.application.*;



import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class CucumberStepDefinitions {


	private static FlexiBook flexiBook; 
	private static Business business; 
	private static Owner owner;
	private static User currentUser;
	private static String error;
	//private int errorCntr;

	private static List<Map<String, String>> preservedProperties;
	private static List<Map<String, String>> customers;
	private static List<Map<String, String>> existingServices;
	private static List<Map<String, String>> appointments;
	private static List<Map<String, String>> serviceCombos;
	private static List<ComboItem> combosInService;
	private static int numCombos = 0;
	private static int numServices = 0; 
	private static List<BookableService> allBookableServices;
	private static Date startDate;
	private static Date endDate;
	private static Time startTime;
	private static Time endTime; 

	@Given ("a Flexibook system exists")
	public void thereIsAFlexiBookSystem() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		error = "";
		//errorCntr = 0; 
		numCombos = 0;
		numServices = 0; 
	}

	//*******************SERVICE IMPLEMENTATION*************************

	/**
	 * @author Sneha Singh
	 */
	@Given ("an owner account exists in the system")
	public void thereIsAnOwner() {
		owner = flexiBook.getOwner();
		if(owner == null) {
			owner = new Owner("owner", "admin", flexiBook);
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Given ("a business exists in the system")
	public void thereIsABusiness() {
		business = flexiBook.getBusiness();
		if(business == null) {
			business = new Business("aName", "anAddress", "aPhoneNo", "anEmail", flexiBook); 
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Given("the Owner with username {string} is logged in")

	public void thereIsAnOwnerLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser(owner);
		assertEquals(string, currentUser.getUsername());
	}


	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void aNewServiceAdditionIsInitiated(String user, String aName, String aDuration, String aDowntimeStart, String aDowntimeDuration) {
		try {  
			FlexiBookController.addService(aName, flexiBook, Integer.parseInt(aDuration), Integer.parseInt(aDowntimeDuration), Integer.parseInt(aDowntimeStart));
		}  

		catch (InvalidInputException e) {
			error = e.getMessage();
			//errorCntr++;
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall exist in the system")
	public void theServiceShallExistInTheSystem(String string) {
		if (flexiBook.hasBookableServices() == true) {
			assertEquals(string, Service.getWithName(string).getName());
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallHave(String string, String string2, String string3, String string4) {
		if (flexiBook.hasBookableServices() == true) {
			int i = flexiBook.indexOfBookableService(Service.getWithName(string));
			assertEquals(Integer.parseInt(string2), ((Service) flexiBook.getBookableService(i)).getDuration());
			assertEquals(Integer.parseInt(string3), ((Service) flexiBook.getBookableService(i)).getDowntimeStart());
			assertEquals(Integer.parseInt(string4), ((Service) flexiBook.getBookableService(i)).getDowntimeDuration());
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Then("the number of services in the system shall be {string}")
	public void theNumberOfServicesInTheSystemShallBe(String string) {

		allBookableServices = flexiBook.getBookableServices();
		for (int i = 0; i < allBookableServices.size(); i++) {
			if (allBookableServices.get(i) instanceof Service) {
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
		for ( int i = 0; i < existingServices.size(); i++) {
			flexiBook.addBookableService(new Service(existingServices.get(i).get("name"), flexiBook, (Integer.parseInt(existingServices.get(i).get("duration"))), (Integer.parseInt(existingServices.get(i).get("downtimeDuration"))), (Integer.parseInt(existingServices.get(i).get("downtimeStart")))));
		}


	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall still preserve the following properties:")
	public void theServicePreservesTheFollowingProperties(String string, io.cucumber.datatable.DataTable dataTable) {
		preservedProperties = dataTable.asMaps(String.class, String.class);
		assertEquals(Service.getWithName(string).getName(), preservedProperties.get(0).get("name"));
		assertEquals(((Service) Service.getWithName(string)).getDuration(), Integer.parseInt(preservedProperties.get(0).get("duration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeDuration(), Integer.parseInt(preservedProperties.get(0).get("downtimeDuration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeStart(), Integer.parseInt(preservedProperties.get(0).get("downtimeStart")));
	}


	/**
	 * @author Sneha Singh
	 */
	@Given("the following customers exist in the system:")
	public void theFollowingCustomersExistInTheSystem(io.cucumber.datatable.DataTable dataTable) {
		customers = dataTable.asMaps(String.class, String.class);
		for (int i=0; i<customers.size(); i++) {
			flexiBook.addCustomer(customers.get(i).get("username"), customers.get(i).get("password"));
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Given("Customer with username {string} is logged in")
	public void customerWithUsernameIsLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser((User.getWithUsername(string)));
		assertEquals(string, currentUser.getUsername());
	}


	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void updateToServiceIsInitiated(String user, String name, String newName, String newDuration, String newDowntimeStart, String newDowntimeDuration) {
		int duration = Integer.parseInt(newDuration);
		int downtimeStart = Integer.parseInt(newDowntimeStart);
		int downtimeDuration = Integer.parseInt(newDowntimeDuration);

		try {  
			FlexiBookController.updateService(((Service) Service.getWithName(name)), newName, flexiBook, duration, downtimeStart, downtimeDuration);
		}  

		catch (InvalidInputException e) {
			error = e.getMessage();

			//errorCntr++;
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallBeUpdatedTo(String string, String string2, String string3, String string4, String string5) {
		if (flexiBook.hasBookableServices() == true) {

			int i = flexiBook.indexOfBookableService(Service.getWithName(string2));
			assertEquals(string2, flexiBook.getBookableService(i).getName());
			assertEquals(Integer.parseInt(string3), ((Service) flexiBook.getBookableService(i)).getDuration());
			assertEquals(Integer.parseInt(string4), ((Service) flexiBook.getBookableService(i)).getDowntimeStart());
			assertEquals(Integer.parseInt(string5), ((Service) flexiBook.getBookableService(i)).getDowntimeDuration());
		}
	}


	/**
	 * @author Sneha Singh
	 */
	@Given("the system's time and date is {string}")
	public void theSystemsDateandTimeIs(String string) {
		String dateAndTime[] = string.split("\\+");
		FlexiBookApplication.setSystemDate(dateAndTime[0]);
		FlexiBookApplication.setSystemTime(dateAndTime[1] + ":00");
	}

	/**
	 * @author Sneha Singh
	 */
	@Given("the following appointments exist in the system:")
	public void theFollowingAppointmentsExistInTheSystem(io.cucumber.datatable.DataTable dataTable) {
		appointments = dataTable.asMaps(String.class, String.class);
		for (int i = 0 ; i< appointments.size(); i++) {
			startDate  = Date.valueOf(appointments.get(i).get("date"));
			endDate = startDate; 
			startTime = Time.valueOf((appointments.get(i).get("startTime")) + ":00");
			startTime = Time.valueOf((appointments.get(i).get("endTime")) + ":00");

			flexiBook.addAppointment(new Appointment((Customer) User.getWithUsername(appointments.get(i).get("customer")), Service.getWithName(appointments.get(i).get("serviceName")), new TimeSlot(startDate, startTime, endDate, endTime, flexiBook), flexiBook));
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@When("{string} initiates the deletion of service {string}")
	public void serviceDeletionIsInitiated(String string, String string2) {

		try {
			FlexiBookController.deleteService(string2, flexiBook);
		} catch (InvalidInputException e) {
			error = e.getMessage();
			//errorCntr++;
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void numberOfAppointmentsOfServiceShouldBe(String string, String string2) {
		if (Service.getWithName(string) != null) {
			assertEquals(Integer.parseInt(string2), Service.getWithName(string).getAppointments().size());
		}
	}

	/**
	 * @author Sneha Singh
	 */
	@Then("the number of appointments in the system shall be {string}")
	public void numberOfAppointmentsInSystemShallBe(String string) {
		assertEquals(Integer.parseInt(string), flexiBook.getAppointments().size());
	}

	/**
	 * @author Sneha Singh
	 */
	@Given("the following service combos exist in the system:")
	public void theFollowingServiceCombosExist(io.cucumber.datatable.DataTable dataTable) {
		serviceCombos = dataTable.asMaps(String.class, String.class);

		for (int i =0; i < serviceCombos.size(); i++ ) {
			String [] comboElements = serviceCombos.get(i).get("services").split(",");
			ServiceCombo temp = new ServiceCombo(serviceCombos.get(i).get("name"), flexiBook);
			for (int k = 0; k < comboElements.length; k++) {
				temp.addService(false, (Service) Service.getWithName(comboElements[k]));

			}
			ComboItem mainService = new ComboItem(true, (Service) Service.getWithName(serviceCombos.get(i).get("mainService")), temp);
			temp.setMainService(mainService);
		}

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
	public void theServiceCombosShallNotContainTheService(String string, String string2) {
		combosInService = ((ServiceCombo) ServiceCombo.getWithName(string)).getServices(); 

		for (int i = 0; i < combosInService.size(); i++) {
			assertNotEquals(combosInService.get(i).getService(), (Service.getWithName(string2)));
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


	//	@After
	//	public void tearDown() {
	//		flexiBook.delete();
	//	}


	//**********************************END SERVICE IMPLEMENTATION*******************8






//	@Given("a Flexibook system exists")
//	public void a_flexibook_system_exists() {
//		flexiBook = FlexiBookApplication.getFlexiBook();
//		throw new io.cucumber.java.PendingException();
//	}
//
//
//	@Given("the system's time and date is {string}")
//	public void the_system_s_time_and_date_is(String string) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("an owner account exists in the system")
//	public void an_owner_account_exists_in_the_system() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("a business exists in the system")
//	public void a_business_exists_in_the_system() {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the following customers exist in the system:")
//	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the following services exist in the system:")
//	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the following service combos exist in the system:")
//	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the business has the following opening hours")
//	public void the_business_has_the_following_opening_hours(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the business has the following holidays")
//	public void the_business_has_the_following_holidays(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("the following appointments exist in the system:")
//	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		// Write code here that turns the phrase above into concrete actions
//		// For automatic transformation, change DataTable to one of
//		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
//		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
//		// Double, Byte, Short, Long, BigInteger or BigDecimal.
//		//
//		// For other transformations you can register a DataTableType.
//		throw new io.cucumber.java.PendingException();
//	}
//	@Given("{string} is logged in to their account")
//	public void is_logged_in_to_their_account(String string) {
//
//		throw new io.cucumber.java.PendingException();
//	}
//	@When("{string} schedules an appointment on {string} for {string} at {string}")
//	public void schedules_an_appointment_on_for_at(String string, String string2, String string3, String string4) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Then("{string} shall have a {string} appointment on {string} from {string} to {string}")
//	public void shall_have_a_appointment_on_from_to(String string, String string2, String string3, String string4, String string5) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Then("there shall be {int} more appointment in the system")
//	public void there_shall_be_more_appointment_in_the_system(Integer int1) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//
//
//
//	@When("{string} attempts to update their {string} appointment on {string} at {string} to {string} at {string}")
//	public void attempts_to_update_their_appointment_on_at_to_at(String string, String string2, String string3, String string4, String string5, String string6) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//
//
//	@Then("the system shall report that the update was {string}")
//	public void the_system_shall_report_that_the_update_was(String string) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//
//
//	@When("{string} attempts to cancel their {string} appointment on {string} at {string}")
//	public void attempts_to_cancel_their_appointment_on_at(String string, String string2, String string3, String string4) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Then("{string}'s {string} appointment on {string} at {string} shall be removed from the system")
//	public void s_appointment_on_at_shall_be_removed_from_the_system(String string, String string2, String string3, String string4) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}
//	@Then("there shall be {int} less appointment in the system")
//	public void there_shall_be_less_appointment_in_the_system(Integer int1) {
//		// Write code here that turns the phrase above into concrete actions
//		throw new io.cucumber.java.PendingException();
//	}


	@After
	public void tearDown() {
		flexiBook.delete();
	}














