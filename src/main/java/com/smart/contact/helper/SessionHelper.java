package com.smart.contact.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {

	public void removeAttributeFromSession(String attributeName) {
		try {
			HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest()
					.getSession();
			session.removeAttribute(attributeName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
