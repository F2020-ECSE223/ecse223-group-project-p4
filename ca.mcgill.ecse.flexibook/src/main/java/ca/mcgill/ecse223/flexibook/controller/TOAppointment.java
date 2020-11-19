/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.controller;

// line 3 "../../../../../FlexiBookTransferObjects.ump"
public class TOAppointment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOAppointment Attributes
  private String customerName;
  private String service;
  private String startTime;
  private String startDate;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOAppointment(String aCustomerName, String aService, String aStartTime, String aStartDate)
  {
    customerName = aCustomerName;
    service = aService;
    startTime = aStartTime;
    startDate = aStartDate;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCustomerName(String aCustomerName)
  {
    boolean wasSet = false;
    customerName = aCustomerName;
    wasSet = true;
    return wasSet;
  }

  public boolean setService(String aService)
  {
    boolean wasSet = false;
    service = aService;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(String aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(String aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public String getCustomerName()
  {
    return customerName;
  }

  public String getService()
  {
    return service;
  }

  public String getStartTime()
  {
    return startTime;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "customerName" + ":" + getCustomerName()+ "," +
            "service" + ":" + getService()+ "," +
            "startTime" + ":" + getStartTime()+ "," +
            "startDate" + ":" + getStartDate()+ "]";
  }
}