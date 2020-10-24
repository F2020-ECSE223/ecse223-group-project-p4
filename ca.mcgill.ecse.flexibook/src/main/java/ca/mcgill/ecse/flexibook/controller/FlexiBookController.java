package ca.mcgill.ecse.flexibook.controller;


import java.sql.Date;
import java.sql.Time;
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
import java.time.LocalTime;
=======
import java.util.ArrayList;
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse223.flexibook.controller.InvalidInputException;



public class FlexiBookController {


//*************************** APPOINTMENT FEATURES****************************//
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
	public static void makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, String startTime, String startDate, FlexiBook flexiBook) throws InvalidInputException {
		
=======
	public static void makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, Time startTime, Date startDate) throws InvalidInputException {

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
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
=======
			thisService = findServiceByName(mainServiceName);
			//find the customer trying to book an appointment
			customer = findCustomerByName(username);

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
			//check if the chosen bookableService is a Service or a ServiceCombo
			
			
			if(thisService.getClass().equals(Service.class)){   //the bookableService is a Service

				Service mainService = (Service) thisService;
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
				timeSlot = getTimeSlot(startTime, startDate, mainService.getDuration(), flexiBook);
				
				
			}
			else if(thisService.getClass().equals(ServiceCombo.class)){			//the bookableService is a ServiceCombo
				
				
				ServiceCombo combo = (ServiceCombo) thisService;
				int duration = 0;
				duration += combo.getMainService().getService().getDuration();
				
=======
				timeSlot = getTimeSlot(startTime, startDate, mainService);
				if(customer != null && mainService != null) {
					appointment = new Appointment(customer, mainService, timeSlot, flexiBook);
				}

			}
			else {			//the bookableService is a ServiceCombo

				ServiceCombo combo = (ServiceCombo) thisService;

				timeSlot = getTimeSlot(startTime, startDate, combo.getMainService().getService());
				appointment = new Appointment(customer, combo, timeSlot, flexiBook);
				ComboItem mainComboItem = new ComboItem(true, (Service)thisService, combo);
				appointment.addChosenItem(mainComboItem);

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
				
				timeSlot = getTimeSlot(startTime, startDate, duration, flexiBook);
				
				
			}
			
			
			//check if the appointment is within valid business hours
			if(checkDateAndTime(timeSlot, flexiBook) == false) {
				throw new InvalidInputException("There are no available slots for " + mainServiceName + " on " + startDate.toString() + " at " + startTime.toString().substring(0,5) + ".");
=======

				appointment.getTimeSlot().setEndTime(timeSlot.getEndTime());

			}

			//check if the appointment is within valid business hours

			if(checkDateAndTime(appointment) == false) {
				throw new InvalidInputException("There are no available slots for " + mainServiceName + " on " + startDate.toString() + " at " + startTime.toString() + ".");
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
			List<Customer> customerList = flexiBook.getCustomers();
			for(int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if(customer.getUsername().equals(username)) {
					break;
				}
			}
		
=======
			customer = findCustomerByName(username);

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
				
				return "successful";
				
=======

				return "Successful";

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
			}



			//update time slot if needed
			if(newStartTime != null && newDate != null) {
				TimeSlot newTimeSlot = null;

				if(thisService.getClass().equals(Service.class)) {		//if bookableService is a Service
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
					
					Service mainService = (Service)thisService;
=======

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
					if(newDate != null && newStartTime != null) {	
						newTimeSlot = getTimeSlot(newStartTime, newDate, mainService.getDuration(), flexiBook);
						appointment.setTimeSlot(newTimeSlot);
					}

				}
				else {			//if bookableService is a ServiceCombo
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
					
					ServiceCombo thisCombo = (ServiceCombo)thisService;
					int duration = thisCombo.getMainService().getService().getDuration();	
					
=======

					Time previousServiceEndTime;

					if(newDate != null && newStartTime != null) {
						newTimeSlot = getTimeSlot(newStartTime, newDate, combo.getMainService().getService());
					}	

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
					//check if there are optional services
					List<ComboItem> comboItemList = thisCombo.getServices();
					Service optionalService;
					
					for(int i = 0; i < comboItemList.size(); i++) {
						optionalService = comboItemList.get(i).getService();
						duration += optionalService.getDuration();
					}
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
					
					newTimeSlot = getTimeSlot(newStartTime, newDate, duration, flexiBook);
					
				}
				
				
				if(checkDateAndTime(newTimeSlot, flexiBook) == false) {
					return "unsuccessful";
=======

					appointment.getTimeSlot().setEndTime(newTimeSlot.getEndTime());
				}


				if(checkDateAndTime(appointment) == false) {
					return "Unsuccessful";
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
				}
				else {
					appointment.setTimeSlot(newTimeSlot);
					return "successful";
				}
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
				
				
			}
			
			return "unsuccessful";
=======

			}

			return "Unsuccessful";
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
			
