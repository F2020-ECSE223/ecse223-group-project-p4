package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/



// line 103 "model.ump"
// line 194 "model.ump"
public class ClosedTiming
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Duration { Start, End }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ClosedTiming Attributes
  private Duration luchBreak;
  private Duration vacation;
  private Duration holiday;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ClosedTiming(Duration aLuchBreak, Duration aVacation, Duration aHoliday)
  {
    luchBreak = aLuchBreak;
    vacation = aVacation;
    holiday = aHoliday;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLuchBreak(Duration aLuchBreak)
  {
    boolean wasSet = false;
    luchBreak = aLuchBreak;
    wasSet = true;
    return wasSet;
  }

  public boolean setVacation(Duration aVacation)
  {
    boolean wasSet = false;
    vacation = aVacation;
    wasSet = true;
    return wasSet;
  }

  public boolean setHoliday(Duration aHoliday)
  {
    boolean wasSet = false;
    holiday = aHoliday;
    wasSet = true;
    return wasSet;
  }

  public Duration getLuchBreak()
  {
    return luchBreak;
  }

  public Duration getVacation()
  {
    return vacation;
  }

  public Duration getHoliday()
  {
    return holiday;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "luchBreak" + "=" + (getLuchBreak() != null ? !getLuchBreak().equals(this)  ? getLuchBreak().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "vacation" + "=" + (getVacation() != null ? !getVacation().equals(this)  ? getVacation().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "holiday" + "=" + (getHoliday() != null ? !getHoliday().equals(this)  ? getHoliday().toString().replaceAll("  ","    ") : "this" : "null");
  }
}