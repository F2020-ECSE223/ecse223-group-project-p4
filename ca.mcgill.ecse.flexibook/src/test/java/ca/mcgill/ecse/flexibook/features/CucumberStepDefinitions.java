package ca.mcgill.ecse.flexibook.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Time;
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
	
	private int errorCntr;
	private Integer appointmentCntr = 0;
	private Integer prevAppointmentCntr = 0;
	private String updateAppointmentSuccess = null;
	private Date SystemDateTime;
	private boolean exception;
	
	
	
	//*******************SERVICE IMPLEMENTATION*************************



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
		currentUser = FlexiBookApplication.setCurrentUser(User.getWithUsername(string));
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

//	/**
//	 * @author Sneha Singh
//	 */
//	@Given("the following service combos exist in the system:")
//	public void theFollowingServiceCombosExist(io.cucumber.datatable.DataTable dataTable) {
//		serviceCombos = dataTable.asMaps(String.class, String.class);
//
//		for (int i =0; i < serviceCombos.size(); i++ ) {
//			String [] comboElements = serviceCombos.get(i).get("services").split(",");
//			ServiceCombo temp = new ServiceCombo(serviceCombos.get(i).get("name"), flexiBook);
//			for (int k = 0; k < comboElements.length; k++) {
//				temp.addService(false, (Service) Service.getWithName(comboElements[k]));
//
//			}
//			ComboItem mainService = new ComboItem(true, (Service) Service.getWithName(serviceCombos.get(i).get("mainService")), temp);
//			temp.setMainService(mainService);
//		}
//
//	}

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




	//**********************************END SERVICE IMPLEMENTATION*******************8

	//**********************************SERVICE COMBO IMPLEMENTATION********************


	
//	/**
//	 * @author yasminamatta
//	 */
//
//	@Given("a Flexibook system exists")
//	public void a_flexibook_system_exists() {
//		flexiBook = FlexiBookApplication.getFlexiBook();
//		error = "";
//		errorCntr = 0;
//
//	}
//
//	/**
//	 * @author yasminamatta
//	 */
//
//	@Given("an owner account exists in the system")
//	public void an_owner_account_exists_in_the_system() {
//		owner = flexiBook.getOwner();
//		if (owner == null) {
//			owner = new Owner("owner", "admin", flexiBook);
//		}
	
//	}

//	/**
//	 * @author yasminamatta
//	 */
//	@Given("a business exists in the system")
//	public void a_business_exists_in_the_system() {
//		business = flexiBook.getBusiness();
//		if (business == null) {
//			business = new Business("aName", "anAdress", "aPhoneNumber", "anEmailAdress", flexiBook);
//		}
//
//	}
//
//	/**
//	 * @author yasminamatta
//	 * @param dataTable
//	 */
//	@Given("the following services exist in the system:")
//	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
//
//		for (int i = 0; i < list.size(); i++) {
//			flexiBook.addBookableService(
//					new Service((list.get(i).get("name")), (flexiBook), (Integer.parseInt(list.get(i).get("duration"))),
//							(Integer.parseInt(list.get(i).get("downtimeStart"))),
//							(Integer.parseInt(list.get(i).get("downtimeDuration")))));
//		}
//
//	}

