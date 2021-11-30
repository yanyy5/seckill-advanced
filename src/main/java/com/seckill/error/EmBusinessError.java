package com.seckill.error;

public enum EmBusinessError implements CommonError{

    // common error type: 10001
    PARAMETER_VALIDATION_ERROR(10001, "invalid para"),
    UNKNOWN_ERROR(10002, "unknown error"),

    // start from 20000: about user info error
    USER_NOT_EXIST(20001, "user not exists"),
    USER_LOGIN_FAILED(20002, "wrong phone/password"),
    USER_NOT_LOGIN(20003, "user not login"),

    // start from 30000: about order operation error
    STOCK_NOT_ENOUGH(30001, "the stock is not enough"),
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
