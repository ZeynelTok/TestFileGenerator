/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.broadridge.testfilegenerator.users;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author TokZ
 */
@XStreamAlias("User")
public class User {

    @XStreamAlias("firstName")
    String firstName;

    @XStreamAlias("lastName")
    String lastName;

    @XStreamAlias("userName")
    String userName;

    @XStreamAlias("role")
    Role role;

    Preferences pref;

    public User() {
    }

    public User(String fN, String lN, String uN, Role r) {
        super();
        this.firstName = fN;
        this.lastName = lN;
        this.userName = uN;
        this.role = r;
        this.pref = Preferences.userNodeForPackage(User.class);


    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }
    
    public void setPreferences(String key, String value){
        Preferences pref = Preferences.userRoot().node("/com/broadridge/testfilegenerator/users");
        pref.put(key, value);
    }

    public Preferences getPreferences() {
        return Preferences.userRoot().node("/com/broadridge/testfilegenerator/users");
    }
}
