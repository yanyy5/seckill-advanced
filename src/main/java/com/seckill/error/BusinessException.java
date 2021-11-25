package com.seckill.error;

// Decorator pattern - BusinessException
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    // receive para of EmBusinessError for exceptions
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    // DIY errMsg
    public BusinessException(CommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
