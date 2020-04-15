package com.tothenew.bootcamp.constants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

/***
 * this class method will help to convert from string to json(or hashmap) and json to string
 *
 */
public class JsonConversion {

    /***
     *
     * @param
     * @return
     * @throws JsonProcessingException
     * @description convert hasmap to json string and return it
     */
    public static<T> String jsonSerialization(T t) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(t);
    }




    //--------------------------------------------------------------------------------------------------------------->
    /***
     *
     * @param jsonString
     * @return
     * @throws JsonProcessingException
     * @desciption convert string to Hashmap and return it
     */
    public static HashMap jsonDeserialization(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString,HashMap.class);
    }




    //--------------------------------------------------------------------------------------------------------------->
}
