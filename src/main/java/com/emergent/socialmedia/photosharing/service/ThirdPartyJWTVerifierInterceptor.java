package com.emergent.socialmedia.photosharing.service;

import com.emergent.socialmedia.photosharing.domain.User;
import com.emergent.socialmedia.photosharing.service.exceptions.UnauthorisedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Component
public class ThirdPartyJWTVerifierInterceptor implements HandlerInterceptor {
    @Autowired
    ThirdPartyAuthorization thirdPartyAuthorization;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<User> userFromToken = null;
        try {
            String authorization = request.getHeader("Authorization");
            if(authorization == null) throw new UnauthorisedUserException("Invalid authorization token!");
            userFromToken = thirdPartyAuthorization.authorizeToken(authorization);
        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorisedUserException("Invalid authorization token!");
        }
        if (!userFromToken.isPresent()) {
            throw new UnauthorisedUserException("Invalid authorization token!");
        }
        return true;
    }
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) throws Exception {}
}
