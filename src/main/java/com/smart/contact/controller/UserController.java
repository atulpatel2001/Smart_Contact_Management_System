package com.smart.contact.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.contact.dao.MyOrderRepository;
import com.smart.contact.model.MyOrder;
import com.smart.contact.service.EmailService;
import com.smart.contact.service.ExcelFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.contact.dao.ContactRepository;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.FileHelpProvider;
import com.smart.contact.helper.Message;
import com.smart.contact.model.Contact;
import com.smart.contact.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private FileHelpProvider fileHelpProvider;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyOrderRepository myOrderRepository;

    @Autowired
    private ExcelFile excelFile;
    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @ModelAttribute
    public void addCommanData(Model model, Principal principal) {
        String userName = principal.getName();
        logger.info("Login By "+userName);

        // get User Detail By UserName(EmailId)
        User user = this.userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User DashBoard | Samrt-Contact-Manager");
        return "user/user_dashboard";
    }

    // show contact form handler

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact | Samrt-Contact-Manager");
        model.addAttribute("contact", new Contact());
        return "user/add_contact_form";

    }

    @PostMapping("/add-contact-data")
    public String addContactData(@Valid @ModelAttribute Contact contact, BindingResult result,
                                 @RequestParam("image") MultipartFile file, Principal principal, Model model, HttpSession session) {
        try {

            if (result.hasErrors()) {
                System.out.println("Error " + result.toString());
                model.addAttribute("contact", contact);
                return "user/add_contact_form";
            }

            // Proccessing and Uploading file
            if (file.isEmpty()) {
                System.out.println("File is Empty");
                contact.setImageUrl("contact.png");

            } else {
                contact.setImageUrl(file.getOriginalFilename());
                boolean isFileUpload = this.fileHelpProvider.uploadFile(file, "static/contact_image");
                if (isFileUpload) {
//                    System.out.println("Succesfull upload File!!");
                    logger.info("upload file");
                } else {
//                    System.out.println("Something Is Wrong !!!");
                    logger.info("something wrong");
                }
            }
            // Save Contact in Database
            String name = principal.getName();
            User userByUserName = this.userRepository.getUserByUserName(name);
            contact.setUser(userByUserName);
            userByUserName.getContacts().add(contact);
            this.userRepository.save(userByUserName);
            session.setAttribute("message", new Message("Successfully Added Your Contact!!", "success"));
            model.addAttribute("contact", new Contact());

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong !! Try Again..", "danger"));
            model.addAttribute("contact", contact);
            return "user/add_contact_form";
        }

        return "user/add_contact_form";

    }

    // per page 5[n] contact
    // current page = 0

    @GetMapping("/show-contact/{page}")
    public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("title", "Show-All Contact | Samrt-Contact-Manager");
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = this.contactRepository.getContactByUserId(user.getId(), pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());
        return "user/show_contact";
    }

    // contact detail show
    @GetMapping("/{cId}/show-contact-profile")
    public String showContactProfile(@PathVariable("cId") Integer cId, Model model, Principal principal) {
        model.addAttribute("title", "Contact-Detail | Samrt-Contact-Manager");
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "user/contact_detail";
    }

    //delete handler
    @GetMapping("/{cId}/delete-contact")
    public RedirectView deleteContact(@PathVariable("cId") Integer cId, Principal principal, Model model,
                                      HttpSession session) {
        Optional<Contact> contactOptional = this.contactRepository.findById(cId);
        Contact contact = contactOptional.get();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/user/show-contact/0");

        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        if (user.getId() == contact.getUser().getId()) {
            if (contact.getImageUrl().trim().equals("contact.png")) {
                logger.info("same photo");
            } else {
                boolean isDelete = this.fileHelpProvider.deleteFile("static/contact_image", contact.getImageUrl());
                if (isDelete) {
                   logger.info("deleted!!");
                } else {
                    logger.info("note deleted!!");
                }
            }

            this.contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact Deleted Successfully", "success"));

        } else {
            session.setAttribute("message", new Message("Something Went Wrong | TryAgain", "danger"));
        }
        return redirectView;

    }

    // update handler

    @PostMapping("/{cId}/update-contact")
    public String updateForm(@PathVariable("cId") Integer cId, Model model) {
        model.addAttribute("title", "update Contact | Samrt-Contact-Manager");
        Contact contact = this.contactRepository.findById(cId).get();
        model.addAttribute("contact", contact);
        return "user/update_form";
    }

    // update data handler
    @PostMapping("/update-contact-data")
    public String updateDatahandler(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
                                    @RequestParam("image") MultipartFile file, Model model, Principal principal, HttpSession session) {
        try {
            // old contact detail
            Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
            if (bindingResult.hasErrors()) {
                logger.info(bindingResult.toString());
                model.addAttribute("contact", contact);
                return "redirect:/user/" + contact.getcId() + "/update-contact";
            }
            // for image
            String folderPath = "static/contact_image";
            if (file.isEmpty()) {
                contact.setImageUrl(oldContact.getImageUrl());
            } else if (!file.isEmpty()) {
                // delete old file
                if(oldContact.getImageUrl().trim().equals("contact.png")){
                    logger.info("same");
                }
                else {
                    boolean isDelete = this.fileHelpProvider.deleteFile(folderPath, oldContact.getImageUrl());
                    if (isDelete) {
                        logger.info("deleted");
                    }
                }
                // update new file
                contact.setImageUrl(file.getOriginalFilename());
                boolean isFileUpload = this.fileHelpProvider.uploadFile(file, folderPath);
                if (isFileUpload) {

                    this.logger.info("uploaded");
                }

            }
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);
            contact.setUser(user);
            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Successfully Update Your Contact!!", "success"));
            return "redirect:/user/" + contact.getcId() + "/show-contact-profile";


        } catch (Exception e) {
            session.setAttribute("message", new Message("Something Went Wrong !! Try Again..", "danger"));
            model.addAttribute("contact", contact);
            return "redirect:/user/" + contact.getcId() + "/show-contact-profile";
        }

    }


    //user profile

    @GetMapping("/profile")
    public String userProfile(Model model) {
        model.addAttribute("title", "User-Profile | Samrt-Contact-Manager");
        return "user/user_profile";
    }


    @PostMapping("/update-user-data")
    public String updateProfile(@ModelAttribute("user") User user, @RequestParam("profile") MultipartFile file) {
        User oldUser = this.userRepository.findById(user.getId()).get();
        if (file.isEmpty()) {
            user.setProfileUrl(oldUser.getProfileUrl());
        this.logger.info("file is Empty ");
        } else if (!file.isEmpty()) {
            String folderpath="static/user_image";

            //delete file
            if(oldUser.getProfileUrl().trim().equals("profile.jpg")){
                logger.info("same");
            }
            else {
                boolean isDeleted = FileHelpProvider.deleteFile(folderpath, oldUser.getProfileUrl());
                if (isDeleted) {
                    logger.info("deleted ");
                } else {
                    logger.info("not deleted");
                }

            }

            //update profile
            user.setProfileUrl(file.getOriginalFilename());
            boolean isSuccess = FileHelpProvider.uploadFile(file, folderpath);
            if(isSuccess){
                logger.info("user image updated");
            }
            else {
                logger.info("user image not update");
            }
        }
        this.userRepository.save(user);

        return "redirect:/user/profile";
    }

    @GetMapping("/setting")
    public String openSetting(Model model) {
        model.addAttribute("title", "Setting | Samrt-Contact-Manager");
        return "user/setting";
    }

    @PostMapping("/change-password")
    public String changePasswordHandler(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
        String userName = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(userName);
        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message", new Message("Your Password is Successfully Changed", "success"));
        } else {
            session.setAttribute("message", new Message("Please Enter Correct Old Password", "danger"));
            return "redirect:/user/setting";
        }
        return "redirect:/user/index";
    }


    @GetMapping("/razor-pay")
    public String razorpayForm(Model model) {
        model.addAttribute("title", "Donate-Money | Samrt-Contact-Manager");
        return "user/razorpay";
    }

    //create order
    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws RazorpayException {
        System.out.println(data);
        int amount = Integer.parseInt(data.get("amount").toString());
        var client = new RazorpayClient("rzp_test_ADEnLyqI9oALQY", "dtZFd3HrvdyoXYGLpfTrEDUv");
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_235425");
        Order order = client.orders.create(orderRequest);

        MyOrder myOrder = new MyOrder();
        myOrder.setOrderId(order.get("id"));
        myOrder.setAmount(order.get("amount").toString());
        myOrder.setPaymentId(null);
        myOrder.setReceipt(order.get("receipt"));
        myOrder.setStatus("created");
        myOrder.setUser(this.userRepository.getUserByUserName(principal.getName()));
        this.myOrderRepository.save(myOrder);
//        System.out.println(order);
        logger.info(String.valueOf(order));
        return order.toString();
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data) {
        System.out.println(data);

        MyOrder myOrder = this.myOrderRepository.findByOrderId(data.get("order_id").toString());
        myOrder.setPaymentId(data.get("payment_id").toString());
        myOrder.setStatus(data.get("status").toString());
        this.myOrderRepository.save(myOrder);
          int amount=Integer.parseInt(myOrder.getAmount());
          amount=amount/100;
        String tableHTML = "<table>" +
                "<tr>" +
                "<th>Order-ID</th>" +
                "<th>Amount</th>" +
                "<th>TranscationId</th>" +
                "<th>Status</th>" +
                "</tr>" +
                "<tr>" +
                "<td>"+myOrder.getOrderId()+"</td>" +
                "<td>"+amount+"</td>" +
                "<td>"+myOrder.getPaymentId()+"</td>" +
                "<td>"+myOrder.getStatus()+"</td>" +
                "</tr>" +
                "</table>";
        String name = myOrder.getUser().getName();
        String text = "<html><head><style>" +
                "h3 { color: #007BFF; }" +
                "table { border-collapse: collapse; width: 100%; }" +
                "th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }" +
                "th { background-color: #f2f2f2; font-weight: bold; }" +
                "</style></head><body>" +
                "<p>Dear " +name + "</p>" +
                "<br>" +
                "<p>We are pleased to inform you that your transaction was successful. Here are the details:</p>" +
                "<br>" +
                tableHTML +
                "<br>" +
                "<p>Thank you for Donate Money. God Bless You, If you have any questions, feel free to contact our support team.</p>" +
                "</body></html>";

// Code to send the email with 'text' as the email content
// You can use your preferred email sending method or library here
        this.emailService.sendEmail(text,"TranscationSuccessful",myOrder.getUser().getEmail());


        return ResponseEntity.ok(Map.of("msg", "updated"));

    }


    @GetMapping("/paytm-page")
    public String paytm_Page() {
        return "user/paytm_page";
    }
    @GetMapping("/download-contact")
    public ResponseEntity<byte[]> downloadExcelFile(Principal principal) throws IOException {
        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);
        String fileName=user.getName()+"_contacts.xlsx";

        List<Contact> contacts = this.contactRepository.getContactByUserId2(user.getId());
        Workbook workbook = this.excelFile.dataToExcel(contacts);
        // Write the workbook to a file
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }
}

