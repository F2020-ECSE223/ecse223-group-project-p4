package ca.mcgill.ecse223.flexibook.controller;


import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.Appointment.AppointmentStatus;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;


public class FlexiBookController {
	
	public FlexiBookController() {}
	
	
	
	

	/**
	 * @author Shaswata Bhattacharyya
	 * @param customerName
	 * @param start
	 * @param customerShowedUp
	 * @param startTime
	 * @param date
	 * @param SystemDate
	 * @param SystemTime
	 * @param flexiBook
	 * @throws InvalidInputException
	 * Appointment state set from "Booked" to "In-Progress"
	 * Start appointment called even if customer does not show up. In case customer does not show up, this method calls endAppointment.
	 * 
	 */
	public static void startAppointment(String customerName, String startTime, String date, Date SystemDate, Time SystemTime, FlexiBook flexiBook) throws InvalidInputException {
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(date);
		Time appointmentTime = Time.valueOf(startTime);
		
		
		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if(appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
				appointment = thisAppointment;
				break;
			}
		}
		
		if(!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
			return;
		}
		
		if(!SystemDate.equals(appointmentDate) || SystemTime.before(appointmentTime)) {
			return;			//appointment reamins booked
		}
		
		try {
			appointment.startAppointment(SystemDate, SystemTime);
		} catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}	
			
			
	}
	
	/**
	 * @author yasminamatta
	 * @param dateTime
	 * @param appointment
	 * @param flexiBook
	 */
	public static void registerNoShow(String dateTime, Appointment appointment) {
		String datePart = dateTime.substring(0, 10);
		String timePart = dateTime.substring(11, 16);
		Date date = Date.valueOf(datePart);
		Time time = Time.valueOf(timePart + ":00");
		
		if(!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
			return;
		}
		if(appointment.getTimeSlot().getStartDate().compareTo(date)>0 && appointment.getTimeSlot().getStartTime().compareTo(time)>0) {
			return;
		}
		appointment.noShow(appointment.getCustomer());
		
	}
	
	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param customerName
	 * @param startTime
	 * @param date
	 * @param flexiBook
	 * @throws InvalidInputException
	 * Appointment state set to "End"
	 * Called when owner ends an appointment. Also called by startAppointment if customer does not show up. 
	 * 
	 */
	public static void endAppointment(String customerName, String startTime, String date, Date todaysDate, Time currentTime, FlexiBook flexiBook) throws InvalidInputException {
		
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(date);
		Time appointmentTime = Time.valueOf(startTime);
		
		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if(appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
				appointment = thisAppointment;
				break;
			}
		}
		
		try {
			//someone else attempts to end the appointment
			if(!FlexiBookApplication.getCurrentUser().getUsername().equals(flexiBook.getOwner().getUsername())) {
				throw new InvalidInputException("Error: Only the owner can end the appointment");
			}
			//owner attempts to end appointment before appointment starts
			if(todaysDate.before(appointmentDate) || (todaysDate.equals(appointmentDate) && currentTime.before(appointmentTime))) {
				return;
			}
			appointment.finishAppointment();
		} catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
	
	
	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param service
	 * @param newService
	 * @param optionalServices
	 * @param startTime
	 * @param date
	 * @param todaysDate
	 * @param flexiBook
	 * @throws InvalidInputException
	 * No change in appointment state(Appointment initially booked. Then it gets cancelled and booked again.)
	 * The only thing that changes for the appointment is the bookableService. No change in date and time.
	 * 
	 */
	public static Appointment cancelAndBookNewService(String username, String service, String newService, List<String> optionalServices, String startTime, String date, Date todaysDate, FlexiBook flexiBook) throws InvalidInputException {
		Appointment appointmentReturned = null;
		Date appointmentDate = Date.valueOf(date);
		if(todaysDate.before(appointmentDate)) {
			
			//attemp to cancel existing appointment
			try {
				FlexiBookController.cancelAppointment(username, startTime, date, todaysDate, flexiBook);
			}catch(RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
			
			//successfully cancelled appointment, so attempt to book new appointment
			try {
				appointmentReturned =FlexiBookController.makeAppointment(username, newService, optionalServices, startTime, date, flexiBook, todaysDate);
				
			} catch(RuntimeException e) {
				//booking new appointment fails so restore original appointment
				appointmentReturned=  FlexiBookController.makeAppointment(username, service, optionalServices, startTime, date, flexiBook, todaysDate);
				throw new InvalidInputException(e.getMessage());
			}
			
		}
		return appointmentReturned;
	} 
	
	
	
	
	
	
	

		
		
		
		
		
		



	
	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username (The username of the customer)
	 * @param mainServiceName (The chosen bookable service)
	 * @param optionalServiceNames (List of optional services, if any)
	 * @param startTime (Selected start time for the appointment, in the format "hh:mm:ss")
	 * @param startDate (Selected date for the appointment)
	 * @throws InvalidInputException
	 * Appointment state set to "Booked"
	 * 
	 */
	public static Appointment makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, String startTime, String startDate, FlexiBook flexiBook, Date todaysDate) throws InvalidInputException {
		
		try {
			
		
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
			BookableService thisService = findServiceByName(mainServiceName, flexiBook); 
			
			//find the customer trying to book an appointment
			customer = findCustomerByName(username, flexiBook);

			
			//check if the chosen bookableService is a Service or a ServiceCombo
			if(thisService.getClass().equals(Service.class)){   //the bookableService is a Service
				
				Service mainService = (Service) thisService;
				timeSlot = getTimeSlot(startTime.substring(0, 5), startDate, mainService.getDuration(), flexiBook);
				
				
			}
			else if(thisService.getClass().equals(ServiceCombo.class)){			//the bookableService is a ServiceCombo
				
				ServiceCombo combo = (ServiceCombo) thisService;
				int duration = 0;
				ComboItem main = combo.getMainService();
				duration += main.getService().getDuration();

				
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
			if(checkDateAndTime(timeSlot, null, flexiBook, todaysDate) == false) {
				throw new InvalidInputException("There are no available slots for " + mainServiceName + " on " + startDate.toString() + " at " + startTime.toString());
			}
			else {
				flexiBook.addAppointment(customer, thisService, timeSlot);
				Appointment appoint = new Appointment(customer,thisService,timeSlot, flexiBook);
				return appoint;
				//FlexiBookPersistence.save(flexiBook);
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
	 * @param newStartTime (string representing new start time of appointment, if any, in the format "hh:mm:ss")
	 * @param newDate (string representing new start date of appointment, if any)
	 * @param oldStartTime (Original start time of appointment)
	 * @param oldDate (Original start date of appointment)
	 * @return a string indicating whether update was successful/unsuccessful
	 * @throws InvalidInputException
	 * Appointment state remains same
	 * 
	 */
	public static String updateAppointmentServices(String username, String serviceName, List<String> newItems, List<String> removedItems, Time oldStartTime, Date oldDate, Date todaysDate, FlexiBook flexiBook) throws InvalidInputException {

		ServiceCombo combo = null;
		Appointment appointment = null;
		Customer customer = null;
		BookableService thisService = null;
		TimeSlot newTimeSlot = null;
		List<BookableService> serviceList = flexiBook.getBookableServices();
		List<ComboItem> newServices = Collections.emptyList();
		List<ComboItem> removedServices = Collections.emptyList();
		
		try {
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
			}
			if(serviceName == null || username == null || oldStartTime == null || oldDate == null) {
				throw new InvalidInputException("Service name, Customer username, previous start time or previous start date cannot be null");
			}
			
			
			//find the service corresponding to the name
			thisService = findServiceByName(serviceName, flexiBook); 
			//find the customer trying to update the appointment
			customer = findCustomerByName(username, flexiBook);
		
			//get the appointment being updated
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime()) && serviceName.equals(thisAppointment.getBookableService().getName())) {
					appointment = thisAppointment;
					break;
				}
			}
			
			if(!((appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) || (appointment.getAppointmentStatus().equals(AppointmentStatus.InProgress)))) {
				return "unsuccessful";
			}
			
			if(!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("Error: A customer can only update their own appointments");
			}
				
			
			//update optional combo items
			if((newItems != null) || (removedItems != null)) {		//the bookableService of the appointment is a Service Combo
				
				int duration = (int) ((appointment.getTimeSlot().getEndTime().getTime() - appointment.getTimeSlot().getStartTime().getTime()) / 60000);
				combo = (ServiceCombo) thisService;
			
				if(newItems != null) {
					
					//add new optional items
					Service newServiceItem;
					for(int i = 0; i < serviceList.size(); i++) {
						if(serviceList.get(i).getClass().equals(Service.class)) {
							newServiceItem = (Service) serviceList.get(i);
							if(newItems.contains(newServiceItem.getName())) {
								duration += newServiceItem.getDuration();
								newServices.add(new ComboItem(false, newServiceItem, combo));
							}
						}
					}
					
				}
				else if(removedItems != null) {
					//remove unwanted items
					
					if(removedItems.contains(combo.getMainService().getService().getName())) {
						return "unsuccessful";
					}
					
					List<ComboItem> itemList = appointment.getChosenItems();
					for(int j = 0; j < itemList.size(); j++) {		//iterate through the list of previously chosen items for this appointment
						ComboItem thisItem = itemList.get(j);
						if(removedItems.contains(thisItem.getService().getName())) {
							if(thisItem.getMandatory() == false) {		//ensure it is an optional service
								duration -= thisItem.getService().getDuration();		//remove
								removedServices.add(thisItem);
							}
							else {
								return "unsuccessful";
							}
						}
					}	
					
				}
				
				//update time slot
				
				String[] oldStart = oldStartTime.toString().split(":");
				String oldtime = oldStart[0] + ":" + oldStart[1];
				newTimeSlot = getTimeSlot(oldtime, oldDate.toString(), duration, flexiBook);
				if(checkDateAndTime(newTimeSlot, appointment, flexiBook, todaysDate) == false) {
					return "unsuccessful";
				}
				else {
					
					appointment.setTimeSlot(newTimeSlot);
					appointment.modifyOptionalServices(newServices, removedServices);
					//FlexiBookPersistence.save(flexiBook);
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
	 * @author Shaswata Bhattacharyya
	 * @param username
	 * @param serviceName
	 * @param newStartTime
	 * @param newDate
	 * @param oldStartTime
	 * @param oldDate
	 * @param todaysDate
	 * @param flexiBook
	 * @return
	 * @throws InvalidInputException
	 */
	public static String updateAppointmentTime(String username, String serviceName, String newStartTime, String newDate, Time oldStartTime, Date oldDate, Date todaysDate, FlexiBook flexiBook) throws InvalidInputException {
		
		try {
			
			//ServiceCombo combo = null;
			Appointment appointment = null;
			//Customer customer = null;
			//BookableService thisService = null;
			TimeSlot newTimeSlot = null;
			
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
			}
			if(serviceName == null || username == null || oldStartTime == null || oldDate == null) {
				throw new InvalidInputException("Service name, Customer username, previous start time or previous start date cannot be null");
			}
			//ONLY ALLOW TIME SLOT UPDATE IF APPOINTMENT STATE IS BOOKED AND NOT IN-PROGRESS
			
			//find the service corresponding to the name
			BookableService thisService = findServiceByName(serviceName, flexiBook);
			//get the appointment being updated
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime()) && serviceName.equals(thisAppointment.getBookableService().getName())) {
					appointment = thisAppointment;
					break;
				}
			}
			
			if(!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
				return "unsuccessful";
			}
			
			if(!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("Error: A customer can only update their own appointments");
			}
			
			//update time slot if needed
			if(newStartTime != null && newDate != null) {
				if(Time.valueOf(newStartTime + ":00").equals(oldStartTime) && Date.valueOf(newDate).equals(oldDate)) {
					return "unsuccessful";
				}
				
				if(thisService.getClass().equals(Service.class)) {		//if bookableService is a Service
					
					Service mainService = (Service)thisService;
					if(newDate != null && newStartTime != null) {	
						newTimeSlot = getTimeSlot(newStartTime, newDate, mainService.getDuration(), flexiBook);
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
				
				if(checkDateAndTime(newTimeSlot, appointment, flexiBook, todaysDate) == false) {
					return "unsuccessful";
				}
				else {
					appointment.modifyAppointmentTime(todaysDate, newTimeSlot);
					appointment.setTimeSlot(newTimeSlot);
					//FlexiBookPersistence.save(flexiBook);
					return "successful";
				}
				
				
			}
			return "unsuccessful";
			
		}catch(RuntimeException e) {
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
	 * Appointment state changed from "Booked" to "End"
	 * 
	 */
	public static void cancelAppointment(String username, String startTime, String startDate, Date todaysDate, FlexiBook flexiBook) throws InvalidInputException {
		//FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(startDate);
		Time appointmentTime = Time.valueOf(startTime);
		
		try {
			
			if(appointmentDate.equals(todaysDate)) {
				throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
			}
			
			
			Owner owner = flexiBook.getOwner();
			if(username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot cancel an appointment");
			}
			
			
			if(username == null || startTime == null || startDate == null) {
				throw new InvalidInputException("Customer username, start time or start date cannot be null");
			}
			
			
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
					appointment = thisAppointment;
					break;
				}
			}
			
			if(username.equals(appointment.getCustomer().getUsername())) {
				appointment.cancelAppointment(todaysDate);
				/////FlexiBookPersistence.save(flexiBook);
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
	private static boolean checkDateAndTime(TimeSlot timeSlot, Appointment appointment, FlexiBook flexiBook, Date todaysDate) {
		
		List<Appointment> existingAppointments = flexiBook.getAppointments();
		
		long startTime = timeSlot.getStartTime().getTime();
		long endTime = timeSlot.getEndTime().getTime();
		Date startDate = timeSlot.getStartDate();
		Date endDate = timeSlot.getStartDate();
		
		//check if time slot not in holiday or vacation
		List<TimeSlot> holidays = flexiBook.getBusiness().getHolidays();
		for(int i = 0; i < holidays.size(); i++) {
			TimeSlot thisHoliday = holidays.get(i);
			Date sdate = thisHoliday.getStartDate();  		//in ms
			Date edate = thisHoliday.getEndDate();
			
			
			if((startDate.after(sdate) && endDate.before(edate)) || (startDate.after(sdate) && startDate.before(edate)) || (endDate.after(sdate) && endDate.before(edate)) || startDate.equals(sdate)) {		//overlap with holiday
				return false;
			}
		}
		
		
		//check time slot not in the weekend
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == 6 || dayOfWeek == 7) {
			return false;
		}
		//check timeslot within busness hours
		for(int i = 0; i < flexiBook.getBusiness().getBusinessHours().size(); i++) {	//for each day

			BusinessHour thisHour = flexiBook.getBusiness().getBusinessHours().get(i);
			long start = thisHour.getStartTime().getTime();
			long end = thisHour.getEndTime().getTime();
			
			if(!(startTime >= start && endTime <= end)) {
				return false;
			}	
				

		}
		
		//check time slot not in the past
		if(!(startDate.after(todaysDate))) {
			return false;

		}
			
		//check time slot not in the past
		if(!(startDate.after(todaysDate))) {
			return false;
		}
		
		//if appointment within downtime of another
		Appointment thisAppointment;
		Service thisService;
		boolean valid = true;
		long stime;
		long etime;
		
		for(int i = 0; i < existingAppointments.size(); i++) {
			thisAppointment = existingAppointments.get(i);
			if(thisAppointment.equals(appointment)) {
				continue;
			}
			stime = thisAppointment.getTimeSlot().getStartTime().getTime();  		//in ms
			etime = thisAppointment.getTimeSlot().getEndTime().getTime();
			
			if(timeSlot.getStartDate().equals(thisAppointment.getTimeSlot().getStartDate())) {		
				if((startTime >= stime && endTime < etime) || (startTime >= stime && startTime < etime) || (endTime >= stime && endTime <= etime) || (startTime <= stime && endTime >= etime)) {                 //if overlap with another appointment
					valid = false;
					if(thisAppointment.getBookableService().getClass().equals(Service.class)) {			//the bookableService is a Service
						thisService = (Service)thisAppointment.getBookableService();
						if(startTime >= (stime + (thisService.getDowntimeStart()*60000)) && endTime <= (stime + (thisService.getDowntimeStart() + thisService.getDowntimeDuration())*60000) && thisService.getDowntimeStart() != 0){
							return true;
						}
						else {
							return false;
						}
					}
					else {				//the bookableService is a ServiceCombo
						
						for(int j = 0; j < thisAppointment.getChosenItems().size(); j++) {
							thisService = thisAppointment.getChosenItems().get(j).getService();
							
							if(startTime >= (stime + (thisService.getDowntimeStart()*60000)) && endTime <= (stime + (thisService.getDowntimeStart() + thisService.getDowntimeDuration())*60000) && thisService.getDowntimeStart() != 0){
								valid = true;		//appointment during the downtime of a service
								break;
							}
							else {
								stime += (thisService.getDuration() * 60000);   //appointment overlapping with a service
							}
						}		
							
					}
				}
				
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
			service = findServiceByAName(allServicesArray[i], aFlexiBook);
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
			service = findServiceByAName(allServicesArray[i], aFlexiBook);
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

	private static Service findServiceByAName(String name, FlexiBook aFlexiBook) {
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

	
	
	///////////////////////////Setup and Update BusinessInfo//////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * setupBusinessInfo method
	 * @author Aroomoogon Krishna
	 * @param name
	 * @param address
	 * @param phoneNumber
	 * @param email
	 * @param dow
	 * @param start
	 * @param end
	 * @param holiday
	 * @param vacation
	 * @throws InvalidInputException
	 */
	public static void setupBusinessInfo(String name, String address, String phoneNumber, String email)
		throws InvalidInputException {
			
			try {
				
				FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
				Business business;
				
				if (!checkBusinessOwner()) {
					throw new InvalidInputException("No permission to set up business information");
				}
				
				//Validates string type attributes
//				if (name == null || address == null || phoneNumber == null || email == null) {
//				throw new InvalidInputException("Parameters: name, address, phoneNumber, email cannot be null");
//				}
				
				if (email != null) {
					if (!validateEmail(email)) {
						throw new InvalidInputException("Invalid email");
					}
				}
				
				if (flexibook.hasBusiness()) {
					business = flexibook.getBusiness();
					business.setName(name);
					business.setAddress(address);
					business.setPhoneNumber(phoneNumber);
					business.setEmail(email);
				} else {
					business = new Business(name, address, phoneNumber, email, flexibook);
				}
			
				
			} catch(RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
		}

		/**
		 * updateBusinessInfo method
		 * @author Aroomoogon Krishna
		 * @param name
		 * @param address
		 * @param phoneNumber
		 * @param email
		 * @param businessHours
		 * @param holidays
		 * @param vacation
		 * @throws InvalidInputException
		 */
	public static void updateBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {
			try{
				FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
				Business b = flexibook.getBusiness();
				
				if (!checkBusinessOwner()) {
					throw new InvalidInputException("No permission to update business information");
				}
				
				if (name != null) {
					b.setName(name);
				}
				
				if (address != null) {
					b.setAddress(address);
				}
				
				if (phoneNumber != null) {
					b.setPhoneNumber(phoneNumber);
				}
				
				if (email != null) {
					if (!validateEmail(email)) {
						throw new InvalidInputException("Invalid email");
					} else {
						b.setEmail(email);
					}
				}

			} catch(RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
		}

		/**
		 * Helper Method
		 * @author Aroomoogon Krishna
		 * @param start
		 * @param end
		 * @return boolean, to indicate whete validity passes or fails
		 */
	private static boolean validateBusinessHourForTiming(Time start, Time end) {
			if (start.after(end)) {
				return false;
			}
			return true;
		}
		
		/**
		 * Helper Method
		 * @author Aroomoogon Krishna
		 * @param b
		 * @param dow
		 * @param start
		 * @param end
		 * @return validity flag
		 */
	private static boolean validateBusinessHourForOverlap(ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow, Time start, Time end, FlexiBook fb) {
			Business b = fb.getBusiness();
			List<BusinessHour> businessHours = b.getBusinessHours();
			for (BusinessHour k: businessHours) {
				if (k.getDayOfWeek() == dow) {
					if ( !( (start.before(k.getStartTime())) && end.before(k.getStartTime()) || ((start.after(k.getEndTime())) && end.after(k.getEndTime())) ) ) {
						return false;
					}
				}
			}
			return true;
		}
		
		/**
		 * Helper Method, getter method
		 * @author Aroomoogon Krishna
		 * @param businessHours
		 * @param day
		 * @return sorted list of business hour all having same day of week
		 */
//		private static List<BusinessHour> getBusinessHourByDay(List<BusinessHour> businessHours, ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow) {
//			List<BusinessHour> result = new ArrayList<BusinessHour>();
//			String s1, s2;
//			Time d1, d2;
//			BusinessHour temp;
//			ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek tempDay;
//			boolean swap = true;
//			
//			
//			for (int i = 0; i < businessHours.size(); i++) {
////				s2 =  businessHours.get(i).getDayOfWeek().toString();
//				tempDay = businessHours.get(i).getDayOfWeek();
//				if (dow.equals(tempDay)) {
//					result.add(businessHours.get(i));
//				}
//			}
//
//			if (result.size() > 1) {
//				while (swap) {
//					swap = false;
//					for (int i = 0; i < (result.size() - 1); i++) {
//						d1 = result.get(i).getStartTime();
//						d2 = result.get(i+1).getStartTime();
//						if (d2.before(d1)) {
//							temp = result.get(i);
//							result.set(i, result.get(i+1));
//							result.set(i+1, temp);
//							swap = true;
//						}
//					}
//				}
//			}
//			return result;
//		}
		
		/**
		 * Helpermethod
		 * @author Aroomoogon Krishna
		 * @return true if current user is owner
		 */
	private static boolean checkBusinessOwner() {
			User current = FlexiBookApplication.getCurrentUser();
			if (current instanceof Owner) {
				return true;
			}
			return false;
		}

		/**
		 * Helper Method to validate business hour parameter and add the later
		 * @author Aroomoogon Krishna
		 * @param b
		 * @param dow
		 * @param start
		 * @param end
		 * @throws InvalidInputException
		 */
	public static void addBusinessHour(String dow, Time start, Time end, FlexiBook fb) throws InvalidInputException {

			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to update business information");
			}
			
			if (getDow(dow) == null) {
				throw new InvalidInputException("Invalid Day of Week");
			}
			
			if (!validateBusinessHourForTiming(start, end)) {
				throw new InvalidInputException("Start time must be before end time");
			}
			
			if (!validateBusinessHourForOverlap(getDow(dow), start, end, fb)) {
				throw new InvalidInputException("The business hours cannot overlap");
			}
			
			fb.getBusiness().addBusinessHour(new BusinessHour(getDow(dow), start, end, fb));
		}
		
		
	public static ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek getDow(String s) {
			if (s.equals("Monday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Monday;
			} else if (s.equals("Tuesday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Tuesday;
			} else if (s.equals("Wednesday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Wednesday;
			} else if (s.equals("Thursday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Thursday;
			} else if (s.equals("Friday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Friday;
			} else if (s.equals("Saturday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Saturday;
			} else if (s.equals("Sunday")) {
				return ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek.Sunday;
			} else {
				return null;
			}
		}
		
		
		
		/**
		 * Getter method
		 * @author Aroomoogon Krishna
		 * @param fb
		 * @return Contact info in list
		 */
	private static List<String> getBusinessContactInfo(FlexiBook fb) {
			List<String> info = new ArrayList<String>();
			if (fb.hasBusiness()) {
				Business b = fb.getBusiness();
				info.add(b.getName());
				info.add(b.getAddress());
				info.add(b.getPhoneNumber());
				info.add(b.getEmail());
				
			}
			return info;
		}
		
		/**
		 * Getter method 
		 * @author Aroomoogon Krishna
		 * @param b
		 * @return
		 */
	private static List<String> getBusinessContactInfo(Business b) {
			List<String> info = new ArrayList<String>();
			info.add(b.getName());
			info.add(b.getAddress());
			info.add(b.getPhoneNumber());
			info.add(b.getEmail());
			return info;
		}

		/**
		 * helper method
		 * @author Aroomoogon Krishna
		 * @param email
		 * @return validity flag
		 * @throws InvalidInputException
		 */
	private static boolean validateEmail(String email) throws InvalidInputException{
			boolean dotchk = false;
			boolean atchk = false;
			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to set up business information");
			}
			for (int i = 0; i < email.length(); i++) {
				if (email.charAt(i) == '.') {
					dotchk = true;
				} else if (email.charAt(i) == '@') {
					atchk = true;
				}
			}
			if (dotchk == true && atchk == true) {
				return true;
			}
			return false;
		}
		
		/**
		 * helper method
		 * @author Aroomoogon Krishna
		 * @param ts
		 * @return validity flag
		 */
	private static boolean validateOffTimeForTiming(Date sD, Time sT, Date eD, Time eT) {
			if (sD.after(eD)) {
				return false;
			} else if (sD.equals(eD)) {
				if (sT.after(eT)) {
					return false;
				}
			}
			
			return true;
		}
		
		/**
		 * @author Aroomoogon Krishna
		 * @param off
		 * @param t1
		 * @return validity flag
		 */
	private static boolean validateHolidayForOverlap(Date sD, Time sT, Date eD, Time eT, FlexiBook fb) {
			List<TimeSlot> off = fb.getBusiness().getHolidays();
			for(TimeSlot t: off) {
				if (sD.before(t.getStartDate())) {
					if (eD.after(t.getStartDate())) {
						return false;
					}
					if (eD.equals(t.getStartDate())) {
						if (eT.after(t.getStartTime())) {
							return false;
						}
					}
				}
				
				if (sD.after(t.getStartDate())) {
					if (t.getEndDate().after(sD)) {
						return false;
					}
					if (t.getEndDate().equals(sD)) {
						if (t.getEndTime().after(sT)) {
							return false;
						}
					}
				}
				
				if (sD.equals(t.getStartDate())) {
					if (eD.equals(t.getEndDate())) {
						if (!(eT.after(t.getStartTime()) || t.getEndTime().after(sT))) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			return true;
		}
		
	private static boolean validateVacationForOverlap(Date sD, Time sT, Date eD, Time eT, FlexiBook fb) {
			List<TimeSlot> off = fb.getBusiness().getVacation();
			for(TimeSlot t: off) {
				if (sD.before(t.getStartDate())) {
					if (eD.after(t.getStartDate())) {
						return false;
					}
					if (eD.equals(t.getStartDate())) {
						if (eT.after(t.getStartTime())) {
							return false;
						}
					}
				}
				
				if (sD.after(t.getStartDate())) {
					if (t.getEndDate().after(sD)) {
						return false;
					}
					if (t.getEndDate().equals(sD)) {
						if (t.getEndTime().after(sT)) {
							return false;
						}
					}
				}
				
				if (sD.equals(t.getStartDate())) {
					if (eD.equals(t.getEndDate())) {
						if (!(eT.after(t.getStartTime()) || t.getEndTime().after(sT))) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
			return true;
		}
		
		/**
		 * method to add holiday timeslot
		 * @author Aroomoogon Krishna
		 * @param b
		 * @param hol
		 * @throws InvalidInputException
		 */
	public static void addHolidaySlot(Date sD, Time sT, Date eD, Time eT, FlexiBook fb) throws InvalidInputException {
			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to update business information");
			}
			
			if (!validateOffTimeForTiming(sD, sT, eD, eT)) {
				throw new InvalidInputException("Start time must be before end time");
			}
			
			if (!validateHolidayForOverlap(sD, sT, eD, eT, fb)) {
				throw new InvalidInputException("Holiday times cannot overlap");
			}
			
			if (!validateVacationForOverlap(sD, sT, eD, eT, fb)) {
				throw new InvalidInputException("Holiday and vacation times cannot overlap");
			}
			
			if (sD.before(FlexiBookApplication.getSystemDate())) {
				throw new InvalidInputException("Holiday cannot start in the past");
			} else if ((sD.equals(FlexiBookApplication.getSystemDate())) && sT.before(FlexiBookApplication.getSystemTime())) {
				throw new InvalidInputException("Holiday cannot start in the past");
			}
			
			fb.getBusiness().addHoliday(new TimeSlot(sD, sT, eD, eT, fb));
		}
		
		/**
		 * Method to add vacation time slot
		 * @author Aroomoogon Krishna
		 * @param b
		 * @param vac
		 * @throws InvalidInputException
		 */
	public static void addVacationSlot(Date sD, Time sT, Date eD, Time eT, FlexiBook fb) throws InvalidInputException {
			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to update business information");
			}
			
			if (!validateOffTimeForTiming(sD, sT, eD, eT)) {
				throw new InvalidInputException("Start time must be before end time");
			}
			
			if (!validateVacationForOverlap(sD, sT, eD, eT, fb)) {
				throw new InvalidInputException("Vacation times cannot overlap");
			}
			
			if (!validateHolidayForOverlap(sD, sT, eD, eT, fb)) {
				throw new InvalidInputException("Holiday and vacation times cannot overlap");
			}
			
			if (sD.before(FlexiBookApplication.getSystemDate())) {
				throw new InvalidInputException("Vacation cannot start in the past");
			} else if ((sD.equals(FlexiBookApplication.getSystemDate())) && sT.before(FlexiBookApplication.getSystemTime())) {
				throw new InvalidInputException("Vacation cannot start in the past");
			}
			
			fb.getBusiness().addVacation(new TimeSlot(sD, sT, eD, eT, fb));
		}
		
	public static BusinessHour findBusinessHour(String s, Time start, FlexiBook fb) {
			ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow = getDow(s);
			Business b = fb.getBusiness();
			List<BusinessHour> businessHours = b.getBusinessHours();
			for (BusinessHour k: businessHours) {
				if (k.getDayOfWeek() == dow) {
					if (k.getStartTime().equals(start)) {
						return k;
					}
				}
			}
			return null;
		}
		
	public static void updateBusinessHour(String s1, Time start, String s2, Time newStart, Time newEnd, FlexiBook fb) 
			throws InvalidInputException {
			ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow = getDow(s1);
			
			BusinessHour temp = findBusinessHour(s1, start, fb);
			
			if (dow == null || temp == null) {
				throw new InvalidInputException("Invalid Day of Week");
			}
			
			
			if (!checkBusinessOwner()) {
				fb.getBusiness().addBusinessHour(temp);
				throw new InvalidInputException("No permission to update business information");
			}
			
			fb.getBusiness().removeBusinessHour(temp);
			
			if (s2 == null && newStart == null && newEnd == null) {
				return;
			}
			
			ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek newDow = getDow(s2);
			
			if (!validateBusinessHourForTiming(newStart, newEnd)) {
				fb.getBusiness().addBusinessHour(temp);
				throw new InvalidInputException("Start time must be before end time");
			}
			
			if (!validateBusinessHourForOverlap(newDow, newStart, newEnd, fb)) {
				fb.getBusiness().addBusinessHour(temp);
				throw new InvalidInputException("The business hours cannot overlap");
			}
			
			fb.getBusiness().addBusinessHour(new BusinessHour(newDow, newStart, newEnd, fb));
		}
		
	public static void updateVacation(Date sD, Time sT, Date new_sD, Time new_sT, Date new_eD, Time new_eT, FlexiBook fb) 
			throws InvalidInputException {
			TimeSlot temp = findOfftime(sD, sT, "vacation", fb);
			
			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to update business information");
			}
			
			if (temp == null) {
				throw new InvalidInputException("No such vacation exist");
			}
			
			fb.getBusiness().removeVacation(temp);
			
			if (new_sD == null && new_sT == null && new_eD == null && new_eT == null) {
				return;
			}
			
			if (!validateOffTimeForTiming(new_sD, new_sT, new_eD, new_eT)) {
				fb.getBusiness().addVacation(temp);
				throw new InvalidInputException("Start time must be before end time");
			}
			
			if (!validateVacationForOverlap(new_sD, new_sT, new_eD, new_eT, fb)) {
				fb.getBusiness().addVacation(temp);
				throw new InvalidInputException("Vacation times cannot overlap");
			}
			
			if (!validateHolidayForOverlap(new_sD, new_sT, new_eD, new_eT, fb)) {
				fb.getBusiness().addVacation(temp);
				throw new InvalidInputException("Holiday and vacation times cannot overlap");
			}
			
			if (new_sD.before(FlexiBookApplication.getSystemDate())) {
				fb.getBusiness().addVacation(temp);
				throw new InvalidInputException("Vacation cannot start in the past");
			} else if ((new_sD.equals(FlexiBookApplication.getSystemDate())) && new_sT.before(FlexiBookApplication.getSystemTime())) {
				fb.getBusiness().addVacation(temp);
				throw new InvalidInputException("Vacation cannot start in the past");
			}
			
			fb.getBusiness().addVacation(new TimeSlot(new_sD, new_sT, new_eD, new_eT, fb));
			
			
		}
		
	public static void updateHoliday(Date sD, Time sT, Date new_sD, Time new_sT, Date new_eD, Time new_eT, FlexiBook fb) 
				throws InvalidInputException {
				TimeSlot temp = findOfftime(sD, sT, "holiday", fb);
				
				if (!checkBusinessOwner()) {
					throw new InvalidInputException("No permission to update business information");
				}
				
				if (temp == null) {
					throw new InvalidInputException("No such vacation exist");
				}
				
				fb.getBusiness().removeHoliday(temp);
				
				if (new_sD == null && new_sT == null && new_eD == null && new_eT == null) {
					return;
				}
				
				if (!validateOffTimeForTiming(new_sD, new_sT, new_eD, new_eT)) {
					fb.getBusiness().addHoliday(temp);
					throw new InvalidInputException("Start time must be before end time");
				}
				
				if (!validateHolidayForOverlap(new_sD, new_sT, new_eD, new_eT, fb)) {
					fb.getBusiness().addHoliday(temp);
					throw new InvalidInputException("Holiday times cannot overlap");
				}
				
				if (!validateVacationForOverlap(new_sD, new_sT, new_eD, new_eT, fb)) {
					fb.getBusiness().addHoliday(temp);
					throw new InvalidInputException("Holiday and vacation times cannot overlap");
				}
				
				if (new_sD.before(FlexiBookApplication.getSystemDate())) {
					fb.getBusiness().addHoliday(temp);
					throw new InvalidInputException("Holiday cannot be in the past");
				} else if ((new_sD.equals(FlexiBookApplication.getSystemDate())) && new_sT.before(FlexiBookApplication.getSystemTime())) {
					fb.getBusiness().addHoliday(temp);
					throw new InvalidInputException("Holiday cannot be in the past");
				}
				
				fb.getBusiness().addHoliday(new TimeSlot(new_sD, new_sT, new_eD, new_eT, fb));
				
				
			}
		
	public static TimeSlot findOfftime(Date sD, Time sT, String type, FlexiBook fb) {
			List<TimeSlot> off = null;
			if (type.equals("vacation")) {
				off = fb.getBusiness().getVacation();
			} else if (type.equals("holiday")) {
				off = fb.getBusiness().getHolidays();
			}
			
			for (TimeSlot k: off) {
				if (sD.equals(k.getStartDate())) {
					if (sT.equals(k.getStartTime())) {
						return k;
					}
				}
			}
			return null;
		}
	
	public static Appointment findClosestAppointment(String username, FlexiBook fb) {
		Date today = FlexiBookApplication.getSystemDate();
		Time now = FlexiBookApplication.getSystemTime();
		
		Customer c = findCustomerByName(username, fb);
		
		Appointment result = null;
		
		List<Appointment> toSearch = c.getAppointments();
		
		for (Appointment k: toSearch) {
			if (k.getTimeSlot().getStartDate().after(today) && k.getTimeSlot().getStartTime().after(now)) {
				if (result == null) {
					result = k;
				} else if (k.getTimeSlot().getStartDate().before(result.getTimeSlot().getStartDate())) {
					result = k;
				} else if (k.getTimeSlot().getStartDate().equals(result.getTimeSlot().getStartDate())) {
					if (k.getTimeSlot().getStartTime().before(result.getTimeSlot().getStartTime())) {
						result = k;
					}
				}
			}
		}
		
		return result;
	}
/////////////////////////////////////////////////////////LOGIN, LOGOUT AND VIEW APPOINTMENT CALENDAR//////////////////////////////////////////////////////////////////


		

		  private static Date InvalidFormat;

		    /**
		     *
		     * @author Venkata Satyanarayana Chivatam
		     * @param username
		     * @param password
		     * @throws InvalidInputException
		     *
		     */

		    public static void LogIn(String username, String password) throws InvalidInputException {
		        FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		        new FlexiBookApplication().setCurrentUser(User.getWithUsername(username));
		        String error = "";

		        if(username.equals(null)||password.equals(null)){
		            throw new InvalidInputException(error.trim());
		        }
		        if(username != User.getWithUsername(username).getUsername() || password != User.getWithUsername(username).getPassword()){
		            throw new InvalidInputException("username/password not found");
		        }
		        if(username == flexiBook.getOwner().getUsername() && password == flexiBook.getOwner().getPassword()) {
		            CreateUser(username,password,flexiBook);
		        }



		    }

		    /**
		     *
		     * @author Venkata Satyanarayana Chivatam
		     * @param username
		     * @throws InvalidInputException
		     *
		     */
		    public static void LogOut(String username) throws InvalidInputException{
		        FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		        new FlexiBookApplication().setCurrentUser(null);

		        if(new FlexiBookApplication().getCurrentUser()==null){

		            throw new InvalidInputException("the user is already logged out");

		        }



		    }

		    /**
		     *
		     * @author Venkata Satyanarayana Chivatam
		     * @param flexiBook
		     * @param mainServiceName
		     * @param optionalServiceNames
		     * @param startDate
		     * @param startTime
		     * @param username
		     * @throws InvalidInputException
		     *
		     */

		    public static void ViewAppointmentCalendar(String username, String mainServiceName, List<String> optionalServiceNames, String startTime, String startDate, FlexiBook flexiBook) throws InvalidInputException {
		        String error = "";
		        Appointment appointment;
		        Customer customer = null;
		        TimeSlot timeSlot = null;
		        List<BookableService> serviceList = flexiBook.getBookableServices();

		        if(!dateValidation(startDate)){
		            throw new InvalidInputException("Invalid date");
		        }

		        List<TimeSlot> timeSlotList = flexiBook.getTimeSlots();
		        List<Appointment> appointmentList = flexiBook.getAppointments();

		        for(int i =0; i<7; i++) {
		            TimeSlot ts = flexiBook.getAppointment(i).getTimeSlot();

		            }
		        }




		    /**
		     *
		     * @author Venkata Satyanarayana Chivatam
		     * @param flexibook
		     * @param passwordO
		     * @param usernameO
		     *
		     */
		    private static User CreateUser (String usernameO, String passwordO, FlexiBook flexibook){
		        FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();

		            Owner owner = new Owner(usernameO,passwordO,flexibook);
		            User.getWithUsername(usernameO).setUsername(usernameO);
		            User.getWithUsername(usernameO).setPassword(passwordO);
		            return owner;

		        }



		    /**
		     *
		     * @author Venkata Satyanarayana Chivatam
		     * @param date
		     * @return boolean
		     * @throws InvalidInputException
		     */
		    private static boolean dateValidation(String date)
		    {
		        boolean status = false;
		        if (checkDate(date)) {
		            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		            dateFormat.setLenient(false);
		            try {
		                dateFormat.parse(date);
		                status = true;
		            } catch (Exception e) {
		                status = false;
		            }
		        }
		        return status;
		    }
		    static boolean checkDate(String date) {
		        String pattern = "(0?[1-9]|1[0-2])\\/-([0-9]{4})\\/-(0?[1-9]|[12][0-9]|3[01])";
		        boolean flag = false;
		        if (date.matches(pattern)) {
		            flag = true;
		        }
		        return flag;
		    }




////////////////////////CUSTOMER ACCOUNT////////////////////
		    
			/**
			 * @author artus
			 * @param Uname
			 * @param Pword
			 * @throws InvalidInputException
			 */

			public static void signUpCustomer (String Uname, String Pword) throws InvalidInputException {


				try {
					FlexiBook flexibook = FlexiBookApplication.getFlexiBook();

					if (Uname == null || Uname.equals("")){
						throw new InvalidInputException("The user name cannot be empty");
					}
					else if(Pword.equals("") || Pword == null){
						throw new InvalidInputException("The password cannot be empty");
					}
					else if ( findUserByName("owner") != null && FlexiBookApplication.getCurrentUser() == findUserByName("owner")) {
						throw new InvalidInputException("You must log out of the owner account before creating a customer account");
					}
					
					else if (findUserByName(Uname) != null) throw new InvalidInputException("The username already exists");

					else flexibook.addCustomer(Uname, Pword);
				} 
				catch (RuntimeException e) {
					throw new InvalidInputException(e.getMessage());
				}

			}

				/**
				 * @author artus
				 * @param oldUname
				 * @param newUname
				 * @param newPword
				 * @throws InvalidInputException
				 */
				public static void accountUpdate (String oldUname, String newUname, String newPword) throws InvalidInputException {
			
					FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
					Owner owner = flexiBook.getOwner();

					try {
			
						if (newUname == null || newUname.equals("") ) {
							throw new InvalidInputException("The user name cannot be empty");
						}
						else if (newPword == null || newPword.equals("") ) {
							throw new InvalidInputException("The password cannot be empty");
						}
			
						User user = findUserByName(oldUname);
						
						if(owner!=null) {
							if(oldUname.equals(owner.getUsername())) {
								if(oldUname.equals(newUname)) {
									owner.setPassword(newPword);
									return;
								}
								else {
									throw new InvalidInputException("Changing username of owner is not allowed");
								}
							}

						}
						List<Customer> customerList = flexiBook.getCustomers();
						User customer = null;
						for(int i = 0; i < customerList.size(); i++) {		
							customer = customerList.get(i);
							if (customer.getUsername().equals(newUname)) {
								throw new InvalidInputException("Username not available");
							}
						}
			
						if (user == null) {
							throw new InvalidInputException("No user found");
						}
						
						else if (user.equals(FlexiBookApplication.getCurrentUser())) {
			
							user.setUsername(newUname);
							user.setPassword(newPword);

						}
						else {
							throw new InvalidInputException("You have to be logged in to the corresponding account to update it");
						}
			
					} 
					catch (RuntimeException e) {
						throw new InvalidInputException(e.getMessage());
					}
			
				}
				
				/**
				 * @author artus
				 * @param Uname
				 * @return
				 */
				private static User findUserByName(String Uname) {
					FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
					User thisCustomer = null;

					
					if (Uname.equals("owner") ) {
						Owner owner = FlexiBookApplication.getFlexiBook().getOwner();
						thisCustomer = owner;
						return thisCustomer;
					}
					
			
					else {List<Customer> customerList = flexiBook.getCustomers();
					
					for(int i = 0; i < customerList.size(); i++) {
						thisCustomer = customerList.get(i);
						if(thisCustomer.getUsername().equals(Uname)) {
							return thisCustomer;
						}
					}
					}
					return thisCustomer;
				}

				
			/**
			 * @author artus
			 * @param Uname
			 * @throws InvalidInputException
			 */

			public static void deleteCustomerAccount (String Uname) throws InvalidInputException {
				
				FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
				User user = findUserByName(Uname);
				User current = FlexiBookApplication.getCurrentUser();
				Appointment app;
				
				try {
					if (Uname.equals("owner") || !(user.equals(current))){
						throw new InvalidInputException("You do not have permission to delete this account");
					}
					if (user.equals(current)) {
						Customer customer = (Customer) user;
						List<Appointment> applist = customer.getAppointments();
						for(int i = 0; i < applist.size(); i++) {
							app = applist.get(i);
							flexibook.removeAppointment(app);
							app.delete();
						}
						flexibook.getCustomers().remove(user);
						user.delete();
					}
					
					FlexiBookApplication.setCurrentUser(null);
				} 
				catch (RuntimeException e) {
					throw new InvalidInputException(e.getMessage());
				}
			}
			
			
			
}


