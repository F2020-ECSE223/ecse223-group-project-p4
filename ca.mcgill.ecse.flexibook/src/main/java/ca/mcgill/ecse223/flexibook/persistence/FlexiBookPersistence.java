package ca.mcgill.ecse223.flexibook.persistence;

import ca.mcgill.ecse.flexibook.model.FlexiBook;

public class FlexiBookPersistence {
	
	private static String filename = "flexibook.data";
	
	public static void setFilename(String filename) {
		FlexiBookPersistence.filename = filename;
	}
	
	public static void save(FlexiBook flexiBook) {
	    PersistenceObjectStream.serialize(flexiBook);
	}

	public static FlexiBook load() {
	    PersistenceObjectStream.setFilename(filename);
	    FlexiBook flexiBook = (FlexiBook) PersistenceObjectStream.deserialize();
	    // model cannot be loaded - create empty FlexiBook
	    if (flexiBook == null) {
	        flexiBook = new FlexiBook();
	    }
	    return flexiBook;
	}
	
	
	

}
