package com.example;

import com.example.hunterPreyPredator.ReplacesMapForFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static String fileWithReplacesName = "replaces";
    public static void main(String[] args) {
	// write your code here

        String folderName = args[0];
        try {

            FileInputStream fileIn = new FileInputStream(folderName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Map<String, String> map1 = (Map<String, String>) in.readObject();
            in.close();
            fileIn.close();
            map1.forEach((s, s2) -> System.out.println(s + ": " + s2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
