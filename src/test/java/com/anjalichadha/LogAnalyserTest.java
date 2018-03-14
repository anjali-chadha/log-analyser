package com.anjalichadha;

import org.junit.Test;

import java.io.File;

public class LogAnalyserTest {
    @Test
    public void testCase1(){
        File resourcesDirectory = new File("src/test/resources/log_file.txt");
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(resourcesDirectory.getAbsolutePath());
        logAnalyser.printMaxVisitUser();
    }

    @Test
    public void testCase2(){
        File resourcesDirectory = new File("src/test/resources/log_file2.txt");
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(resourcesDirectory.getAbsolutePath());
        logAnalyser.printMaxVisitUser();
    }

    @Test
    public void corruptFileTestCase(){
        File resourcesDirectory = new File("src/test/resources/log_file3.txt");
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(resourcesDirectory.getAbsolutePath());
        logAnalyser.printMaxVisitUser();
    }

    @Test
    public void fileWithCustomDelimiterTestCase(){
        File resourcesDirectory = new File("src/test/resources/log_file4.txt");
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(resourcesDirectory.getAbsolutePath());
        logAnalyser.setDelimiter(":");
        logAnalyser.printMaxVisitUser();
    }

    @Test
    public void emptyFileTestCase(){
        File resourcesDirectory = new File("src/test/resources/log_file5.txt");
        LogAnalyser logAnalyser = LogAnalyser.getLogAnalyserInstance(resourcesDirectory.getAbsolutePath());
        logAnalyser.printMaxVisitUser();
    }

}
