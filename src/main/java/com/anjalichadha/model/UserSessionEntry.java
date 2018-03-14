package com.anjalichadha.model;

/**
 * Model class for defining a user session and number of unique visits in it.
 */
public class UserSessionEntry {
    private User user;
    private Session session;
    private int numberOfVisits = 1;

    public UserSessionEntry(User user, Session session) {
        this.user = user;
        this.session = session;
    }

    public void incrementSessionVisits() {
        this.numberOfVisits++;
    }

    public int getNumberOfVisits() {
        return numberOfVisits;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "UserSessionEntry{" +
                "user=" + user +
                ", session=" + session +
                ", numberOfVisits=" + numberOfVisits +
                '}';
    }

    public String formattedOutput() {
        return user.getUserId() + "," +
                numberOfVisits + "," +
                session.getEndTimeStamp();
    }
}
