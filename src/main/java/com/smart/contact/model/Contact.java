package com.smart.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    @NotBlank(message = "Contact Name must be Required!!")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 20 characters")
    private String name;
    private String nickName;
    private String work;
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email is Not Valid")
    @NotBlank(message = "Email must be Required!!")
    private String email;
    @NotBlank(message = "Phone Number must be Required!!")
    @Pattern(regexp = "\\d{10,11}",message = "Phone Number is Not Valid")
    private String phoneNumber;
    private String imageUrl;
    @ManyToOne
    @JsonIgnore
    private User user;


    @Column(length = 60000)
    private String description;

    public Contact() {
    }


    public Contact(int cId, String name, String nickName, String work, String email, String phoneNumber,
            String imageUrl, User user, String description) {
        this.cId = cId;
        this.name = name;
        this.nickName = nickName;
        this.work = work;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.user = user;
        this.description = description;
    }
    

    public Contact(String name, String nickName, String work, String email, String phoneNumber, String imageUrl,
            User user, String description) {
        this.name = name;
        this.nickName = nickName;
        this.work = work;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.user = user;
        this.description = description;
    }


    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    


    @Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", email="
				+ email + ", phoneNumber=" + phoneNumber + ", imageUrl=" + imageUrl + ", user=" + user
				+ ", description=" + description + "]";
	}


	public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }
    

    
    

  
}
