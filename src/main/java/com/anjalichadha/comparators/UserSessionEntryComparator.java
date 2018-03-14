package com.anjalichadha.comparators;

import com.anjalichadha.model.UserSessionEntry;

import java.util.Comparator;

/**
 * Implements a custom comparator for UserSessionEntry.
 */
public class UserSessionEntryComparator implements Comparator<UserSessionEntry> {
    @Override
    public int compare(UserSessionEntry o1, UserSessionEntry o2) {
        int visitsDiff = o1.getNumberOfVisits() - o2.getNumberOfVisits();
        if(visitsDiff != 0) return visitsDiff;
        else return (int)(o1.getSession().getEndTimeStamp() - o2.getSession().getEndTimeStamp());
    }
}
