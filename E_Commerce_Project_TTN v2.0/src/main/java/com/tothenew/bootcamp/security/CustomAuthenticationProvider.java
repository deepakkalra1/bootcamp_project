//package com.tothenew.bootcamp.service.oAuthSecurityService;
//
//import com.tothenew.bootcamp.security.GrantAuthorityImpl;
//import com.tothenew.bootcamp.entity.Seller.Seller;
//import com.tothenew.bootcamp.exceptionHandling.FieldMissingException;
//import com.tothenew.bootcamp.exceptionHandling.GiveMessageException;
//import com.tothenew.bootcamp.service.userService.CustomerService;
//import org.bouncycastle.jcajce.provider.digest.SHA256;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.List;
//
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    CustomerService customerService;
//
//    SHA256 passwordEncoder = SHA25
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
//
//
//
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//        Seller user = customerService.userByEmail(username);
//
//        System.out.println(username);
//        System.out.println(password);
//
//        List<GrantAuthorityImpl> authorities = (List<GrantAuthorityImpl>) authentication.getAuthorities();
//
//
//        if (user.getPassword()==passwordEncoder.encode(password) ){
//
//            System.out.println("mtched");
//
//            return new UsernamePasswordAuthenticationToken(username,password,authorities);
//
//        }
//
//        System.out.println("not matched");
//        return  usernamePasswordAuthenticationToken;
//
//
//
//
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}
