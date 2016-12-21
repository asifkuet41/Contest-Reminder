package com.example.asif.contestremind;

/**
 * Created by asif on 12/19/16.
 */

public class Contest {

    /** Status of the contest */
    private String mStatus;

    /** Name of the contest */
    private String mName;

    /** Time of the contest */
    private long mTimeInMillisecond;

    /**
     * Create a new {@link Contest}object.
     *
     * @param status is the Status of the contest that is finished or upcoming
     * @param name is the name of the contest
     * @param timeInMillisecond is the date of the contest
     */
    public Contest(String status,String name, long timeInMillisecond){
        mStatus=status;
        mName=name;
        mTimeInMillisecond=timeInMillisecond;
    }

    /**
     * Returns the status of the contest.
     */
    public String getStatus(){
        return mStatus;
    }

    /**
     * Returns the name of the contest.
     */
    public String getName(){
        return  mName;
    }

    /**
     * Returns the date of the contest
     */
    public long getTimeInMillisecond(){
        return mTimeInMillisecond;
    }
}
