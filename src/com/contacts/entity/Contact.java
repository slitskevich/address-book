package com.contacts.entity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contact extends Entity implements Serializable {
	
	private static final String ID = "id";
	private static final String EMAIL = "email";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	
	private static final long serialVersionUID = 721756743894563L;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Contact(ResultSet set) throws SQLException {
		super(set);
		this.id = set.getInt(ID);
		this.email = set.getString(EMAIL);
		this.firstName = set.getString(FIRST_NAME);
		this.lastName = set.getString(LAST_NAME);
    }
}
