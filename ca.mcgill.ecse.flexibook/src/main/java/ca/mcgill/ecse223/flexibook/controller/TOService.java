/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/

package ca.mcgill.ecse223.flexibook.controller;

// line 12 "../../../../../FlexiBookTransferObjects.ump"
public class TOService
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOService Attributes
  private String serviceName;
  private int serviceDur;
  private int downtimeDur;
  private int downtimeDurStart;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOService(String aServiceName, int aServiceDur, int aDowntimeDur, int aDowntimeDurStart)
  {
    serviceName = aServiceName;
    serviceDur = aServiceDur;
    downtimeDur = aDowntimeDur;
    downtimeDurStart = aDowntimeDurStart;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setServiceName(String aServiceName)
  {
    boolean wasSet = false;
    serviceName = aServiceName;
    wasSet = true;
    return wasSet;
  }

  public boolean setServiceDur(int aServiceDur)
  {
    boolean wasSet = false;
    serviceDur = aServiceDur;
    wasSet = true;
    return wasSet;
  }

  public boolean setDowntimeDur(int aDowntimeDur)
  {
    boolean wasSet = false;
    downtimeDur = aDowntimeDur;
    wasSet = true;
    return wasSet;
  }

  public boolean setDowntimeDurStart(int aDowntimeDurStart)
  {
    boolean wasSet = false;
    downtimeDurStart = aDowntimeDurStart;
    wasSet = true;
    return wasSet;
  }

  public String getServiceName()
  {
    return serviceName;
  }

  public int getServiceDur()
  {
    return serviceDur;
  }

  public int getDowntimeDur()
  {
    return downtimeDur;
  }

  public int getDowntimeDurStart()
  {
    return downtimeDurStart;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "serviceName" + ":" + getServiceName()+ "," +
            "serviceDur" + ":" + getServiceDur()+ "," +
            "downtimeDur" + ":" + getDowntimeDur()+ "," +
            "downtimeDurStart" + ":" + getDowntimeDurStart()+ "]";
  }
}