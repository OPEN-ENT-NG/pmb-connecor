package fr.openent.pmb.bean;

import java.util.Base64;

public class Credential {
    private String username;
    private String password;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }

    public String basic() {
        String value = String.format("%s:%s", this.username(), this.password());
        return Base64.getEncoder().encodeToString(value.getBytes());
    }
}
