package ca.mcgill.ecse223.flexibook.controller;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import ca.mcgill.ecse.flexibook.application.FlexiBookApplication;
import ca.mcgill.ecse.flexibook.model.*;
import ca.mcgill.ecse.flexibook.model.Appointment.AppointmentStatus;
import ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;

public class FlexiBookController {

	public FlexiBookController() {
		
	}
	

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
	 * @throws InvalidInputException Appointment state set from "Booked" to
	 *                               "In-Progress" Start appointment called even if
	 *                               customer does not show up. In case customer
	 *                               does not show up, this method calls
	 *                               endAppointment.
	 * 
	 */
	public static void startAppointment(String customerName, String startTime, String date, Date systemDate, Time systemTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(date);
		Time appointmentTime = Time.valueOf(startTime);

		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if (appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate())
					&& (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
				appointment = thisAppointment;
				break;
			}
		}

		if (!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked) && !appointment.getAppointmentStatus().equals(AppointmentStatus.InProgress)) {
			throw new InvalidInputException("The appointment is not Booked");
		}
		if(appointment.getAppointmentStatus().equals(AppointmentStatus.InProgress)) {
	throw new InvalidInputException("The appointment is in progress");
		}
		if (appointment.getTimeSlot().getStartDate().before(systemDate)
				||(appointment.getTimeSlot().getStartDate().equals(systemDate) && appointment.getTimeSlot().getStartTime().after(systemTime))) {
		throw new InvalidInputException("You cannot start an appointment before its start time");
		}
		try {
			appointment.startAppointment(systemDate, systemTime);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		FlexiBookPersistence.save(flexiBook);

	}

	/**
	 * @author yasminamatta
	 * @param dateTime
	 * @param appointment
	 * @param flexiBook
	 */
	public static void registerNoShow(String username, String appointmentDate, String appointmentTime, Date todaysDate, Time currentTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		
		
		
		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if (thisAppointment.getCustomer().getUsername().equals(username)
					&& appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate().toString())
					&& (appointmentTime).equals(thisAppointment.getTimeSlot().getStartTime().toString())) {
				appointment = thisAppointment;
				break;
			}
		}
	
		if(appointment.getAppointmentStatus().equals(AppointmentStatus.InProgress)) {
			throw new InvalidInputException("The appointment is in progress.");
		}
