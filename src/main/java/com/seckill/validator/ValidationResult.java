package com.seckill.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {

    // if the validation result is wrong
    private boolean hasErrors = false;

    // save the error message
    private Map<String, String> errMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }

    // implement a common method: get error result by formatted String information
    public String getErrMsg(){
        return StringUtils.join(errMsgMap.values().toArray(), ",");
    }

}
