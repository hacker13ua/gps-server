package com.procom.gps;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.procom.gps.storage.BucketsStorage;
import com.procom.gps.storage.FileBucketsStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Evgeniy Surovskiy
 */
public class GPSBucketsCollector
{
    private static final Logger LOG = LoggerFactory.getLogger(GPSBucketsCollector.class);
    private static final int BATCH_SIZE = 3;

    private final BucketsStorage bucketsStorage = new FileBucketsStorage();
    private final Multimap<String, AbstractGPSBucket> bucketsToSave = Multimaps.synchronizedListMultimap(ArrayListMultimap.<String, AbstractGPSBucket>create(50, BATCH_SIZE));

    public void processNewBucket(final String rawString)
    {
        final AbstractGPSBucket bucket = GL300ResponseParser.parse(rawString);
        if(bucket != null)
        {
            bucketsToSave.put(bucket.getTrackerUniqueId(), bucket);
            trySaveBuckets(bucket.getTrackerUniqueId());
        }
    }

    private void trySaveBuckets(String bucketUID)
    {
        if(bucketsToSave.get(bucketUID).size() >= BATCH_SIZE)
        {
            bucketsStorage.saveBucketsList(new ArrayList<>(bucketsToSave.removeAll(bucketUID)));
        }
    }
}
