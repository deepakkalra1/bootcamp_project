package com.tothenew.bootcamp.constants;

import com.sun.mail.imap.AppendUID;

import java.util.regex.Pattern;

public class RegularExp {


    public final static String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    //minimum 8 characters and 1 no and 1 alphabet
    public final static String passwordRegex="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    //should be
    public final static String ifKeyIsForAddressSellerProfileUpdate="address_[0-9]*$";



    //------------------------------------------------------------------------------------------------------------------>
    public static boolean verifyEmailCorrectness(String received_email) {
        return Pattern.matches(RegularExp.emailRegex, received_email);
    }





    //------------------------------------------------------------------------------------------------------------------>
    public static boolean verifyPasswordCorrectness(String received_password) {
        return Pattern.matches(RegularExp.passwordRegex, received_password);
    }





    //------------------------------------------------------------------------------------------------------------------>
    public static boolean verifyIfAddressIsAskedForUpdationInSellerProfileUpdate(String keyToBeVerified){
        return Pattern.matches(ifKeyIsForAddressSellerProfileUpdate,keyToBeVerified);
    }





}
