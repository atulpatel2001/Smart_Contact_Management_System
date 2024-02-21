package com.smart.contact.controller;

import com.smart.contact.dao.OTPRepository;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.GeneratUniqueRandomNumber;
import com.smart.contact.helper.Message;
import com.smart.contact.model.OTP;
import com.smart.contact.model.User;
import com.smart.contact.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;


@Controller

public class ForgotPasswordController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OTPRepository otpRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        model.addAttribute("title", "ForgotPassword | Samrt-Contact-Manager");
        return "forgot_password_form";
    }

    @PostMapping("/sendemail")
    public String getEmailForOtp(@RequestParam("email") String email, Model model, HttpSession session) {
        User user = this.userRepository.getUserByUserName(email);
        model.addAttribute("title", "ForgotPassword | Samrt-Contact-Manager");
        int otp = GeneratUniqueRandomNumber.generateRandomNumber(100000,999999);


        if (user == null) {
            session.setAttribute("message", new Message("Do Not Register Email or Email is Wrong !!", "warning"));
            return "redirect:/forgot-password";
        } else {
            String subject = " Reset Your Smart Contact Manager Password";
            String text = "<html><head><style>" +
                    "h3 { color: #007BFF; }" +
                    "</style></head><body>" +
                    "<p>Dear " + user.getName() + ",</p>" +
                    "<br>" +
                    "<p>We have received a request to reset your password for the Smart Contact Manager.</p>" +
                    "<p>To ensure the security of your account, please enter the following One-Time Password (OTP) within the Smart Contact Manager application:</p>" +
                    "<br>" +
                    "<h3>OTP: <span style=\"color: #007BFF; font-weight: bold;\">" + otp + "</span></h3>" +
                    "<br>" +
                    "<p>Please note that the OTP is valid for a limited time period only. If you don't enter the OTP within 10 minutes, you may need to restart the password reset process.</p>" +
                    "</body></html>";
            boolean isDeliverd = this.emailService.sendEmail(text, subject, email);
            if (isDeliverd) {
                session.setAttribute("myOtp",otp);
                session.setAttribute("email",email);
                OTP save=new OTP(otp, user.getEmail());
                this.otpRepository.save(save);
                session.setAttribute("message", new Message(" OTP Sent Successfully!!", "success"));

                return "veryfie_otp";
            } else {
                session.setAttribute("message", new Message("Something Went Wrong Try Again!!!", "warning"));
                return "redirect:/forgot-password";
            }

        }

    }

    @PostMapping("/veryfieotp")
    public String veryFileEmailOtp(@RequestParam("otp") int otp,Model model,HttpSession session){
        model.addAttribute("title", "ForgotPassword | Samrt-Contact-Manager");

               int otp1 = (int) session.getAttribute("myOtp");


            if(otp1==otp){
                System.out.println("match");
                session.setAttribute("message", new Message("OTP is Valid ,You Can Create New Password", "success"));
                return "newpassword_form";
            }
            else {
                session.setAttribute("message", new Message("Doesn't Match OTP so Give Valid OTP!!", "warning"));
                return "veryfie_otp";
            }


    }


    @PostMapping("/newpasswordhandler")
    public String newpasswordhandler(@RequestParam("newPassword") String newPassword,Model model,HttpSession session){
        model.addAttribute("title", "New-Password | Samrt-Contact-Manager");
        try {
            String email = (String) session.getAttribute("email");
            User user = this.userRepository.getUserByUserName(email);
            user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(user);
            return "redirect:/signin?change=Successfully Create New Password!!";

        }
        catch(Exception e){
            session.setAttribute("message", new Message("Something Went Wrong TryAgain!!", "warning"));
            return "redirect:/forgot-password";
        }

    }


}
