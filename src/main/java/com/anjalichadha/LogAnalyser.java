package com.anjalichadha;

import com.anjalichadha.comparators.UserSessionEntryComparator;
import com.anjalichadha.model.Session;
import com.anjalichadha.model.User;
import com.anjalichadha.model.UserSessionEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * Given an input log file, this class provides APIs to process the log file.
 */
public class LogAnalyser {
    private String logFilePath;
    private boolean headers = false;
    private String fileDelimiter = ", "; //Default Delimiter
    private Map<User, TreeSet<Long>> timestampMapByUser;

    private LogAnalyser(String logFilePath, boolean headers) {
        this.logFilePath = logFilePath;
        this.headers = headers;
        timestampMapByUser = new HashMap<>();
    }

    /**
     * Returns a LogAnalyser instance given an input log file.
     * @param logFilePath
     * @return
     */
    public static LogAnalyser getLogAnalyserInstance(String logFilePath) {
        return new LogAnalyser(logFilePath, false);
    }

    /**
     * Returns a LogAnalyser instance given an input log file and skip the first line
     * for processing as headers are present.
     * @param logFilePath
     * @return
     */
    public static LogAnalyser getLogAnalyserInstanceWithHeaders(String logFilePath) {
        return new LogAnalyser(logFilePath, true);
    }

    /**
     * Public API exposed to client to find a user with maximum visits by session
     */
    public void printMaxVisitUser() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(logFilePath))) {

            br.lines().filter(line -> isValidEntry(line))
                    .map(line -> line.split(fileDelimiter))
                    .forEach(s -> processInputLine(s));

            if(!timestampMapByUser.isEmpty()){
                process();
            } else {
                System.out.println("The input log file is either empty or all entries are in incorrect format.");
            }

        } catch (IOException e) {
            System.out.println("Given input file doesn't exist. Please check the path");
        }
    }

    /**
     * Returns true if input line has the expected format
     * Expected format - (Long delimiter Long)
     * @param entry
     * @return
     */
    private boolean isValidEntry(String entry) {
        boolean isValid = true;
        if(entry == null || entry.isEmpty()) return false;

        String[] arr = entry.split(fileDelimiter);
        if(arr.length != 2) return false;

        try {
            Long.parseLong(arr[0]);
            long number = Long.parseLong(arr[1]);
            if(number < 0) isValid = false;
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return isValid;
    }

    private void processInputLine(String[] input) {
        User user = new User(input[0]);
        long timeStamp = Long.parseLong(input[1]);
        createTimestampMapByUser(user, timeStamp);
    }

    /**
     * Finds the maximum visit user from a given input line.
     */
    private void process() {
        PriorityQueue<UserSessionEntry> sessionPQ = new PriorityQueue<>(new UserSessionEntryComparator().reversed());
        for(Map.Entry<User, TreeSet<Long>> e: timestampMapByUser.entrySet()) {
            User user = e.getKey();
            TreeSet<Long> set = e.getValue();
            mergeTimeStamps(user, set, sessionPQ);
        }
        printMaxUser(sessionPQ.peek());
    }

    /**
     * Creates a map of users with their respective set of timestamps.
     * This method uses a TreeSet to get these timestamps in sorted order.
     * @param user
     * @param timeStamp
     */
    private void createTimestampMapByUser(User user, long timeStamp) {
        if(timestampMapByUser.containsKey(user)) {
            timestampMapByUser.get(user).add(timeStamp);
        } else {
            TreeSet<Long> set = new TreeSet<>();
            set.add(timeStamp);
            timestampMapByUser.put(user, set);
        }
    }

    /**
     * Iterates through a list of timestamps of every user, and merges these timestamps in case they are
     * part of the same session. While performing merging of timestamps, this method also updates the startTimestamp
     * and endTimestamp if applicable.
     *
     * These session entries are added in a Priority Queue which uses a custom operator to sort the entries on
     * the basis of number of visits and last time stamps.
     * @param user
     * @param set
     * @param sessionPQ
     */
    private void  mergeTimeStamps(User user, TreeSet<Long> set, PriorityQueue<UserSessionEntry> sessionPQ) {
        UserSessionEntry currentUserSession = new UserSessionEntry(user, new Session(set.pollFirst()));
        Session currentSession = currentUserSession.getSession();

        while(! set.isEmpty()) {
            long timestamp = set.pollFirst();
            if(currentSession.isSameSession(timestamp)) {
                currentSession.updateSessionTimeStamps(timestamp);
                currentUserSession.incrementSessionVisits();
            } else {
                sessionPQ.add(currentUserSession);
                currentSession = new Session(timestamp);
                currentUserSession = new UserSessionEntry(user,currentSession);
            }
        }
        sessionPQ.add(currentUserSession);
    }

    /**
     * Print the user session entry in the required output format
     * @param maxUser
     */
    private void printMaxUser(UserSessionEntry maxUser) {
        System.out.println(maxUser.formattedOutput());
    }

    /**
     * Sets the delimiter used in the log file to separate different entities in one line
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        fileDelimiter = delimiter;
    }
}
