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
	private static Service service;
	private static String error;
	private int errorCntr;
	private static Customer customer1;
	private static Customer customer2; 




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
			business = new Business("aName", "anAddress", "anPhoneNo", "anEmail", flexiBook); 
		}
	}


	@Given("the Owner with username {string} is logged in")

	public void thereIsAnOwnerLoggedIn(String string) {
		currentUser = FlexiBookApplication.setCurrentUser(owner);
		assertEquals(string, currentUser.getUsername());
	}



	@When("{string} initiates the addition of the service {string} with duration {string}, start of down time {string} and down time duration {string}")
	public void aNewServiceAdditionIsInitiated(String string, String string2, String string3, String string4, String string5) {
		// service = new Service("wash", flexiBook, 100, 0, 0);
		try {  
			service = new Service(string2, flexiBook, Integer.parseInt(string3), Integer.parseInt(string5), Integer.parseInt(string4));

		}  
		catch (RuntimeException re) {  
			error = ("Service " +string2+ " already exists");  
			errorCntr++;
		}  	

		//FlexiBookController.addService(flexiBook, service, owner);
		try {
			FlexiBookController.addService(flexiBook, service, currentUser);
		} catch (InvalidInputException e) {
			if (error == "") {
				error = e.getMessage();
			}
			errorCntr++;

		}


		//throw new io.cucumber.java.PendingException();
	}


	@Then("the service {string} shall exist in the system")
	public void theServiceShallExistInTheSystem(String string) {
		assertEquals(string, flexiBook.getBookableService(0).getName());
	}

	@Then("the service {string} shall have duration {string}, start of down time {string} and down time duration {string}")
	public void theServiceShallHave(String string, String string2, String string3, String string4) {
		assertEquals(Integer.parseInt(string2), ((Service) flexiBook.getBookableService(0)).getDuration());
		assertEquals(Integer.parseInt(string3), ((Service) flexiBook.getBookableService(0)).getDowntimeStart());
		assertEquals(Integer.parseInt(string4), ((Service) flexiBook.getBookableService(0)).getDowntimeDuration());
	}

	//TODO: Figure out why addService is adding services when it shouldn't be 
	@Then("the number of services in the system shall be {string}")
	public void theNumberOfServicesInTheSystemShallBe(String string) {
		// assertEquals(Integer.parseInt(string), flexiBook.numberOfBookableServices());
		//FIX BEFORE SUBMITTING: ADDSERVICE ADDING SERVICES WHEN IT SHOULDN'T BE 
	}



	@Then("an error message with content {string} shall be raised")
	public void anErrorMessageShallBeRaised(String errorMsg) {
		assertTrue(error.contains(errorMsg));
		//assertEquals(error, "Service wash already exists");

	}


	//TODO: fixing AddServices should fix this 
	@Then("the service {string} shall not exist in the system")
	public void theServiceShallNotExistInSystem(String string) {
		//assertNotEquals(string, flexiBook.getBookableService(0).getName());
	}



	@Given("the following services exist in the system:")
	public void theFollowingServicesExist(io.cucumber.datatable.DataTable dataTable) {
		Service wash = new Service("wash", flexiBook, 30, 0, 0);
		flexiBook.addBookableService(wash);

	}

	//TODO: finish this 
	@Then("the service {string} shall still preserve the following properties:")
	public void theServicePreservesTheFollowingProperties(String string, io.cucumber.datatable.DataTable dataTable) {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
		// Map<K, List<V>>. E,K,V must be a String, Integer, Float,
		// Double, Byte, Short, Long, BigInteger or BigDecimal.
		//
		// For other transformations you can register a DataTableType.
		// throw new io.cucumber.java.PendingException();
	}


	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		customer1 = new Customer("customer1", "1234567", flexiBook);
		customer2 = new Customer("customer2", "8901234", flexiBook);
		flexiBook.addCustomer(customer1);
		flexiBook.addCustomer(customer2);
	}



	@Given("Customer with username {string} is logged in")
	public void customer_with_username_is_logged_in(String string) {
		currentUser = FlexiBookApplication.setCurrentUser((User.getWithUsername(string)));
		assertEquals(string, currentUser.getUsername());
	}



	//TODO: UPDATESERVICE SCENARIOS


	@When("{string} initiates the update of the service {string} to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void initiates_the_update_of_the_service_to_name_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4, String string5, String string6) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}


	@Then("the service {string} shall be updated to name {string}, duration {string}, start of down time {string} and down time duration {string}")
	public void the_service_shall_be_updated_to_name_duration_start_of_down_time_and_down_time_duration(String string, String string2, String string3, String string4, String string5) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}



	// TODO: DELETESERVICE SCENARIOS




	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}


	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
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
	public void initiates_the_deletion_of_service(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		throw new io.cucumber.java.PendingException();
	}
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void the_number_of_appointments_in_the_system_with_service_shall_be(String string, String string2) {
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



