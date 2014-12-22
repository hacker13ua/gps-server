package com.procom.gps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Evgeniy Surovskiy
 */
public class GL300ResponseParser
{
    private static final String GTFRI = "GTFRI";
    private static final Logger LOG = LoggerFactory.getLogger(GL300ResponseParser.class);

    public static GL300Bucket parse(final String response)
    {
        LOG.debug("Response string to parse = " + response);
        final String[] responseInfo = response.split(",");
        final String header = responseInfo[0];
        if(GTFRI.equals(header.split(":")[1]))
        {
            final GL300Bucket gl300Bucket = new GL300Bucket();
            gl300Bucket.setTrackerUniqueId(responseInfo[2]);
//            gl300Bucket.setProtocolVersion(responseInfo[1]);
            gl300Bucket.setLongitude(Double.parseDouble(responseInfo[11]));
            gl300Bucket.setLatitude(Double.parseDouble(responseInfo[12]));
//            gl300Bucket.setAltitude(Double.parseDouble(responseInfo[10]));
//            gl300Bucket.setAzimuth(Short.parseShort(responseInfo[9]));
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            try
            {
                gl300Bucket.setDate((simpleDateFormat.parse(responseInfo[13])));
            }
            catch(final ParseException e)
            {
                LOG.error("Error while parse date ", e);
            }
            LOG.debug(gl300Bucket.toString());
            return gl300Bucket;
        }
        else
        {
            LOG.warn("Response is not " + GTFRI + " Can't parse it. Full response: " + response);
            return null;
        }
    }
}