//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//	@Given("the Owner with username {string} is logged in")
//	public void the_owner_with_username_is_logged_in(String string) {
//		FlexiBookApplication.setCurrentUser(User.getWithUsername(string));
//
//	}

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
			FlexiBookController.defineServiceCombo(string2, string, flexiBook, string3, string5, string4);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCntr++;
		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the service combo {string} shall exist in the system")
	public void the_service_combo_shall_exist_in_the_system(String string) {
		boolean flag = false;
		if (BookableService.getWithName(string) != null
				&& BookableService.getWithName(string) instanceof ServiceCombo) {
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
	public void the_service_combo_shall_contain_the_services_with_mandatory_setting(String string, String string2,
			String string3) {

		ServiceCombo serviceCombo = (ServiceCombo) ServiceCombo.getWithName(string);

		String[] services = string2.split(",");
		String[] mandatory = string3.split(",");

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
	public void the_main_service_of_the_service_combo_shall_be(String string, String string2) {
		assertEquals(((ServiceCombo) BookableService.getWithName(string)).getMainService().getService().getName(),
				string2);
	}

	@Then("the service {string} in service combo {string} shall be mandatory")
	public void the_service_in_service_combo_shall_be_mandatory(String string, String string2) {

		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo
					&& flexiBook.getBookableServices().get(i).getName().equals(string2)) {
				ServiceCombo serviceC = (ServiceCombo) flexiBook.getBookableServices().get(i);
				for (ComboItem comboItem : serviceC.getServices()) {
					if (comboItem.getService().getName().equals(string)) {
						assertEquals(true, comboItem.getMandatory());
					}
				}
			}
		}
	}

//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//	@Then("the number of service combos in the system shall be {string}")
//	public void the_number_of_service_combos_in_the_system_shall_be(String string) {
//		int noOfServiceCombo = 0;
//		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
//			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo) {
//				noOfServiceCombo++;
//			}
//		}
//		assertEquals(noOfServiceCombo, Integer.parseInt(string));
//	}
//
	/**
	 * @author yasminamatta
	 * @param dataTable
	 */

	@Given("the following service combos exist in the system:")
	public void the_following_service_combos_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);

		String[] services;
		String[] mandatories;

		for (int i = 0; i < list.size(); i++) {

			mandatories = list.get(i).get("mandatory").split(",");
			services = list.get(i).get("services").split(",");

			ServiceCombo serviceCombo = new ServiceCombo(list.get(i).get("name"), flexiBook);
			for (int j = 0; j < services.length; j++) {
				if (services[j].equals(list.get(i).get("mainService"))) {
					serviceCombo.setMainService(
							new ComboItem(true, (Service) BookableService.getWithName(services[j]), serviceCombo));
				} else {
					serviceCombo.addService(new ComboItem(Boolean.parseBoolean(mandatories[j]),
							(Service) BookableService.getWithName(services[j]), serviceCombo));

				}

			}

		}

	}

//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//
//	@Then("an error message with content {string} shall be raised")
//	public void an_error_message_with_content_shall_be_raised(String string) {
//		assertTrue(error.contains(string));
//	}

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the service combo {string} shall not exist in the system")
	public void the_service_combo_shall_not_exist_in_the_system(String string) {

		assertNull(ServiceCombo.getWithName(string));

	}
