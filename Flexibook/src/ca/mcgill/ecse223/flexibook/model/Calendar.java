package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.util.*;
import java.sql.Time;

// line 61 "model.ump"
// line 167 "model.ump"
public class Calendar
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Calendar Attributes
  private boolean isAvailable;

  //Calendar Associations
  private BusinessInfo businessInfo;
  private List<TimeSlot> timeslots;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Calendar(boolean aIsAvailable, BusinessInfo aBusinessInfo)
  {
    isAvailable = aIsAvailable;
    if (aBusinessInfo == null || aBusinessInfo.getSchedule() != null)
    {
      throw new RuntimeException("Unable to create Calendar due to aBusinessInfo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    businessInfo = aBusinessInfo;
    timeslots = new ArrayList<TimeSlot>();
  }

  public Calendar(boolean aIsAvailable, String aBusinessNameForBusinessInfo, String aEmailAddressForBusinessInfo, String aPhoneNoForBusinessInfo, Time aOpeningTimeForBusinessInfo, Time aClosingTimeForBusinessInfo, String aBusinessAddressForBusinessInfo, ServiceList aServicesForBusinessInfo, Owner aOwnerForBusinessInfo)
  {
    isAvailable = aIsAvailable;
    businessInfo = new BusinessInfo(aBusinessNameForBusinessInfo, aEmailAddressForBusinessInfo, aPhoneNoForBusinessInfo, aOpeningTimeForBusinessInfo, aClosingTimeForBusinessInfo, aBusinessAddressForBusinessInfo, aServicesForBusinessInfo, aOwnerForBusinessInfo, this);
    timeslots = new ArrayList<TimeSlot>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsAvailable(boolean aIsAvailable)
  {
    boolean wasSet = false;
    isAvailable = aIsAvailable;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template association_GetOne */
  public BusinessInfo getBusinessInfo()
  {
    return businessInfo;
  }
  /* Code from template association_GetMany */
  public TimeSlot getTimeslot(int index)
  {
    TimeSlot aTimeslot = timeslots.get(index);
    return aTimeslot;
  }

  public List<TimeSlot> getTimeslots()
  {
    List<TimeSlot> newTimeslots = Collections.unmodifiableList(timeslots);
    return newTimeslots;
  }

  public int numberOfTimeslots()
  {
    int number = timeslots.size();
    return number;
  }

  public boolean hasTimeslots()
  {
    boolean has = timeslots.size() > 0;
    return has;
  }

  public int indexOfTimeslot(TimeSlot aTimeslot)
  {
    int index = timeslots.indexOf(aTimeslot);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTimeslots()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public TimeSlot addTimeslot(Time aStartTime, Appointment aAppointment)
  {
    return new TimeSlot(aStartTime, aAppointment, this);
  }

  public boolean addTimeslot(TimeSlot aTimeslot)
  {
    boolean wasAdded = false;
    if (timeslots.contains(aTimeslot)) { return false; }
    Calendar existingCalendar = aTimeslot.getCalendar();
    boolean isNewCalendar = existingCalendar != null && !this.equals(existingCalendar);
    if (isNewCalendar)
    {
      aTimeslot.setCalendar(this);
    }
    else
    {
      timeslots.add(aTimeslot);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTimeslot(TimeSlot aTimeslot)
  {
    boolean wasRemoved = false;
    //Unable to remove aTimeslot, as it must always have a calendar
    if (!this.equals(aTimeslot.getCalendar()))
    {
      timeslots.remove(aTimeslot);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTimeslotAt(TimeSlot aTimeslot, int index)
  {  
    boolean wasAdded = false;
    if(addTimeslot(aTimeslot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTimeslots()) { index = numberOfTimeslots() - 1; }
      timeslots.remove(aTimeslot);
      timeslots.add(index, aTimeslot);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTimeslotAt(TimeSlot aTimeslot, int index)
  {
    boolean wasAdded = false;
    if(timeslots.contains(aTimeslot))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTimeslots()) { index = numberOfTimeslots() - 1; }
      timeslots.remove(aTimeslot);
      timeslots.add(index, aTimeslot);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTimeslotAt(aTimeslot, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    BusinessInfo existingBusinessInfo = businessInfo;
    businessInfo = null;
    if (existingBusinessInfo != null)
    {
      existingBusinessInfo.delete();
    }
    while (timeslots.size() > 0)
    {
      TimeSlot aTimeslot = timeslots.get(timeslots.size() - 1);
      aTimeslot.delete();
      timeslots.remove(aTimeslot);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "isAvailable" + ":" + getIsAvailable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "businessInfo = "+(getBusinessInfo()!=null?Integer.toHexString(System.identityHashCode(getBusinessInfo())):"null");
  }
}