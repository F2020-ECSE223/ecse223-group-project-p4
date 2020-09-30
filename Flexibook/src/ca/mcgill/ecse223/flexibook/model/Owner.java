package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.util.*;
import java.sql.Time;

// line 21 "model.ump"
// line 131 "model.ump"
public class Owner extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Owner Associations
  private ServiceList serviceList;
  private BusinessInfo business;
  private List<Appointment> appointments;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner(String aUsername, String aPassword, ServiceList aServiceList, BusinessInfo aBusiness)
  {
    super(aUsername, aPassword);
    if (aServiceList == null || aServiceList.getOwner() != null)
    {
      throw new RuntimeException("Unable to create Owner due to aServiceList. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    serviceList = aServiceList;
    if (aBusiness == null || aBusiness.getOwner() != null)
    {
      throw new RuntimeException("Unable to create Owner due to aBusiness. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    business = aBusiness;
    appointments = new ArrayList<Appointment>();
  }

  public Owner(String aUsername, String aPassword, List aListOfServicesForServiceList, List aListOfServiceComboForServiceList, BusinessInfo aBusinessInfoForServiceList, Service aServiceForServiceList, String aBusinessNameForBusiness, String aEmailAddressForBusiness, String aPhoneNoForBusiness, Time aOpeningTimeForBusiness, Time aClosingTimeForBusiness, String aBusinessAddressForBusiness, ServiceList aServicesForBusiness, Calendar aScheduleForBusiness)
  {
    super(aUsername, aPassword);
    serviceList = new ServiceList(aListOfServicesForServiceList, aListOfServiceComboForServiceList, this, aBusinessInfoForServiceList, aServiceForServiceList);
    business = new BusinessInfo(aBusinessNameForBusiness, aEmailAddressForBusiness, aPhoneNoForBusiness, aOpeningTimeForBusiness, aClosingTimeForBusiness, aBusinessAddressForBusiness, aServicesForBusiness, this, aScheduleForBusiness);
    appointments = new ArrayList<Appointment>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public ServiceList getServiceList()
  {
    return serviceList;
  }
  /* Code from template association_GetOne */
  public BusinessInfo getBusiness()
  {
    return business;
  }
  /* Code from template association_GetMany */
  public Appointment getAppointment(int index)
  {
    Appointment aAppointment = appointments.get(index);
    return aAppointment;
  }

  public List<Appointment> getAppointments()
  {
    List<Appointment> newAppointments = Collections.unmodifiableList(appointments);
    return newAppointments;
  }

  public int numberOfAppointments()
  {
    int number = appointments.size();
    return number;
  }

  public boolean hasAppointments()
  {
    boolean has = appointments.size() > 0;
    return has;
  }

  public int indexOfAppointment(Appointment aAppointment)
  {
    int index = appointments.indexOf(aAppointment);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAppointments()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Appointment addAppointment(int aAppointmentDuration, Time aDate, Customer aCustomer, TimeSlot aBookingTime)
  {
    return new Appointment(aAppointmentDuration, aDate, aCustomer, aBookingTime, this);
  }

  public boolean addAppointment(Appointment aAppointment)
  {
    boolean wasAdded = false;
    if (appointments.contains(aAppointment)) { return false; }
    Owner existingOwner = aAppointment.getOwner();
    boolean isNewOwner = existingOwner != null && !this.equals(existingOwner);
    if (isNewOwner)
    {
      aAppointment.setOwner(this);
    }
    else
    {
      appointments.add(aAppointment);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAppointment(Appointment aAppointment)
  {
    boolean wasRemoved = false;
    //Unable to remove aAppointment, as it must always have a owner
    if (!this.equals(aAppointment.getOwner()))
    {
      appointments.remove(aAppointment);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAppointmentAt(Appointment aAppointment, int index)
  {  
    boolean wasAdded = false;
    if(addAppointment(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAppointmentAt(Appointment aAppointment, int index)
  {
    boolean wasAdded = false;
    if(appointments.contains(aAppointment))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAppointments()) { index = numberOfAppointments() - 1; }
      appointments.remove(aAppointment);
      appointments.add(index, aAppointment);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAppointmentAt(aAppointment, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ServiceList existingServiceList = serviceList;
    serviceList = null;
    if (existingServiceList != null)
    {
      existingServiceList.delete();
    }
    BusinessInfo existingBusiness = business;
    business = null;
    if (existingBusiness != null)
    {
      existingBusiness.delete();
    }
    for(int i=appointments.size(); i > 0; i--)
    {
      Appointment aAppointment = appointments.get(i - 1);
      aAppointment.delete();
    }
    super.delete();
  }

}