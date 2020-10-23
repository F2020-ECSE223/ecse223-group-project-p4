package ca.mcgill.ecse.flexibook.controller;


import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;



public class FlexiBookController {

	public static Service service; 
	private static List<Appointment> appointments;


	
	/**
	 * Helper Method: Add/Update/Delete Service Feature
	 * @author Sneha Singh
	 * @param user
	 * @return boolean true if logged in User is owner
	 * @throws InvalidInputException
	 */
	private static boolean checkOwner(User user) throws InvalidInputException  {
		boolean userIsOwner = false; 
		
		if (user instanceof Owner) {
			userIsOwner = true; 
		}

		if (user instanceof Customer) {
			userIsOwner = false; 
			throw new InvalidInputException("You are not authorized to perform this operation");
		}
		return userIsOwner;
	}

	
	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh
	 * @param duration
	 * @return boolean true if duration is positive 
	 * @throws InvalidInputException
	 */
	private static boolean checkPositiveDuration(int duration) throws InvalidInputException {
		boolean durationIsPositive = true;
		if (duration <= 0) {
			durationIsPositive = false; 
			throw new InvalidInputException("Duration must be positive");
		}
		return durationIsPositive;
	}

	

	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh
	 * {@summary} checks for positive downtime duration
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @return boolean true if downtime duration is positive 
	 * @throws InvalidInputException
	 */
	private static boolean checkPositiveDowntimeDuration(int downtimeStart, int downtimeDuration) throws InvalidInputException {
		boolean downtimeDurationIsPositive = true; 
		if (downtimeDuration <= 0 && downtimeStart > 0) {
			downtimeDurationIsPositive = false;
			throw new InvalidInputException("Downtime duration must be positive");
		}
		return downtimeDurationIsPositive; 
	}

	
	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh 
	 * {@summary} Checks to make sure downtime duration is 0 if there is no downtime associated with service
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @return boolean true if downtime and downtime duration are consistent 
	 * @throws InvalidInputException
	 */
	private static boolean checkDowntimeDurationIsZero(int downtimeStart, int downtimeDuration) throws InvalidInputException {
		boolean downtimeDurationIsZero = true; 
		if ((downtimeStart == 0) && (downtimeDuration < 0)) {
			downtimeDurationIsZero = false;
			throw new InvalidInputException("Downtime duration must be 0");
		}

		return downtimeDurationIsZero;
	}


	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh
	 * {@summary} Checks to ensure that downtime makes sense 
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @param duration
	 * @return boolean true if downtime is valid (starts after and lasts for no longer than duration of service)
	 * @throws InvalidInputException
	 */
	private static boolean checkDowntimeStartsDuringService(int downtimeStart, int downtimeDuration, int duration) throws InvalidInputException {
		boolean downtimeStartsDuringService = true;

		if (downtimeStart < 0) {
			downtimeStartsDuringService = false;
			throw new InvalidInputException("Downtime must not start before the beginning of the service");
		}

		if (downtimeStart == 0 && downtimeDuration > 0) {
			downtimeStartsDuringService = false;
			throw new InvalidInputException("Downtime must not start at the beginning of the service");
		}

		if (downtimeStart > duration) {
			downtimeStartsDuringService = false;
			throw new InvalidInputException("Downtime must not start after the end of the service");
		}

		if ((downtimeStart + downtimeDuration > duration) && (downtimeStart < duration)){
			downtimeStartsDuringService = false;
			throw new InvalidInputException("Downtime must not end after the service");
		}


		return downtimeStartsDuringService;

	}
	
	
	
	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh
	 * {@summary} Consolidates all helper methods that check timing 
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @param duration
	 * @return boolean true if all timings make sense
	 * @throws InvalidInputException
	 */
	public static boolean timingsMakeSense(int downtimeStart, int downtimeDuration, int duration) throws InvalidInputException  {
		boolean timingsMakeSense = true; 
		
		try {
			checkPositiveDuration(duration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			//service.delete();
			throw e;
		}
		
		try {
			checkPositiveDowntimeDuration(downtimeStart, downtimeDuration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			//service.delete();
			throw e;
		}
		
		try {
			checkDowntimeDurationIsZero(downtimeStart, downtimeDuration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			//service.delete();
			throw e; 
		}
		
		try {
			checkDowntimeStartsDuringService(downtimeStart, downtimeDuration, duration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			//service.delete();
			throw e; 
			
		}
		return timingsMakeSense;
	}


	
	/**
	 * Helper Method: Add/Update Service
	 * @author Sneha Singh
	 * {@summary} checks to see if service with a specified name already exists within FlexiBook  
	 * @param aFlexiBook
	 * @param name
	 * @return boolean false if service doesn't exist  
	 * @throws InvalidInputException
	 */
	private static boolean serviceExistsAlready(FlexiBook aFlexiBook, String name)  throws InvalidInputException {
		boolean serviceExists = false;

		if (aFlexiBook.getBookableServices().contains(Service.getWithName(name))){
			serviceExists = true;
			throw new InvalidInputException("Service " +name+ " already exists");
		}

		return serviceExists;
	}


//****************************ADD SERVICE**********************************************

	/**
	 * AddService Feature Implementation 
	 * @author Sneha Singh
	 * {@summary} Adds service with specified parameters to FlexiBook if all criteria is satisfied 
	 * @param name
	 * @param flexiBook
	 * @param duration
	 * @param downtimeDuration
	 * @param downtimeStart
	 * @throws InvalidInputException
	 */
	public static void addService (String name, FlexiBook flexiBook, int duration, int downtimeDuration, int downtimeStart) throws InvalidInputException {
		
		if ((checkOwner(FlexiBookApplication.getCurrentUser()) == true) && (timingsMakeSense(downtimeStart, downtimeDuration, duration) == true) && (serviceExistsAlready(flexiBook, name) == false)) { 
			service = new Service(name, flexiBook, duration, downtimeDuration, downtimeStart);
		}
		
	}


//**************************UPDATE SERVICE***********************************************

	

	/**
	 * Helper Method: UpdateService 
	 * @author Sneha Singh
	 * {@summary} checks to make sure that the correct service is being updated
	 * @param serviceToUpdate
	 * @param newName
	 * @param flexiBook
	 * @return
	 * @throws InvalidInputException
	 */
	private static boolean updatingCorrectService (Service serviceToUpdate, String newName, FlexiBook flexiBook) throws InvalidInputException {
		boolean updatingCorrService = false; 
		
		
		if (flexiBook.getBookableServices().contains(serviceToUpdate)) {
			updatingCorrService = true; 
		}
	 
		//check to ensure updated service name isn't the same as the name of a pre-existing service
		if ((!(newName.equals(serviceToUpdate.getName()))) && flexiBook.getBookableServices().contains(Service.getWithName(newName))) { 
			throw new InvalidInputException("Service " +newName+ " already exists");
		}
		
		return updatingCorrService;
	}
	
	/**
	 * UpdateService Implementation
	 * @author Sneha Singh
	 * {@summary} updates parameters of an existing service provided all criteria is satisfied 
	 * @param serviceToUpdate
	 * @param newName
	 * @param flexiBook
	 * @param newDuration
	 * @param newDowntimeStart
	 * @param newDowntimeDuration
	 * @throws InvalidInputException
	 */
	public static void updateService(Service serviceToUpdate, String newName, FlexiBook flexiBook, int newDuration, int newDowntimeStart, int newDowntimeDuration) throws InvalidInputException {
		if ((checkOwner(FlexiBookApplication.getCurrentUser()) == true) && (timingsMakeSense(newDowntimeStart, newDowntimeDuration, newDuration) == true) && (updatingCorrectService(serviceToUpdate, newName, flexiBook)==true)) {
			serviceToUpdate.setName(newName);
			serviceToUpdate.setDuration(newDuration);
			serviceToUpdate.setDowntimeDuration(newDowntimeDuration);
			serviceToUpdate.setDowntimeStart(newDowntimeStart);
		}
	}

	
//*******************************DELETE SERVICE***********************************************	

	/**
	 * Helper Method: Delete Service
	 * @author Sneha Singh
	 * {@summary} checks to ensure no future appointments exist when attempting to delete a Service
	 * @param name
	 * @return boolean true if not future appointments for that service exist 
	 * @throws InvalidInputException
	 */
	private static boolean noFutureApptsExist(String name) throws InvalidInputException {
		boolean noFutureAppts = true; 
		Service serviceToDelete = (Service) Service.getWithName(name);
		
		
		if (serviceToDelete.hasAppointments() == true) {
			appointments = serviceToDelete.getAppointments();
				for (int i =0; i < appointments.size(); i++) {
					if (appointments.get(i).getTimeSlot().getStartDate().compareTo(FlexiBookApplication.getSystemDate()) > 0) {
						throw new InvalidInputException("The service contains future appointments");
					}
				}
		}
	
		return noFutureAppts; 
	}
	
	
	/**
	 * Helper Method: Delete Service
	 * @author Sneha Singh
	 * {@summary} removes Service from relevant ServiceCombos or deletes ServiceCombos when initiating Service deletion 
	 * @param name
	 * @param flexiBook
	 */
	private static void removeServiceFromCombos(String name, FlexiBook flexiBook) {
		
		List<BookableService> allServices = flexiBook.getBookableServices();
		ArrayList<ServiceCombo> serviceCombos = new ArrayList<ServiceCombo>();
		
		for (int i = 0; i < allServices.size(); i++) {
			if (allServices.get(i) instanceof ServiceCombo) {
				serviceCombos.add((ServiceCombo) allServices.get(i));
			}
		}
		
		for (int j = 0; j < serviceCombos.size(); j++) {
			if (serviceCombos.get(j).getMainService().getService().getName().equals(name)) {
				serviceCombos.get(j).delete();
			}
			
			for (int k = 0; k < serviceCombos.get(j).getServices().size(); k++) {
				if (serviceCombos.get(j).getServices().get(k).getService().getName().equals(name)) {
					serviceCombos.get(j).getServices().get(k).delete();
				}
			}
		}
	}

	
	/**
	 * DeleteService Implementation 
	 * @author Sneha Singh
	 * {@summary} Deletes a specified Service provided all criteria is satisfied 
	 * @param name
	 * @param flexiBook
	 * @throws InvalidInputException
	 */
	public static void deleteService (String name, FlexiBook flexiBook) throws InvalidInputException {
		Service serviceToDelete = (Service) Service.getWithName(name);
	
		if (checkOwner(FlexiBookApplication.getCurrentUser()) == true && noFutureApptsExist(name)) {
			
			removeServiceFromCombos(name, flexiBook);
			serviceToDelete.delete();
		}
	}
	
	
}



