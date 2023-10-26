package com.backend.yearbook.controller;

import com.backend.yearbook.model.MailModel;
import com.backend.yearbook.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @PostMapping("/send/{mail}")
public String sendMail(@PathVariable String mail, @RequestBody MailModel mailModel){
 mailService.sendEmail(mail,mailModel);
        return "Successfully sent the mail";
}

}
