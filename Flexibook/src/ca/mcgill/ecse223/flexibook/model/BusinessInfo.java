package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.sql.Time;
import java.util.*;

// line 36 "model.ump"
// line 142 "model.ump"
public class BusinessInfo
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BusinessInfo Attributes
  private String businessName;
  private String emailAddress;
  private String phoneNo;
  private Time openingTime;
  private Time closingTime;
  private String businessAddress;

  //BusinessInfo Associations
  private ServiceList services;
  private List<Customer> customerProfiles;
  private Owner owner;
  private Calendar schedule;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BusinessInfo(String aBusinessName, String aEmailAddress, String aPhoneNo, Time aOpeningTime, Time aClosingTime, String aBusinessAddress, ServiceList aServices, Owner aOwner, Calendar aSchedule)
  {
    businessName = aBusinessName;
    emailAddress = aEmailAddress;
    phoneNo = aPhoneNo;
    openingTime = aOpeningTime;
    closingTime = aClosingTime;
    businessAddress = aBusinessAddress;
    if (aServices == null || aServices.getBusinessInfo() != null)
    {
      throw new RuntimeException("Unable to create BusinessInfo due to aServices. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    services = aServices;
    customerProfiles = new ArrayList<Customer>();
    if (aOwner == null || aOwner.getBusiness() != null)
    {
      throw new RuntimeException("Unable to create BusinessInfo due to aOwner. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    owner = aOwner;
    if (aSchedule == null || aSchedule.getBusinessInfo() != null)
    {
      throw new RuntimeException("Unable to create BusinessInfo due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    schedule = aSchedule;
  }

  public BusinessInfo(String aBusinessName, String aEmailAddress, String aPhoneNo, Time aOpeningTime, Time aClosingTime, String aBusinessAddress, List aListOfServicesForServices, List aListOfServiceComboForServices, Owner aOwnerForServices, Service aServiceForServices, String aUsernameForOwner, String aPasswordForOwner, ServiceList aServiceListForOwner, boolean aIsAvailableForSchedule)
  {
    businessName = aBusinessName;
    emailAddress = aEmailAddress;
    phoneNo = aPhoneNo;
    openingTime = aOpeningTime;
    closingTime = aClosingTime;
    businessAddress = aBusinessAddress;
    services = new ServiceList(aListOfServicesForServices, aListOfServiceComboForServices, aOwnerForServices, this, aServiceForServices);
    customerProfiles = new ArrayList<Customer>();
    owner = new Owner(aUsernameForOwner, aPasswordForOwner, aServiceListForOwner, this);
    schedule = new Calendar(aIsAvailableForSchedule, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setBusinessName(String aBusinessName)
  {
    boolean wasSet = false;
    businessName = aBusinessName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmailAddress(String aEmailAddress)
  {
    boolean wasSet = false;
    emailAddress = aEmailAddress;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNo(String aPhoneNo)
  {
    boolean wasSet = false;
    phoneNo = aPhoneNo;
    wasSet = true;
    return wasSet;
  }

  public boolean setOpeningTime(Time aOpeningTime)
  {
    boolean wasSet = false;
    openingTime = aOpeningTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setClosingTime(Time aClosingTime)
  {
    boolean wasSet = false;
    closingTime = aClosingTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setBusinessAddress(String aBusinessAddress)
  {
    boolean wasSet = false;
    businessAddress = aBusinessAddress;
    wasSet = true;
    return wasSet;
  }

  public String getBusinessName()
  {
    return businessName;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getPhoneNo()
  {
    return phoneNo;
  }

  public Time getOpeningTime()
  {
    return openingTime;
  }

  public Time getClosingTime()
  {
    return closingTime;
  }

  public String getBusinessAddress()
  {
    return businessAddress;
  }
  /* Code from template association_GetOne */
  public ServiceList getServices()
  {
    return services;
  }
  /* Code from template association_GetMany */
  public Customer getCustomerProfile(int index)
  {
    Customer aCustomerProfile = customerProfiles.get(index);
    return aCustomerProfile;
  }

  public List<Customer> getCustomerProfiles()
  {
    List<Customer> newCustomerProfiles = Collections.unmodifiableList(customerProfiles);
    return newCustomerProfiles;
  }

  public int numberOfCustomerProfiles()
  {
    int number = customerProfiles.size();
    return number;
  }

  public boolean hasCustomerProfiles()
  {
    boolean has = customerProfiles.size() > 0;
    return has;
  }

  public int indexOfCustomerProfile(Customer aCustomerProfile)
  {
    int index = customerProfiles.indexOf(aCustomerProfile);
    return index;
  }
  /* Code from template association_GetOne */
  public Owner getOwner()
  {
    return owner;
  }
  /* Code from template association_GetOne */
  public Calendar getSchedule()
  {
    return schedule;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCustomerProfiles()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Customer addCustomerProfile(String aUsername, String aPassword, SignUp aAccount)
  {
    return new Customer(aUsername, aPassword, aAccount, this);
  }

  public boolean addCustomerProfile(Customer aCustomerProfile)
  {
    boolean wasAdded = false;
    if (customerProfiles.contains(aCustomerProfile)) { return false; }
    BusinessInfo existingBusinessInfo = aCustomerProfile.getBusinessInfo();
    boolean isNewBusinessInfo = existingBusinessInfo != null && !this.equals(existingBusinessInfo);
    if (isNewBusinessInfo)
    {
      aCustomerProfile.setBusinessInfo(this);
    }
    else
    {
      customerProfiles.add(aCustomerProfile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCustomerProfile(Customer aCustomerProfile)
  {
    boolean wasRemoved = false;
    //Unable to remove aCustomerProfile, as it must always have a businessInfo
    if (!this.equals(aCustomerProfile.getBusinessInfo()))
    {
      customerProfiles.remove(aCustomerProfile);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCustomerProfileAt(Customer aCustomerProfile, int index)
  {  
    boolean wasAdded = false;
    if(addCustomerProfile(aCustomerProfile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerProfiles()) { index = numberOfCustomerProfiles() - 1; }
      customerProfiles.remove(aCustomerProfile);
      customerProfiles.add(index, aCustomerProfile);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCustomerProfileAt(Customer aCustomerProfile, int index)
  {
    boolean wasAdded = false;
    if(customerProfiles.contains(aCustomerProfile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCustomerProfiles()) { index = numberOfCustomerProfiles() - 1; }
      customerProfiles.remove(aCustomerProfile);
      customerProfiles.add(index, aCustomerProfile);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCustomerProfileAt(aCustomerProfile, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ServiceList existingServices = services;
    services = null;
    if (existingServices != null)
    {
      existingServices.delete();
    }
    for(int i=customerProfiles.size(); i > 0; i--)
    {
      Customer aCustomerProfile = customerProfiles.get(i - 1);
      aCustomerProfile.delete();
    }
    Owner existingOwner = owner;
    owner = null;
    if (existingOwner != null)
    {
      existingOwner.delete();
    }
    Calendar existingSchedule = schedule;
    schedule = null;
    if (existingSchedule != null)
    {
      existingSchedule.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "businessName" + ":" + getBusinessName()+ "," +
            "emailAddress" + ":" + getEmailAddress()+ "," +
            "phoneNo" + ":" + getPhoneNo()+ "," +
            "businessAddress" + ":" + getBusinessAddress()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "openingTime" + "=" + (getOpeningTime() != null ? !getOpeningTime().equals(this)  ? getOpeningTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "closingTime" + "=" + (getClosingTime() != null ? !getClosingTime().equals(this)  ? getClosingTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "services = "+(getServices()!=null?Integer.toHexString(System.identityHashCode(getServices())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "owner = "+(getOwner()!=null?Integer.toHexString(System.identityHashCode(getOwner())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "schedule = "+(getSchedule()!=null?Integer.toHexString(System.identityHashCode(getSchedule())):"null");
  }
}