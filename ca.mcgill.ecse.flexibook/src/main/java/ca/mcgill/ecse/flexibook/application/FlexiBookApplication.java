/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;

import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.User;



public class FlexiBookApplication {
	
    public static FlexiBook getFlexiBook() {
        return new FlexiBook();
    }

    public static void main(String[] args) {
        //System.out.println(new FlexiBookApplication().getGreeting());
    }
    
    public static User getCurrentUser() {
    	return null;
    }
    
    public static void setCurrentUser(String username) {
    	
    }
}
