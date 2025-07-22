package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.exception.AppException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
	@Value("${sendgrid.from-email}")
	@NonFinal
	String fromEmail;

	SendGrid sendGrid;

	public void sendEmail(String toEmail, String subject, String body) {
		Email from = new Email(fromEmail);
		Email to = new Email(toEmail);

		Content content = new Content("text/html", body);
		Mail mail = new Mail(from, subject, to, content);

		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			System.out.println("SendGrid response: " + response.getStatusCode());
		} catch (IOException ex) {
			throw new AppException(ErrorCode.SEND_EMAIL_SENDGRID_ERROR);
		}
	}
}
