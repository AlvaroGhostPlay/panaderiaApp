package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.config.LabelsMessageProperties;
import com.aevasquez.msvc.clients.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl {

    @Autowired
    private LabelsMessageProperties labelsMessageProperties;

    public EmailRequest createEmailReuqestSendWelcome(String email, String subject, String template, String... data){
        List<String> dataList = List.of(data);
        List<String> welcomeLabels =labelsMessageProperties.getWelcome().getWelcomeLabel();
        Map<String, Object> content = new HashMap<>();
        for (int i = 0; i < welcomeLabels.size() && i < dataList.size(); i++) {
            System.out.println(welcomeLabels.get(i));
            System.out.println(dataList.get(i));
            content.put(welcomeLabels.get(i), dataList.get(i));
        }

        return new EmailRequest(
                email,
                subject,
                template,
                content
        );
    }
}
