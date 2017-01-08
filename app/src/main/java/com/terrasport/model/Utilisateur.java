package com.terrasport.model;

import java.util.List;

public class Utilisateur {

	private Integer id;
	private String nom;
	private String prenom;
	private Integer age;
	private String email;
	private String login;
	private String password;
	private String sexe;
	
	private List<CompetenceSport> competencesSport;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSexe() {
		return sexe;
	}
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public List<CompetenceSport> getCompetencesSport() {
		return competencesSport;
	}
	public void setCompetencesSport(List<CompetenceSport> competencesSport) {
		this.competencesSport = competencesSport;
	}
}