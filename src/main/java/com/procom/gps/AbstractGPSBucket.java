package com.procom.gps;

import java.util.Date;

/**
 * @author Evgeniy Surovskiy
 */
public abstract class AbstractGPSBucket
{
    protected String trackerUniqueId;
    protected double longitude;
    protected double latitude;
    protected Date date;

    public String getTrackerUniqueId()
    {
        return trackerUniqueId;
    }

    public void setTrackerUniqueId(String trackerUniqueId)
    {
        this.trackerUniqueId = trackerUniqueId;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "AbstractGPSBucket{" +
                "trackerUniqueId=" + trackerUniqueId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", date=" + date +
                '}';
    }
}
