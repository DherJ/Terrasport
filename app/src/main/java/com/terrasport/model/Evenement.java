package com.terrasport.model;

import java.sql.Timestamp;
import java.util.List;

public class Evenement {

	private Integer id;
	private boolean prive;
	private Integer nbPlaces;
	private Integer nbPlacesRestantes;
	private Timestamp date;
	private Terrain terrain;
	private Sport sport;
	private Niveau niveauCible;
	private Utilisateur utilisateurCreateur;
	private List<Participation> participation;
	private List<DemandeParticipation> demandesParticipation;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean getPrive() {
		return prive;
	}
	public void setPrive(boolean prive) {
		this.prive = prive;
	}
	public Integer getNbPlaces() {
		return nbPlaces;
	}
	public void setNbPlaces(Integer nbPlaces) {
		this.nbPlaces = nbPlaces;
	}
	public Integer getNbPlacesRestantes() {
		return nbPlacesRestantes;
	}
	public void setNbPlacesRestantes(Integer nbPlacesRestantes) {
		this.nbPlacesRestantes = nbPlacesRestantes;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public Terrain getTerrain() {
		return terrain;
	}
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	public Sport getSport() {
		return sport;
	}
	public void setSport(Sport sport) {
		this.sport = sport;
	}
	public Niveau getNiveauCible() {
		return niveauCible;
	}
	public void setNiveauCible(Niveau niveauCible) {
		this.niveauCible = niveauCible;
	}
	public Utilisateur getUtilisateurCreateur() {
		return utilisateurCreateur;
	}
	public void setUtilisateurCreateur(Utilisateur utilisateurCreateur) {
		this.utilisateurCreateur = utilisateurCreateur;
	}
	public List<Participation> getParticipation() {
		return participation;
	}
	public void setParticipation(List<Participation> participation) {
		this.participation = participation;
	}
	public List<DemandeParticipation> getDemandesParticipation() {
		return demandesParticipation;
	}
	public void setDemandesParticipation(List<DemandeParticipation> demandesParticipation) {
		this.demandesParticipation = demandesParticipation;
	}
}