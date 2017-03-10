package com.terrasport.event;

import com.terrasport.model.Utilisateur;

import java.util.List;

public class AllUtilisateurEvent {

	private List<Utilisateur> utilisateurs;

	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}
}