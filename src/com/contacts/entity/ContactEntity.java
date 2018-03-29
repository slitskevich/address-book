package com.contacts.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.validator.routines.EmailValidator;

@XmlRootElement
public class ContactEntity extends Entity implements Serializable {
	
	private static final String ID = "id";
	private static final String ADDRESS = "email";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	
	private static final long serialVersionUID = 721756743894563L;
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public ContactEntity() {
		
	}

	public ContactEntity(ResultSet set) throws SQLException {
		super(set);
		this.id = set.getInt(ID);
		this.address = set.getString(ADDRESS);
		this.firstName = set.getString(FIRST_NAME);
		this.lastName = set.getString(LAST_NAME);
    }
	
	public void validate() throws ValidationException {
		if (this.getFirstName() == null || this.getFirstName().isEmpty()) {
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
