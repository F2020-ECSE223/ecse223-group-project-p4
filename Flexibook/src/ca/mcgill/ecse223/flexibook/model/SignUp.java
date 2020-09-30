package ca.mcgill.ecse223.flexibook.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.30.1.5099.60569f335 modeling language!*/



// line 84 "model.ump"
// line 189 "model.ump"
public class SignUp
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SignUp Associations
  private Customer register;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SignUp(Customer aRegister)
  {
    if (aRegister == null || aRegister.getAccount() != null)
    {
      throw new RuntimeException("Unable to create SignUp due to aRegister. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    register = aRegister;
  }

  public SignUp(String aUsernameForRegister, String aPasswordForRegister, BusinessInfo aBusinessInfoForRegister)
  {
    register = new Customer(aUsernameForRegister, aPasswordForRegister, this, aBusinessInfoForRegister);
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Customer getRegister()
  {
    return register;
  }

  public void delete()
  {
    Customer existingRegister = register;
    register = null;
    if (existingRegister != null)
    {
      existingRegister.delete();
    }
  }

}