//		if (!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {
//			throw new InvalidInputException("The appoi")
//		}
		if (appointment.getTimeSlot().getStartDate().after(todaysDate) || (appointment.getTimeSlot().getStartDate().equals(todaysDate) && appointment.getTimeSlot().getStartTime().after(currentTime))) {
			throw new InvalidInputException("You cannot register a no show for an appointment that did not start.");
		}

		try {
			appointment.noShow(appointment.getCustomer());
			FlexiBookPersistence.save(flexiBook);

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param customerName
	 * @param startTime
	 * @param date
	 * @param flexiBook
	 * @throws InvalidInputException Appointment state set to "End" Called when
	 *                               owner ends an appointment. Also called by
	 *                               startAppointment if customer does not show up.
	 * 
	 */
	public static void endAppointment(String customerName, String startTime, String date, Date todaysDate, Time currentTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(date);
		Time appointmentTime = Time.valueOf(startTime);

		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if (appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate())
					&& (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
				appointment = thisAppointment;
				break;
			}
		}

		try {

			// owner attempts to end appointment before appointment starts
			if (todaysDate.before(appointmentDate)
					|| (todaysDate.equals(appointmentDate) && currentTime.before(appointmentTime))) {
				throw new InvalidInputException("You cannot end the appointment before it starts");
			}
			appointment.finishAppointment();
			FlexiBookPersistence.save(flexiBook);
		} catch (RuntimeException e) {
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
	public static Appointment cancelAndBookNewService(String username, String newService, List<String> optionalServices, String startTime, String date, Date todaysDate, Time todaysTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		Appointment appointmentReturned = null;
		Date appointmentDate = Date.valueOf(date);
		Time appointmentTime = Time.valueOf(startTime + ":00");
		
		List<Appointment> appointmentList = flexiBook.getAppointments();
		for (int i = 0; i < appointmentList.size(); i++) {
			Appointment thisAppointment = appointmentList.get(i);
			if(appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate()) && (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
				appointmentReturned = thisAppointment;
				break;
			}
		}
		String service = appointmentReturned.getBookableService().getName();
		
		if(todaysDate.before(appointmentDate) && !todaysDate.equals(appointmentDate)) {
			//attemp to cancel existing appointment
			try {
				
				FlexiBookController.cancelAppointment(username, startTime, date, todaysDate);
				
			}catch(RuntimeException e) {
				throw new InvalidInputException(e.getMessage());
			}
			
			//successfully cancelled appointment, so attempt to book new appointment
			try {
				
				appointmentReturned = FlexiBookController.makeAppointment(username, newService, optionalServices, startTime, date, todaysDate, todaysTime);
				FlexiBookPersistence.save(flexiBook);
				
			} catch(InvalidInputException e) {
				//booking new appointment fails so restore original appointment
				return FlexiBookController.makeAppointment(username, service, optionalServices, startTime, date, todaysDate, todaysTime);
			}
		}
		
		FlexiBookPersistence.save(flexiBook);
		return appointmentReturned;
		
	} 
	
	
	
	
	/**
	 * @author Shaswata Bhattacharyya
	 * @return
	 */
	public static ArrayList<String> getServiceList(){
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<String> serviceList = new ArrayList<>();
		
		for(BookableService service : flexiBook.getBookableServices()) {
			serviceList.add(service.getName());
		}
		
		return serviceList;
	}
	
	
	
	public static ArrayList<TOAppointment> getAppointmentsWithDate(Date thisDate){
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOAppointment> appointmentList = new ArrayList<>();
		
		for(Appointment appointment : flexiBook.getAppointments()) {
			if(appointment.getTimeSlot().getStartDate().equals(thisDate)) {
				String name = appointment.getCustomer().getUsername();
				String date = appointment.getTimeSlot().getStartDate().toString();
				String time = appointment.getTimeSlot().getStartTime().toString();
				String service = appointment.getBookableService().getName();
				TOAppointment thisAppointment = new TOAppointment(name, service, time, date);
				appointmentList.add(thisAppointment);
			}
		}
		return appointmentList;
	}

	
	public static ArrayList<TOAppointment> getCustomerAppointments(String username){
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Customer customer = findCustomerByName(username, flexiBook);
		ArrayList<TOAppointment> appointmentList = new ArrayList<>();
		
		for(Appointment appointment : customer.getAppointments()) {
			String date = appointment.getTimeSlot().getStartDate().toString();
			String time = appointment.getTimeSlot().getStartTime().toString();
			String service = appointment.getBookableService().getName();
			TOAppointment thisAppointment = new TOAppointment(username, service, time, date);
			appointmentList.add(thisAppointment);
		}
		
		return appointmentList;
	}
		


	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username             (The username of the customer)
	 * @param mainServiceName      (The chosen bookable service)
	 * @param optionalServiceNames (List of optional services, if any)
	 * @param startTime            (Selected start time for the appointment, in the
	 *                             format "hh:mm:ss")
	 * @param startDate            (Selected date for the appointment)
	 * @throws InvalidInputException Appointment state set to "Booked"
	 * 
	 */
	public static Appointment makeAppointment(String username, String mainServiceName, List<String> optionalServiceNames, String startTime, String startDate, Date todaysDate, Time todaysTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		try {

			Customer customer = null;
			TimeSlot timeSlot = null;
			List<BookableService> serviceList = flexiBook.getBookableServices();

			if (mainServiceName == null || username == null || startTime == null || startDate == null) {
				throw new InvalidInputException(
						"Service name, Customer username, start time or start date cannot be null");
			}

			Owner owner = flexiBook.getOwner();
			if (username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot make an appointment");
			}

			// find the available service from the flexibook corresponding to the name
			BookableService thisService = findServiceByName(mainServiceName, flexiBook);
			
			// find the customer trying to book an appointment
			customer = findCustomerByName(username, flexiBook);

			// check if the chosen bookableService is a Service or a ServiceCombo
			if (thisService.getClass().equals(Service.class)) { // the bookableService is a Service

				Service mainService = (Service) thisService;
				timeSlot = getTimeSlot(startTime, startDate, mainService.getDuration(), flexiBook);

			} else if (thisService.getClass().equals(ServiceCombo.class)) { // the bookableService is a ServiceCombo

				ServiceCombo combo = (ServiceCombo) thisService;
			
				int duration = 0;
				ComboItem main = combo.getMainService();
				duration += main.getService().getDuration();
				for (ComboItem coi : combo.getServices()) {
					if (coi.isMandatory()) {
						if (!coi.equals(main)) {
							duration += coi.getService().getDuration();

						}
					}
				}

				if (optionalServiceNames != null) {
					Service optionalService;
					for (int i = 0; i < serviceList.size(); i++) {
						if (optionalServiceNames.contains(serviceList.get(i).getName())) {

							optionalService = (Service) serviceList.get(i);
							duration += optionalService.getDuration();


						}
					}

				}

				timeSlot = getTimeSlot(startTime, startDate, duration, flexiBook);
				
				}
			

			// check if the appointment is within valid business hours
			if (checkDateAndTime(timeSlot, null, flexiBook, todaysDate, todaysTime, true) == false) {
				throw new InvalidInputException("There are no available slots for " + mainServiceName + " on " + startDate.toString() + " at " + startTime.toString());
			} else {

				Appointment appointment = new Appointment(customer, thisService, timeSlot, flexiBook);
				
				if (thisService.getClass().equals(ServiceCombo.class)) { 
				ServiceCombo combo = (ServiceCombo) thisService;

				for(int z=0; z <combo.getServices().size(); z++) {
					if(combo.getService(z).isMandatory()) {
						appointment.addChosenItem(combo.getService(z));
					}
				
			if (optionalServiceNames != null) {
				Service optionalService;
				for (int i = 0; i < serviceList.size(); i++) {
					if (optionalServiceNames.contains(serviceList.get(i).getName())) {
						for(int r=0; r<optionalServiceNames.size(); r++) {
					if(combo.getService(z).getService().getName().equals(optionalServiceNames.get(r))){
						
						appointment.addChosenItem(combo.getService(z));
						}
						
						}	
					}		
			
						}
						}
					}
				}
				FlexiBookPersistence.save(flexiBook);
				return appointment;
			
			}
			

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	/**
	 * 
	 * @author Shaswata Bhattacharyya
	 * @param username     (The username of the customer)
	 * @param serviceName  (The name of the bookable service of the appointment)
	 * @param newItems     (New optional services to be added, if any)
	 * @param removedItems (Optional services to be removed, if any)
	 * @param newStartTime (string representing new start time of appointment, if
	 *                     any, in the format "hh:mm:ss")
	 * @param newDate      (string representing new start date of appointment, if
	 *                     any)
	 * @param oldStartTime (Original start time of appointment)
	 * @param oldDate      (Original start date of appointment)
	 * @return a string indicating whether update was successful/unsuccessful
	 * @throws InvalidInputException Appointment state remains same
	 * 
	 */
	public static String updateAppointmentServices(String username, String serviceName, List<String> newItems, List<String> removedItems, Time oldStartTime, Date oldDate, Date todaysDate, Time todaysTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		ServiceCombo combo = null;
		Appointment appointment = null;
		Customer customer = null;
		BookableService thisService = null;
		TimeSlot newTimeSlot = null;
		List<BookableService> serviceList = flexiBook.getBookableServices();
		List<ComboItem> newServices = new ArrayList<ComboItem>();
		List<ComboItem> removedServices = new ArrayList<ComboItem>();

		try {
			Owner owner = flexiBook.getOwner();
			if (username.equals(owner.getUsername())) {
				throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
			}
			if (serviceName == null || username == null || oldStartTime == null || oldDate == null) {
				throw new InvalidInputException(
						"Service name, Customer username, previous start time or previous start date cannot be null");
			}

			// find the service corresponding to the name
			thisService = findServiceByName(serviceName, flexiBook);
			// find the customer trying to update the appointment
			customer = findCustomerByName(username, flexiBook);

			// get the appointment being updated
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if (oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime()) && serviceName.equals(thisAppointment.getBookableService().getName())) {
					appointment = thisAppointment;
					break;
				}
			}

			if ((!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked))
					&& !appointment.getAppointmentStatus().equals(AppointmentStatus.InProgress)) {
				return "unsuccessful";
			}

			if (!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("Error: A customer can only update their own appointments");
			}

			// update optional combo items
			if ((newItems != null) || (removedItems != null)) { // the bookableService of the appointment is a Service
																// Combo

				int duration = (int) ((appointment.getTimeSlot().getEndTime().getTime()
						- appointment.getTimeSlot().getStartTime().getTime()) / 60000);
				combo = (ServiceCombo) thisService;

				if (newItems != null) {

					// add new optional items
					Service newServiceItem;
					for (int i = 0; i < serviceList.size(); i++) {
						if (serviceList.get(i).getClass().equals(Service.class)) {
							newServiceItem = (Service) serviceList.get(i);
							if (newItems.contains(newServiceItem.getName())) {
								duration += newServiceItem.getDuration();
								newServices.add(new ComboItem(false, newServiceItem, combo));
							}
						}
					}

				} else if (removedItems != null) {
					// remove unwanted items

					if (removedItems.contains(combo.getMainService().getService().getName())) {
						return "unsuccessful";
					}

					List<ComboItem> itemList = appointment.getChosenItems();
					for (int j = 0; j < itemList.size(); j++) { // iterate through the list of previously chosen items
																// for this appointment
						ComboItem thisItem = itemList.get(j);
						if (removedItems.contains(thisItem.getService().getName())) {
							if (thisItem.getMandatory() == false) { // ensure it is an optional service
								duration -= thisItem.getService().getDuration(); // remove
								removedServices.add(thisItem);
							} else {
								return "unsuccessful";
							}
						}
					}

				}

				// update time slot

				// List <Appointment> listOfts = flexiBook.getAppointments();
				String[] oldStart = oldStartTime.toString().split(":");
				String oldtime = oldStart[0] + ":" + oldStart[1];

				newTimeSlot = getTimeSlot(oldtime, oldDate.toString(), duration, flexiBook);

				if (checkDateAndTime(newTimeSlot, appointment, flexiBook, todaysDate, todaysTime, false) == false) {
					return "unsuccessful";
				} else {

					String e = newTimeSlot.getEndTime().toString();
					appointment.setTimeSlot(newTimeSlot);
					appointment.modifyOptionalServices(newServices, removedServices);
					FlexiBookPersistence.save(flexiBook);
					return "successful";
				}
			}

			return "unsuccessful";
		} catch (RuntimeException e) {
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


	public static String updateAppointmentTime(String username, String newStartTime, String newDate, Time oldStartTime, Date oldDate, Date todaysDate, Time todaysTime) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();

		try {

			// ServiceCombo combo = null;
			Appointment appointment = null;
			// Customer customer = null;
			// BookableService thisService = null;
			TimeSlot newTimeSlot = null;

			Owner owner = flexiBook.getOwner();
			if (username.equals(owner.getUsername())) {
				throw new InvalidInputException("Error: An owner cannot update a customer's appointment");
			}

			if(username == null || oldStartTime == null || oldDate == null) {
				throw new InvalidInputException("Service name, Customer username, previous start time or previous start date cannot be null");
			}
			//ONLY ALLOW TIME SLOT UPDATE IF APPOINTMENT STATE IS BOOKED AND NOT IN-PROGRESS
			
			//get the appointment being updated
			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				if(oldDate.equals(thisAppointment.getTimeSlot().getStartDate()) && oldStartTime.equals(thisAppointment.getTimeSlot().getStartTime())) {
					appointment = thisAppointment;
					break;
				}
			}
			//find the service corresponding to the name
			BookableService thisService = appointment.getBookableService();
			
			
			if(!appointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {

				return "unsuccessful";
			}

			if (!appointment.getCustomer().getUsername().equals(username)) {
				throw new InvalidInputException("Error: A customer can only update their own appointments");
			}

			// update time slot if needed
			if (newStartTime != null && newDate != null) {
				if (Time.valueOf(newStartTime + ":00").equals(oldStartTime) && Date.valueOf(newDate).equals(oldDate)) {
					return "unsuccessful";
				}

				if (thisService.getClass().equals(Service.class)) { // if bookableService is a Service

					Service mainService = (Service) thisService;
					if (newDate != null && newStartTime != null) {
						newTimeSlot = getTimeSlot(newStartTime, newDate, mainService.getDuration(), flexiBook);
					}

				} else { // if bookableService is a ServiceCombo

					ServiceCombo thisCombo = (ServiceCombo) thisService;
					int duration = thisCombo.getMainService().getService().getDuration();
//					for (ComboItem coi : thisCombo.getServices()) {
//						if(coi.isMandatory() && !coi.getService().equals(thisCombo.getMainService().getService())) {
//							duration += coi.getService().getDuration();
//						}
//					}

					// check if there are optional services
					List<ComboItem> comboItemList = appointment.getChosenItems();
					Service optionalService;

					for (int i = 0; i < comboItemList.size(); i++) {
						optionalService = comboItemList.get(i).getService();
						
						
						if ( !comboItemList.get(i).equals(thisCombo.getMainService())) {
							duration += optionalService.getDuration();
						}
					}

					newTimeSlot = getTimeSlot(newStartTime, newDate, duration, flexiBook);

				}

				if (checkDateAndTime(newTimeSlot, appointment, flexiBook, todaysDate, todaysTime, false) == false) {
					return "unsuccessful";
				} else {
					appointment.modifyAppointmentTime(todaysDate, newTimeSlot);
					FlexiBookPersistence.save(flexiBook);
					return "successful";
				}

			}
			return "unsuccessful";

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	private static boolean noOptionalServicesExist() {
		// TODO Auto-generated method stub
		return false;
	}


	/**
	 *
	 * @author Shaswata Bhattacharyya
	 * @param username  (The username of the customer trying to cancel the
	 *                  appointment)
	 * @param startTime (The start time of the appointment to be cancelled)
	 * @param startDate (The date of the appointment to be cancelled)
	 * @throws InvalidInputException Appointment state changed from "Booked" to
	 *                               "End"
	 * 
	 */
	public static void cancelAppointment(String username, String startTime, String startDate, Date todaysDate) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Appointment appointment = null;
		Date appointmentDate = Date.valueOf(startDate);
		Time appointmentTime = Time.valueOf(startTime + ":00");

		try {

			if (appointmentDate.equals(todaysDate)) {
				throw new InvalidInputException("Cannot cancel an appointment on the appointment date");
			}

			Owner owner = flexiBook.getOwner();
			if (username.equals(owner.getUsername())) {
				throw new InvalidInputException("An owner cannot cancel an appointment");
			}

			if (username == null || startTime == null || startDate == null) {
				throw new InvalidInputException("Customer username, start time or start date cannot be null");
			}

			List<Appointment> appointmentList = flexiBook.getAppointments();
			for (int i = 0; i < appointmentList.size(); i++) {
				Appointment thisAppointment = appointmentList.get(i);
				String n = thisAppointment.getCustomer().getUsername();
				String s = thisAppointment.getBookableService().getName();
				if (appointmentDate.equals(thisAppointment.getTimeSlot().getStartDate())
						&& (appointmentTime.equals(thisAppointment.getTimeSlot().getStartTime()))) {
					appointment = thisAppointment;
				}
			}

			if (username.equals(appointment.getCustomer().getUsername())) {
				appointment.cancelAppointment(todaysDate);
				FlexiBookPersistence.save(flexiBook);
			} else {
				throw new InvalidInputException("A customer can only cancel their own appointments");
			}

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	// private methods

	/**
	 * @author Shaswata Bhattacharyya
	 * @param startTime
	 * @param startDate
	 * @param service
	 * @return time slot created using the start date, start time, end date and end
	 *         time
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
	private static boolean checkDateAndTime(TimeSlot timeSlot, Appointment appointment, FlexiBook flexiBook, Date todaysDate, Time todaysTime, boolean make) {

		List<Appointment> existingAppointments = flexiBook.getAppointments();
		Time startTimeApp = timeSlot.getStartTime();
		Time endTimeApp = timeSlot.getEndTime();
		List<TimeSlot> holidays = flexiBook.getBusiness().getHolidays();
		long startTime = timeSlot.getStartTime().getTime();
		long endTime = timeSlot.getEndTime().getTime();
		Date startDate = timeSlot.getStartDate();
		Date endDate = timeSlot.getStartDate();

		// check if time slot not in holiday or vacation
		
		for (int i = 0; i < holidays.size(); i++) {
			TimeSlot thisHoliday = holidays.get(i);
			Date sdate = thisHoliday.getStartDate(); // in ms
			Date edate = thisHoliday.getEndDate();
			Time etime = thisHoliday.getEndTime();
			Time stime = thisHoliday.getStartTime();

			if ((startDate.after(sdate) && endDate.before(edate)) || (startDate.after(sdate) && startDate.before(edate))
					|| (endDate.after(sdate) && endDate.before(edate))
					|| (startDate.equals(sdate) && endTimeApp.after(stime))) { // overlap with holiday
				return false;
			}
		}

		List<TimeSlot> vacations = flexiBook.getBusiness().getVacation();
		for (int i = 0; i < vacations.size(); i++) {
			TimeSlot thisVacation = vacations.get(i);
			Date vsDate = thisVacation.getStartDate(); // in ms
			Date veDate = thisVacation.getEndDate();
			Time veTime = thisVacation.getEndTime();
			Time vsTime = thisVacation.getStartTime();

			if ((startDate.after(vsDate) && endDate.before(veDate))
					|| (startDate.after(vsTime) && startDate.before(veTime))
					|| (endDate.after(vsDate) && endDate.before(veDate))
					|| (startDate.equals(vsDate) && endTimeApp.after(vsTime)
							|| (startDate.equals(veDate) && startTimeApp.before(veTime)))) { // overlap with holiday
				return false;
			}
		}

		// check time slot not in the weekend
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 6 || dayOfWeek == 7) {
			return false;
		}
		
		DayOfWeek day = null; 
		switch(dayOfWeek) {
		case 1:
			day = DayOfWeek.Monday;
			break;
		case 2:
			day = DayOfWeek.Tuesday;
			break;
		case 3:
			day = DayOfWeek.Wednesday;
			break;
		case 4:
			day = DayOfWeek.Thursday;
			break;
		case 5:
			day = DayOfWeek.Friday;
			break;
		}
		
		boolean outsideHours = true;
		// check timeslot within busness hours
		for (int i = 0; i < flexiBook.getBusiness().getBusinessHours().size(); i++) { // for each day
			
			BusinessHour thisHour = flexiBook.getBusiness().getBusinessHours().get(i);
			if(thisHour.getDayOfWeek() == day) {
				long start = thisHour.getStartTime().getTime();
				long end = thisHour.getEndTime().getTime();
				
				if (startTime >= start && endTime <= end) {
					outsideHours = false;
					break;
				}
			}

		}
		
		if(outsideHours) {
			return false;
		}

		
		// checking if is not in past year
		if(Integer.parseInt(startDate.toString().substring(0, 4)) <= Integer.parseInt(todaysDate.toString().substring(0, 4))-1 ){
			return false;
		}
		
		
		// check time slot not in the past		
		if ( startDate.before(todaysDate) && startTimeApp.before(todaysTime)){
			return false;
		}
		
		
		//check if start time of the appointment is before todays time
		if(startDate.equals(todaysDate) && startTimeApp.after(todaysTime)) {
			if(make == false) {			//not making appointment
				return false;
			}
		}
		
		
		Appointment thisAppointment;
		Service thisService = null;
		boolean valid = true;
		long stime;
		long etime;
		int index = 0;
		for (int i = 0; i < existingAppointments.size(); i++) {
			thisAppointment = existingAppointments.get(i);

			if (thisAppointment.equals(appointment)) {
				continue;
			}
			stime = thisAppointment.getTimeSlot().getStartTime().getTime(); // in ms
			
			etime = thisAppointment.getTimeSlot().getEndTime().getTime();
			long durationTime = etime - stime;
			if (timeSlot.getStartDate().equals(thisAppointment.getTimeSlot().getStartDate())
					&& thisAppointment.getAppointmentStatus().equals(AppointmentStatus.Booked)) {

				if ((startTime >= stime && endTime < etime) || (startTime >= stime && startTime < etime)
						|| (endTime > stime && endTime <= etime) || (startTime <= stime && endTime >= etime)) { // if
																													// overlap
																													// with
																													// another
																													// appointment
					valid = false;

					if (thisAppointment.getBookableService().getClass().equals(Service.class)) { // the bookableService
																									// is a Service
						thisService = (Service) thisAppointment.getBookableService();
						if (startTime >= (stime + (thisService.getDowntimeStart() * 60000))
								&& endTime <= (stime
										+ (thisService.getDowntimeStart() + thisService.getDowntimeDuration()) * 60000)
								&& thisService.getDowntimeDuration() != 0) {
							return true;
						} else {
							return false;
						}
					} else { // the bookableService is a ServiceCombo
						ServiceCombo servCombo = (ServiceCombo) thisAppointment.getBookableService();
//						
						Service main  = servCombo.getMainService().getService();
						if( startTime >= (stime+ (main.getDowntimeStart()) *60000) && endTime <= (stime + (main.getDowntimeStart() + main.getDowntimeDuration()) * 60000) && main.getDowntimeDuration() !=0 ){
							valid = true;
							break;
						}
						else {
							stime += (main.getDuration() * 60000);
						}
						
						for(int j=0; j< thisAppointment.getChosenItems().size();j++) {
							thisService = thisAppointment.getChosenItems().get(j).getService();
							
							if( startTime  >= (stime +( thisService.getDowntimeStart()* 60000)) && endTime <= (stime + (thisService.getDowntimeStart() + thisService.getDowntimeDuration()) * 60000) && thisService.getDowntimeDuration() !=0 ){
							valid = true;
							break;
						}
							else {
								stime += (thisService.getDuration() * 60000);
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
		for (int i = 0; i < customerList.size(); i++) {
			thisCustomer = customerList.get(i);
			if (thisCustomer.getUsername().equals(username)) {
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
		for (int i = 0; i < serviceList.size(); i++) {
			thisService = serviceList.get(i);
			if (thisService.getName().equals(serviceName)) {
				return thisService;
			}
		}

		return null;
	}

	// ************************************SERVICE
	// FEATURES******************************

	public static Service service;
	private static List<Appointment> appointments;
	//private static Owner owner = new Owner("owner", "admin", FlexiBookApplication.getFlexiBook());
	
	//public static ArrayList<TOService> existingServices = new ArrayList<TOService>(); 
	
	public static ArrayList<TOService> getExistingServices() {

		ArrayList<TOService> existingServices = new ArrayList<TOService>(); 
		for (BookableService service : FlexiBookApplication.getFlexiBook().getBookableServices()) {
			if (service instanceof Service) {
			TOService toService = new TOService(service.getName(), ((Service) service).getDuration(), ((Service) service).getDowntimeStart(), ((Service) service).getDowntimeDuration());
			existingServices.add(toService);
			}
		}
		FlexiBookPersistence.save(FlexiBookApplication.getFlexiBook());
		return existingServices;
	}


	/**
	 * Helper Method: Add/Update/Delete Service Feature
	 * 
	 * @author Sneha Singh
	 * @param user
	 * @return boolean true if logged in User is owner
	 * @throws InvalidInputException
	 */
	private static boolean checkOwner(User user) throws InvalidInputException {
		boolean userIsOwner = false;
		
		//added to test UI, delete before submitting 
//		if (user == null) {
//			FlexiBookApplication.setCurrentUser(owner);
//			user = FlexiBookApplication.getCurrentUser();
//		}
		
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
	 * 
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
	 * 
	 * @author Sneha Singh {@summary} checks for positive downtime duration
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @return boolean true if downtime duration is positive
	 * @throws InvalidInputException
	 */
	private static boolean checkPositiveDowntimeDuration(int downtimeStart, int downtimeDuration)
			throws InvalidInputException {
		boolean downtimeDurationIsPositive = true;
		if (downtimeDuration <= 0 && downtimeStart > 0) {
			downtimeDurationIsPositive = false;
			throw new InvalidInputException("Downtime duration must be positive");
		}
		return downtimeDurationIsPositive;
	}

	/**
	 * Helper Method: Add/Update Service
	 * 
	 * @author Sneha Singh {@summary} Checks to make sure downtime duration is 0 if
	 *         there is no downtime associated with service
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @return boolean true if downtime and downtime duration are consistent
	 * @throws InvalidInputException
	 */
	private static boolean checkDowntimeDurationIsZero(int downtimeStart, int downtimeDuration)
			throws InvalidInputException {
		boolean downtimeDurationIsZero = true;
		if ((downtimeStart == 0) && (downtimeDuration < 0)) {
			downtimeDurationIsZero = false;
			throw new InvalidInputException("Downtime duration must be 0");
		}

		return downtimeDurationIsZero;
	}

	/**
	 * Helper Method: Add/Update Service
	 * 
	 * @author Sneha Singh {@summary} Checks to ensure that downtime makes sense
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @param duration
	 * @return boolean true if downtime is valid (starts after and lasts for no
	 *         longer than duration of service)
	 * @throws InvalidInputException
	 */
	private static boolean checkDowntimeStartsDuringService(int downtimeStart, int downtimeDuration, int duration)
			throws InvalidInputException {
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

		if ((downtimeStart + downtimeDuration > duration) && (downtimeStart < duration)) {
			downtimeStartsDuringService = false;
			throw new InvalidInputException("Downtime must not end after the service");
		}

		return downtimeStartsDuringService;

	}

	/**
	 * Helper Method: Add/Update Service
	 * 
	 * @author Sneha Singh {@summary} Consolidates all helper methods that check
	 *         timing
	 * @param downtimeStart
	 * @param downtimeDuration
	 * @param duration
	 * @return boolean true if all timings make sense
	 * @throws InvalidInputException
	 */
	private static boolean timingsMakeSense(int downtimeStart, int downtimeDuration, int duration)
			throws InvalidInputException {
		boolean timingsMakeSense = true;

		try {
			checkPositiveDuration(duration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			// service.delete();
			throw e;
		}

		try {
			checkPositiveDowntimeDuration(downtimeStart, downtimeDuration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			// service.delete();
			throw e;
		}

		try {
			checkDowntimeDurationIsZero(downtimeStart, downtimeDuration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			// service.delete();
			throw e;
		}

		try {
			checkDowntimeStartsDuringService(downtimeStart, downtimeDuration, duration);
		} catch (InvalidInputException e) {
			timingsMakeSense = false;
			// service.delete();
			throw e;

		}
		return timingsMakeSense;
	}

	/**
	 * Helper Method: Add/Update Service
	 * 
	 * @author Sneha Singh {@summary} checks to see if service with a specified name
	 *         already exists within FlexiBook
	 * @param aFlexiBook
	 * @param name
	 * @return boolean false if service doesn't exist
	 * @throws InvalidInputException
	 */
	private static boolean serviceExistsAlready(String name) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		boolean serviceExists = false;
	
		
		if (flexiBook.getBookableServices().contains(Service.getWithName(name))) {
			serviceExists = true;
			throw new InvalidInputException("Service " + name + " already exists");
		}
		FlexiBookPersistence.save(flexiBook);
		return serviceExists;
	}

	// ****************************ADD
	// SERVICE**********************************************

	/**
	 * AddService Feature Implementation
	 * 
	 * @author Sneha Singh 
	 * {@summary} Adds service with specified parameters to
	 *         FlexiBook if all criteria is satisfied
	 * @param name
	 * @param flexiBook
	 * @param duration
	 * @param downtimeDuration
	 * @param downtimeStart
	 * @throws InvalidInputException
	 */
	public static void addService(String name, int duration, int downtimeDuration, int downtimeStart) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
		try { 
			if ((checkOwner(FlexiBookApplication.getCurrentUser()) == true) && (timingsMakeSense(downtimeStart, downtimeDuration, duration) == true) && (serviceExistsAlready(name) == false)) {
				new Service(name, flexiBook, duration, downtimeDuration, downtimeStart);
				FlexiBookPersistence.save(flexiBook);
			}
		}
		catch (RuntimeException e) {
			String error = e.getMessage();
			throw new InvalidInputException(error);
		}
	}
	

	// **************************UPDATE
	// SERVICE***********************************************

	/**
	 * Helper Method: UpdateService
	 * 
	 * @author Sneha Singh {@summary} checks to make sure that the correct service
	 *         is being updated
	 * @param serviceToUpdate
	 * @param newName
	 * @param flexiBook
	 * @return
	 * @throws InvalidInputException
	 */
	private static boolean updatingCorrectService(Service serviceToUpdate, String newName) throws InvalidInputException {
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		boolean updatingCorrService = false;

		if (flexiBook.getBookableServices().contains(serviceToUpdate)) {
			updatingCorrService = true;
		}

		// check to ensure updated service name isn't the same as the name of a
		// pre-existing service
		if ((!(newName.equals(serviceToUpdate.getName())))
				&& flexiBook.getBookableServices().contains(Service.getWithName(newName))) {
			throw new InvalidInputException("Service " + newName + " already exists");
		}
		
		FlexiBookPersistence.save(flexiBook);
		return updatingCorrService;
	}

	/**
	 * UpdateService Implementation
	 * 
	 * @author Sneha Singh {@summary} updates parameters of an existing service
	 *         provided all criteria is satisfied
	 * @param serviceToUpdate
	 * @param newName
	 * @param flexiBook
	 * @param newDuration
	 * @param newDowntimeStart
	 * @param newDowntimeDuration
	 * @throws InvalidInputException
	 */
	public static void updateService(String name, String newName, int newDuration, int newDowntimeStart, int newDowntimeDuration) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Service serviceToUpdate = null;
		
		for(int i = 0; i < flexiBook.getBookableServices().size(); i++) {
			if(flexiBook.getBookableService(i) instanceof Service) {
				serviceToUpdate = (Service) flexiBook.getBookableService(i);
				if(serviceToUpdate.getName().equals(name)) {
					break;
				}
			}
			
		}
		
		try {
			if ((checkOwner(FlexiBookApplication.getCurrentUser()) == true) && (timingsMakeSense(newDowntimeStart, newDowntimeDuration, newDuration) == true) && (updatingCorrectService(serviceToUpdate, newName) == true)) {
				serviceToUpdate.setName(newName);
				serviceToUpdate.setDuration(newDuration);
				serviceToUpdate.setDowntimeDuration(newDowntimeDuration);
				serviceToUpdate.setDowntimeStart(newDowntimeStart);
			}
			FlexiBookPersistence.save(flexiBook);
			
		} catch(RuntimeException e) {
			String error = e.getMessage();
			throw new InvalidInputException(error);
		}
		
		
		
		
	
	}

	// *******************************DELETE
	// SERVICE***********************************************

	/**
	 * Helper Method: Delete Service
	 * 
	 * @author Sneha Singh {@summary} checks to ensure no future appointments exist
	 *         when attempting to delete a Service
	 * @param name
	 * @return boolean true if not future appointments for that service exist
	 * @throws InvalidInputException
	 */
	private static boolean noFutureApptsExist(String name) throws InvalidInputException {
		boolean noFutureAppts = true;
		Service serviceToDelete = (Service) Service.getWithName(name);

		if (serviceToDelete.hasAppointments() == true) {
			appointments = serviceToDelete.getAppointments();
			for (int i = 0; i < appointments.size(); i++) {
				if (appointments.get(i).getTimeSlot().getStartDate().after(FlexiBookApplication.getSystemDate())) {
					noFutureAppts = false;
					break;
				}
			}
		}
		return noFutureAppts;
		
	}

	/**
	 * Helper Method: Delete Service
	 * 
	 * @author Sneha Singh {@summary} removes Service from relevant ServiceCombos or
	 *         deletes ServiceCombos when initiating Service deletion
	 * @param name
	 * @param flexiBook
	 */
	private static void removeServiceFromCombos(String name) {

		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		
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
		FlexiBookPersistence.save(flexiBook);
	}

	/**
	 * DeleteService Implementation
	 * 
	 * @author Sneha Singh {@summary} Deletes a specified Service provided all
	 *         criteria is satisfied
	 * @param name
	 * @param flexiBook
	 * @throws InvalidInputException
	 */
	public static void deleteService(String name) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Service serviceToDelete = (Service) Service.getWithName(name);
		try {
			
			if (checkOwner(FlexiBookApplication.getCurrentUser()) == true) {
				if(noFutureApptsExist(name) == true) {
					removeServiceFromCombos(name);
					serviceToDelete.delete();
					FlexiBookPersistence.save(flexiBook);
				} else {
					throw new InvalidInputException("The service contains future appointments");
				}
				
			}
			
		}catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
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

	public static void defineServiceCombo(String aName, String user, FlexiBook aFlexiBook, String aMainService, String mandatory, String allServices) throws InvalidInputException {

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

	/////////////////////////// Setup and Update
	/////////////////////////// BusinessInfo//////////////////////////////////////////////////////////////////////////////////////

	/**
	 * setupBusinessInfo method
	 * 
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
	public static void setupBusinessInfo(String name, String address, String phoneNumber, String email) throws InvalidInputException {

		try {

			FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
			Business business;

			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to set up business information");
			}

			// Validates string type attributes
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
			
			FlexiBookPersistence.save(flexibook);

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * updateBusinessInfo method
	 * 
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
	public static void updateBusinessInfo(String name, String address, String phoneNumber, String email)
			throws InvalidInputException {
		try {
			FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
			Business b = flexibook.getBusiness();

			if (!checkBusinessOwner()) {
				throw new InvalidInputException("No permission to update business information");
			}
			
			if (name.equals("") && address.equals("") && phoneNumber.equals("") && email.equals("")) {
				throw new InvalidInputException("Nothing to update");
			}

			if (name != null && !name.equals("")) {
				b.setName(name);
			}

			if (address != null && !address.equals("")) {
				b.setAddress(address);
			}

			if (phoneNumber != null && !phoneNumber.equals("")) {
				b.setPhoneNumber(phoneNumber);
			}

			if (email != null && !email.equals("")) {
				if (!validateEmail(email)) {
					throw new InvalidInputException("Invalid email");
				} else {
					b.setEmail(email);
				}
			}
			
			FlexiBookPersistence.save(flexibook);

		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
	 * Helper Method
	 * 
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
	 * 
	 * @author Aroomoogon Krishna
	 * @param b
	 * @param dow
	 * @param start
	 * @param end
	 * @return validity flag
	 */
	private static boolean validateBusinessHourForOverlap(ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow,
			Time start, Time end, FlexiBook fb) {
		Business b = fb.getBusiness();
		List<BusinessHour> businessHours = b.getBusinessHours();
		for (BusinessHour k : businessHours) {
			if (k.getDayOfWeek() == dow) {
				if (!((start.before(k.getStartTime())) && end.before(k.getStartTime())
						|| ((start.after(k.getEndTime())) && end.after(k.getEndTime())))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Helpermethod
	 * 
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
	 * 
	 * @author Aroomoogon Krishna
	 * @param b
	 * @param dow
	 * @param start
	 * @param end
	 * @throws InvalidInputException
	 */
	public static void addBusinessHour(String dow, String startTime, String endTime) throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
		
		Time start;
		Time end;
		
		try {
			start = Time.valueOf(startTime + ":00");
			if(endTime.equals("0:00") || endTime.equals("00:00")) {
				end = Time.valueOf("23:59:59");
			}else {
				end = Time.valueOf(endTime + ":00");
			}
			
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
		}
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
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
	
		
		
		FlexiBookPersistence.save(fb);
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
	 * 
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
	 * 
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
	 * 
	 * @author Aroomoogon Krishna
	 * @param email
	 * @return validity flag
	 * @throws InvalidInputException
	 */
	private static boolean validateEmail(String email) throws InvalidInputException {
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
	 * 
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
		for (TimeSlot t : off) {
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
		for (TimeSlot t : off) {
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
	 * 
	 * @author Aroomoogon Krishna
	 * @param b
	 * @param hol
	 * @throws InvalidInputException
	 */
	public static void addHolidaySlot(String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
	
		Time sT;
		Time eT;
		
		Date sD = Date.valueOf(startDate);
		Date eD = Date.valueOf(endDate);
		try {
			sT = Time.valueOf(startTime + ":00");
			eT = Time.valueOf(endTime + ":00");
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
		}
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
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
		} else if ((sD.equals(FlexiBookApplication.getSystemDate()))
				&& sT.before(FlexiBookApplication.getSystemTime())) {
			throw new InvalidInputException("Holiday cannot start in the past");
		}

		fb.getBusiness().addHoliday(new TimeSlot(sD, sT, eD, eT, fb));
		
		FlexiBookPersistence.save(fb);
	}

	/**
	 * Method to add vacation time slot
	 * 
	 * @author Aroomoogon Krishna
	 * @param b
	 * @param vac
	 * @throws InvalidInputException
	 */
	public static void addVacationSlot(String startDate, String startTime, String endDate, String endTime) throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
		
		Time sT;
		Time eT;
		
		Date sD = Date.valueOf(startDate);
		Date eD = Date.valueOf(endDate);
		try {
			sT = Time.valueOf(startTime + ":00");
			eT = Time.valueOf(endTime + ":00");
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
		}
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
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
		} else if ((sD.equals(FlexiBookApplication.getSystemDate()))
				&& sT.before(FlexiBookApplication.getSystemTime())) {
			throw new InvalidInputException("Vacation cannot start in the past");
		}

		fb.getBusiness().addVacation(new TimeSlot(sD, sT, eD, eT, fb));
		
		FlexiBookPersistence.save(fb);
	}

	public static BusinessHour findBusinessHour(String s, Time start, FlexiBook fb) {
		ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow = getDow(s);
		Business b = fb.getBusiness();
		List<BusinessHour> businessHours = b.getBusinessHours();
		for (BusinessHour k : businessHours) {
			if (k.getDayOfWeek() == dow) {
				if (k.getStartTime().equals(start)) {
					return k;
				}
			}
		}
		return null;
	}

	public static void updateBusinessHour(String s1, String oldStart, String s2, String newStart, String newEnd)
			throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();

		Time oldS = null;
		Time newS = null;
		Time newE = null;
		
		try {
			if (s2.equals("") && newStart.equals("") && newEnd.equals("")) {
				oldS = Time.valueOf(oldStart + ":00");
				s2 = null;
			} else {
				oldS = Time.valueOf(oldStart + ":00");
				newS = Time.valueOf(newStart + ":00");
				newE = Time.valueOf(newEnd + ":00");
			}
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
		}		
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
		ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek dow = getDow(s1);
		
		BusinessHour temp = findBusinessHour(s1, oldS, fb);

		if (dow == null) {
			throw new InvalidInputException("Invalid Day of Week");
		}
		
		if (temp == null) {
			throw new InvalidInputException("Business Hour to be updated not found");
		}

		if (!checkBusinessOwner()) {
			fb.getBusiness().addBusinessHour(temp);
			throw new InvalidInputException("No permission to update business information");
		}

		fb.getBusiness().removeBusinessHour(temp);

		if (s2 == null && newS == null && newE == null) {
			return;
		}

		ca.mcgill.ecse.flexibook.model.BusinessHour.DayOfWeek newDow = getDow(s2);

		if (newDow == null) {
			throw new InvalidInputException("Invalid Day of Week");
		}
		
		if (!validateBusinessHourForTiming(newS, newE)) {
			fb.getBusiness().addBusinessHour(temp);
			throw new InvalidInputException("Start time must be before end time");
		}

		if (!validateBusinessHourForOverlap(newDow, newS, newE, fb)) {
			fb.getBusiness().addBusinessHour(temp);
			throw new InvalidInputException("The business hours cannot overlap");
		}

		fb.getBusiness().addBusinessHour(new BusinessHour(newDow, newS, newE, fb));
		
		FlexiBookPersistence.save(fb);
	}

	public static void updateVacation(String startDate, String startTime, String new_startDate, String new_startTime, String new_endDate, String new_endTime) throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
		
		Date sD;
		Time sT;
		try {
			sD = Date.valueOf(startDate);
			sT = Time.valueOf(startTime + ":00");
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong input format");
		}
		
		Date new_sD = null;
		Time new_sT = null;
		Date new_eD = null;
		Time new_eT = null;
		if (new_startDate != null && new_startTime != null && new_endDate != null && new_endTime != null) {
			try {
				new_sD = Date.valueOf(new_startDate);
				new_sT = Time.valueOf(new_startTime + ":00");
				new_eD = Date.valueOf(new_endDate);
				new_eT = Time.valueOf(new_endTime + ":00");
			} catch (IllegalArgumentException e) {
				throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
			}
		}	
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
		
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
		} else if ((new_sD.equals(FlexiBookApplication.getSystemDate()))
				&& new_sT.before(FlexiBookApplication.getSystemTime())) {
			fb.getBusiness().addVacation(temp);
			throw new InvalidInputException("Vacation cannot start in the past");
		}

		fb.getBusiness().addVacation(new TimeSlot(new_sD, new_sT, new_eD, new_eT, fb));
		
		FlexiBookPersistence.save(fb);

	}

	public static void updateHoliday(String startDate, String startTime, String new_startDate, String new_startTime, String new_endDate, String new_endTime) throws InvalidInputException {
		
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
		
		Date sD;
		Time sT;
		try {
			sD = Date.valueOf(startDate);
			sT = Time.valueOf(startTime + ":00");
		} catch (IllegalArgumentException e) {
			throw new InvalidInputException("Wrong input format");
		}
		
		Date new_sD = null;
		Time new_sT = null;
		Date new_eD = null;
		Time new_eT = null;
		if (new_startDate != null && new_startTime != null && new_endDate != null && new_endTime != null) {
			try {
				new_sD = Date.valueOf(new_startDate);
				new_sT = Time.valueOf(new_startTime + ":00");
				new_eD = Date.valueOf(new_endDate);
				new_eT = Time.valueOf(new_endTime + ":00");
			} catch (IllegalArgumentException e) {
				throw new InvalidInputException("Wrong Input Format. Time should be hh:mm");
			}
		}
		
		if (!fb.hasBusiness()) {
			throw new InvalidInputException("No business exist");
		}
		
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
		} else if ((new_sD.equals(FlexiBookApplication.getSystemDate()))
				&& new_sT.before(FlexiBookApplication.getSystemTime())) {
			fb.getBusiness().addHoliday(temp);
			throw new InvalidInputException("Holiday cannot be in the past");
		}

		fb.getBusiness().addHoliday(new TimeSlot(new_sD, new_sT, new_eD, new_eT, fb));
		
		FlexiBookPersistence.save(fb);

	}

	public static TimeSlot findOfftime(Date sD, Time sT, String type, FlexiBook fb) {
		List<TimeSlot> off = null;
		if (type.equals("vacation")) {
			off = fb.getBusiness().getVacation();
		} else if (type.equals("holiday")) {
			off = fb.getBusiness().getHolidays();
		}

		for (TimeSlot k : off) {
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

		for (Appointment k : toSearch) {
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
	
	public static TOBusiness getBusiness() {
		FlexiBook fb = FlexiBookApplication.getFlexiBook();
		TOBusiness t = null;
		if (fb.hasBusiness()) {
			Business b = fb.getBusiness();
			t = new TOBusiness(b.getName(), b.getAddress(), b.getPhoneNumber(), b.getEmail());
			return t;
		} else {
			return null;
		}
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

	public static void logIn(String username, String password) throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();

		boolean flag = false;
		boolean var = false;
		User customer = null;
		
		try {
			
			
			if(flexiBook.getOwner()==null) {
				if (username.equals("owner") && password.equals("owner")) {
					if(flexiBook.getOwner() == null) {			//first time
						createUser(username, password, flexiBook);
						return;
					}
			}
			}
				else {
					if(flexiBook.getOwner().getPassword().equals(password)) {
						FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
						return;
						
					}
				}
//			if (username.equals("owner") && password.equals("owner")) {
//				if(flexiBook.getOwner() == null) {			//first time
//					createUser(username, password, flexiBook);
//					return;
//				}
//				else{	//not first time
//					FlexiBookApplication.setCurrentUser(flexiBook.getOwner());
//					return;
//				}
//				
//			
//			}
			List<Customer> customerList = flexiBook.getCustomers();
			for (int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if (username.equals(customer.getUsername())) {
					flag = true;
					
					if (customer.getPassword().equals(password)) {
						var = true;
						break;
					}
					else {
						FlexiBookApplication.setCurrentUser(null);
						throw new InvalidInputException("Username/password not found");
					}
				}
			}
			
			
			if (!flag) {
				FlexiBookApplication.setCurrentUser(null);
				throw new InvalidInputException("Username/password not found");
			}
			if (!var) {
				FlexiBookApplication.setCurrentUser(null);
				throw new InvalidInputException("Username/password not found");

			}

			FlexiBookApplication.setCurrentUser(customer);
			FlexiBookPersistence.save(flexiBook);
			
		} catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
	}
	

	/**
	 *
	 * @author Venkata Satyanarayana Chivatam
	 * @param username
	 * @throws InvalidInputException
	 *
	 */
	public static void logOut() throws InvalidInputException {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();

		try {
			if (FlexiBookApplication.getCurrentUser() == null) {
				throw new InvalidInputException("The user is already logged out");
			}
			FlexiBookApplication.setCurrentUser(null);
			FlexiBookPersistence.save(flexiBook);
		} catch(RuntimeException e) {
			FlexiBookPersistence.save(flexiBook);
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}

	

	public static List<TOUser> getCustomers() {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOUser> customers = new ArrayList<TOUser>();
		for (User customer : FlexiBookApplication.getFlexiBook().getCustomers()) {
			if (customer instanceof Customer) {
				TOUser toUser= new TOUser(customer.getUsername(), customer.getPassword() );
				customers.add(toUser);
			}
		}
		return customers;

	}


	public static List<TOBusinessHour> getBusinesshrs() {
		ArrayList<TOBusinessHour> businesshrs = new ArrayList<TOBusinessHour>();
		for (BusinessHour businesshr : FlexiBookApplication.getFlexiBook().getBusiness().getBusinessHours()) {
			TOBusinessHour toBusinessHour = new TOBusinessHour(businesshr.getStartTime(), businesshr.getEndTime());
			businesshrs.add(toBusinessHour);
		}
		return businesshrs;

	}


	public static List<TOTimeSlot> getUnavailableTimeSlots(String UATSdate) throws InvalidInputException {
		if(dateValidation(UATSdate)==false){
			throw new InvalidInputException(UATSdate + " is not a valid date");
		}
		Date givenUATS = Date.valueOf(UATSdate);
		givenUATS = cleanDate(givenUATS);
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOTimeSlot> unavailableTimeSlots = new ArrayList<>();
		for(TimeSlot unavailableTimeSlot : FlexiBookApplication.getFlexiBook().getTimeSlots()){
			for(int i = 0; i < flexiBook.getAppointments().size(); i++){
				if(flexiBook.getAppointment(i).getTimeSlot().getStartDate().equals(givenUATS) && flexiBook.getAppointment(i).getTimeSlot().getEndDate().equals(givenUATS)) {
					//check if the given date is is holiday, vacation, weekend, past date, or if any appointment was overlapping
					if (!checkDateAndTime(unavailableTimeSlot, flexiBook.getAppointment(i), flexiBook, flexiBook.getAppointment(i).getTimeSlot().getStartDate(), flexiBook.getAppointment(i).getTimeSlot().getStartTime(), false)) {
						//check if the appointment service has any downtime
						if (FlexiBookController.getServices().get(i).getDowntimeDur()!=0) {
							//consider the downtime and divide the timelots accordingly
							if(flexiBook.getAppointment(i).getBookableService() instanceof Service) {
								TOTimeSlot appStartToDT = getTOTimeSlot(flexiBook.getAppointment(i).getTimeSlot().getStartTime().toString(), UATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeStart());
								TOTimeSlot downtimeSlot = getTOTimeSlot(appStartToDT.getEndTime().toString(), UATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeDuration());
								TOTimeSlot appAfterToDT = getTOTimeSlot(downtimeSlot.getEndTime().toString(), UATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDuration() - ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeDuration());
								TOTimeSlot toTimeSlotappStart = new TOTimeSlot(givenUATS, appAfterToDT.getStartTime(), givenUATS, appAfterToDT.getEndTime());
								TOTimeSlot toTimeSlotappEnd = new TOTimeSlot(givenUATS, appAfterToDT.getStartTime(), givenUATS, appAfterToDT.getEndTime());
								unavailableTimeSlots.add(toTimeSlotappStart);
								unavailableTimeSlots.add(toTimeSlotappEnd);
							}else {
								TOTimeSlot appStartToDT = getTOTimeSlot(flexiBook.getAppointment(i).getTimeSlot().getStartTime().toString(), UATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeStart());
								TOTimeSlot downtimeSlot = getTOTimeSlot(appStartToDT.getEndTime().toString(), UATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeDuration());
								TOTimeSlot appAfterToDT = getTOTimeSlot(downtimeSlot.getEndTime().toString(), UATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDuration() - ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeDuration());
								TOTimeSlot toTimeSlotappStart = new TOTimeSlot(givenUATS, appAfterToDT.getStartTime(), givenUATS, appAfterToDT.getEndTime());
								TOTimeSlot toTimeSlotappEnd = new TOTimeSlot(givenUATS, appAfterToDT.getStartTime(), givenUATS, appAfterToDT.getEndTime());
								unavailableTimeSlots.add(toTimeSlotappStart);
								unavailableTimeSlots.add(toTimeSlotappEnd);
							}
							
							
						}
					}
				}

			}

		}
		return unavailableTimeSlots;
	}

	private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
	
	private static TOTimeSlot getTOTimeSlot(String startTime, String startDate, int duration) {

		String[] num = startTime.split(":");
		Integer hour = Integer.parseInt(num[0]);
		Integer min = Integer.parseInt(num[1]);
		LocalTime etime = LocalTime.of(hour, min);
		LocalTime localEndTime = etime.plusHours(0).plusMinutes(duration);
		Time EndTime = Time.valueOf(localEndTime);
		Time StartTime = Time.valueOf(startTime);
		Date ADate = Date.valueOf(startDate);
		TOTimeSlot timeSlot = new TOTimeSlot(ADate, StartTime, ADate, EndTime);

		return timeSlot;
	}
	
	

	public static List<TOTimeSlot> getUnavailableTimeSlotForWeek(String sdate) throws InvalidInputException{
		ArrayList<TOTimeSlot> unavailable = new ArrayList<>();
		
		try {
			Date date = Date.valueOf(sdate);
			for(int i = 1; i<8; i++){
				date = new Date(date.getTime() + i*MILLIS_IN_A_DAY);
				List<TOTimeSlot> thisUnavailable = getUnavailableTimeSlots(date.toString());
					unavailable.add(thisUnavailable.get(i));
				}
			
			return unavailable;
		} catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}
	
	public static List<TOTimeSlot> getAvailableTimeSlotForWeek(String sdate) throws InvalidInputException{
		ArrayList<TOTimeSlot> available = new ArrayList<>();
		
		try {
			Date date = Date.valueOf(sdate);
			for(int i = 1; i<8; i++){
				date = new Date(date.getTime() + i*MILLIS_IN_A_DAY);
				List<TOTimeSlot> thisAvailable = getAvailableTimeSlots(date.toString());
				for (int k = 0; k < thisAvailable.size()-1; k++) {
					available.add(thisAvailable.get(k));
				}
			}
			return available;
		} catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		
		
	}


	public static List<TOTimeSlot> getAvailableTimeSlots(String ATSdate) throws InvalidInputException {
		if(!dateValidation(ATSdate)){
			throw new InvalidInputException(ATSdate + " is not a valid date");
		}
		Date givenDate = Date.valueOf(ATSdate);
		givenDate = cleanDate(givenDate);
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOTimeSlot> availableTimeSlots = new ArrayList<>();
		for(TimeSlot availableTimeSlot : FlexiBookApplication.getFlexiBook().getTimeSlots()){
			for(int i = 0; i < flexiBook.getAppointments().size(); i++){
				if(flexiBook.getAppointment(i).getTimeSlot().getStartDate().equals(givenDate)) {
					if (checkDateAndTime(availableTimeSlot, flexiBook.getAppointment(i), flexiBook, flexiBook.getAppointment(i).getTimeSlot().getStartDate(), flexiBook.getAppointment(i).getTimeSlot().getStartTime(), false)) {
						if (FlexiBookController.getServices().get(i).getDowntimeDur()!=0) {
							
							if(flexiBook.getAppointment(i).getBookableService() instanceof Service) {
								TOTimeSlot appStartToDT = getTOTimeSlot(flexiBook.getAppointment(i).getTimeSlot().getStartTime().toString(), ATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeStart());
								TOTimeSlot downtimeSlot = getTOTimeSlot(appStartToDT.getEndTime().toString(), ATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeDuration());
								TOTimeSlot appAfterToDT = getTOTimeSlot(downtimeSlot.getEndTime().toString(), ATSdate, ((Service) flexiBook.getAppointment(i).getBookableService()).getDuration() - ((Service) flexiBook.getAppointment(i).getBookableService()).getDowntimeDuration());
								TOTimeSlot toTimeSlotDowntime = new TOTimeSlot(givenDate, downtimeSlot.getStartTime(), givenDate, downtimeSlot.getEndTime());
								availableTimeSlots.add(toTimeSlotDowntime);
								TOTimeSlot btwAppTS = new TOTimeSlot(givenDate, appAfterToDT.getEndTime(), givenDate, flexiBook.getAppointment(i+1).getTimeSlot().getStartTime());
								availableTimeSlots.add(btwAppTS);
							}else {
								TOTimeSlot appStartToDT = getTOTimeSlot(flexiBook.getAppointment(i).getTimeSlot().getStartTime().toString(), ATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeStart());
								TOTimeSlot downtimeSlot = getTOTimeSlot(appStartToDT.getEndTime().toString(), ATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeDuration());
								TOTimeSlot appAfterToDT = getTOTimeSlot(downtimeSlot.getEndTime().toString(), ATSdate, ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDuration() - ((ServiceCombo) flexiBook.getAppointment(i).getBookableService()).getMainService().getService().getDowntimeDuration());
								TOTimeSlot toTimeSlotDowntime = new TOTimeSlot(givenDate, downtimeSlot.getStartTime(), givenDate, downtimeSlot.getEndTime());
								availableTimeSlots.add(toTimeSlotDowntime);
								TOTimeSlot btwAppTS = new TOTimeSlot(givenDate, appAfterToDT.getEndTime(), givenDate, flexiBook.getAppointment(i+1).getTimeSlot().getStartTime());
								availableTimeSlots.add(btwAppTS);
							}
							

						}
					}
				}

			}

		}
		return availableTimeSlots;


	}

	public static List<TOService> getServices() {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		ArrayList<TOService> services = new ArrayList<TOService>();
		for (BookableService service : flexiBook.getBookableServices()) {
			if(service instanceof Service){
				TOService toService = new TOService(((Service)service).getName(), ((Service)service).getDuration(), ((Service)service).getDowntimeStart(), ((Service)service).getDowntimeDuration());
				services.add(toService);
			}
		}
		return services;
	}



	private static Date cleanDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		java.util.Date tempCleanedDate = cal.getTime();
		java.sql.Date cleanedDate = new java.sql.Date(tempCleanedDate.getTime());
		return cleanedDate;}

	/**
	 *
	 * @author Venkata Satyanarayana Chivatam
	 * @param flexibook
	 * @param passwordO
	 * @param usernameO
	 *
	 */
	private static void createUser(String username, String password, FlexiBook flexiBook) {
		
		if(username.equals("owner")) {
			Owner owner = new Owner(username, password, flexiBook);
			FlexiBookApplication.setCurrentUser(owner);
		}
		else {
			Customer newCustomer = new Customer(username, password, flexiBook);
			FlexiBookApplication.setCurrentUser(newCustomer);
		}
		

	}

	/**
	 *
	 * @author Venkata Satyanarayana Chivatam
	 * @param Adate
	 * @return boolean
	 * @throws InvalidInputException
	 */
	public static boolean dateValidation(String Adate) throws InvalidInputException {

		String[] arr = Adate.split("-");
		int year = Integer.parseInt(arr[0]);
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
		String ss = Adate + " is not a valid date";
		if (month == 2 && day > 28) {
			return false;
		} else if (month > 12 || day > 31) {
			return false;
		}
		return true;
	}

////////////////////////CUSTOMER ACCOUNT////////////////////

	/**
	 * @author artus
	 * @param Uname
	 * @param Pword
	 * @throws InvalidInputException
	 */

	public static void signUpCustomer(String Uname, String Pword) throws InvalidInputException {

		try {
			FlexiBook flexibook = FlexiBookApplication.getFlexiBook();

			if (Uname == null || Uname.equals("")) {
				throw new InvalidInputException("The user name cannot be empty");
			} else if (Pword.equals("") || Pword == null) {
				throw new InvalidInputException("The password cannot be empty");
			} else if (findUserByName("owner") != null && FlexiBookApplication.getCurrentUser() == findUserByName("owner")) {
				throw new InvalidInputException("You must log out of the owner account before creating a customer account");
			}
			else if (findUserByName(Uname) != null)
				throw new InvalidInputException("The username already exists");
			else {
				flexibook.addCustomer(Uname, Pword);
				FlexiBookApplication.setCurrentUser(User.getWithUsername(Uname));
				FlexiBookPersistence.save(flexibook);
			}
		} catch (RuntimeException e) {
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
	public static void accountUpdate(String oldUname, String newUname, String newPword) throws InvalidInputException {

		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Owner owner = flexiBook.getOwner();

		try {

			if (newUname == null || newUname.equals("")) {
				throw new InvalidInputException("The user name cannot be empty");
			} else if (newPword == null || newPword.equals("")) {
				throw new InvalidInputException("The password cannot be empty");
			}

			User user = findUserByName(oldUname);

			if (owner != null) {
				if (oldUname.equals(owner.getUsername())) {
					if (oldUname.equals(newUname)) {
						owner.setPassword(newPword);
						FlexiBookPersistence.save(flexiBook);
						return;
					} else {
						throw new InvalidInputException("Changing username of owner is not allowed");
					}
				}

			}
			if (!oldUname.equals("owner") && !newUname.equals("owner")) {
					List<Customer> customerList = flexiBook.getCustomers();
			User customer = null;
			for (int i = 0; i < customerList.size(); i++) {
				customer = customerList.get(i);
				if (customer.getUsername().equals(newUname) && !customer.getUsername().equals(oldUname)) {
					throw new InvalidInputException("Username not available");
				}
			}
			}

			//if (user == null) {
			//	throw new InvalidInputException("No user found");
			//}

			if (user.equals(FlexiBookApplication.getCurrentUser())) {
				if (user.getUsername().equals(oldUname) ) {
					user.setUsername(newUname);
					user.setPassword(newPword);
				}
				else if (oldUname.equals(newUname)) user.setPassword(newPword);
				else if (newPword.equals(user.getPassword())) user.setUsername(newUname);
				FlexiBookPersistence.save(flexiBook);

			} else {
				throw new InvalidInputException("You have to be logged in to the corresponding account to update it");
			}

		} catch (RuntimeException e) {
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
		User user = null;
		User thisCustomer = null;

		if (Uname.equals("owner")) {
			Owner owner = FlexiBookApplication.getFlexiBook().getOwner();
			user = owner;
		}

		else {
			List<Customer> customerList = flexiBook.getCustomers();

			for (int i = 0; i < customerList.size(); i++) {
				thisCustomer = customerList.get(i);
				if (thisCustomer.getUsername().equals(Uname)) {
					user = thisCustomer;
				}
			}
		}
		return user;
	}

	/**
	 * @author artus
	 * @param Uname
	 * @throws InvalidInputException
	 */

	public static void deleteCustomerAccount(String Uname) throws InvalidInputException {

		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		User user = findUserByName(Uname);
		User current = FlexiBookApplication.getCurrentUser();
		Appointment app;

		try {
			if (Uname.equals("owner") || !(user.equals(current))) {
				throw new InvalidInputException("You do not have permission to delete this account");
			}
			if (user.equals(current)) {
				Customer customer = (Customer) user;
				List<Appointment> applist = customer.getAppointments();
				for (int i = 0; i < applist.size(); i++) {
					app = applist.get(i);
					flexibook.removeAppointment(app);
					app.delete();
				}
				user.delete();
				flexibook.getCustomers().remove(user);
				FlexiBookPersistence.save(flexibook);
			}

			FlexiBookApplication.setCurrentUser(null);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

}
