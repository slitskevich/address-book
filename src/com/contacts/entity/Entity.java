package com.contacts.entity;

import java.sql.ResultSet;

/**
 * Abstract class representing entity
 */
public abstract class Entity {
	
	/**
	 * Instantiates a new entity.
	 */
	public Entity() {
		
	}
	
	/**
	 * Instantiates a new entity with data from DB request result set
	 *
	 * @param set the DB request result set
	 */
	public Entity(ResultSet set) {
		
	}
}