//
	/**
	 * @author yasminamatta
	 * @param string
	 * @param dataTable
	 */

	@Then("the service combo {string} shall preserve the following properties:")
	public void the_service_combo_shall_preserve_the_following_properties(String string,
			io.cucumber.datatable.DataTable dataTable) {
		String services = "";
		String mandatory = "";
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).get("name").contains(string)) {
				ServiceCombo serviceCombo = (ServiceCombo) BookableService.getWithName(string);
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
//
//	/**
//	 * @author yasminamatta
//	 * @param dataTable
//	 */
//
//	@Given("the following customers exist in the system:")
//	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
//
//		for (int i = 0; i < list.size(); i++) {
//			flexiBook.addCustomer(list.get(i).get("username"), list.get(i).get("password"));
//
//		}
//	}
//
//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//
//	@Given("Customer with username {string} is logged in")
//	public void customer_with_username_is_logged_in(String string) {
//		FlexiBookApplication.setCurrentUser(User.getWithUsername(string));
//
//	}
//
//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//
//	@Given("the system's time and date is {string}")
//	public void the_system_s_time_and_date_is(String string) {
//		String datePart = string.substring(0, 10);
//		String timePart = string.substring(11, 16);
////		Date date = Date.valueOf(datePart);
////		Time time = Time.valueOf(timePart + ":00");
//		FlexiBookApplication.setSystemDate(datePart);
//		FlexiBookApplication.setSystemTime(timePart+ ":00");
//	}
//
//	/**
//	 * @author yasminamatta
//	 * @param dataTable
//	 */
//	@Given("the following appointments exist in the system:")
//	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
//		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
//		for (int i = 0; i < list.size(); i++) {
//
//			String a = list.get(i).get("date");
//
//			Date startDate = Date.valueOf(a);
//			Time startTime = Time.valueOf(list.get(i).get("startTime") + ":00");
//			Date endDate = Date.valueOf(list.get(i).get("date"));
//			Time endTime = Time.valueOf(list.get(i).get("endTime") + ":00");
//			TimeSlot timeSlot = new TimeSlot(startDate, startTime, endDate, endTime, flexiBook);
//
//			flexiBook.addAppointment((Customer) Customer.getWithUsername(list.get(i).get("customer")),
//					(BookableService) BookableService.getWithName(list.get(i).get("serviceName")), timeSlot);
//		}
//	}
//
	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */
	@When("{string} initiates the deletion of service combo {string}")
	public void initiates_the_deletion_of_service_combo(String string, String string2) {
		try {
			FlexiBookController.deleteServiceCombo(string, string2, flexiBook);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCntr++;
		}
	}
//
//	/**
//	 * @author yasminamatta
//	 * @param string
//	 * @param string2
//	 */
//	@Then("the number of appointments in the system with service {string} shall be {string}")
//	public void the_number_of_appointments_in_the_system_with_service_shall_be(String string, String string2) {
//		if (Service.getWithName(string) != null) {
//			assertEquals(Integer.parseInt(string2), Service.getWithName(string).numberOfAppointments());
//		}
//	}
//
//	/**
//	 * @author yasminamatta
//	 * @param string
//	 */
//
//	@Then("the number of appointments in the system shall be {string}")
//	public void the_number_of_appointments_in_the_system_shall_be(String string) {
//		
//		assertEquals(flexiBook.numberOfAppointments(), Integer.parseInt(string));
//	}

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
			String string, String string2, String string3, String string4, String string5, String string6) {
		try {
			FlexiBookController.updateServiceCombo(string2, string3, string, flexiBook, string4, string6, string5);
		} catch (InvalidInputException e) {
			error += e.getMessage();
			errorCntr++;
		}

	}

	/**
	 * @author yasminamatta
	 * @param string
	 * @param string2
	 */
	@Then("the service combo {string} shall be updated to name {string}")
	public void the_service_combo_shall_be_updated_to_name(String string, String string2) {

		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo
					&& flexiBook.getBookableServices().get(i).getName().equals(string)) {

				assertEquals(string2, flexiBook.getBookableService(i).getName());

			}
		}

	}
	
	
	//**************************************SET UP BUSINESS IMPLEMENTATION*****************************
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
	}
	
	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("the user is logged in to an account with username {string}")
	public void the_user_is_logged_in_to_an_account_with_username(String string) {
	    FlexiBookApplication.setCurrentUser(User.getWithUsername(string));
	}
	
	/**
	 * @author Aroomoogon Krishna
	 */
	@When("the user tries to set up the business information with new {string} and {string} and {string} and {string}")
	public void the_user_tries_to_set_up_the_business_information_with_new_and_and_and(String string, String string2, String string3, String string4) {
	    exception = false;
		try {
	    	FlexiBookController.setupBusinessInfo(string, string2, string3, string4, null, null, null, null, null);
	    } catch (InvalidInputException e) {
	    	exception = true;
	    	error = e.getMessage();
	    }
	}
	
	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("a new business with new {string} and {string} and {string} and {string} shall {string} created")
	public void a_new_business_with_new_and_and_and_shall_created(String string, String string2, String string3, String string4, String string5) {
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
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Given("the business has a business hour on {string} with start time {string} and end time {string}")
	public void the_business_has_a_business_hour_on_with_start_time_and_end_time(String string, String string2, String string3) {
	   new BusinessHour(DayOfWeek.valueOf(string), Time.valueOf(string2), Time.valueOf(string3), flexiBook);
	}
	
	/**
	 * @author Aroomoogon Krishna
	 */	
	@When("the user tries to add a new business hour on {string} with start time {string} and end time {string}")
	public void the_user_tries_to_add_a_new_business_hour_on_with_start_time_and_end_time(String string, String string2, String string3) {
	    exception = false;
		try {
	    	FlexiBookController.setupBusinessInfo(null, null, null, null, DayOfWeek.valueOf(string), Time.valueOf(string2), Time.valueOf(string2), null, null);
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
		return;
	}
	
	
	/**
	 * @author Aroomoogon Krishna
	 */	
	@When("the user tries to access the business information")
	public void the_user_tries_to_access_the_business_information() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	/**
	 * @author Aroomoogon Krishna
	 */
	@Then("the {string} and {string} and {string} and {string} shall be provided to the user")
	public void the_and_and_and_shall_be_provided_to_the_user(String string, String string2, String string3, String string4) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	
	private static String oldUname;
	private static String oldPword;

	private static int before;




/**---- Artus implementation */ 
	
		/**
		 * @author artus
		 * @param string
		 */
	
		@Given("there is no existing username {string}")
		public void thereIsNoExistingUsername(String string) {
			List<Customer> customerList = flexiBook.getCustomers();
			Customer customer = null;
				for(int i = 0; i < customerList.size(); i++) {
					customer = customerList.get(i);
    			assertTrue(customer.getUsername() != string);
    		}
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
			}
			catch (InvalidInputException e){
				error += e.getMessage();
			}
		}
		/**
		 * @author artus
		 */
		@Then("a new customer account shall be created")
		public void aNewCustomerAccountShallBeCreated() {
			boolean test=false;
    		
    		if(flexiBook.getCustomers().size()==before+1) test = true;
    		
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
			if(string.equals("owner")) {
		    	assertEquals(Uname, string);
		    	assertEquals(Pword, string2);
		    	tf = true;
		    }
			else {
				for(int i = 0; i < customerList.size(); i++) {
					customer = customerList.get(i);
		    	if (customer.getUsername().equals(string)) {
		    		if (customer.getPassword().equals(string2)) tf =true;
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
			boolean tf=false;
    		
    		if(flexiBook.getCustomers().size()==before) {
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
					owner  = new Owner("owner", "ownerPass", flexiBook);
					flexiBook.setOwner(owner);
				}
				else {
					flexiBook.addCustomer(string, "ownerPass");
				}
			}
				
		/**
		 * @author artus
		 * @param string1
		 * @param string2
		 */
	
//		@Given("an owner account exists in the system with username {string} and password {string}")
//		public void anOwnerAccountExistsInTheSystemWithUsernameAndPassword(String string1, String string2) {
//			flexiBook = FlexiBookApplication.getFlexiBook();
//			flexiBook.setOwner(new Owner("owner", "ownerPass", flexiBook));
//		}

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
//		@Given("the user is logged in to an account with username {string}")
//		public void theUserIsLoggedInToAnAccountWithUsername(String string) {
//
//			FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
//			User user;
//			List<Customer> customerList = flexiBook.getCustomers();
//			User customer = null;
//
//			if (string.equals("owner")) {
//
//				Owner newOwner = FlexiBookApplication.getFlexiBook().getOwner();
//				user = newOwner;
//				FlexiBookApplication.setCurrentUser(user);
//				
//			}
//
//			else {
//				
//				for(int i = 0; i < customerList.size(); i++) {
//					customer = customerList.get(i);
//					if (customer.getUsername().equals(string)) {
//						user = customer;
//						FlexiBookApplication.setCurrentUser(user);
//					}
//				}
//			}
//			
//		}	
		
		
		/**
		 * @author artus
		 * @param string
		 */
		@When("the user tries to delete account with the username {string}")
		public void theUserTriesToDeleteAccountWithTheUsername(String string) {
		
				try {
					FlexiBookController.deleteCustomerAccount(string);
				}
				catch (InvalidInputException e){
					error += e.getMessage();
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
		    
			for(int i = 0; i < customerList.size(); i++) {
				user = customerList.get(i);
		    	if (user.getUsername() == string) tf = false;
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
			
			if (flexiBook.getAppointments().size() == 0) tf=true;
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
		public void anErrorMessageShallBeRaised2(String string)  {
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

		    
		    if (owner !=null) tf = true;
		    if (string.equals("owner")) tf =true;
		    else {
		    	for(int i = 0; i < customerList.size(); i++) {
					user = customerList.get(i);
		    	if (user.getUsername() == string) tf = true;
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
			}
			catch (InvalidInputException e) {
				  error += e.getMessage();
			}
	    	
		}
		
		/**
		 * @author artus
		 */
		@Then("the account shall not be updated")
		public void the_account_shall_not_be_updated() {
	        User user = FlexiBookApplication.getCurrentUser();
	    	boolean test = false;
	    	
	    	if(oldUname.equals(user.getUsername()) || oldPword.equals(user.getPassword())) {
	    		
	    			test = true;
	    		
	    	}
	    	assertTrue(test);
		}

	
	@After
	public void tearDown() {
		flexiBook.delete();
	}

}
