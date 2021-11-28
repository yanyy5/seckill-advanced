package com.seckill.response;

public class CommonReturnType {

    // the processing result of the request: success, fail
    private String status;

    // if status == success, data <- json data for front-end
    // if status == fail, data <- error code
    private Object data;

    // identify a common method
    public static CommonReturnType create(Object result){
       return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
