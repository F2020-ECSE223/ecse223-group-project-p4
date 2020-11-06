/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse.flexibook.model;
import java.io.Serializable;
import java.sql.Time;
import java.util.*;

// line 11 "../../../../../FlexiBookPersistence.ump"
// line 3 "../../../../../FlexiBookStates.ump"
// line 89 "../../../../../FlexiBook.ump"
public class Appointment implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Appointment State Machines
  public enum AppointmentStatus { Booked, InProgress, Finished }
  private AppointmentStatus appointmentStatus;

  //Appointment Associations
  private Customer customer;
  private BookableService bookableService;
  private List<ComboItem> chosenItems;
  private TimeSlot timeSlot;
  private FlexiBook flexiBook;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Appointment(Customer aCustomer, BookableService aBookableService, TimeSlot aTimeSlot, FlexiBook aFlexiBook)
  {
    boolean didAddCustomer = setCustomer(aCustomer);
    if (!didAddCustomer)
    {
      throw new RuntimeException("Unable to create appointment due to customer. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddBookableService = setBookableService(aBookableService);
    if (!didAddBookableService)
    {
      throw new RuntimeException("Unable to create appointment due to bookableService. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    chosenItems = new ArrayList<ComboItem>();
    if (!setTimeSlot(aTimeSlot))
    {
      throw new RuntimeException("Unable to create Appointment due to aTimeSlot. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddFlexiBook = setFlexiBook(aFlexiBook);
    if (!didAddFlexiBook)
    {
      throw new RuntimeException("Unable to create appointment due to flexiBook. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    setAppointmentStatus(AppointmentStatus.Booked);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getAppointmentStatusFullName()
  {
    String answer = appointmentStatus.toString();
    return answer;
  }

  public AppointmentStatus getAppointmentStatus()
  {
    return appointmentStatus;
  }

  public boolean modifyAppointmentTime(Date systemDate,TimeSlot newSlot)
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case Booked:
        if (atleastDayBefore(systemDate))
        {
        // line 10 "../../../../../FlexiBookStates.ump"
          doUpdateAppointment(newSlot);
          setAppointmentStatus(AppointmentStatus.Booked);
          wasEventProcessed = true;
          break;
        }
        if (!(atleastDayBefore(systemDate)))
        {
        // line 18 "../../../../../FlexiBookStates.ump"
          rejectUpdateAppointment();
          setAppointmentStatus(AppointmentStatus.Booked);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean modifyOptionalServices(List<ComboItem> newServices,List<ComboItem> removedServices)
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case Booked:
        // line 14 "../../../../../FlexiBookStates.ump"
        changeOptServices(newServices, removedServices);
        setAppointmentStatus(AppointmentStatus.Booked);
        wasEventProcessed = true;
        break;
      case InProgress:
        // line 47 "../../../../../FlexiBookStates.ump"
        changeOptServices(newServices, removedServices);
        setAppointmentStatus(AppointmentStatus.InProgress);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelAppointment(Date systemDate)
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case Booked:
        if (atleastDayBefore(systemDate))
        {
        // line 22 "../../../../../FlexiBookStates.ump"
          doCancelAppointment();
          setAppointmentStatus(AppointmentStatus.Finished);
          wasEventProcessed = true;
          break;
        }
        if (!(atleastDayBefore(systemDate)))
        {
        // line 26 "../../../../../FlexiBookStates.ump"
          rejectCancelAppointment();
          setAppointmentStatus(AppointmentStatus.Booked);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startAppointment(Date systemDate,Time systemTime)
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case Booked:
        if (isAfterStartTime(systemDate,systemTime))
        {
        // line 30 "../../../../../FlexiBookStates.ump"
          doStartAppointment();
          setAppointmentStatus(AppointmentStatus.InProgress);
          wasEventProcessed = true;
          break;
        }
        if (!(isAfterStartTime(systemDate,systemTime)))
        {
        // line 34 "../../../../../FlexiBookStates.ump"
          rejectStartAppointment();
          setAppointmentStatus(AppointmentStatus.Booked);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean noShow(Customer customer)
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case Booked:
        // line 38 "../../../../../FlexiBookStates.ump"
        addNoShow(customer);
        setAppointmentStatus(AppointmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean finishAppointment()
  {
    boolean wasEventProcessed = false;
    
    AppointmentStatus aAppointmentStatus = appointmentStatus;
    switch (aAppointmentStatus)
    {
      case InProgress:
        setAppointmentStatus(AppointmentStatus.Finished);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setAppointmentStatus(AppointmentStatus aAppointmentStatus)
  {
    appointmentStatus = aAppointmentStatus;
  }
  /* Code from template association_GetOne */
  public Customer getCustomer()
  {
    return customer;
  }
  /* Code from template association_GetOne */
  public BookableService getBookableService()
  {
    return bookableService;
  }
  /* Code from template association_GetMany */
  public ComboItem getChosenItem(int index)
  {
    ComboItem aChosenItem = chosenItems.get(index);
    return aChosenItem;
  }

  public List<ComboItem> getChosenItems()
  {
    List<ComboItem> newChosenItems = Collections.unmodifiableList(chosenItems);
    return newChosenItems;
  }

  public int numberOfChosenItems()
  {
    int number = chosenItems.size();
    return number;
  }

  public boolean hasChosenItems()
  {
    boolean has = chosenItems.size() > 0;
    return has;
  }

  public int indexOfChosenItem(ComboItem aChosenItem)
  {
    int index = chosenItems.indexOf(aChosenItem);
    return index;
  }
  /* Code from template association_GetOne */
  public TimeSlot getTimeSlot()
  {
    return timeSlot;
  }
  /* Code from template association_GetOne */
  public FlexiBook getFlexiBook()
  {
    return flexiBook;
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
      existingCustomer.removeAppointment(this);
    }
    customer.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBookableService(BookableService aBookableService)
  {
    boolean wasSet = false;
    if (aBookableService == null)
    {
      return wasSet;
    }

    BookableService existingBookableService = bookableService;
    bookableService = aBookableService;
    if (existingBookableService != null && !existingBookableService.equals(aBookableService))
    {
      existingBookableService.removeAppointment(this);
    }
    bookableService.addAppointment(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfChosenItems()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addChosenItem(ComboItem aChosenItem)
  {
    boolean wasAdded = false;
    if (chosenItems.contains(aChosenItem)) { return false; }
    chosenItems.add(aChosenItem);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeChosenItem(ComboItem aChosenItem)
  {
    boolean wasRemoved = false;
    if (chosenItems.contains(aChosenItem))
    {
      chosenItems.remove(aChosenItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addChosenItemAt(ComboItem aChosenItem, int index)
  {  
    boolean wasAdded = false;
    if(addChosenItem(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveChosenItemAt(ComboItem aChosenItem, int index)
  {
    boolean wasAdded = false;
    if(chosenItems.contains(aChosenItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfChosenItems()) { index = numberOfChosenItems() - 1; }
      chosenItems.remove(aChosenItem);
      chosenItems.add(index, aChosenItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addChosenItemAt(aChosenItem, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTimeSlot(TimeSlot aNewTimeSlot)
  {
    boolean wasSet = false;
    if (aNewTimeSlot != null)
    {
      timeSlot = aNewTimeSlot;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setFlexiBook(FlexiBook aFlexiBook)
  {
    boolean wasSet = false;
    if (aFlexiBook == null)
    {
      return wasSet;
    }

    FlexiBook existingFlexiBook = flexiBook;
    flexiBook = aFlexiBook;
    if (existingFlexiBook != null && !existingFlexiBook.equals(aFlexiBook))
    {
      existingFlexiBook.removeAppointment(this);
    }
    flexiBook.addAppointment(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Customer placeholderCustomer = customer;
    this.customer = null;
    if(placeholderCustomer != null)
    {
      placeholderCustomer.removeAppointment(this);
    }
    BookableService placeholderBookableService = bookableService;
    this.bookableService = null;
    if(placeholderBookableService != null)
    {
      placeholderBookableService.removeAppointment(this);
    }
    chosenItems.clear();
    timeSlot = null;
    FlexiBook placeholderFlexiBook = flexiBook;
    this.flexiBook = null;
    if(placeholderFlexiBook != null)
    {
      placeholderFlexiBook.removeAppointment(this);
    }
  }

  // line 58 "../../../../../FlexiBookStates.ump"
   private void doStartAppointment(){
    
  }

  // line 61 "../../../../../FlexiBookStates.ump"
   private void rejectStartAppointment(){
    throw new RuntimeException("Not yet time for appointment.");
  }

  // line 65 "../../../../../FlexiBookStates.ump"
   private void makeAppointment(){
    //TODO: Implement;
  }

  // line 70 "../../../../../FlexiBookStates.ump"
   private void doCancelAppointment(){
    FlexiBook flexiBook = this.getFlexiBook();
		this.delete();
  }

  // line 75 "../../../../../FlexiBookStates.ump"
   private void rejectCancelAppointment(){
    throw new RuntimeException("Cannot cancel an appointment on the appointment date");
  }

  // line 79 "../../../../../FlexiBookStates.ump"
   private void doUpdateAppointment(TimeSlot newSlot){
    this.setTimeSlot(newSlot);
  }

  // line 83 "../../../../../FlexiBookStates.ump"
   private void rejectUpdateAppointment(){
    throw new RuntimeException("Cannot change appointment date and time on the appointment date");
  }

  // line 87 "../../../../../FlexiBookStates.ump"
   private void changeOptServices(List<ComboItem> newServices, List<ComboItem> removedServices){
    for(int i = 0; i < newServices.size(); i++){
			this.addChosenItem(newServices.get(i));
		}
		for(int i = 0; i < removedServices.size(); i++){
			this.removeChosenItem(removedServices.get(i));
		}
  }

  // line 96 "../../../../../FlexiBookStates.ump"
   private void addNoShow(Customer customer){
    Integer noShows = customer.getNoShows();
		noShows++;
		customer.setNoShows(noShows);
  }

  // line 102 "../../../../../FlexiBookStates.ump"
   private boolean isAfterStartTime(Date systemDate, Time systemTime){
    if(systemDate.equals(this.getTimeSlot().getStartDate()) && !systemTime.before(this.getTimeSlot().getStartTime())){
			return true;
		}
		else{
		 	return false;
		}
  }

  // line 111 "../../../../../FlexiBookStates.ump"
   private boolean atleastDayBefore(Date systemDate){
    if(systemDate.before(this.getTimeSlot().getStartDate())){
			return true;
		}
		else{
		 	return false;
		}
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 14 "../../../../../FlexiBookPersistence.ump"
  private static final long serialVersionUID = 2315072607928790501L ;

  
}