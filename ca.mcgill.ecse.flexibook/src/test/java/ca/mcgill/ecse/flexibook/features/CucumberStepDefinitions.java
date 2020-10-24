package ca.mcgill.ecse.flexibook.features;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;


import ca.mcgill.ecse.flexibook.model.BookableService;
import ca.mcgill.ecse.flexibook.model.Business;
import ca.mcgill.ecse.flexibook.model.ComboItem;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.Owner;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse.flexibook.model.ServiceCombo;
import ca.mcgill.ecse.flexibook.model.TimeSlot;
import ca.mcgill.ecse.flexibook.model.User;
import ca.mcgill.ecse223.flexibook.controller.FlexiBookController;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;
import ca.mcgill.ecse.flexibook.model.Customer;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CucumberStepDefinitions {
	private FlexiBook flexiBook;
	private Owner owner;
	private Business business;
	private String error;
	int errorCntr;

	/**
	 * @author yasminamatta
	 */

	@Given("a Flexibook system exists")
	public void a_flexibook_system_exists() {
		flexiBook = FlexiBookApplication.getFlexiBook();
		error = "";
		errorCntr = 0;

	}

	/**
	 * @author yasminamatta
	 */

	@Given("an owner account exists in the system")
	public void an_owner_account_exists_in_the_system() {
		owner = flexiBook.getOwner();
		if (owner == null) {
			owner = new Owner("owner", "admin", flexiBook);
		}
	}

	/**
	 * @author yasminamatta
	 */
	@Given("a business exists in the system")
	public void a_business_exists_in_the_system() {
		business = flexiBook.getBusiness();
		if (business == null) {
			business = new Business("aName", "anAdress", "aPhoneNumber", "anEmailAdress", flexiBook);
		}

	}

	/**
	 * @author yasminamatta
	 * @param dataTable
	 */
	@Given("the following services exist in the system:")
	public void the_following_services_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);

		for (int i = 0; i < list.size(); i++) {
			flexiBook.addBookableService(
					new Service((list.get(i).get("name")), (flexiBook), (Integer.parseInt(list.get(i).get("duration"))),
							(Integer.parseInt(list.get(i).get("downtimeStart"))),
							(Integer.parseInt(list.get(i).get("downtimeDuration")))));
		}

	}

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Given("the Owner with username {string} is logged in")
	public void the_owner_with_username_is_logged_in(String string) {
		FlexiBookApplication.setCurrentUser(User.getWithUsername(string));

	}

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

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the number of service combos in the system shall be {string}")
	public void the_number_of_service_combos_in_the_system_shall_be(String string) {
		int noOfServiceCombo = 0;
		for (int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if (flexiBook.getBookableServices().get(i) instanceof ServiceCombo) {
				noOfServiceCombo++;
			}
		}
		assertEquals(noOfServiceCombo, Integer.parseInt(string));
	}

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

	/**
	 * @author yasminamatta
	 * @param string
	 */

	@Then("an error message with content {string} shall be raised")
	public void an_error_message_with_content_shall_be_raised(String string) {
		assertTrue(error.contains(string));
	}

	/**
	 * @author yasminamatta
	 * @param string
	 */
	@Then("the service combo {string} shall not exist in the system")
	public void the_service_combo_shall_not_exist_in_the_system(String string) {

		assertNull(ServiceCombo.getWithName(string));

	}

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

	/**
	 * @author yasminamatta
	 * @param dataTable
	 */

	@Given("the following customers exist in the system:")
	public void the_following_customers_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);

		for (int i = 0; i < list.size(); i++) {
			flexiBook.addCustomer(list.get(i).get("username"), list.get(i).get("password"));

		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 */

	@Given("Customer with username {string} is logged in")
	public void customer_with_username_is_logged_in(String string) {
		FlexiBookApplication.setCurrentUser(User.getWithUsername(string));

	}

	/**
	 * @author yasminamatta
	 * @param string
	 */

	@Given("the system's time and date is {string}")
	public void the_system_s_time_and_date_is(String string) {
		String datePart = string.substring(0, 10);
		String timePart = string.substring(11, 16);
//		Date date = Date.valueOf(datePart);
//		Time time = Time.valueOf(timePart + ":00");
		FlexiBookApplication.setSystemDate(datePart);
		FlexiBookApplication.setSystemTime(timePart+ ":00");
	}

	/**
	 * @author yasminamatta
	 * @param dataTable
	 */
	@Given("the following appointments exist in the system:")
	public void the_following_appointments_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> list = dataTable.asMaps(String.class, String.class);
		for (int i = 0; i < list.size(); i++) {

			String a = list.get(i).get("date");

			Date startDate = Date.valueOf(a);
			Time startTime = Time.valueOf(list.get(i).get("startTime") + ":00");
			Date endDate = Date.valueOf(list.get(i).get("date"));
			Time endTime = Time.valueOf(list.get(i).get("endTime") + ":00");
			TimeSlot timeSlot = new TimeSlot(startDate, startTime, endDate, endTime, flexiBook);

			flexiBook.addAppointment((Customer) Customer.getWithUsername(list.get(i).get("customer")),
					(BookableService) BookableService.getWithName(list.get(i).get("serviceName")), timeSlot);
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
			FlexiBookController.deleteServiceCombo(string, string2, flexiBook);
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
	@Then("the number of appointments in the system with service {string} shall be {string}")
	public void the_number_of_appointments_in_the_system_with_service_shall_be(String string, String string2) {
		if (Service.getWithName(string) != null) {
			assertEquals(Integer.parseInt(string2), Service.getWithName(string).numberOfAppointments());
		}
	}

	/**
	 * @author yasminamatta
	 * @param string
	 */

	@Then("the number of appointments in the system shall be {string}")
	public void the_number_of_appointments_in_the_system_shall_be(String string) {
		
		assertEquals(flexiBook.numberOfAppointments(), Integer.parseInt(string));
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

	/**
	 * @author yasminamatta
	 */
	@After
	public void tearDown() {
		flexiBook.delete();
	}

}
