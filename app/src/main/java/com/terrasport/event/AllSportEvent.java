package com.terrasport.event;

import com.terrasport.model.Sport;

import java.util.List;

public class AllSportEvent {

	private List<Sport> sports;

	public List<Sport> getSports() {
		return sports;
	}

	public void setSports(List<Sport> sports) {
		this.sports = sports;
	}
}