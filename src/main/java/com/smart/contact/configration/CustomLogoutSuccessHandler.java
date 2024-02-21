package com.smart.contact.configration;


import com.smart.contact.dao.UserRepository;
import com.smart.contact.model.LoggedInUsers;
import com.smart.contact.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    // Autowire the LoggedInUsers bean
    @Autowired
    private LoggedInUsers loggedInUsers;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            String username = authentication.getName();
            User userByUserName = this.userRepository.getUserByUserName(username);
            userByUserName.setActive(false);
            this.userRepository.save(userByUserName);
            loggedInUsers.removeUser(username);
        }

        // Redirect the user to the login page or wherever you want after logout
          response.sendRedirect("/signin");
    }
}

