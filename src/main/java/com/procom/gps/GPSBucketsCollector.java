package com.procom.gps;

import com.procom.gps.storage.BucketsStorage;
import com.procom.gps.storage.FileBucketsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Evgeniy Surovskiy
 */
public class GPSBucketsCollector
{
    private static final Logger LOG = LoggerFactory.getLogger(GPSBucketsCollector.class);
    private final BucketsStorage bucketsStorage = new FileBucketsStorage();

    public void processNewBucket(final String rawString)
    {
        final AbstractGPSBucket bucket = GL300ResponseParser.parse(rawString);
        if(bucket != null)
        {
            bucketsStorage.saveSingleBucket(bucket);
        }
    }
}
