package com.terrasport.event;

import com.terrasport.model.CompetenceSport;

import java.util.List;

public class AllCompetenceSportEvent {

	private List<CompetenceSport> competenceSports;

	public List<CompetenceSport> getCompetenceSports() {
		return competenceSports;
	}

	public void setCompetenceSports(List<CompetenceSport> competenceSports) {
		this.competenceSports = competenceSports;
	}
}