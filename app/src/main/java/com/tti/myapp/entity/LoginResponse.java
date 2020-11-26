package com.tti.myapp.entity;

public class LoginResponse {

    /**
     * msg : success
     * code : 0
     * expire : 604800
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NyIsImlhdCI6MTYwMjMyMzE1OSwiZXhwIjoxNjAyOTI3OTU5fQ.Mzhy094-vczBq7jZm5-R45nZPUXEGYlE3BlFVuLwZTDzbaE8XTX1zU7TsmuI-W6BLox2-IQPdLk72SHqbkb41g
     */

    private String msg;
    private int code;
    private int expire;
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
