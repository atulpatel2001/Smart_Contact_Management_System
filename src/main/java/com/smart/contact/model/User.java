package com.smart.contact.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userId")
	private int id;
	@Column(name = "userName")
	@NotBlank(message = "Name must be Require")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	private String name;
	@Column(name = "userEmail", unique = true)
	@NotBlank
	@Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email is Not Valid")
	private String email;
	@NotBlank(message = "Password must be Required!!")
	private String password;
	private String profileUrl;
	private String role;
	private boolean isActive;
	private LocalDate joinDate;
	@Column(length = 500)
	private String about;
	@AssertTrue(message="Please Agree tearm And Condition")
	private boolean termAndCondition;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();

	public User() {
	}

	public User(int id, String name, String email, String password, String profileUrl, String role, boolean isActive, LocalDate joinDate, String about, boolean termAndCondition, List<Contact> contacts) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.profileUrl = profileUrl;
		this.role = role;
		this.isActive = isActive;
		this.joinDate = joinDate;
		this.about = about;
		this.termAndCondition = termAndCondition;
		this.contacts = contacts;
	}

	public User(String name, String email, String password, String profileUrl, String role, boolean isActive, LocalDate joinDate, String about, boolean termAndCondition, List<Contact> contacts) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.profileUrl = profileUrl;
		this.role = role;
		this.isActive = isActive;
		this.joinDate = joinDate;
		this.about = about;
		this.termAndCondition = termAndCondition;
		this.contacts = contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}


	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public boolean getTermAndCondition() {
		return termAndCondition;
	}

	public void setTermAndCondition(boolean termAndCondition) {
		this.termAndCondition = termAndCondition;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
	public LocalDate getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	public boolean isTermAndCondition() {
		return termAndCondition;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", profileUrl='" + profileUrl + '\'' +
				", role='" + role + '\'' +
				", isActive=" + isActive +
				", joinDate=" + joinDate +
				", about='" + about + '\'' +
				", termAndCondition=" + termAndCondition +
				'}';
	}
}
