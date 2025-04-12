package com.email.AIEmail_writer.controller;

import com.email.AIEmail_writer.Emailservice.emailService;
import com.email.AIEmail_writer.model.Emailrequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorController {
    @Autowired
    private final emailService emailservice;

    public EmailGeneratorController(emailService emailservice) {
        this.emailservice = emailservice;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody Emailrequest emailrequest) {
        String response=emailservice.generateEmailReply(emailrequest);
        return ResponseEntity.ok(response);
    }

}
