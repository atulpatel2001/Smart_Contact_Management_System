package com.smart.contact;

import com.smart.contact.dao.ContactRepository;
import com.smart.contact.model.Contact;
import com.smart.contact.service.EmailService;
import com.smart.contact.service.PdfService;
import com.smart.contact.service.TilloSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class SmartcontactmanagerApplication implements CommandLineRunner {
	@Autowired
 private TilloSms tilloSms;

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
//		this.tilloSms.sendSms();
	}
}
