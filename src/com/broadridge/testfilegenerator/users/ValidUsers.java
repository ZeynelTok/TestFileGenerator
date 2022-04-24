/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.broadridge.testfilegenerator.users;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zeyne
 */
@XStreamAlias("ValidUsers")
public class ValidUsers {
    
    @XStreamImplicit
//        (itemFieldName = "User")
    public ArrayList<User> users = new ArrayList<User>();   

    public ArrayList<User> getUsers() {
        return users;
    }
    
}
