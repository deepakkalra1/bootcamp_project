package com.tothenew.bootcamp.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }



    //--------------------------------------------------------------------------------------------------------------->
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }




    //--------------------------------------------------------------------------------------------------------------->
    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        /*
        i have commented avobe bcoz i wanted my own custom credential check
        so i have provided my own authenticated provider which is customerAuthenticationProvider
        below
         */
        //CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();

        return authenticationProvider;
    }




    //--------------------------------------------------------------------------------------------------------------->
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }




    //--------------------------------------------------------------------------------------------------------------->
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()

                //registration api's
                .antMatchers("/register/customer").anonymous()
                .antMatchers("/register/seller").anonymous()

                .antMatchers("/user/customer/profile").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/address").anonymous()
                .antMatchers("/user/customer/profile/update").anonymous()
                .antMatchers("/user/customer/password/update/{oldPassword}/{newPassword}/{confirmPassword}").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/add-address").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/delete-address/{addressId}").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/update-address").hasAnyRole("CUSTOMER")

                .antMatchers("/user/seller/profile").hasAnyRole("SELLER")
                .antMatchers("/user/seller/profile/update").hasAnyRole("SELLER")
                .antMatchers("/user/seller/update-address").hasAnyRole("SELLER")

                //activation api's
                .antMatchers("/user/activation").anonymous()
                .antMatchers("/user/activation/resend/{email}").anonymous()

                //password reset api's
                .antMatchers("/user/reset-password/token-generator/{email}").anonymous()
                .antMatchers("/user/reset-password/{token}/{newPassword}/{confirmPassword}").anonymous()

                //admin api's
                .antMatchers("/user/admin/view-all-customers").anonymous()
                .antMatchers("/user/admin/view-all-sellers").anonymous()
                .antMatchers("/user/admin/activate-customer/{email}").anonymous()
                .antMatchers("/user/admin/activate-seller/{email}").anonymous()
                .antMatchers("/user/admin/deactivate-customer/{email}").anonymous()
                .antMatchers("/user/admin/deactivate-seller/{email}").anonymous()
                .antMatchers("/user/admin/add/field/{fieldValue}").anonymous()
                .antMatchers("/user/admin/view/fields").anonymous()
                .antMatchers("/user/admin/add/category").anonymous()
                .antMatchers("/user/admin/view/category/{id}").anonymous()
                .antMatchers("/user/admin/view/categories").anonymous()
                .antMatchers("/user/admin/update/category/{id}/{name}").anonymous()
                .antMatchers("/user/admin/add/category-metadata-value").anonymous()
                .antMatchers("/user/admin/update/category-metadata-value").anonymous()

                .antMatchers("/doLogout").hasAnyRole("ADMIN", "CUSTOMER", "SELLER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }





}