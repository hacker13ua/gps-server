package com.procom.gps;

/**
 * @author Evgeniy Surovskiy
 */
public class GL300Bucket extends AbstractGPSBucket
{
    private String protocolVersion;
    private short azimuth;
    private double altitude;

    public String getProtocolVersion()
    {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion)
    {
        this.protocolVersion = protocolVersion;
    }

    public short getAzimuth()
    {
        return azimuth;
    }

    public void setAzimuth(short azimuth)
    {
        this.azimuth = azimuth;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public void setAltitude(double altitude)
    {
        this.altitude = altitude;
    }

    @Override
    public String toString()
    {
        return "GL300Bucket{" +
                "trackerUniqueId=" + trackerUniqueId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", date=" + date +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", azimuth=" + azimuth +
                ", altitude=" + altitude +
                '}';
    }
}
