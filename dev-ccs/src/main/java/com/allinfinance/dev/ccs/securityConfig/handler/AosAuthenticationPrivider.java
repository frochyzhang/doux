package com.allinfinance.dev.ccs.securityConfig.handler;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 登录验证密码
 */
public class AosAuthenticationPrivider extends DaoAuthenticationProvider {
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            byte[] bytes = Base64.decodeBase64(presentedPassword);
            String dePass= StringUtils.newStringUtf8(bytes);
            if(dePass.contains("#")){
                String[] vars = dePass.split("\\#");
                presentedPassword=vars[0];
            }else{
                presentedPassword=dePass;
            }

            if (!super.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("密码验证失败");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }
    }