package org.tools.hqlbuilder.webservice.wicket;

import java.io.Serializable;

public class UserData implements Serializable {
    private static final long serialVersionUID = -2712060426070134480L;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    public String getPassword() {
        return this.password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
