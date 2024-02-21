package com.smart.contact.service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class SMSService {
    public boolean sendSms(String message,String number){
    boolean f=false;
    try {
        String sender_id="FSTSMS";
        String APIKEY = "8kjIeSE7qugwtA59pfydRrGPL0x3UKVh21Yn6zivaXOmT4BoJHq9o6DlzXLZCOnGWEyFds4NcSwReAj3";
        String language = "english";
        String rout = "p";
        message=URLEncoder.encode(message, "UTF-8");
//        String myUrl="https://www.fast2sms.com/dev/bulkV2?authorization="+APIKEY+"&sender_id="+sender_id+"&message="+message+"&language="+language+"&route="+ rout +"&numbers="+number;
         String myUrl="https://www.fast2sms.com/dev/bulkV2?authorization=8kjIeSE7qugwtA59pfydRrGPL0x3UKVh21Yn6zivaXOmT4BoJHq9o6DlzXLZCOnGWEyFds4NcSwReAj3&route=q&message=Hello%20I%20am%20Atul&flash=1&numbers=7096773572";
        URL url=new URL(myUrl);
        HttpsURLConnection con= (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent","Mozilla/5.0");
        con.setRequestProperty("cache-control","no-cache");
        System.out.println("Wait");
        int code=con.getResponseCode();
        System.out.println("Response code:-"+code);
        StringBuffer response=new StringBuffer();
        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
        while (true){
            String line=br.readLine();
            if(line == null){
                break;
            }
            response.append(line);

        }
        f=true;
    }
    catch (Exception e){
        e.printStackTrace();
    }
    return f;
}
    public static void main(String[] args) {
SMSService s=new SMSService();
    s.sendSms("5555","7096773572");


    }
}
