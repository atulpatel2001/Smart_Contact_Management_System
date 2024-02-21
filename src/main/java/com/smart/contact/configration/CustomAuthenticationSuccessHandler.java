package com.smart.contact.configration;

import com.smart.contact.dao.UserRepository;
import com.smart.contact.model.LoggedInUsers;
import com.smart.contact.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // Autowire the LoggedInUsers bean
    @Autowired
    private LoggedInUsers loggedInUsers;

    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        User userByUserName = this.userRepository.getUserByUserName(username);
        userByUserName.setActive(true);
        this.userRepository.save(userByUserName);
        loggedInUsers.addUser(username);

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/index");
                return;
            }
        }
        response.sendRedirect("/user/index");


    }

}

