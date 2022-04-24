/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.broadridge.testfilegenerator.users;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 *
 * @author zeyne
 */
public class UsersUtils {

    public static ArrayList<User> validatedUsers = new ArrayList<User>();
    public static User loggedInUser;

    public static void createUsers() {

        File xml = new File("users.xml");
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.processAnnotations(User.class);
        xstream.processAnnotations(ValidUsers.class);
        final ValidUsers a = (ValidUsers) xstream.fromXML(xml);
        validatedUsers = a.getUsers();

    }

    public static boolean validateUser() {

//         bring up all the valid users for the application
//        
//         find user profile - type of access and if enabled/disabled/password locked
        for (int i = 0; i < validatedUsers.size(); i++) {
            if (validatedUsers.get(i).getUserName().equals(System.getProperty("user.name"))) {
                loggedInUser = validatedUsers.get(i);
                return true;
            }
        }
        return false;
    }

    public static void updateUsers(String text) throws FileNotFoundException {
        File fileToWrite = new File("users.xml");
        FileOutputStream fos = new FileOutputStream(fileToWrite);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw, true);
        pw.write(text);
        pw.flush();
    }

}
