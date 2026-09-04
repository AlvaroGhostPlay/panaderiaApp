package com.aevasquez.msvc.clients.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "messages")
public class LabelsMessageProperties {

    private Welcome welcome;

    public Welcome getWelcome() {
        return welcome;
    }

    public void setWelcome(Welcome welcome) {
        this.welcome = welcome;
    }

    public static class Welcome {

        private List<String> welcomeLabel;

        public List<String> getWelcomeLabel() {
            return welcomeLabel;
        }

        public void setWelcomeLabel(List<String> welcomeLabel) {
            this.welcomeLabel = welcomeLabel;
        }
    }

}
