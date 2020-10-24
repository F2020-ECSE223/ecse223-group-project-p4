package ca.mcgill.ecse.flexibook.controller;

public class InvalidInputException extends Exception{

	public InvalidInputException(String errorMessage) {
		super(errorMessage);
	}

}
