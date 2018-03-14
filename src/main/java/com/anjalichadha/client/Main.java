package com.anjalichadha.client;

import com.anjalichadha.LogAnalyser;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Invalid number of arguments.");
            System.out.println("You can pass two arguments: fileName [delimiter]");
            return;
        }
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(args[0]);

        if(args.length >= 2) logAnalyser.setDelimiter(args[1]);
        logAnalyser.printMaxVisitUser();
    }
}
