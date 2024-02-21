package com.smart.contact.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Forgot_Password_OTP")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otpId")
    private long id;
    private long otp;
    @Column(name = "userEmail")
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OTP(long id, long otp, String email) {
        this.id = id;
        this.otp = otp;
        this.email = email;
    }

    public OTP(long otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    public OTP() {
    }

    @Override
    public String toString() {
        return "OTP{" +
                "id=" + id +
                ", otp=" + otp +
                ", email='" + email + '\'' +
                '}';
    }
}
