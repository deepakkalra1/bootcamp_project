package com.tothenew.bootcamp.customerFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tothenew.bootcamp.constants.JwtUtility;
import com.tothenew.bootcamp.entity.User.*;
import com.tothenew.bootcamp.entity.UserLoginAttempts;
import com.tothenew.bootcamp.enums.StatusCode;
import com.tothenew.bootcamp.enums.UsersType;
import com.tothenew.bootcamp.pojo.CommonResponseVO;
import com.tothenew.bootcamp.repositories.*;
import com.tothenew.bootcamp.configurations.email.EmailServiceImpl;
import com.tothenew.bootcamp.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class LockUserOnOverAttempt extends GenericFilterBean {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserLoginAttemptsRepository userLoginAttemptsRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    SellerRepository sellerRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //----------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @description
     * purpose is to de-activate the user.. if he attempt wrong pass for three times
     * if request came for oauth/token.. check otherwise continue chain
     *if user is active ..go in..else send error response
     *  counting no of times.. user has faild to provide right pass.. if it is less then 2..ok then..
     *  but if it is equal to 2 or greater
     *      and if accounnt is not locked.. then de-activate it and send reponse
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //created http servelet response for http type operation
        HttpServletResponse response1 = (HttpServletResponse) response;


        //created url... bcoz on every time filters get run.. so to restrict it.. for only oauth/token..otherwise.. continue the chain
        String url =  ServletUriComponentsBuilder.fromCurrentRequest().replacePath("oauth/token").toUriString();
                if (ServletUriComponentsBuilder.fromCurrentRequest().toUriString().equals(url) ) {
                    try {
                    String email = request.getParameter("username");
                    String password = request.getParameter("password");
                    String role = roleRepository.findAuthorityWithEmail(email);
                    response1.setContentType("application/json");

                    if (role.equals(UsersType.CUSTOMER.toString())) {
                        Customer customer = customerRepository.findByEmail(email);
                        System.out.println(passwordEncoder.matches(password,customer.getPassword()));
                        customerFilter(customer, role, email, password, request, response, chain);
                    } else if (role.equals(UsersType.SELLER.toString())) {
                        Seller seller = sellerRepository.findByEmail(email);
                        sellerFilter(seller, role, email, password, request, response, chain);
                    } else if (role.equals(UsersType.ADMIN.toString())) {
                        Admin admin = adminRepository.findByEmail(email);
                        adminFilter(admin, role, email, password, request, response, chain);
                    }
                }

                        catch(NullPointerException e){
                        response1.setStatus(404);
                        CommonResponseVO commonResponseVO = new CommonResponseVO
                                (
                                        Arrays.asList(StatusCode.BAD_CREDENTIALS.toString()),
                                        Arrays.asList("User Does Not Exist")
                                );
                        String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);
                        response1.getWriter().write(jsonResponseData);
                    }

                        }
                        //if the request did not came for oauth/token... let it go ahead
                        else
                            {
                                chain.doFilter(request, response);
                            }
    }

    public void customerFilter(Customer user,String role,String email,String password,ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response1 = (HttpServletResponse) response;
        ObjectMapper objectMapper = new ObjectMapper();

        UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByEmail(email);

        if (userLoginAttempts==null){

            userLoginAttempts = new UserLoginAttempts();
            userLoginAttempts.setEmail(email);
            userLoginAttempts.setAttempts(0);
        }

        //if he is active.. acount is not deactivated.. then only allowed to go in
        if (user.isIs_active())
        {
            //if passwowrd match fails and is for the 1st or 2nd tym.. then only warning will be send
            if ( passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue()<2 ){
                userLoginAttempts.setAttempts(userLoginAttempts.getAttempts().intValue()+1);
                userLoginAttemptsRepository.save(userLoginAttempts);

                System.out.println(userLoginAttempts.getAttempts());
                response1.setStatus(400);
                CommonResponseVO commonResponseVO = new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.BAD_CREDENTIALS.toString()),
                                Arrays.asList("Wrong password attempt"+userLoginAttempts.getAttempts().intValue())
                        );
                String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);
                response1.getWriter().write(jsonResponseData);
            }
            //if password fails and is for the 3rd time..account is deactivated
            else if (passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue() == 2 )
            {
                    customerRepository.setCustomerActiveStatus(0,email);

                    userLoginAttemptsRepository.delete(userLoginAttempts);

                CommonResponseVO commonResponseVO= new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString()),
                                Arrays.asList("Invalid 3 attempts have locked the Account")
                        );
                response1.setStatus(400);
                response1.setContentType("application/json");
                response1.getWriter().write(objectMapper.writeValueAsString(commonResponseVO));
            }
            //if password has matched..clear the count of wrong attempts
            else if (passwordEncoder.matches(password, user.getPassword()) == true )
            {
                userLoginAttemptsRepository.delete(userLoginAttempts);
                chain.doFilter(request, response);
            }
        }
        // if account is de-active already
        else
        {
            response1.setStatus(401);
            response1.setContentType("application/json");
            CommonResponseVO commonResponseVO = new CommonResponseVO
                    (
                            Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString(),
                                    StatusCode.LINK_GENERATED.toString()
                            ),
                            Arrays.asList("You account is De-activated",
                                    "New Activation Link has been sent to your Registered Email Id")
                    );
            String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);

            String jwt = JwtUtility.createJWT(5, "activation", email,role);
            String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
            tokenService.storeTokenInDB(jwt);
            emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);

            response1.getWriter().write(jsonResponseData);
        }


    }












    public void sellerFilter(Seller user,String role,String email,String password,ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException {


        HttpServletResponse response1 = (HttpServletResponse) response;
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByEmail(email);
        if (userLoginAttempts==null){
            userLoginAttempts = new UserLoginAttempts();
            userLoginAttempts.setEmail(email);
            userLoginAttempts.setAttempts(0);
        }
//
//        if (previousAttemptedEmail!=email){
//            countAttemptFailure=0;
//        }
        //if he is active.. acount is not deactivated.. then only allowed to go in
        if (user.isIs_active())
        {
            //if passwowrd match fails and is for the 1st or 2nd tym.. then only warning will be send
            if ( passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue()<2 ){
                userLoginAttempts.setAttempts(userLoginAttempts.getAttempts().intValue()+1);
                userLoginAttemptsRepository.save(userLoginAttempts);

                System.out.println(userLoginAttempts.getAttempts());
                response1.setStatus(400);
                CommonResponseVO commonResponseVO = new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.BAD_CREDENTIALS.toString()),
                                Arrays.asList("Wrong password attempt"+userLoginAttempts.getAttempts().intValue())
                        );
                String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);
                response1.getWriter().write(jsonResponseData);
            }
            //if password fails and is for the 3rd time..account is deactivated
            else if (passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue() == 2 )
            {
                sellerRepository.setSellerActiveStatus(0,email);

                userLoginAttemptsRepository.delete(userLoginAttempts);

                CommonResponseVO commonResponseVO= new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString()),
                                Arrays.asList("Invalid 3 attempts have locked the Account")
                        );
                response1.setStatus(400);
                response1.setContentType("application/json");
                response1.getWriter().write(objectMapper.writeValueAsString(commonResponseVO));
            }
            //if password has matched..clear the count of wrong attempts
            else if (passwordEncoder.matches(password, user.getPassword()) == true )
            {
                userLoginAttemptsRepository.delete(userLoginAttempts);
                chain.doFilter(request, response);
            }
        }
        // if account is de-active already
        else
        {
            response1.setStatus(401);
            response1.setContentType("application/json");
            CommonResponseVO commonResponseVO = new CommonResponseVO
                    (
                            Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString(),
                                    StatusCode.LINK_GENERATED.toString()
                            ),
                            Arrays.asList("You account is De-activated",
                                    "New Activation Link has been sent to your Registered Email Id")
                    );
            String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);

            String jwt = JwtUtility.createJWT(5, "activation", email,role);
            tokenService.storeTokenInDB(jwt);
            String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
            emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);
            response1.getWriter().write(jsonResponseData);
        }



    }







    public void adminFilter(Admin user,String role,String email,String password,ServletRequest request,ServletResponse response,FilterChain chain) throws IOException, ServletException {


        HttpServletResponse response1 = (HttpServletResponse) response;
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginAttempts userLoginAttempts = userLoginAttemptsRepository.findByEmail(email);
        if (userLoginAttempts==null){
            userLoginAttempts = new UserLoginAttempts();
            userLoginAttempts.setEmail(email);
            userLoginAttempts.setAttempts(0);
        }

        //if he is active.. acount is not deactivated.. then only allowed to go in
        if (user.isIs_active())
        {
            //if passwowrd match fails and is for the 1st or 2nd tym.. then only warning will be send
            if ( passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue()<2 ){
                userLoginAttempts.setAttempts(userLoginAttempts.getAttempts().intValue()+1);
                userLoginAttemptsRepository.save(userLoginAttempts);

                System.out.println(userLoginAttempts.getAttempts());
                response1.setStatus(400);
                CommonResponseVO commonResponseVO = new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.BAD_CREDENTIALS.toString()),
                                Arrays.asList("Wrong password attempt"+userLoginAttempts.getAttempts().intValue())
                        );
                String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);
                response1.getWriter().write(jsonResponseData);
            }
            //if password fails and is for the 3rd time..account is deactivated
            else if (passwordEncoder.matches(password, user.getPassword())==false && userLoginAttempts.getAttempts().intValue() == 2 )
            {
                adminRepository.setAdminActiveStatus(0,email);

                userLoginAttemptsRepository.delete(userLoginAttempts);

                CommonResponseVO commonResponseVO= new CommonResponseVO
                        (
                                Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString()),
                                Arrays.asList("Invalid 3 attempts have locked the Account")
                        );
                response1.setStatus(400);
                response1.setContentType("application/json");
                response1.getWriter().write(objectMapper.writeValueAsString(commonResponseVO));
            }
            //if password has matched..clear the count of wrong attempts
            else if (passwordEncoder.matches(password, user.getPassword()) == true )
            {
                userLoginAttemptsRepository.delete(userLoginAttempts);
                chain.doFilter(request, response);
            }
        }
        // if account is de-active already
        else
        {
            response1.setStatus(401);
            response1.setContentType("application/json");
            CommonResponseVO commonResponseVO = new CommonResponseVO
                    (
                            Arrays.asList(StatusCode.ACCOUNT_DEACTIVATED.toString(),
                                    StatusCode.LINK_GENERATED.toString()
                            ),
                            Arrays.asList("You account is De-activated",
                                    "New Activation Link has been sent to your Registered Email Id")
                    );
            String jsonResponseData = objectMapper.writeValueAsString(commonResponseVO);

            String jwt = JwtUtility.createJWT(5, "activation", email,role);
            String userActivationLink = ServletUriComponentsBuilder.fromCurrentRequest().replacePath("user/activation?token={jwt}").buildAndExpand(jwt).toUriString();
            emailService.sendSimpleMessage(email, "Activation Link", userActivationLink);
            tokenService.storeTokenInDB(jwt);
            response1.getWriter().write(jsonResponseData);
        }



    }


}
