package org.reyabreu.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "THING")
class Thing {

	@Id @GeneratedValue	@Column(name = "ID")
	Integer id
	
	@Column(name = "NAME")
	String name
	
	@Column(name = "OWNER")
	String owner
}
