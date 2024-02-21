package com.smart.contact.controller;

import com.paytm.pg.merchant.PaytmChecksum;
import com.smart.contact.configration.PaytmConfig;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.security.Principal;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/payment")
public class PaytmController {

    Random random = new Random();

    @PostMapping("/start-payment")
    public Map<String, Object> startPayment(@RequestBody Map<String, Object> data) {
        String orderId = "ORDER" + random.nextInt(1000000);


        //parama Created
        JSONObject paytmParams = new JSONObject();

        //body information
        JSONObject body = new JSONObject();
        body.put("requestType", "Payment");
        body.put("mid", PaytmConfig.MID);
        body.put("websiteName", PaytmConfig.WEBSITE);
        body.put("orderId", orderId);
        body.put("callbackUrl", "https://localhost:8080/payment-success");

        //txn amount
        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", data.get("amount"));
        txnAmount.put("currency", PaytmConfig.CURANCY);


        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", "CUST_001");

        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);
        ResponseEntity<Map> response=null;
        try {
            String checksum = PaytmChecksum.generateSignature(body.toString(), PaytmConfig.MKEY);
            JSONObject head = new JSONObject();
            head.put("signature", checksum);

            paytmParams.put("body", body);
            paytmParams.put("head", head);

            URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=" + PaytmConfig.MKEY + "&orderId=" + orderId + "");
            /* for Production */
            // URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paytmParams.toMap(), header);
            RestTemplate restTemplate=new RestTemplate();
            response=restTemplate.postForEntity(url.toString(),httpEntity,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map body1=response.getBody();
        body1.put("orderId",orderId);
        body1.put("amount",txnAmount.get("value"));
        return body1;
    }

    public String capturePayment(){
        return "";
    }
}
