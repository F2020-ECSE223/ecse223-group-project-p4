package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.util.*;
import java.sql.Time;

// line 6 "model.ump"
// line 120 "model.ump"
public class Customer extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Customer Associations
  private SignUp account;
  private BusinessInfo businessInfo;
  private List<Appointment> booking;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Customer(String aUsername, String aPassword, SignUp aAccount, BusinessInfo aBusinessInfo)
  {
    super(aUsername, aPassword);
    if (aAccount == null || aAccount.getRegister() != null)
    {
      throw new RuntimeException("Unable to create Customer due to aAccount. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    account = aAccount;
    boolean didAddBusinessInfo = setBusinessInfo(aBusinessInfo);
    if (!didAddBusinessInfo)
    {
      throw new RuntimeException("Unable to create customerProfile due to businessInfo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    booking = new ArrayList<Appointment>();
  }

  public Customer(String aUsername, String aPassword, BusinessInfo aBusinessInfo)
  {
    super(aUsername, aPassword);
    account = new SignUp(this);
    boolean didAddBusinessInfo = setBusinessInfo(aBusinessInfo);
    if (!didAddBusinessInfo)
    {
      throw new RuntimeException("Unable to create customerProfile due to businessInfo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    booking = new ArrayList<Appointment>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public SignUp getAccount()
  {
    return account;
  }
  /* Code from template association_GetOne */
  public BusinessInfo getBusinessInfo()
  {
    return businessInfo;
  }
  /* Code from template association_GetMany */
  public Appointment getBooking(int index)
  {
    Appointment aBooking = booking.get(index);
    return aBooking;
  }

  public List<Appointment> getBooking()
  {
    List<Appointment> newBooking = Collections.unmodifiableList(booking);
    return newBooking;
  }

  public int numberOfBooking()
  {
    int number = booking.size();
    return number;
  }

  public boolean hasBooking()
  {
    boolean has = booking.size() > 0;
    return has;
  }

  public int indexOfBooking(Appointment aBooking)
  {
    int index = booking.indexOf(aBooking);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBusinessInfo(BusinessInfo aBusinessInfo)
  {
    boolean wasSet = false;
    if (aBusinessInfo == null)
    {
      return wasSet;
    }

    BusinessInfo existingBusinessInfo = businessInfo;
    businessInfo = aBusinessInfo;
    if (existingBusinessInfo != null && !existingBusinessInfo.equals(aBusinessInfo))
    {
      existingBusinessInfo.removeCustomerProfile(this);
    }
    businessInfo.addCustomerProfile(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBooking()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Appointment addBooking(int aAppointmentDuration, Time aDate, TimeSlot aBookingTime, Owner aOwner)
  {
    return new Appointment(aAppointmentDuration, aDate, this, aBookingTime, aOwner);
  }

  public boolean addBooking(Appointment aBooking)
  {
    boolean wasAdded = false;
    if (booking.contains(aBooking)) { return false; }
    Customer existingCustomer = aBooking.getCustomer();
    boolean isNewCustomer = existingCustomer != null && !this.equals(existingCustomer);
    if (isNewCustomer)
    {
      aBooking.setCustomer(this);
    }
    else
    {
      booking.add(aBooking);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBooking(Appointment aBooking)
  {
    boolean wasRemoved = false;
    //Unable to remove aBooking, as it must always have a customer
    if (!this.equals(aBooking.getCustomer()))
    {
      booking.remove(aBooking);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBookingAt(Appointment aBooking, int index)
  {  
    boolean wasAdded = false;
    if(addBooking(aBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBooking()) { index = numberOfBooking() - 1; }
      booking.remove(aBooking);
      booking.add(index, aBooking);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBookingAt(Appointment aBooking, int index)
  {
    boolean wasAdded = false;
    if(booking.contains(aBooking))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBooking()) { index = numberOfBooking() - 1; }
      booking.remove(aBooking);
      booking.add(index, aBooking);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBookingAt(aBooking, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    SignUp existingAccount = account;
    account = null;
    if (existingAccount != null)
    {
      existingAccount.delete();
    }
    BusinessInfo placeholderBusinessInfo = businessInfo;
    this.businessInfo = null;
    if(placeholderBusinessInfo != null)
    {
      placeholderBusinessInfo.removeCustomerProfile(this);
    }
    for(int i=booking.size(); i > 0; i--)
    {
      Appointment aBooking = booking.get(i - 1);
      aBooking.delete();
    }
    super.delete();
  }

}