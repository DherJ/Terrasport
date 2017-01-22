package com.terrasport.event;

import com.terrasport.model.Terrain;

import java.util.List;

public class AllTerrainEvent {

	private List<Terrain> terrains;

	public List<Terrain> getTerrains() {
		return terrains;
	}

	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}
}