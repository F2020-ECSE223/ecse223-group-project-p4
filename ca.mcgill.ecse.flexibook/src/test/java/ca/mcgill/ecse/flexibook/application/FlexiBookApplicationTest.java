/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.sql.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcgill.ecse.flexibook.model.Business;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.Service;
import ca.mcgill.ecse223.flexibook.persistence.FlexiBookPersistence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class FlexiBookApplicationTest {

	
	@Test
	public void testPersistence() {
		
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
		Date date = FlexiBookApplication.getSystemDate();
		Service dummyService = new Service("Wash", flexiBook, 40, 0, 0);
		FlexiBookPersistence.save(flexiBook);
		
		// load model again and check it
		FlexiBook flexiBook2 = FlexiBookPersistence.load();
		Service service = (Service) flexiBook2.getBookableService(0);
		assertEquals("Wash", service.getName());
		assertEquals(40, service.getDuration());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
