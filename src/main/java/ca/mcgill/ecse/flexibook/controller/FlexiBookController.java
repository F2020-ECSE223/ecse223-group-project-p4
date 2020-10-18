package ca.mcgill.ecse.flexibook.controller;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


import ca.mcgill.ecse.flexibook.model.Appointment;
import ca.mcgill.ecse.flexibook.model.BookableService;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse.flexibook.model.Business;
import ca.mcgill.ecse.flexibook.model.BusinessHour;
import ca.mcgill.ecse.flexibook.model.ComboItem;
import ca.mcgill.ecse.flexibook.model.Customer;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Owner;
import ca.mcgill.ecse.flexibook.model.ServiceCombo;
import ca.mcgill.ecse.flexibook.model.TimeSlot;
import ca.mcgill.ecse.flexibook.model.User;


class FlexiBookController {

	 
	/** 
	 * Add/Update/Delete Service Feature - Helper Method
	 * Public method to check type of user logged in to system 
	 * @author Sneha Singh
	 * @param user 
	 * @return int for type of user 
	 */

	public static int checkUser(User user)  {
		if (user instanceof Owner) {
			return 0;
		}
		
		if (user instanceof Customer) {
			return 1;
		}
		
		else return 2;
	}
	
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks for positive duration
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if duration is positive 
	 * @throws Throwable IllegalArgumentException 
	 */
	public static boolean checkPositiveDuration(Service service) throws Throwable {
		if (service.getDuration() <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		else return true;
	}
	
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks for positive downtime duration
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime duration is positive 
	 * @throws Throwable IllegalArgumentException 
	 */
	public static boolean checkPositiveDowntimeDuration(Service service) throws Throwable {
		if (service.getDowntimeDuration() <= 0) {
			throw new IllegalArgumentException("Downtime duration must be positive");
		}
		else return true;
	}
	
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks to make sure downtime duration is 0 if there is no downtime associated w service
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime and downtime duration are consistnt 
	 * @throws Throwable IllegalArgumentException 
	 */
	public static boolean checkDowntimeDurationIsZero(Service service) throws Throwable {
		if ((service.getDowntimeStart() == 0) && (service.getDowntimeDuration() <= 0)) {
			throw new IllegalArgumentException("Downtime duration must be 0");
		}
		else return true;
	}
	
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Checks to ensure that downtime makes sense 
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if downtime is valid (starts after and exists for no more than duration of service)
	 * @throws Throwable IllegalArgumentException 
	 */
	public static boolean checkDowntimeStartsDuringService(Service service) throws Throwable {
		if (service.getDowntimeStart() < 0) {
			throw new IllegalArgumentException("Downtime must not start before the beginning of the service");
		}
		
		if (service.getDowntimeStart() == 0) {
			throw new IllegalArgumentException("Downtime must not start at the beginning of the service");
		}
		
		if (service.getDowntimeStart() + service.getDowntimeDuration() < service.getDuration()) {
			throw new IllegalArgumentException("Downtime must not end after the service");
		}
		
		if (service.getDowntimeStart() > service.getDuration()) {
			throw new IllegalArgumentException("Downtime must not start after the end of the service");
		}
		else return true;
	
	}
	
	/**
	 * Add/Update/Delete Service Feature - Helper Method
	 * Consolidates all above helper methods into one  
	 * @author Sneha Singh
	 * @param service
	 * @return boolean true if all timings make sense 
	 * @throws Throwable IllegalArgumentException 
	 */
	public static boolean timingsMakeSense(Service service) throws Throwable {
		if (checkPositiveDuration(service) == true && checkPositiveDowntimeDuration(service) == true && checkDowntimeDurationIsZero(service) == true && checkDowntimeStartsDuringService(service) == true) {
			return true;
		}
		else return false;
	}
	

	/**
	 * Add/Update/Delete Service Feature - Helper Method 
	 * Checks to see if service with a specified name already exists within the FlexiBook 
	 * @author Sneha Singh
	 * @param aFlexiBook
	 * @param service
	 * @return boolean true if service already exists 
	 */
	public static boolean serviceExistsAlready(FlexiBook aFlexiBook, Service service)  {
		boolean serviceExists = false;
		if (aFlexiBook.getBookableServices().contains(Service.getWithName(service.getName()))){
			serviceExists = true;
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
	 * @throws Throwable
	 */
	public static void addService (FlexiBook aFlexiBook, Service service, User user) throws Throwable {
		if (checkUser(user) == 0 && timingsMakeSense(service) == true && serviceExistsAlready(aFlexiBook, service) == false) {
			aFlexiBook.addBookableService(service);
		}
		
		if (serviceExistsAlready(aFlexiBook, service) == true) {
			throw new IllegalArgumentException("Service " +service.getName()+ " already exists");
		}
		
		
		if (checkUser(user) != 0) {
			throw new IllegalArgumentException("You are not authorized to perform this operation");
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
	 * @throws Throwable
	 */
	public static boolean updatingCorrectService(FlexiBook aFlexiBook, Service serviceToUpdate, Service newService) throws Throwable{
		
		boolean updatingCorrService = false; 
		//If service to be updated has same name as new service, test ignores the fact that the service already exists in the list of Bookable Services 
		//Ex: updating service "colour" with a new service that has the same name but different duration/downtimeStart/downtimeDuration 
		if (serviceToUpdate.getName().equalsIgnoreCase(newService.getName())) {
			updatingCorrService = true; 
		}
		
		else {
			//If the new service's name is different than that of the one being updated, and a service with that name exists already, then the service cannot be added 
			//Ex: updating service "colour" to a new service "dry", when a service with name "dry" already exists
			if (serviceExistsAlready(aFlexiBook, newService)) {
				throw new IllegalArgumentException("Service " +newService.getName()+ " exists already");
			}
			else {
				updatingCorrService = true; 
			}
				
		}
		
		return updatingCorrService;
	
	}	
	
	/**
	 * UpdateService Feature Implementation 
	 * Updates existing service provided all criteria is met 
	 * @param aFlexiBook
	 * @param serviceToUpdate
	 * @param newService
	 * @param user
	 * @throws Throwable
	 */
	
	public static void updateService (FlexiBook aFlexiBook, Service serviceToUpdate, Service newService, User user)	throws Throwable {
		
		if (checkUser(user) == 0 && timingsMakeSense(newService) == true && updatingCorrectService(aFlexiBook, serviceToUpdate, newService) == true) {
			int index = aFlexiBook.indexOfBookableService(Service.getWithName(serviceToUpdate.getName()));
			aFlexiBook.addOrMoveBookableServiceAt(newService, index);
		}
		
	
		if (checkUser(user) != 0) { 
			throw new IllegalArgumentException("You are not authorized to perform this operation");
		}
		
	}
		
	//UpdateService ends 
	
	
	//DeleteService Begins
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
	
	



