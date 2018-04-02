package com.contacts.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * The Class ContactEntity represents contact entity
 */
@XmlRootElement
public class ContactEntity extends Entity implements Serializable {
	
	private static final String ID = "id";
	private static final String ADDRESS = "email";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";

	private static final long serialVersionUID = 721756743894563L;
	
	/** Initial and invalid ID value */
	private static final int INITIAL_ID = -1;
    
    /** The contact ID. */
    private int id = INITIAL_ID;
    
    /** The first name. */
    private String firstName;
    
    /** The last name. */
    private String lastName;
    
    /** The address. */
    private String address;
    
    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/**
	 * Instantiates a new contact entity.
	 */
	public ContactEntity() {
		
	}
	
	/**
	 * Instantiates a new contact entity.
	 *
	 * @param id the id
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param address the address
	 */
	public ContactEntity(int id, String firstName, String lastName, String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	/**
	 * Instantiates a new contact entity with the data from DB request ResultSet
	 *
	 * @param set the DB request result set
	 * @throws SQLException if contact attributes can't be accessed in the result set
	 */
	public ContactEntity(ResultSet set) throws SQLException {
		super(set);
		this.id = set.getInt(ID);
		this.address = set.getString(ADDRESS);
		this.firstName = set.getString(FIRST_NAME);
		this.lastName = set.getString(LAST_NAME);
    }
	
	/**
	 * Validates entity attributes. All attributes are considered to be mandatory. E-mail format validation is done as well.
	 *
	 * @throws ValidationException if validation fails
	 */
	public void validate() throws ValidationException {
		if (this.getId() == INITIAL_ID) {
			throw new ValidationException("Missing ID value");
		} else if (this.getFirstName() == null || this.getFirstName().isEmpty()) {
			throw new ValidationException("Missing first name value");
		} else if (this.getLastName() == null || this.getLastName().isEmpty()) {
			throw new ValidationException("Missing last name value");
		} else if (this.getAddress() == null || this.getAddress().isEmpty()) {
			throw new ValidationException("Missing e-mail value");
		} else if (!EmailValidator.getInstance().isValid(this.getAddress())) {
			throw new ValidationException("Invalid e-mail address: " + this.getAddress());
		}
	}

}
