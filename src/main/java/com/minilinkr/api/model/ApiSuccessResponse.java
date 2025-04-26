package com.minilinkr.api.model;

import java.util.Map;

public class ApiSuccessResponse {

    boolean ok;
    String message;
    Map<String, Object> responseMetadata;

    public ApiSuccessResponse(boolean ok, String message, Map<String, Object> responseMetadata) {
        this.ok = ok;
        this.message = message;
        this.responseMetadata = responseMetadata;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(Map<String, Object> responseMetadata) {
        this.responseMetadata = responseMetadata;
    }
}
