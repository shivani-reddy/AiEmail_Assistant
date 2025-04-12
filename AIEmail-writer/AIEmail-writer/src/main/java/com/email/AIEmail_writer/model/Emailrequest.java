package com.email.AIEmail_writer.model;

import lombok.Data;

@Data
public class Emailrequest {
    private String emailContent;
    private String tone;


    public String getTone() {
        return tone;
    }

    public String getEmailContent() {
        return emailContent;
    }
}
