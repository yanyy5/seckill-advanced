package com.seckill.error;

public enum EmBusinessError implements CommonError{

    // common error type: 00001
    PARAMETER_VALIDATION_ERROR(00001, "invalid para"),

    // start from 10000: about user info error
    USER_NOT_EXIST(10001, "user not exists"),
    ;

    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
