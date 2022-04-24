/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package testfilegenerator;

import com.prowidesoftware.swift.model.mt.mt9xx.MT940;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zeyne
 */

//OUTPUTTER OBJECT THAT IS CREATED WHEN FILES ARE BEING OUTPUTTED, CURRENTLY OUTPUTS FILES USING 2 THREADS RUNNING SIMULTANEOUSLY
public class Outputter implements Runnable {

    private int start;
    private int end;
    MT940[][][] allMessages;
    String outputPath;

    public Outputter(int start, int end, MT940[][][] allMessages, String outputPath) {
        this.start = start;
        this.end = end;
        this.allMessages = allMessages;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        for (int account = start; account <= end; account++) {
            for (int day = 1; day <= allMessages[0].length - 1; day++) {
                for (int file = 1; file <= allMessages[0][0].length - 1; file++) {
                    String filename = "Day" + day + "Account" + account + "File" + file;
                    String finalPath = outputPath + "\\Day" + day;
                    File fileToWrite = new File(finalPath, filename + ".txt");
                    try {
                        FileOutputStream fos = new FileOutputStream(fileToWrite);
                        OutputStreamWriter osw = new OutputStreamWriter(fos);
                        BufferedWriter bw = new BufferedWriter(osw);
                        PrintWriter pw = new PrintWriter(bw, true);
                        pw.write(allMessages[account][day][file].message());
                        pw.flush();
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
    }
    

}
