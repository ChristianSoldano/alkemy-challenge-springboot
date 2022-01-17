package com.soldano.AlkemySpringboot.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    SendGrid sendGrid;
    @Value("${app.sendgrid.templateId}")
    private String templateId;

    public Response sendEmail(String toAddress) {
//        Mail mail = new Mail(
//                new Email("soldanochristian@hotmail.com"),
//                "Probando sendgrid",
//                new Email("siderjonas@gmail.com"),
//                new Content("text/plain",
//                        "A VER SI SE MANDA ESTA PORQUERIA ")
//        );
        Mail mail = prepareMail(toAddress);
        mail.setReplyTo(new Email("soldanochristian@hotmail.com"));
        Request request = new Request();

        Response response = null;

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response = this.sendGrid.api(request);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return response;
    }

    private Mail prepareMail(String toAddress) {
        Mail mail = new Mail();
        Email fromEmail = new Email();
        fromEmail.setEmail("soldanochristian@hotmail.com");
        mail.setFrom(fromEmail);
        mail.setSubject("");
        Email to = new Email();
        to.setEmail(toAddress);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);
        return mail;
    }
}
