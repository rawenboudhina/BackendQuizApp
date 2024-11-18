package com.quizapplication.quizapp.Response;

public class AuthResponse {
    private String msg;
    private boolean status;

    // Constructeur par défaut
    public AuthResponse() {}

    // Constructeur avec paramètres
    public AuthResponse(String msg, boolean status) {
        this.msg = msg;
        this.status = status;
    }

    // Getters et setters
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
