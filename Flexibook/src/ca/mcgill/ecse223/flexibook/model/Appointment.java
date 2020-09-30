package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.sql.Time;
import java.util.*;

// line 50 "model.ump"
// line 153 "model.ump"
public class Appointment
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment Attributes
  private int appointmentDuration;
  private Time date;

  //Appointment Associations
  private Customer customer;
  private TimeSlot bookingTime;
  private List<ServiceCombo> services;
  private Owner owner;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(int aAppointmentDuration, Time aDate, Customer aCustomer, TimeSlot aBookingTime, Owner aOwner)
  {
    appointmentDuration = aAppointmentDuration;
    date = aDate;
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create booking due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (aBookingTime == null || aBookingTime.getAppointment() != null)
    {
      throw new RuntimeException("Unable to create Appointment due to aBookingTime. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    bookingTime = aBookingTime;
    services = new ArrayList<ServiceCombo>();
    boolean didAddOwner = setOwner(aOwner);
    if (!didAddOwner)
    {
      throw new RuntimeException("Unable to create appointment due to owner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Appointment(int aAppointmentDuration, Time aDate, Customer aCustomer, Time aStartTimeForBookingTime, Calendar aCalendarForBookingTime, Owner aOwner)
  {
    appointmentDuration = aAppointmentDuration;
    date = aDate;
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create booking due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    bookingTime = new TimeSlot(aStartTimeForBookingTime, this, aCalendarForBookingTime);
    services = new ArrayList<ServiceCombo>();
    boolean didAddOwner = setOwner(aOwner);
    if (!didAddOwner)
    {
      throw new RuntimeException("Unable to create appointment due to owner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAppointmentDuration(int aAppointmentDuration)
  {
    boolean wasSet = false;
    appointmentDuration = aAppointmentDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Time aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public int getAppointmentDuration()
  {
    return appointmentDuration;
  }

  public Time getDate()
  {
    return date;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public TimeSlot getBookingTime()
  {
    return bookingTime;
  }
  /* Code from template association_GetMany */
  public ServiceCombo getService(int index)
  {
    ServiceCombo aService = services.get(index);
    return aService;
  }

  public List<ServiceCombo> getServices()
  {
    List<ServiceCombo> newServices = Collections.unmodifiableList(services);
    return newServices;
  }

  public int numberOfServices()
  {
    int number = services.size();
    return number;
  }

  public boolean hasServices()
  {
    boolean has = services.size() > 0;
    return has;
  }

  public int indexOfService(ServiceCombo aService)
  {
    int index = services.indexOf(aService);
    return index;
  }
  /* Code from template association_GetOne */
  public Owner getOwner()
  {
    return owner;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCustomer(Customer aCustomer)
  {
    boolean wasSet = false;
    if (aCustomer == null)
    {
      return wasSet;
    }

    Customer existingCustomer = customer;
    customer = aCustomer;
    if (existingCustomer != null && !existingCustomer.equals(aCustomer))
    {
      existingCustomer.removeBooking(this);
    }
    customer.addBooking(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfServices()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addService(ServiceCombo aService)
  {
    boolean wasAdded = false;
    if (services.contains(aService)) { return false; }
    services.add(aService);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeService(ServiceCombo aService)
  {
    boolean wasRemoved = false;
    if (services.contains(aService))
    {
      services.remove(aService);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceAt(ServiceCombo aService, int index)
  {  
    boolean wasAdded = false;
    if(addService(aService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServices()) { index = numberOfServices() - 1; }
      services.remove(aService);
      services.add(index, aService);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveServiceAt(ServiceCombo aService, int index)
  {
    boolean wasAdded = false;
    if(services.contains(aService))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServices()) { index = numberOfServices() - 1; }
      services.remove(aService);
      services.add(index, aService);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addServiceAt(aService, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOwner(Owner aOwner)
  {
    boolean wasSet = false;
    if (aOwner == null)
    {
      return wasSet;
    }

    Owner existingOwner = owner;
    owner = aOwner;
    if (existingOwner != null && !existingOwner.equals(aOwner))
    {
      existingOwner.removeAppointment(this);
    }
    owner.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeBooking(this);
    }
    TimeSlot existingBookingTime = bookingTime;
    bookingTime = null;
    if (existingBookingTime != null)
    {
      existingBookingTime.delete();
    }
    services.clear();
    Owner placeholderOwner = owner;
    this.owner = null;
    if(placeholderOwner != null)
    {
      placeholderOwner.removeAppointment(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "appointmentDuration" + ":" + getAppointmentDuration()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "customer = "+(getCustomer()!=null?Integer.toHexString(System.identityHashCode(getCustomer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bookingTime = "+(getBookingTime()!=null?Integer.toHexString(System.identityHashCode(getBookingTime())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "owner = "+(getOwner()!=null?Integer.toHexString(System.identityHashCode(getOwner())):"null");
  }
}