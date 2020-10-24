package ca.mcgill.ecse.flexibook.controller;


import java.util.ArrayList;


import ca.mcgill.ecse.flexibook.model.BookableService;
import ca.mcgill.ecse.flexibook.model.ComboItem;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse.flexibook.model.ServiceCombo;

public class FlexiBookController {

	/**
	 * Define Service Combo method : It lets the user define a new service combo in
	 * the system
	 * 
	 * @author yasminamatta
	 * @param aName
	 * @param user
	 * @param aFlexiBook
	 * @param aMainService
	 * @param mandatory
	 * @param allServices
	 * @throws InvalidInputException
	 */

	public static void defineServiceCombo(String aName, String user, FlexiBook aFlexiBook, String aMainService,
			String mandatory, String allServices) throws InvalidInputException {

		Service service;
		ComboItem comboItem;
		boolean mandatoryResult;

		if (!user.equalsIgnoreCase(aFlexiBook.getOwner().getUsername())) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}

		if (findServiceComboByName(aName, aFlexiBook) != null) {
			throw new InvalidInputException("Service combo " + aName + " already exists");

		}

		String[] mandatoryArray = mandatory.split(",");

		String[] allServicesArray = allServices.split(",");

		if (findServiceByName(aMainService, aFlexiBook) == null) {
			throw new InvalidInputException("Service " + aMainService + " does not exist");
		}

		if (!(allServices.contains(aMainService))) {
			throw new InvalidInputException("Main service must be included in the services");
		}

		for (int j = 0; j < allServicesArray.length; j++) {

			if (allServicesArray[j].equals(aMainService)) {

				if (!(Boolean.parseBoolean(mandatoryArray[j]))) {
					throw new InvalidInputException("Main service must be mandatory");
				}
			}

		}

		if (allServicesArray.length < 2) {
			throw new InvalidInputException("A service Combo must contain at least 2 services");

		}

		for (int i = 0; i < allServicesArray.length; i++) {
			if (!BookableService.hasWithName(allServicesArray[i])) {
				throw new InvalidInputException("Service " + allServicesArray[i] + " does not exist");
			}
		}
		ServiceCombo serviceCombo = new ServiceCombo(aName, aFlexiBook);
		for (int i = 0; i < allServicesArray.length; i++) {
			service = findServiceByName(allServicesArray[i], aFlexiBook);
			mandatoryResult = Boolean.valueOf(mandatoryArray[i]);
			comboItem = new ComboItem(mandatoryResult, service, serviceCombo);
			if (comboItem.getService().getName().equals(aMainService)) {
				serviceCombo.setMainService(comboItem);
			}
			serviceCombo.addService(comboItem);
			
		}

	}

	/**
	 * Update Service Combo method: it lets the owner update an existing service
	 * combo in the system
	 * 
	 * @author yasminamatta
	 * @param oldServiceCombo
	 * @param newName
	 * @param user
	 * @param aFlexiBook
	 * @param aMainService
	 * @param mandatory
	 * @param allServices
	 * @throws InvalidInputException
	 */

	public static void updateServiceCombo(String oldServiceCombo, String newName, String user, FlexiBook aFlexiBook,
			String aMainService, String mandatory, String allServices) throws InvalidInputException {

		Service service;
		ComboItem comboItem;
		boolean mandatoryResult;

		String[] mandatoryArray = mandatory.split(",");

		String[] allServicesArray = allServices.split(",");

		if (!user.equals(aFlexiBook.getOwner().getUsername())) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		if (findServiceComboByName(newName, aFlexiBook) != null && !newName.equals(oldServiceCombo)) {
			throw new InvalidInputException("Service combo " + newName + " already exists");
		}

		if (findServiceByName(aMainService, aFlexiBook) == null) {
			throw new InvalidInputException("Service " + aMainService + " does not exist");
		}

		if (!(allServices.contains(aMainService))) {
			throw new InvalidInputException("Main service must be included in the services");
		}

		for (int j = 0; j < allServicesArray.length; j++) {

			if (allServicesArray[j].equals(aMainService)) {

				if (!(Boolean.parseBoolean(mandatoryArray[j]))) {
					throw new InvalidInputException("Main service must be mandatory");
				}
			}

		}
		for (int i = 0; i < allServicesArray.length; i++) {
			if (!BookableService.hasWithName(allServicesArray[i])) {
				throw new InvalidInputException("Service " + allServicesArray[i] + " does not exist");
			}
		}

		if (allServicesArray.length < 2) {
			throw new InvalidInputException("A service Combo must have at least 2 services");

		}

		ServiceCombo serviceCombo = findServiceComboByName(oldServiceCombo, aFlexiBook);

		ArrayList<ComboItem> storedItems = new ArrayList<>();
		for (int i = 0; i < serviceCombo.getServices().size(); i++) {

			storedItems.add(serviceCombo.getServices().get(i));
		}

		serviceCombo.setName(newName);
		for (int i = 0; i < allServicesArray.length; i++) {
			service = findServiceByName(allServicesArray[i], aFlexiBook);
			mandatoryResult = Boolean.valueOf(mandatoryArray[i]);
			comboItem = new ComboItem(mandatoryResult, service, serviceCombo);

			serviceCombo.addService(comboItem);
			if (comboItem.getService().getName().equals(aMainService)) {
				serviceCombo.setMainService(comboItem);

			}
		}

		for (int i = 0; i < storedItems.size(); i++) {
			storedItems.get(i).delete();

		}

	}

	/**
	 * Delete Service Combo method: it lets the owner delete an existing service
	 * combo in the system
	 * 
	 * @author yasminamatta
	 * @param user
	 * @param serviceName
	 * @param aFlexiBook
	 * @throws InvalidInputException
	 */

	public static void deleteServiceCombo(String user, String serviceName, FlexiBook aFlexiBook)
			throws InvalidInputException {

		if (!user.equals(aFlexiBook.getOwner().getUsername())) {
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		BookableService deleteService = findServiceComboByName(serviceName, aFlexiBook);
		if (deleteService.hasAppointments()) {
			for (int i = 0; i < deleteService.getAppointments().size(); i++) {
				if (deleteService.getAppointments().get(i).getTimeSlot().getStartDate()
						.compareTo(FlexiBookApplication.getSystemDate()) > 0) {
					throw new InvalidInputException("Service combo " + serviceName + " has future appointments");
				}
			}

		}

		if (deleteService != null) {
			deleteService.delete();

		}
	}

	/**
	 * Helper method: It finds the service combo in the specified system using its
	 * name
	 * 
	 * @author yasminamatta
	 * @param name
	 * @param aFlexiBook
	 * @return the service combo with the given name
	 */

	private static ServiceCombo findServiceComboByName(String name, FlexiBook aFlexiBook) {
		BookableService specificService = null;
		for (BookableService service : aFlexiBook.getBookableServices()) {
			if (service instanceof ServiceCombo) {
				if (service.getName().equals(name)) {
					specificService = service;

				}
			}
		}
		return (ServiceCombo) specificService;
	}

	/**
	 * Helper method: It finds the service in the specified system using its name
	 * 
	 * @author yasminamatta
	 * @param name
	 * @param aFlexiBook
	 * @return the service with the given name
	 */

	private static Service findServiceByName(String name, FlexiBook aFlexiBook) {
		BookableService specificService = null;
		for (BookableService service : aFlexiBook.getBookableServices()) {
			if (service instanceof Service) {
				if (service.getName().equals(name)) {
					specificService = service;

				}
			}
		}
		return (Service) specificService;
	}

}
