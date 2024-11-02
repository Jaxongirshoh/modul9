package dev.wisespirit.modul9.dto;

public record BaseResponse<T>(T result,ErrorData error,boolean success) {
    public BaseResponse(T result){
        this(result,null,true);
    }
    public BaseResponse(ErrorData error){
        this(null,error,false);
    }
}
