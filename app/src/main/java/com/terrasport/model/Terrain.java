package com.terrasport.model;

public class Terrain {

	private Integer id;
	private String latitude;
	private String longitude;
	private String nom;
	private boolean isPublic;
	private boolean isOccupe;
	private Sport sport;
	private Evenement evenementEnCours;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	public boolean getIsOccupe() {
		return isOccupe;
	}
	public void setIsOccupe(boolean isOccupe) {
		this.isOccupe = isOccupe;
	}
	public Sport getSport() {
		return sport;
	}
	public void setSport(Sport sport) {
		this.sport = sport;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Evenement getEvenementEnCours() {
		return evenementEnCours;
	}
	public void setEvenementEnCours(Evenement evenementEnCours) {
		this.evenementEnCours = evenementEnCours;
	}
}