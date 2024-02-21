package com.smart.contact.service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TilloSms {
    private static final String ACCOUNT_SID = "AC7d5f43d60a12ef766b7de12245292759";
    private static final String AUTH_TOKEN = "2dbb03d349261d047c18b5b9b77ad379";


    public void sendSms(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Replace "FROM_PHONE_NUMBER" with your Twilio phone number
        // Replace "TO_PHONE_NUMBER" with the recipient's phone number
        String fromPhoneNumber = "+17623399718";
        String toPhoneNumber = "+917096773572";

        // The message you want to send
        String messageBody = "Hello, this is a test message from Twilio!";

        try {
            // Send the SMS
            Message message = Message.creator(new PhoneNumber(toPhoneNumber), new PhoneNumber(fromPhoneNumber), messageBody)
                    .create();

            // Print the SMS SID if successful
            System.out.println("SMS sent successfully. SID: " + message.getSid());
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
    }

