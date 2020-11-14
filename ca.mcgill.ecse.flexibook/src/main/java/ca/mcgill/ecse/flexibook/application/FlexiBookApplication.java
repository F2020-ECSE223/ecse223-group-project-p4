/*
 /*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;

import java.sql.Date;
import java.sql.Time;

import ca.mcgill.ecse.flexibook.model.Customer;
import ca.mcgill.ecse.flexibook.model.Appointment;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Owner;
import ca.mcgill.ecse.flexibook.model.User;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;
import ca.mcgill.ecse223.flexibook.view.FlexiBookPage;

public class FlexiBookApplication {

	private static FlexiBook flexiBook;
	private static User currentUser; 
	private static Time currentTime;
	private static Date currentDate; 
	private static Appointment currentAppointment; 
	
	
	public static void main(String[] args) {
		
		// start UI
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FlexiBookPage().setVisible(true);
            }
        });
        
	}
	

	public static FlexiBook getFlexiBook() {
		if (flexiBook == null) {
			// load model
	        flexiBook = FlexiBookPersistence.load();
		}
 		return flexiBook;
	}

	
	public static User getCurrentUser() {
		return currentUser;
	}


	public static User setCurrentUser(User user) {
		currentUser = user;
		return currentUser;
	}


	public static Date setSystemDate(String date) {
		currentDate= Date.valueOf(date);
		return currentDate; 
	}

	public static Time setSystemTime(String time) {
		currentTime = Time.valueOf(time);
		return currentTime; 
	}

	public static Date getSystemDate() {
		return currentDate; 
	}


	public static Time getSystemTime() {
		return currentTime; 
	}

	public static User findUser(String name) {
		if(name.equalsIgnoreCase("owner")) {
			Owner owner = flexiBook.getOwner();
			return owner;
		}
		else {
			for (int i=0; i<flexiBook.getCustomers().size(); i++) {
				if(flexiBook.getCustomer(i).getUsername().equalsIgnoreCase(name)) {
					Customer customer = flexiBook.getCustomer(i);
					return customer;
				}
			}
		}
		return null;
	}

	public static Appointment getAppointment() {
		return currentAppointment; 
	}
	
	public static Appointment setAppointment(Appointment appointment) {
		currentAppointment = appointment; 
		return currentAppointment;
		
	}

}