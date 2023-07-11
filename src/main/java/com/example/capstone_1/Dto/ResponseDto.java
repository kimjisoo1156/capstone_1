package com.example.capstone_1.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName="set")
public class ResponseDto <D>{
    private boolean result;
    private String message;
    private D data;

    public static <D> ResponseDto<D> setSucces(String message, D data){

        return ResponseDto.set(true,message,data);
    }
    public static <D> ResponseDto<D> setFailed(String message){
        return ResponseDto.set(false,message,null);
    }

}
