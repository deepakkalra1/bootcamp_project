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
                .antMatchers("/user/customer/address").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/profile/update").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/password/update").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/add-address").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/delete-address/{addressId}").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/update-address").hasAnyRole("CUSTOMER")

                .antMatchers("/user/seller/password/update").hasAnyRole("SELLER")
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
                .antMatchers("/user/admin/view-all-customers").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/view-all-sellers").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/activate-customer").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/activate-seller").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/deactivate-customer").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/deactivate-seller").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/add/field/{fieldValue}").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/view/fields").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/add/category").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/view/category/{id}").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/view/categories").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/update/category").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/add/category-metadata-value").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/update/category-metadata-value").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/products").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/product").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/product/deactivate").hasAnyRole("ADMIN")
                .antMatchers("/user/admin/product/activate").hasAnyRole("ADMIN")


                .antMatchers("/user/seller/view/categories").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product/add").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product-variation/add").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product/variation").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product/variations").hasAnyRole("SELLER")
                .antMatchers("/user/seller/products").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product/delete").hasAnyRole("SELLER")
                .antMatchers("/user/seller/product/update").hasAnyRole("SELLER")

                .antMatchers("/user/customer/view/categories").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/view/category/filters").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/product").hasAnyRole("CUSTOMER")
                .antMatchers("/user/customer/products").hasAnyRole("CUSTOMER")

                .antMatchers("/user/customer/delete-address/{addressId}").hasAnyRole("CUSTOMER")
                .antMatchers("/doLogout").hasAnyRole("ADMIN", "CUSTOMER", "SELLER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }





}