package ca.mcgill.ecse.flexibook.controller;


import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.Appointment;
import ca.mcgill.ecse.flexibook.model.BookableService;
import ca.mcgill.ecse.flexibook.model.Business;
import ca.mcgill.ecse.flexibook.model.ComboItem;
import ca.mcgill.ecse.flexibook.model.BusinessHour;
import ca.mcgill.ecse.flexibook.model.Customer;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Owner;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse.flexibook.model.TimeSlot;
import ca.mcgill.ecse.flexibook.model.ServiceCombo;
import ca.mcgill.ecse.flexibook.model.User;





public class FlexiBookController {
	
	public FlexiBookController() {}
	
	
	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username (The username of the customer)
	 * @param mainServiceName (The chosen bookable service)
	 * @param optionalServiceNames (List of optional services, if any)
	 * @param startTime (Selected start time for the appointment)
	 * @param startDate (Selected date for the appointment)
	 * @throws InvalidInputException
	 * 
	 */
	public static void makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, String startTime, String startDate, FlexiBook flexiBook) throws InvalidInputException {
		
		try {
			
		
			Appointment appointment;
			Customer customer = null;
			TimeSlot timeSlot = null;
			List<BookableService> serviceList = flexiBook.getBookableServices();
			
			if(mainServiceName == null || username == null || startTime == null || startDate == null) {
				throw new InvalidInputException("Service name, Customer username, start time or start date cannot be null");
			}
			
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot make an appointment");
			}
			
			//find the available service from the flexibook corresponding to the name
			BookableService thisService = null; 
			
			for(int i = 0; i < serviceList.size(); i++) {
				thisService = serviceList.get(i);
				if(thisService.getName().equals(mainServiceName)) {
					break;
				}
			}
			
			
			//find the customer trying to book an appointment
			List<Customer> customerList = flexiBook.getCustomers();
			for(int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if(customer.getUsername().equals(username)) {
					break;
				}
			}
			//check if the chosen bookableService is a Service or a ServiceCombo
			
			
			if(thisService.getClass().equals(Service.class)){   //the bookableService is a Service
				
				Service mainService = (Service) thisService;
				timeSlot = getTimeSlot(startTime, startDate, mainService.getDuration(), flexiBook);
				
				
			}
			else if(thisService.getClass().equals(ServiceCombo.class)){			//the bookableService is a ServiceCombo
				
				
				ServiceCombo combo = (ServiceCombo) thisService;
				int duration = 0;
				duration += combo.getMainService().getService().getDuration();
				
				//check if there are optional services
				if(optionalServiceNames != null) {
					Service optionalService;
					for(int i = 0; i < serviceList.size(); i++) {
						if(optionalServiceNames.contains(serviceList.get(i).getName())) {
			
							optionalService = (Service) serviceList.get(i);
							duration += optionalService.getDuration();
							ComboItem optionalComboItem = new ComboItem(false, optionalService, combo);
							combo.addService(optionalComboItem);		
							
						}
					}
					
				}
				
				timeSlot = getTimeSlot(startTime, startDate, duration, flexiBook);
				
				
			}
			
			
			//check if the appointment is within valid business hours
			if(checkDateAndTime(timeSlot, flexiBook) == false) {
				throw new InvalidInputException("There are no available slots for " + mainServiceName + " on " + startDate.toString() + " at " + startTime.toString().substring(0,5) + ".");
			}
			else {
				appointment = new Appointment(customer, thisService, timeSlot, flexiBook);
			}
			
			
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
	
	
	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username (The username of the customer)
	 * @param serviceName (The name of the bookable service of the appointment)
	 * @param newItems (New optional services to be added, if any)
	 * @param removedItems (Optional services to be removed, if any)
	 * @param newStartTime (string representing new start time of appointment, if any)
	 * @param newDate (string representing new start date of appointment, if any)
	 * @param oldStartTime (Original start time of appointment)
	 * @param oldDate (Original start date of appointment)
	 * @return a string indicating whether update was successful/unsuccessful
	 * @throws InvalidInputException
	 * 
	 */
	public static String updateAppointment(String username, String serviceName, List<String> newItems, List<String> removedItems, String newStartTime, String newDate, Time oldStartTime, Date oldDate, FlexiBook flexiBook) throws InvalidInputException {

		ServiceCombo combo = null;
		Appointment appointment = null;
		Customer customer = null;
		BookableService thisService = null;
		List<BookableService> serviceList = flexiBook.getBookableServices();
		
		try {
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
			}
			
			if(serviceName == null || username == null || oldStartTime == null || oldDate == null) {
				throw new InvalidInputException("Service name, Customer username, previous start time or previous start date cannot be null");
			}
			
			//find the service corresponding to the name
			for(int i = 0; i < serviceList.size(); i++) {
				thisService = serviceList.get(i);
				if(thisService.getName().equals(serviceName)) {
					break;
				}
			}
			
			//find the customer trying to update the appointment
			List<Customer> customerList = flexiBook.getCustomers();
			for(int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if(customer.getUsername().equals(username)) {
					break;
				}
			}
		
			//get the appointment being updated
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime()) && serviceName.equals(thisAppointment.getBookableService().getName())) {
					appointment = thisAppointment;
					break;
				}
			}
			
			if(!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("Error: A customer can only update their own appointments");
			}
				
			
			
			//update optional combo items
			if((newItems != null) || (removedItems != null)) {		//the bookableService of the appointment is a Service Combo
				
				combo = (ServiceCombo) thisService;
				List<ComboItem> itemList = appointment.getChosenItems();
				
				if(newItems != null) {
					//add new optional items
					Service newService;
					for(int i = 0; i < serviceList.size(); i++) {
						newService = (Service) serviceList.get(i);
						if(newItems.contains(newService.getName())) {
							ComboItem newComboItem = new ComboItem(false, newService, combo);
							appointment.addChosenItem(newComboItem);
						}
					}
				}
				else if(removedItems != null) {
					//remove unwanted items
					for(int i = 0; i < removedItems.size(); i++) {		
						for(int j = 0; j < itemList.size(); j++) {		//iterate through the list of previously chosen items for this appointment
							ComboItem thisItem = itemList.get(j);
							if(thisItem.getService().getName().equals(removedItems.get(i))) {
								if(thisItem.getMandatory() == false) {		//ensure it is an optional service
									appointment.removeChosenItem(thisItem);		//remove
								}	
							}
						}
					}
				}
				
				return "successful";
				
			}
			
			
			
			//update time slot if needed
			if(newStartTime != null && newDate != null) {
				TimeSlot newTimeSlot = null;
				
				if(thisService.getClass().equals(Service.class)) {		//if bookableService is a Service
					
					Service mainService = (Service)thisService;
					if(newDate != null && newStartTime != null) {	
						newTimeSlot = getTimeSlot(newStartTime, newDate, mainService.getDuration(), flexiBook);
						appointment.setTimeSlot(newTimeSlot);
					}
					
				}
				else {			//if bookableService is a ServiceCombo
					
					ServiceCombo thisCombo = (ServiceCombo)thisService;
					int duration = thisCombo.getMainService().getService().getDuration();	
					
					//check if there are optional services
					List<ComboItem> comboItemList = thisCombo.getServices();
					Service optionalService;
					
					for(int i = 0; i < comboItemList.size(); i++) {
						optionalService = comboItemList.get(i).getService();
						duration += optionalService.getDuration();
					}
					
					newTimeSlot = getTimeSlot(newStartTime, newDate, duration, flexiBook);
					
				}
				
				
				if(checkDateAndTime(newTimeSlot, flexiBook) == false) {
					return "unsuccessful";
				}
				else {
					appointment.setTimeSlot(newTimeSlot);
					return "successful";
				}
				
				
			}
			
			return "unsuccessful";
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
		
		
	
	/**
	 *
	 * @author Shaswata Bhattacharyya	
	 * @param username (The username of the customer trying to cancel the appointment)
	 * @param startTime (The start time of the appointment to be cancelled)
	 * @param startDate (The date of the appointment to be cancelled)
	 * @throws InvalidInputException
	 * 
	 */
	public static void cancelAppointment(String username, Time startTime, Date startDate, Date todaysDate, FlexiBook flexiBook) throws InvalidInputException {
		//FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		
		try {
			
			if(startDate.equals(todaysDate)) {
				throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
			}
			
			
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot cancel an appointment");
			}
			
			
			if(username == null || startTime == null || startDate == null) {
				throw new InvalidInputException("Customer username, start time or start date cannot be null");
			}
			
			List<Appointment> appointmentList = null;

			appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(startDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (startTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
					appointment = thisAppointment;
					break;
				}
			}
			
			if(username.equals(appointment.getCustomer().getUsername())) {
				flexiBook.removeAppointment(appointment);
			}
			else{
				throw new InvalidInputException("A customer can only cancel their own appointments");
			}
			
			
			
			
			
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
	}
	
	
	
	//private methods
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param startTime
	 * @param startDate
	 * @param service
	 * @return time slot created using the start date, start time, end date and end time
	 */
	private static TimeSlot getTimeSlot(String startTime, String startDate, int duration, FlexiBook flexiBook) {
		
		String[] num = startTime.split(":");
		Integer hour = Integer.parseInt(num[0]);
		Integer min = Integer.parseInt(num[1]);
		LocalTime etime = LocalTime.of(hour, min);
		LocalTime localEndTime = etime.plusHours(0).plusMinutes(duration);
		Time EndTime = Time.valueOf(localEndTime);
		Time StartTime = Time.valueOf(startTime + ":00");
		Date ADate = Date.valueOf(startDate);
		TimeSlot timeSlot = new TimeSlot(ADate, StartTime, ADate, EndTime, flexiBook);
		
		return timeSlot;
	}
	
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param appointment
	 * @return true if valid time slot, false otherwise
	 */
	private static boolean checkDateAndTime(TimeSlot timeSlot, FlexiBook flexiBook) {
		
		List<Appointment> existingAppointments = flexiBook.getAppointments();
		
		long startTime = timeSlot.getStartTime().getTime();
		long endTime = timeSlot.getEndTime().getTime();

		
		//check if time slot not in holiday or vacation
		if(flexiBook.getBusiness().getHolidays().contains(timeSlot) || flexiBook.getBusiness().getVacation().contains(timeSlot)) {
			return false;
		}
		
		//check time slot within business hours
	
		for(int i = 0; i < flexiBook.getBusiness().getBusinessHours().size(); i++) {
			BusinessHour thisHour = flexiBook.getBusiness().getBusinessHours().get(i);
			long start = thisHour.getStartTime().getTime();
			long end = thisHour.getStartTime().getTime();
			if(!(startTime <= start || endTime >= end)) {
				return false;
			}
		}
		
		//if appointment within downtime of another
		Appointment thisAppointment;
		Service thisService;
		boolean valid = true;
		long stime;
		
		for(int i = 0; i < existingAppointments.size(); i++) {
			thisAppointment = existingAppointments.get(i);
			stime = thisAppointment.getTimeSlot().getStartTime().getTime();  		//in ms
			
			if(timeSlot.getStartTime().getTime() > stime && timeSlot.getEndTime().getTime() < thisAppointment.getTimeSlot().getEndTime().getTime()) {		//if overlap with another appointment
				
				if(thisAppointment.getBookableService().getClass().equals(Service.class)) {			//the bookableService is a Service
					thisService = (Service)thisAppointment.getBookableService();
					if(timeSlot.getStartTime().getTime() > (stime + (thisService.getDowntimeStart()*60000)) && timeSlot.getEndTime().getTime() < (stime + (thisService.getDowntimeStart() + thisService.getDowntimeDuration())*60000)){
						return true;
					}
					else {
						return false;
					}
				}
				else {				//the bookableService is a ServiceCombo
					
					for(int j = 0; j < thisAppointment.getChosenItems().size(); j++) {
						thisService = thisAppointment.getChosenItems().get(i).getService();
						if(timeSlot.getStartTime().getTime() > (stime + (thisService.getDowntimeStart()*60000)) && timeSlot.getEndTime().getTime() < (stime + (thisService.getDowntimeStart() + thisService.getDowntimeDuration())*60000 )){
							return true;		//appointment during the downtime of a service
						}
						else {
							stime += thisService.getDuration() * 60000;
							valid = false;		//appointment overlapping with a service
						}
					}		
						
				}
				
			}
			else {
				valid = true;
			}
			
			
		}
		
		return valid;
	}
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @return the customer corresponding to the username
	 */
	private static Customer findCustomerByName(String username, FlexiBook flexiBook) {
		Customer thisCustomer = null;
		
		List<Customer> customerList = flexiBook.getCustomers();
		for(int i = 0; i < customerList.size(); i++) {
			thisCustomer = customerList.get(i);
			if(thisCustomer.getUsername().equals(username)) {
				return thisCustomer;
			}
		}
		return thisCustomer;
	}
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @param serviceName
	 * @return the bookable service corresponding to the service name
	 */
	private static BookableService findServiceByName(String serviceName, FlexiBook flexiBook) {
		BookableService thisService = null;
		
		List<BookableService> serviceList = flexiBook.getBookableServices();
		for(int i = 0; i < serviceList.size(); i++) {
			thisService = serviceList.get(i);
			if(thisService.getName().equals(serviceName)) {
				return thisService;
			}
		}
		
		return null;
	}
	
	}
	

	
	
	
	

