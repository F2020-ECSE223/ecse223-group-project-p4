package ca.mcgill.ecse223.flexibook.controller;/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.sql.Time;

// line 2 "model.ump"
// line 12 "model.ump"
public class TOBusinessHour
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBusinessHour Attributes
  private Time startTime;
  private Time endTime;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBusinessHour(Time aStartTime, Time aEndTime)
  {
    startTime = aStartTime;
    endTime = aEndTime;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null");
  }
}