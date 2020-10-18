package ca.mcgill.ecse223.flexibook.controller;


import java.sql.Date;
import java.sql.Time;
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
	
	
	
	public static void makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, Time startTime, Date startDate) throws InvalidInputException {
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		Customer customer = null;
		TimeSlot timeSlot = null;
		Time previousServiceEndTime = null;
		BookableService thisService = null;
		List<BookableService> serviceList = flexiBook.getBookableServices();
		
		if(mainServiceName == null || username == null || startTime == null || startDate == null) {
			throw new InvalidInputException("Service name, Customer username, start time or start date cannot be null");
		}
		
		//find the available service from the flexibook corresponding to the name
		thisService = findServiceByName(mainServiceName);
		//find the customer trying to book an appointment
		customer = findCustomerByName(username);
		
		//check if the chosen bookableService is a Service or a ServiceCombo
		if(thisService.getClass().equals(Service.class)){   //the bookableService is a Service
			
			Service mainService = (Service) thisService;
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
			
			//check if there are optional services
			if(optionalServiceNames != null) {
				Service optionalService;
				for(int i = 0; i < serviceList.size(); i++) {
					if(optionalServiceNames.contains(serviceList.get(i).getName())) {
						optionalService = (Service) serviceList.get(i);
						previousServiceEndTime = timeSlot.getEndTime();
						timeSlot = getTimeSlot(previousServiceEndTime, startDate, optionalService);
						ComboItem optionalComboItem = new ComboItem(false, optionalService, combo);
						appointment.addChosenItem(optionalComboItem);		
						break;
					}
				}
			}
			
			appointment.getTimeSlot().setEndTime(timeSlot.getEndTime());
			
		}
		
		//check if the appointment is within valid business hours
		
		if(checkDateAndTime(appointment) == false) {
			throw new InvalidInputException("Time slot for creating a new appointment is invalid");
		}
		try {
			flexiBook.addAppointment(appointment);
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
	
	
	
	public static void updateAppointment(String username, String serviceName, List<String> newItems, List<String> removedItems, Time newStartTime, Date newDate, Time oldStartTime, Date oldDate) throws InvalidInputException {

		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		ServiceCombo combo = null;
		Customer customer = null;
		BookableService thisService = null;
		List<BookableService> serviceList = flexiBook.getBookableServices();
		
		if(serviceName == null || username == null || oldStartTime == null || oldDate == null) {
			throw new InvalidInputException("Service name, Customer username, previous start time or previous start date cannot be null");
		}
		
		//find the service corresponding to the name
		thisService = findServiceByName(serviceName);
		//find the customer trying to update the appointment
		customer = findCustomerByName(username);
	
		//get the appointment being updated
		List<Appointment> appointmentList = customer.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if(oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime())) {
				appointment = thisAppointment;
				break;
			}
		}
		
		
		
		//update optional combo items
		if((newItems != null) || (removedItems != null)) {		//the bookableService of the appointment is a Service Combo
			combo = (ServiceCombo) thisService;
			List<ComboItem> itemList = appointment.getChosenItems();
			
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
		
		//update time slot if needed
		if(newStartTime != null && newDate != null) {
			flexiBook.removeTimeSlot(appointment.getTimeSlot());
			
			if(thisService.getClass().equals(Service.class)) {		//if bookableService is a Service
				
				if(newDate != null && newStartTime != null) {	
					TimeSlot newTimeSlot = getTimeSlot(newStartTime, newDate, (Service) thisService);
					appointment.setTimeSlot(newTimeSlot);
				}
				
			}
			else {			//if bookableService is a ServiceCombo
				
				TimeSlot newTimeSlot = null;
				Time previousServiceEndTime;
				
				if(newDate != null && newStartTime != null) {
					newTimeSlot = getTimeSlot(newStartTime, newDate, combo.getMainService().getService());
					appointment.setTimeSlot(newTimeSlot);

				}	
				
				//check if there are optional services
				List<ComboItem> comboItemList = combo.getServices();
				if(comboItemList != null) {
					Service optionalService;
					for(int i = 0; i < comboItemList.size(); i++) {
						optionalService = comboItemList.get(i).getService();
						previousServiceEndTime = newTimeSlot.getEndTime();
						newTimeSlot = getTimeSlot(previousServiceEndTime, newDate, optionalService);		
					}
				}
					
					appointment.getTimeSlot().setEndTime(newTimeSlot.getEndTime());
			}
		}
		
		
		
		
		if(checkDateAndTime(appointment) == false) {
			throw new InvalidInputException("Time slot for creating a new appointment is invalid.");
		}
		try {
			flexiBook.addAppointment(appointment);
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
		
		
	
		
	public static void cancelAppointment(String username, Time startTime, Date startDate) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		int initial = flexiBook.getAppointments().size();
		int Final;
		
		if(username == null || startTime == null || startDate == null) {
			throw new InvalidInputException("Customer username, start time or start date cannot be null");
		}
		
		List<Appointment> appointmentList = null;
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
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if(timeSlot.getStartDate().equals(thisAppointment.getTimeSlot().getStartDate()) && (timeSlot.getStartTime().equals(thisAppointment.getTimeSlot().getStartTime()))) {
				//remove the appointment
				customer.removeAppointment(thisAppointment);
				break;
			}
		}
		
		
		Final = flexiBook.getAppointments().size();
		if(Final != initial - 1) {
			throw new InvalidInputException("Failed to remove appointment because either username or time slot doesn't match");
		}
		
		
		
	}
	
	
	
	//private methods
	
	private static TimeSlot getTimeSlot(Time startTime, Date startDate, Service service) {
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Time endTime = null;
		
		
		//CALCULATE END_TIME
		endTime = new Time(((startTime.getTime() / (1000 * 60)) + service.getDuration()) * 1000 * 60);
		TimeSlot timeSlot1 = new TimeSlot(startDate, startTime, startDate, endTime, flexiBook);
		return timeSlot1;
		
	}
	
	
	
	
	private static boolean checkDateAndTime(Appointment appointment) {
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		TimeSlot timeSlot = appointment.getTimeSlot();
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
			if(!(startTime >= start && endTime <= end)) {
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
				
				if(appointment.getBookableService().getClass().equals(Service.class)) {			//the bookableService is a Service
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
			
		}
		
		return valid;
	}
	
	
	private static Customer findCustomerByName(String username) {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Customer thisCustomer;
		
		List<Customer> customerList = flexiBook.getCustomers();
		for(int i = 0; i < customerList.size(); i++) {
			thisCustomer = customerList.get(i);
			if(thisCustomer.getUsername().equals(username)) {
				return thisCustomer;
			}
		}
		return null;
	}
	
	private static BookableService findServiceByName(String serviceName) {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		BookableService thisService;
		
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
	

	
	
	
	

