package com.bktutor.services.impl;

import com.bktutor.common.entity.Booking;
import com.bktutor.common.entity.Student;
import com.bktutor.common.entity.Tutor;
import com.bktutor.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @Async
    public void sendNewBookingNotificationToTutor(Booking newBooking) {
        Tutor tutor = newBooking.getSlot().getTutor();
        Student student = newBooking.getStudent();

        String toAddress = tutor.getEmail();
        String fromAddress = "nampham2131@gmail.com";
        String senderName = "BK Tutor System";
        String subject = "[BK Tutor] You have a new booking request!";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'on' EEEE, dd MMMM yyyy");
        String formattedTime = newBooking.getSlot().getStartTime().format(formatter);

        String content = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }"
                + ".container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 8px; }"
                + ".header { background-color: #0066cc; color: white; padding: 10px 20px; border-radius: 8px 8px 0 0; }"
                + ".footer { margin-top: 20px; font-size: 0.9em; color: #888; }"
                + ".info { background-color: #f9f9f9; padding: 10px; border-radius: 6px; margin-top: 10px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'><h2>New Tutoring Request</h2></div>"
                + "<p>Dear <b>" + tutor.getFullName() + "</b>,</p>"
                + "<p>You have received a new tutoring session request from a student.</p>"

                + "<div class='info'>"
                + "<p><b>Student:</b> " + student.getFullName() + "<br>"
                + "<b>Subject:</b> " + newBooking.getSubject() + "<br>"
                + "<b>Time:</b> " + formattedTime + "<br>"
                + "<b>Type:</b> " + newBooking.getType() + "</p>"
                + "</div>"

                + "<p>Please log in to your <a href='https://bktutor.com/dashboard'>BK Tutor Dashboard</a> "
                + "to confirm or reject this request.</p>"

                + "<p>Thank you,<br>"
                + "<b>The BK Tutor Team</b></p>"

                + "<div class='footer'>"
                + "<p>This is an automated message. Please do not reply directly to this email.</p>"
                + "</div>"
                + "</div>"
                + "</body></html>";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
