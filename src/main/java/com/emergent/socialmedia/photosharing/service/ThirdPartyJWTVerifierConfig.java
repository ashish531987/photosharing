package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.resources.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class ThirdPartyJWTVerifierConfig implements WebMvcConfigurer {

    @Autowired
    ThirdPartyJWTVerifierInterceptor thirdPartyJWTVerifierInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(thirdPartyJWTVerifierInterceptor).excludePathPatterns(UserResource.REST_LOGIN);
    }
}
