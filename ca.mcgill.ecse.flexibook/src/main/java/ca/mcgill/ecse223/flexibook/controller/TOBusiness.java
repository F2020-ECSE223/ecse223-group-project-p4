package ca.mcgill.ecse223.flexibook.controller;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.util.*;

// line 3 "model.ump"
// line 49 "model.ump"
public class TOBusiness
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBusiness Attributes
  private String name;
  private String address;
  private String phoneNumber;
  private String email;

  //TOBusiness Associations
  private List<TOBusinessHour> businessHours;
  private List<TOTimeSlot> holidays;
  private List<TOTimeSlot> vacation;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBusiness(String aName, String aAddress, String aPhoneNumber, String aEmail)
  {
    name = aName;
    address = aAddress;
    phoneNumber = aPhoneNumber;
    email = aEmail;
    businessHours = new ArrayList<TOBusinessHour>();
    holidays = new ArrayList<TOTimeSlot>();
    vacation = new ArrayList<TOTimeSlot>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAddress(String aAddress)
  {
    boolean wasSet = false;
    address = aAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getAddress()
  {
    return address;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getEmail()
  {
    return email;
  }
  /* Code from template association_GetMany */
  public TOBusinessHour getBusinessHour(int index)
  {
    TOBusinessHour aBusinessHour = businessHours.get(index);
    return aBusinessHour;
  }

  public List<TOBusinessHour> getBusinessHours()
  {
    List<TOBusinessHour> newBusinessHours = Collections.unmodifiableList(businessHours);
    return newBusinessHours;
  }

  public int numberOfBusinessHours()
  {
    int number = businessHours.size();
    return number;
  }

  public boolean hasBusinessHours()
  {
    boolean has = businessHours.size() > 0;
    return has;
  }

  public int indexOfBusinessHour(TOBusinessHour aBusinessHour)
  {
    int index = businessHours.indexOf(aBusinessHour);
    return index;
  }
  /* Code from template association_GetMany */
  public TOTimeSlot getHoliday(int index)
  {
    TOTimeSlot aHoliday = holidays.get(index);
    return aHoliday;
  }

  public List<TOTimeSlot> getHolidays()
  {
    List<TOTimeSlot> newHolidays = Collections.unmodifiableList(holidays);
    return newHolidays;
  }

  public int numberOfHolidays()
  {
    int number = holidays.size();
    return number;
  }

  public boolean hasHolidays()
  {
    boolean has = holidays.size() > 0;
    return has;
  }

  public int indexOfHoliday(TOTimeSlot aHoliday)
  {
    int index = holidays.indexOf(aHoliday);
    return index;
  }
  /* Code from template association_GetMany */
  public TOTimeSlot getVacation(int index)
  {
    TOTimeSlot aVacation = vacation.get(index);
    return aVacation;
  }

  public List<TOTimeSlot> getVacation()
  {
    List<TOTimeSlot> newVacation = Collections.unmodifiableList(vacation);
    return newVacation;
  }

  public int numberOfVacation()
  {
    int number = vacation.size();
    return number;
  }

  public boolean hasVacation()
  {
    boolean has = vacation.size() > 0;
    return has;
  }

  public int indexOfVacation(TOTimeSlot aVacation)
  {
    int index = vacation.indexOf(aVacation);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBusinessHours()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addBusinessHour(TOBusinessHour aBusinessHour)
  {
    boolean wasAdded = false;
    if (businessHours.contains(aBusinessHour)) { return false; }
    businessHours.add(aBusinessHour);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBusinessHour(TOBusinessHour aBusinessHour)
  {
    boolean wasRemoved = false;
    if (businessHours.contains(aBusinessHour))
    {
      businessHours.remove(aBusinessHour);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBusinessHourAt(TOBusinessHour aBusinessHour, int index)
  {  
    boolean wasAdded = false;
    if(addBusinessHour(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBusinessHourAt(TOBusinessHour aBusinessHour, int index)
  {
    boolean wasAdded = false;
    if(businessHours.contains(aBusinessHour))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBusinessHours()) { index = numberOfBusinessHours() - 1; }
      businessHours.remove(aBusinessHour);
      businessHours.add(index, aBusinessHour);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBusinessHourAt(aBusinessHour, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfHolidays()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addHoliday(TOTimeSlot aHoliday)
  {
    boolean wasAdded = false;
    if (holidays.contains(aHoliday)) { return false; }
    holidays.add(aHoliday);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeHoliday(TOTimeSlot aHoliday)
  {
    boolean wasRemoved = false;
    if (holidays.contains(aHoliday))
    {
      holidays.remove(aHoliday);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addHolidayAt(TOTimeSlot aHoliday, int index)
  {  
    boolean wasAdded = false;
    if(addHoliday(aHoliday))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfHolidays()) { index = numberOfHolidays() - 1; }
      holidays.remove(aHoliday);
      holidays.add(index, aHoliday);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveHolidayAt(TOTimeSlot aHoliday, int index)
  {
    boolean wasAdded = false;
    if(holidays.contains(aHoliday))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfHolidays()) { index = numberOfHolidays() - 1; }
      holidays.remove(aHoliday);
      holidays.add(index, aHoliday);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addHolidayAt(aHoliday, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfVacation()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addVacation(TOTimeSlot aVacation)
  {
    boolean wasAdded = false;
    if (vacation.contains(aVacation)) { return false; }
    vacation.add(aVacation);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeVacation(TOTimeSlot aVacation)
  {
    boolean wasRemoved = false;
    if (vacation.contains(aVacation))
    {
      vacation.remove(aVacation);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addVacationAt(TOTimeSlot aVacation, int index)
  {  
    boolean wasAdded = false;
    if(addVacation(aVacation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVacation()) { index = numberOfVacation() - 1; }
      vacation.remove(aVacation);
      vacation.add(index, aVacation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveVacationAt(TOTimeSlot aVacation, int index)
  {
    boolean wasAdded = false;
    if(vacation.contains(aVacation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfVacation()) { index = numberOfVacation() - 1; }
      vacation.remove(aVacation);
      vacation.add(index, aVacation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addVacationAt(aVacation, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    businessHours.clear();
    holidays.clear();
    vacation.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "address" + ":" + getAddress()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "," +
            "email" + ":" + getEmail()+ "]";
  }
}