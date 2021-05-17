package com.wfw.auth.service;

import com.wfw.auth.helper.PasswordHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaonanzhou
 * @date 2021/5/14 14:58
 * @description
 **/
@Service
public class VipUserDetailService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(VipUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String usernameFx = "found";

        logger.info("用户身份验证...");
        if (!username.equals(usernameFx)) {
            logger.error("用户不存在!");
            throw new UsernameNotFoundException(usernameFx);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));

        String password = PasswordHelper.encryptPassword("123456");
        return new User(usernameFx, password, true, true, true, true, authorities);
    }


}
