package com.luxoft.training.spring.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Value("${security.oauth2.client.accessTokenUri}")
    private String accessTokenUri;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").authenticated();
    }

    @Primary
    @Bean(name = "OAuth2RestTemplate")
    @LoadBalanced
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details);
    }

    @Bean(name = "OAuth2CardService")
    @LoadBalanced
    public OAuth2RestTemplate oAuth2CardService() {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setAccessTokenUri(accessTokenUri);
        resource.setClientId("client");
        resource.setClientSecret("secret");
        resource.setUsername("card");
        resource.setPassword("card");
        DefaultAccessTokenRequest accessTokenRequest = new DefaultAccessTokenRequest();
        OAuth2ClientContext context = new DefaultOAuth2ClientContext(accessTokenRequest);
        return new OAuth2RestTemplate(resource, context);
    }
}
