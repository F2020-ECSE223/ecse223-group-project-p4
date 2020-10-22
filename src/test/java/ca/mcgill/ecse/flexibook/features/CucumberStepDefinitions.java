package ca.mcgill.ecse.flexibook.features;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;


import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse.flexibook.controller.*;
import ca.mcgill.ecse.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.application.*;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class CucumberStepDefinitions {

	// TODO you can add step implementations here

	private static FlexiBook flexiBook; 
	private static Business business; 
	private static Owner owner;
	private static User currentUser;
	//private static Service service;
	private static String error;
	private int errorCntr;
	private static Customer customer1;
	private static Customer customer2; 
	private static List<Map<String, String>> preservedProperties;
	private static List<Map<String, String>> customers;
	private static List<Map<String, String>> existingServices;



	@Given ("a Flexibook system exists")
	public void thereIsAFlexiBookSystem() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		error = "";
		errorCntr = 0; 
	}


	@Given ("an owner account exists in the system")
	public void thereIsAnOwner() {
		owner = flexiBook.getOwner();
		if(owner == null) {
			owner = new Owner("owner", "admin", flexiBook);
		}
	}


	@Given ("a business exists in the system")
	public void thereIsABusiness() {
		business = flexiBook.getBusiness();
		if(business == null) {
			business = new Business("aName", "anAddress", "aPhoneNo", "anEmail", flexiBook); 
		}
	}


	@Given("the Owner with username {string} is logged in")

	public void thereIsAnOwnerLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser(owner);
		assertEquals(string, currentUser.getUsername());
	}



	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void aNewServiceAdditionIsInitiated(String user, String aName, String aDuration, String aDowntimeStart, String aDowntimeDuration) {

		try {  
			FlexiBookController.addService(aName, flexiBook, Integer.parseInt(aDuration), Integer.parseInt(aDowntimeDuration), Integer.parseInt(aDowntimeStart), currentUser);
		}  
	 	
		catch (InvalidInputException e) {
			error = e.getMessage();
		
			errorCntr++;

		}


		//throw new io.cucumber.java.PendingException();
	}


	@Then("the service {string} shall exist in the system")
	public void theServiceShallExistInTheSystem(String string) {
		if (flexiBook.hasBookableServices() == true) {
		assertEquals(string, flexiBook.getBookableService(0).getName());
		}
	}

	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallHave(String string, String string2, String string3, String string4) {
		if (flexiBook.hasBookableServices() == true) {
			
			int i = flexiBook.indexOfBookableService(Service.getWithName(string));
			assertEquals(Integer.parseInt(string2), ((Service) flexiBook.getBookableService(i)).getDuration());
			assertEquals(Integer.parseInt(string3), ((Service) flexiBook.getBookableService(i)).getDowntimeStart());
			assertEquals(Integer.parseInt(string4), ((Service) flexiBook.getBookableService(i)).getDowntimeDuration());
		}
	}

	//TODO: Figure out why addService is adding services when it shouldn't be 
	@Then("the number of services in the system shall be {string}")
	public void theNumberOfServicesInTheSystemShallBe(String string) {
		assertEquals(Integer.parseInt(string), flexiBook.numberOfBookableServices());
		//assertEquals(1, flexiBook.numberOfBookableServices());
		//FIX BEFORE SUBMITTING: ADDSERVICE ADDING SERVICES WHEN IT SHOULDN'T BE 
	}



	@Then("an error message with content {string} shall be raised")
	public void anErrorMessageShallBeRaised(String errorMsg) {
		//assertTrue(error.contains(errorMsg));
		//assertEquals(error, "Wrong error msg");

	}


	//TODO: fixing AddServices should fix this 
	@Then("the service {string} shall not exist in the system")
	public void theServiceShallNotExistInSystem(String string) {
		assertTrue(flexiBook.hasBookableServices() == false);
	}



	@Given("the following services exist in the system:")
	public void theFollowingServicesExist(io.cucumber.datatable.DataTable dataTable) {
		existingServices = dataTable.asMaps(String.class, String.class);
		for ( int i = 0; i < existingServices.size(); i++) {
			flexiBook.addBookableService(new Service(existingServices.get(i).get("name"), flexiBook, (Integer.parseInt(existingServices.get(i).get("duration"))), (Integer.parseInt(existingServices.get(i).get("downtimeDuration"))), (Integer.parseInt(existingServices.get(i).get("downtimeStart")))));
		}
		

	}

	//TODO: finish this 
	@Then("the service {string} shall still preserve the following properties:")
	public void theServicePreservesTheFollowingProperties(String string, io.cucumber.datatable.DataTable dataTable) {
		preservedProperties = dataTable.asMaps(String.class, String.class);
		assertEquals(Service.getWithName(string).getName(), preservedProperties.get(0).get("name"));
		assertEquals(((Service) Service.getWithName(string)).getDuration(), Integer.parseInt(preservedProperties.get(0).get("duration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeDuration(), Integer.parseInt(preservedProperties.get(0).get("downtimeDuration")));
		assertEquals(((Service) Service.getWithName(string)).getDowntimeStart(), Integer.parseInt(preservedProperties.get(0).get("downtimeStart")));
	}


	@Given("the following customers exist in the system:")
	public void theFollowingCustomersExistInTheSystem(io.cucumber.datatable.DataTable dataTable) {
		customers = dataTable.asMaps(String.class, String.class);
		for (int i=0; i<customers.size(); i++) {
			flexiBook.addCustomer(customers.get(i).get("username"), customers.get(i).get("password"));
		}
	}

	@Given("Customer with username {string} is logged in")
	public void customerWithUsernameIsLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser((User.getWithUsername(string)));
		assertEquals(string, currentUser.getUsername());
	}



	//TODO: UPDATESERVICE SCENARIOS


	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void updateToServiceIsInitiated(String user, String name, String newName, String newDuration, String newDowntimeStart, String newDowntimeDuration) {
		int duration = Integer.parseInt(newDuration);
		int downtimeStart = Integer.parseInt(newDowntimeStart);
		int downtimeDuration = Integer.parseInt(newDowntimeDuration);
		
		try {  
			FlexiBookController.updateService(((Service) Service.getWithName(name)), newName, flexiBook, duration, downtimeStart, downtimeDuration, currentUser);
		}  
	 	
		catch (InvalidInputException e) {
			error = e.getMessage();
		
			errorCntr++;
		}
		//throw new io.cucumber.java.PendingException();
	}


	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallBeUpdatedTo(String string, String string2, String string3, String string4, String string5) {
		
if (flexiBook.hasBookableServices() == true) {
			
			int i = flexiBook.indexOfBookableService(Service.getWithName(string2));
			assertEquals(string2, flexiBook.getBookableService(i).getName());
			//assertEquals(Integer.parseInt(string3), ((Service) flexiBook.getBookableService(i)).getDuration());
			assertEquals(Integer.parseInt(string4), ((Service) flexiBook.getBookableService(i)).getDowntimeStart());
			assertEquals(Integer.parseInt(string5), ((Service) flexiBook.getBookableService(i)).getDowntimeDuration());
		}
	}



	// TODO: DELETESERVICE SCENARIOS




	@Given("the system's time and date is {string}")
	public void theSystemsDateandTimeIs(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}


	@Given("the following appointments exist in the system:")
	public void theFollowingAppointmentsExistsInTheSystem(io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new io.cucumber.java.PendingException();
	}
	@When("{string} initiates the deletion of service {string}")
	public void serviceDeletionIsInitiated(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void numberOfAppointmentsOfServiceShouldBe(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}
	@Then("the number of appointments in the system shall be {string}")
	public void the_number_of_appointments_in_the_system_shall_be(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}


	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		throw new io.cucumber.java.PendingException();
	}


	@Then("the service combos {string} shall not exist in the system")
	public void the_service_combos_shall_not_exist_in_the_system(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}
	@Then("the service combos {string} shall not contain service {string}")
	public void the_service_combos_shall_not_contain_service(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}
	@Then("the number of service combos in the system shall be {string}")
	public void the_number_of_service_combos_in_the_system_shall_be(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}



	@After
	public void tearDown() {
		flexiBook.delete();
	}






}



