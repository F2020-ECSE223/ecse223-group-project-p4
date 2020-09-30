package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/


import java.util.*;

// line 12 "model.ump"
// line 126 "model.ump"
public class Service
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Service Attributes
  private String serviceCost;
  private String serviceName;
  private String serviceDuration;
  private String serviceDowntime;
  private String serviceDescription;

  //Service Associations
  private List<ServiceList> serviceLists;
  private List<ServiceCombo> serviceCombos;
  private ServiceCombo serviceCombo;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Service(String aServiceCost, String aServiceName, String aServiceDuration, String aServiceDowntime, String aServiceDescription, ServiceCombo aServiceCombo)
  {
    serviceCost = aServiceCost;
    serviceName = aServiceName;
    serviceDuration = aServiceDuration;
    serviceDowntime = aServiceDowntime;
    serviceDescription = aServiceDescription;
    serviceLists = new ArrayList<ServiceList>();
    serviceCombos = new ArrayList<ServiceCombo>();
    boolean didAddServiceCombo = setServiceCombo(aServiceCombo);
    if (!didAddServiceCombo)
    {
      throw new RuntimeException("Unable to create mainService due to serviceCombo. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setServiceCost(String aServiceCost)
  {
    boolean wasSet = false;
    serviceCost = aServiceCost;
    wasSet = true;
    return wasSet;
  }

  public boolean setServiceName(String aServiceName)
  {
    boolean wasSet = false;
    serviceName = aServiceName;
    wasSet = true;
    return wasSet;
  }

  public boolean setServiceDuration(String aServiceDuration)
  {
    boolean wasSet = false;
    serviceDuration = aServiceDuration;
    wasSet = true;
    return wasSet;
  }

  public boolean setServiceDowntime(String aServiceDowntime)
  {
    boolean wasSet = false;
    serviceDowntime = aServiceDowntime;
    wasSet = true;
    return wasSet;
  }

  public boolean setServiceDescription(String aServiceDescription)
  {
    boolean wasSet = false;
    serviceDescription = aServiceDescription;
    wasSet = true;
    return wasSet;
  }

  public String getServiceCost()
  {
    return serviceCost;
  }

  public String getServiceName()
  {
    return serviceName;
  }

  public String getServiceDuration()
  {
    return serviceDuration;
  }

  public String getServiceDowntime()
  {
    return serviceDowntime;
  }

  public String getServiceDescription()
  {
    return serviceDescription;
  }
  /* Code from template association_GetMany */
  public ServiceList getServiceList(int index)
  {
    ServiceList aServiceList = serviceLists.get(index);
    return aServiceList;
  }

  public List<ServiceList> getServiceLists()
  {
    List<ServiceList> newServiceLists = Collections.unmodifiableList(serviceLists);
    return newServiceLists;
  }

  public int numberOfServiceLists()
  {
    int number = serviceLists.size();
    return number;
  }

  public boolean hasServiceLists()
  {
    boolean has = serviceLists.size() > 0;
    return has;
  }

  public int indexOfServiceList(ServiceList aServiceList)
  {
    int index = serviceLists.indexOf(aServiceList);
    return index;
  }
  /* Code from template association_GetMany */
  public ServiceCombo getServiceCombo(int index)
  {
    ServiceCombo aServiceCombo = serviceCombos.get(index);
    return aServiceCombo;
  }

  public List<ServiceCombo> getServiceCombos()
  {
    List<ServiceCombo> newServiceCombos = Collections.unmodifiableList(serviceCombos);
    return newServiceCombos;
  }

  public int numberOfServiceCombos()
  {
    int number = serviceCombos.size();
    return number;
  }

  public boolean hasServiceCombos()
  {
    boolean has = serviceCombos.size() > 0;
    return has;
  }

  public int indexOfServiceCombo(ServiceCombo aServiceCombo)
  {
    int index = serviceCombos.indexOf(aServiceCombo);
    return index;
  }
  /* Code from template association_GetOne */
  public ServiceCombo getServiceCombo()
  {
    return serviceCombo;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfServiceLists()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ServiceList addServiceList(List aListOfServices, List aListOfServiceCombo, Owner aOwner, BusinessInfo aBusinessInfo)
  {
    return new ServiceList(aListOfServices, aListOfServiceCombo, aOwner, aBusinessInfo, this);
  }

  public boolean addServiceList(ServiceList aServiceList)
  {
    boolean wasAdded = false;
    if (serviceLists.contains(aServiceList)) { return false; }
    Service existingService = aServiceList.getService();
    boolean isNewService = existingService != null && !this.equals(existingService);
    if (isNewService)
    {
      aServiceList.setService(this);
    }
    else
    {
      serviceLists.add(aServiceList);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeServiceList(ServiceList aServiceList)
  {
    boolean wasRemoved = false;
    //Unable to remove aServiceList, as it must always have a service
    if (!this.equals(aServiceList.getService()))
    {
      serviceLists.remove(aServiceList);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceListAt(ServiceList aServiceList, int index)
  {  
    boolean wasAdded = false;
    if(addServiceList(aServiceList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceLists()) { index = numberOfServiceLists() - 1; }
      serviceLists.remove(aServiceList);
      serviceLists.add(index, aServiceList);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveServiceListAt(ServiceList aServiceList, int index)
  {
    boolean wasAdded = false;
    if(serviceLists.contains(aServiceList))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceLists()) { index = numberOfServiceLists() - 1; }
      serviceLists.remove(aServiceList);
      serviceLists.add(index, aServiceList);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addServiceListAt(aServiceList, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfServiceCombos()
  {
    return 0;
  }
  /* Code from template association_AddManyToManyMethod */
  public boolean addServiceCombo(ServiceCombo aServiceCombo)
  {
    boolean wasAdded = false;
    if (serviceCombos.contains(aServiceCombo)) { return false; }
    serviceCombos.add(aServiceCombo);
    if (aServiceCombo.indexOfService(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aServiceCombo.addService(this);
      if (!wasAdded)
      {
        serviceCombos.remove(aServiceCombo);
      }
    }
    return wasAdded;
  }
  /* Code from template association_RemoveMany */
  public boolean removeServiceCombo(ServiceCombo aServiceCombo)
  {
    boolean wasRemoved = false;
    if (!serviceCombos.contains(aServiceCombo))
    {
      return wasRemoved;
    }

    int oldIndex = serviceCombos.indexOf(aServiceCombo);
    serviceCombos.remove(oldIndex);
    if (aServiceCombo.indexOfService(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aServiceCombo.removeService(this);
      if (!wasRemoved)
      {
        serviceCombos.add(oldIndex,aServiceCombo);
      }
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addServiceComboAt(ServiceCombo aServiceCombo, int index)
  {  
    boolean wasAdded = false;
    if(addServiceCombo(aServiceCombo))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceCombos()) { index = numberOfServiceCombos() - 1; }
      serviceCombos.remove(aServiceCombo);
      serviceCombos.add(index, aServiceCombo);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveServiceComboAt(ServiceCombo aServiceCombo, int index)
  {
    boolean wasAdded = false;
    if(serviceCombos.contains(aServiceCombo))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfServiceCombos()) { index = numberOfServiceCombos() - 1; }
      serviceCombos.remove(aServiceCombo);
      serviceCombos.add(index, aServiceCombo);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addServiceComboAt(aServiceCombo, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setServiceCombo(ServiceCombo aServiceCombo)
  {
    boolean wasSet = false;
    if (aServiceCombo == null)
    {
      return wasSet;
    }

    ServiceCombo existingServiceCombo = serviceCombo;
    serviceCombo = aServiceCombo;
    if (existingServiceCombo != null && !existingServiceCombo.equals(aServiceCombo))
    {
      existingServiceCombo.removeMainService(this);
    }
    serviceCombo.addMainService(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    while (serviceLists.size() > 0)
    {
      ServiceList aServiceList = serviceLists.get(serviceLists.size() - 1);
      aServiceList.delete();
      serviceLists.remove(aServiceList);
    }
    
    ArrayList<ServiceCombo> copyOfServiceCombos = new ArrayList<ServiceCombo>(serviceCombos);
    serviceCombos.clear();
    for(ServiceCombo aServiceCombo : copyOfServiceCombos)
    {
      aServiceCombo.removeService(this);
    }
    ServiceCombo placeholderServiceCombo = serviceCombo;
    this.serviceCombo = null;
    if(placeholderServiceCombo != null)
    {
      placeholderServiceCombo.removeMainService(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "serviceCost" + ":" + getServiceCost()+ "," +
            "serviceName" + ":" + getServiceName()+ "," +
            "serviceDuration" + ":" + getServiceDuration()+ "," +
            "serviceDowntime" + ":" + getServiceDowntime()+ "," +
            "serviceDescription" + ":" + getServiceDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "serviceCombo = "+(getServiceCombo()!=null?Integer.toHexString(System.identityHashCode(getServiceCombo())):"null");
  }
}