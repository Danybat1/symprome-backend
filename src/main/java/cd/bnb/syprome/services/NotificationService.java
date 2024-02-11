package cd.bnb.syprome.services;

import cd.bnb.syprome.utilities.AppInfo;
import cd.bnb.syprome.utilities.MailUtils;
import cd.bnb.syprome.utilities.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AppInfo appInfo;
    @Autowired
    JavaMailSender javaMailSender;


    public void sendEmail(MailUtils email){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<MailUtils> request = new HttpEntity<>(email, headers);
        log.info("{} : {}",email,appInfo);
        ResponseEntity<String> response = restTemplate.postForEntity(appInfo.getSmsUrl()+"mail", request , String.class );

    }

    public void sendSms(SmsUtil sms){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<SmsUtil> request = new HttpEntity<>(sms, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(appInfo.getSmsUrl()+"sms", request , String.class );

    }

    public void sendMailWithAttach(String sender,String recever,String subject, String msg,String path){
        MimeMessagePreparator mail = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recever);
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setText(msg, true);
            FileSystemResource file = new FileSystemResource(new File(path));
            helper.addAttachment("doc.pdf", file);
        };
        try {
            javaMailSender.send(mail);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }


}
