package org.greengin.senseitweb.entities.activities.senseit;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.datanucleus.jpa.annotations.Extension;


@Entity
public class SensorInput {

	@Id
    @Column (nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Extension (vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
   	String id;
	
	@Basic
	float rate;
	
	@Basic
	String sensor;
	

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getId() {
		return id;
	}
	
	
}