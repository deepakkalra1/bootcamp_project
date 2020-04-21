package com.tothenew.bootcamp.constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ConversionUtility {



    //---------------------------------------------------------------------------------------------------------------->>
    /***
     *
     * @param hash
     * @return
     * it converts the byte[]
     * which is received after hashing
     * to normal hexadecimal format
     * since byte array contain numbers
     */
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }






    //------------------------------------------------------------------------------------------------------------------>
    public static<T> HashMap<String, String> turnIntoHashmapWithGettersNormalFieldsOnlyNotOtherClasses(T t) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            Class c = t.getClass();
            for (Method method : c.getDeclaredMethods()) {
                String methodName = method.getName();
                if (methodName.charAt(0) == 'g' && methodName.charAt(1) == 'e' && methodName.charAt(2) == 't') {

                    if (method.getReturnType().getSimpleName().equals("String")
                            || method.getReturnType().getSimpleName().equals("int")
                            || method.getReturnType().getSimpleName().equals("Integer")
                            || method.getReturnType().getSimpleName().equals("boolean")
                            || method.getReturnType().getSimpleName().equals("Boolean")
                    ) {
                        hashMap.put(methodName, String.valueOf(method.invoke(t)));

                    }
                }
            }
        }
        catch (InvocationTargetException | IllegalAccessException ie){

        }

        return hashMap;

    }




}
