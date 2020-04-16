package com.tothenew.bootcamp.pojo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class CommonResponseVO<T> {

    private String name;

    private T data;

    private HashMap<Integer, String> message = new HashMap<Integer, String>();
    private HashMap<Integer, String> description = new HashMap<Integer, String>();

    int i[] = new int[]{0};

    public CommonResponseVO(){

    }

public CommonResponseVO(List<String> listMessage){
    listMessage.forEach((String singleMessage)->{
        message.put(i[0],singleMessage);
        ++i[0];

    });



}

    public CommonResponseVO(List<String> listMessage,List<String> descriptionList){
        listMessage.forEach((String singleMessage)->{
            message.put(i[0],singleMessage);

            ++i[0];

        });
        i[0]=0;
        descriptionList.forEach((String singleDescription)->{
            description.put(i[0],singleDescription);

            ++i[0];

        });



    }


    //--------------------------------------------------------------------------------------------------------------->
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, String> getMessage() {
        return message;
    }

    public void setMessage(HashMap<Integer, String> message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HashMap<Integer, String> getDescription() {
        return description;
    }

    public void setDescription(HashMap<Integer, String> description) {
        this.description = description;
    }

}