=======

			java.util.Date todaysDate = new java.util.Date();
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
			if(startDate.equals(todaysDate)) {
				throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
			}


			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot cancel an appointment");
			}
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
			
			
=======

			int initial = flexiBook.getAppointments().size();
			int Final;

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
			if(username == null || startTime == null || startDate == null) {
				throw new InvalidInputException("Customer username, start time or start date cannot be null");
			}

			List<Appointment> appointmentList = null;
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java

			appointmentList = flexiBook.getAppointments();
=======
			Customer customer = null;
			TimeSlot timeSlot = null;

			//find the customer trying to cancel an appointment
			customer = findCustomerByName(username);

			//find the time slot to be freed
			List<TimeSlot> timeSlots = flexiBook.getTimeSlots();
			for(int i = 0; i < timeSlots.size(); i++) {
				TimeSlot thisSlot = timeSlots.get(i);
				if(thisSlot.getStartDate().equals(startDate) && thisSlot.getStartTime().equals(startTime)) {
					timeSlot = thisSlot;
					break;
				}
			}

			flexiBook.removeTimeSlot(timeSlot);

			appointmentList = customer.getAppointments();
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(startDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (startTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
					appointment = thisAppointment;
					break;
				}
			}
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
			
			if(username.equals(appointment.getCustomer().getUsername())) {
				flexiBook.removeAppointment(appointment);
			}
			else{
				throw new InvalidInputException("A customer can only cancel their own appointments");
			}
			
			
			
			
			
=======

			if(!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("A customer can only update their own appointments");
			}

			flexiBook.removeAppointment(appointment);



>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
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
=======
	private static TimeSlot getTimeSlot(Time startTime, Date startDate, Service service) {

		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Time endTime = null;


		//CALCULATE END_TIME
		endTime = new Time(((startTime.getTime() / (1000 * 60)) + service.getDuration()) * 1000 * 60);
		TimeSlot timeSlot1 = new TimeSlot(startDate, startTime, startDate, endTime, flexiBook);
		return timeSlot1;

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
	}



	/**
	 * @author Shaswata Bhattacharyya
	 * @param appointment
	 * @return true if valid time slot, false otherwise
	 */
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
	private static boolean checkDateAndTime(TimeSlot timeSlot, FlexiBook flexiBook) {
		
=======
	private static boolean checkDateAndTime(Appointment appointment) {

		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		TimeSlot timeSlot = appointment.getTimeSlot();
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
				
				if(thisAppointment.getBookableService().getClass().equals(Service.class)) {			//the bookableService is a Service
=======

				if(appointment.getBookableService().getClass().equals(Service.class)) {			//the bookableService is a Service
>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
			else {
				valid = true;
			}
			
			
=======

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
		}

		return valid;
	}


	/**
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @return the customer corresponding to the username
	 */
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
	private static Customer findCustomerByName(String username, FlexiBook flexiBook) {
		Customer thisCustomer = null;
		
=======
	private static Customer findCustomerByName(String username) {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Customer thisCustomer;

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
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
<<<<<<< HEAD:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse/flexibook/controller/FlexiBookController.java
	private static BookableService findServiceByName(String serviceName, FlexiBook flexiBook) {
		BookableService thisService = null;
		
=======
	private static BookableService findServiceByName(String serviceName) {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		BookableService thisService;

>>>>>>> 108f0e3fe22ba62754f92bdcaef26a0351b6c275:ca.mcgill.ecse.flexibook/src/main/java/ca/mcgill/ecse223/flexibook/controller/FlexiBookController.java
		List<BookableService> serviceList = flexiBook.getBookableServices();
		for(int i = 0; i < serviceList.size(); i++) {
			thisService = serviceList.get(i);
			if(thisService.getName().equals(serviceName)) {
				return thisService;
			}
		}

		return null;
	}

	
	
	//************************************SERVICE FEATURES******************************

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



