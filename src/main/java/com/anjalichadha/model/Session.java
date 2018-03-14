package com.anjalichadha.model;

import java.util.UUID;

/**
 * A session is defined by a unique sessionId, its starting timestamp
 * and ending timestamp.
 *
 * If a timestamp doesn't lie neither between start and end timestamp
 * nor within one hour distance of the startTimeStamp
 * and endTimeStamp, then it doesn't lie in the same session.
 */
public class Session {

    private String sessionId = UUID.randomUUID().toString();;
    private long startTimeStamp;
    private long endTimeStamp;
    private final static long MILLISECONDS_PER_HOUR = 3600000;

    public Session(long timeStamp) {
        this.startTimeStamp = timeStamp;
        this.endTimeStamp = timeStamp;
    }

    public Session(long startTimeStamp, long endTimeStamp) {
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    /**
     * Returns boolean describing whether given timestamp is part of
     * current session or not.
     * @param timeStamp
     * @return
     */
    public boolean isSameSession(long timeStamp) {
        long startTimeStampDiff = Math.abs(timeStamp - this.startTimeStamp);
        long endTimeStampDiff = Math.abs(timeStamp - this.endTimeStamp);

        if(startTimeStampDiff <= MILLISECONDS_PER_HOUR || endTimeStampDiff <= MILLISECONDS_PER_HOUR) {
            return true;
        }
        return false;
    }

    /**
     * Update the session starting or ending timestamps to include the new
     * timestamp.
     * @param timeStamp
     */
    public void updateSessionTimeStamps(long timeStamp) {
        if(timeStamp < this.startTimeStamp) {
            this.startTimeStamp = timeStamp;
        } else if(timeStamp > this.endTimeStamp) {
            this.endTimeStamp = timeStamp;
        }
        //If none of the above cases, timeStamp lies in the middle of start and end, do nothing
    }
}
