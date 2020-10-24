package ca.mcgill.ecse223.flexibook.controller;

public class InvalidInputException extends Exception{

	public InvalidInputException(String errorMessage) {
		super(errorMessage);
	}

}
