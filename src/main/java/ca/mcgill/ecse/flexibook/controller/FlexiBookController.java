package ca.mcgill.ecse.flexibook.controller;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import ca.mcgill.ecse.flexibook.model.*;



public class FlexiBookController {

	public static Service service; 

	/** 
	 * Add/Update/Delete Service Feature - Helper Method
	 * Public method to check type of user logged in to system 
	 * @author Sneha Singh
	 * @param user 
	 * @return int for type of user 
	 */

	private static boolean checkUser(User user) throws InvalidInputException  {
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
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks for positive duration
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if duration is positive 
	 * @throws InvalidInputException InvalidInputException 
	 */
//	private static boolean checkPositiveDuration(Service service) throws InvalidInputException {
//		boolean durationIsPositive = true;
//		if (service.getDuration() <= 0) {
//			durationIsPositive = false; 
//			throw new InvalidInputException("Duration must be positive");
//		}
//		return durationIsPositive;
//	}
	
	private static boolean checkPositiveDuration(int duration) throws InvalidInputException {
		boolean durationIsPositive = true;
		if (duration <= 0) {
			durationIsPositive = false; 
			throw new InvalidInputException("Duration must be positive");
		}
		return durationIsPositive;
	}

	//, int newDowntime, int newDowntimeDuration)
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks for positive downtime duration
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime duration is positive 
	 * @throws InvalidInputException InvalidInputException 
	 */
//	private static boolean checkPositiveDowntimeDuration(Service service) throws InvalidInputException {
//		boolean downtimeDurationIsPositive = true; 
//		if (service.getDowntimeDuration() <= 0 && service.getDowntimeStart() > 0) {
//			downtimeDurationIsPositive = false;
//			throw new InvalidInputException("Downtime duration must be positive");
//		}
//		return downtimeDurationIsPositive; 
//	}

	private static boolean checkPositiveDowntimeDuration(int downtimeStart, int downtimeDuration) throws InvalidInputException {
		boolean downtimeDurationIsPositive = true; 
		if (downtimeDuration <= 0 && downtimeStart > 0) {
			downtimeDurationIsPositive = false;
			throw new InvalidInputException("Downtime duration must be positive");
		}
		return downtimeDurationIsPositive; 
	}

	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks to make sure downtime duration is 0 if there is no downtime associated w service
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime and downtime duration are consistnt 
	 * @throws InvalidInputException InvalidInputException 
	 */
//	public static boolean checkDowntimeDurationIsZero(Service service) throws InvalidInputException {
//		boolean downtimeDurationIsZero = true; 
//		if ((service.getDowntimeStart() == 0) && !(service.getDowntimeDuration() >= 0)) {
//			downtimeDurationIsZero = false;
//			throw new InvalidInputException("Downtime duration must be 0");
//		}
//
//		return downtimeDurationIsZero;
//	}

	
	public static boolean checkDowntimeDurationIsZero(int downtimeStart, int downtimeDuration) throws InvalidInputException {
		boolean downtimeDurationIsZero = true; 
		if ((downtimeStart == 0) && (downtimeDuration < 0)) {
			downtimeDurationIsZero = false;
			throw new InvalidInputException("Downtime duration must be 0");
		}

		return downtimeDurationIsZero;
	}
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks to ensure that downtime makes sense 
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime is valid (starts after and exists for no more than duration of service)
	 * @throws InvalidInputException InvalidInputException 
	 */
//	private static boolean checkDowntimeStartsDuringService(Service service) throws InvalidInputException {
//		boolean downtimeStartsDuringService = true;
//
//		if (service.getDowntimeStart() < 0) {
//			downtimeStartsDuringService = false;
//			throw new InvalidInputException("Downtime must not start before the beginning of the service");
//		}
//
//		if (service.getDowntimeStart() == 0 && service.getDowntimeDuration() > 0) {
//			downtimeStartsDuringService = false;
//			throw new InvalidInputException("Downtime must not start at the beginning of the service");
//		}
//
//		if (service.getDowntimeStart() > service.getDuration()) {
//			downtimeStartsDuringService = false;
//			throw new InvalidInputException("Downtime must not start after the end of the service");
//		}
//
//		if (service.getDowntimeStart() + service.getDowntimeDuration() > service.getDuration()) {
//			downtimeStartsDuringService = false;
//			throw new InvalidInputException("Downtime must not end after the service");
//		}
//
//
//		return downtimeStartsDuringService;
//
//	}

	
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
	 * Add/Update/Delete Service Feature - Helper Method
	 * Consolidates all above helper methods into one  
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if all timings make sense 
	 * @throws InvalidInputException InvalidInputException 
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
	 * Add/Update/Delete Service Feature - Helper Method 
	 * Checks to see if service with a specified name already exists within the FlexiBook 
	 * @author Sneha Singh
	 * @param aFlexiBook
	 * @param service
	 * @return boolean true if service already exists 
	 */
//	private static boolean serviceExistsAlready(FlexiBook aFlexiBook, String a name )  {
//		boolean serviceExists = false;
//		if (aFlexiBook.getBookableServices().contains(Service.getWithName(service.getName()))){
//			serviceExists = true;
//		}
//
//		return serviceExists;
//	}
	
		private static boolean serviceExistsAlready(FlexiBook aFlexiBook, String name)  throws InvalidInputException {
		boolean serviceExists = false;
		
		if (aFlexiBook.getBookableServices().contains(Service.getWithName(name))){
			serviceExists = true;
			throw new InvalidInputException("Service " +name+ " already exists");
		}
	
		return serviceExists;
	}

	//Add Service Begins


	/**
	 * AddService Feature Implementation 
	 * Adds service to FlexiBook if all criteria is satisfied 
	 * @author Sneha Singh
	 * @param aFlexiBook
	 * @param service
	 * @param user
	 * @throws InvalidInputException
	 */
//	public static void addService (FlexiBook aFlexiBook, Service service, User user) throws InvalidInputException {
//		//if (checkUser(user) != 0) {
//		//	throw new InvalidInputException("You are not authorized to perform this operation");
//		//}
//		
//		if ((checkUser(user) == true) && (timingsMakeSense(service) == true)) { //&& serviceExistsAlready(aFlexiBook, service) == false
//			aFlexiBook.addBookableService(service);
//		}
//		else {
//			aFlexiBook.removeBookableService(service);
//		}

//		if (serviceExistsAlready(aFlexiBook, service) == true) {
//			throw new InvalidInputException("Service " +service.getName()+ " already exists");
//		}

		

	//}	

	public static void addService (String name, FlexiBook flexiBook, int duration, int downtimeDuration, int downtimeStart, User user) throws InvalidInputException {
		//if (checkUser(user) != 0) {
		//	throw new InvalidInputException("You are not authorized to perform this operation");
		//}
		
		if ((checkUser(user) == true) && (timingsMakeSense(downtimeStart, downtimeDuration, duration) == true) && (serviceExistsAlready(flexiBook, name) == false)) { 
			service = new Service(name, flexiBook, duration, downtimeDuration, downtimeStart);
		}
		
	}
	// AddService Ends 

	//Update Service Begins

	/** 
	 * Update Service Feature - Helper Method 
	 * Checks to make sure that the only the selected service is being updated and that the new service does not have the same 
	 * name as a pre-existing service 
	 * @author Sneha Singh
	 * @param aFlexiBook
	 * @param serviceToUpdate
	 * @param newService
	 * @return boolean true if the new service coincides with the selected pre-existing service to update
	 * @throws InvalidInputException
	 */
//	private static boolean updatingCorrectService(FlexiBook aFlexiBook, Service serviceToUpdate, Service newService) throws InvalidInputException{
//
//		boolean updatingCorrService = false; 
//		//If service to be updated has same name as new service, test ignores the fact that the service already exists in the list of Bookable Services 
//		//Ex: updating service "colour" with a new service that has the same name but different duration/downtimeStart/downtimeDuration 
//		if (serviceToUpdate.getName().equalsIgnoreCase(newService.getName())) {
//			updatingCorrService = true; 
//		}
//
//		else {
//			//If the new service's name is different than that of the one being updated, and a service with that name exists already, then the service cannot be added 
//			//Ex: updating service "colour" to a new service "dry", when a service with name "dry" already exists
//			if (serviceExistsAlready(aFlexiBook, newService)) {
//				throw new InvalidInputException("Service " +newService.getName()+ " exists already");
//			}
//			else {
//				updatingCorrService = true; 
//			}
//
//		}
//
//		return updatingCorrService;
//
//	}	
	
	

	/**
	 * UpdateService Feature Implementation 
	 * Updates existing service provided all criteria is met 
	 * @param aFlexiBook
	 * @param serviceToUpdate
	 * @param newService
	 * @param user
	 * @throws InvalidInputException
	 */

//	public static void updateService (FlexiBook aFlexiBook, Service serviceToUpdate, Service newService, User user)	throws InvalidInputException {
//
//		if (checkUser(user) == true && timingsMakeSense(newService) == true && updatingCorrectService(aFlexiBook, serviceToUpdate, newService) == true) {
//			int index = aFlexiBook.indexOfBookableService(Service.getWithName(serviceToUpdate.getName()));
//			aFlexiBook.addOrMoveBookableServiceAt(newService, index);
//		}


		//if (checkUser(user) != 0) { 
		//	throw new InvalidInputException("You are not authorized to perform this operation");
		//}

	//}

	//UpdateService ends 

	public static boolean updatingCorrectService (Service serviceToUpdate, String newName, FlexiBook flexiBook) throws InvalidInputException {
		boolean updatingCorrService = false; 
		
		if (flexiBook.getBookableServices().contains(serviceToUpdate)) {
			updatingCorrService = true; 
		}
		
		if ((!(newName.equals(serviceToUpdate.getName()))) && flexiBook.getBookableServices().contains(Service.getWithName(newName))) { 
			throw new InvalidInputException("Service " +newName+ " already exists");
		}
		
		return updatingCorrService;
	}
	
	public static void updateService(Service serviceToUpdate, String newName, FlexiBook flexiBook, int newDuration, int newDowntimeStart, int newDowntimeDuration, User user) throws InvalidInputException {
		if ((checkUser(user) == true) && (timingsMakeSense(newDowntimeStart, newDowntimeDuration, newDuration) == true) && (updatingCorrectService(serviceToUpdate, newName, flexiBook)==true)) {
			serviceToUpdate.setName(newName);
			serviceToUpdate.setDuration(newDuration);
			serviceToUpdate.setDowntimeDuration(newDowntimeDuration);
			serviceToUpdate.setDowntimeStart(newDowntimeStart);
		}
	}




	//DeleteService Begins


	//Check if service exists in any combos 

	//Check if service exists in any upcoming appointments 


//		public void servicePartOfCombo (FlexiBook aFlexiBook, Service serviceToDelete)	{
//			//check all serviceCombos that have this as a service 
//			int i = 0;
//			List<BookableService> allServices = aFlexiBook.getBookableServices();
//			List<ServiceCombo> serviceCombosContainingService = new ArrayList<ServiceCombo>();
//			
//			
//			//List of all service combos
//			for (i=0 ; i< allServices.size() ; i++) {
//				if (allServices.get(i) instanceof ServiceCombo) {
//					serviceCombosContainingService.add((ServiceCombo) allServices.get(i));
//					}
//			}
//			
//			for (int j = 0; j < serviceCombosContainingService.size(); j++) {
//				if (serviceCombosContainingService.get(i).getMainService().equals(serviceToDelete)) {
//					aFlexiBook.removeBookableService(serviceCombosContainingService.get(i));
//				}
//			}	
//			
//			for (int k = 0; j < serviceCombosContainingService.size(); j++) {
//				if (serviceCombosContainingService.get(i).getServices().contains(serviceToDelete)) {
//					
//					serviceCombosContainingService.get(i).removeService(ComboItem.getService());
//				
//				}
//			}	
//			
//				aFlexiBook.removeBookableService(serviceToDelete);
//			}
//				
//		}
//				
//		public static void deleteService (FlexiBook aFlexiBook, Service serviceToUpdate, Service newService, User user)	throws InvalidInputException {
//				
//				if (checkUser(user) == 0 && timingsMakeSense(newService) == true && updatingCorrectService(aFlexiBook, serviceToUpdate, newService) == true) {
//					int index = aFlexiBook.indexOfBookableService(Service.getWithName(serviceToUpdate.getName()));
//					aFlexiBook.addOrMoveBookableServiceAt(newService, index);
//				}
//				
//			
//				if (checkUser(user) != 0) { 
//					throw new InvalidInputException("You are not authorized to perform this operation");
//				}
//				
//			}
	//			
	//		
	//		
	//		
	//		
	//		
	//		
	//		
	//		


}



















