package com.procom.gps.storage;

import com.procom.gps.AbstractGPSBucket;

import java.util.List;
import java.util.Map;

/**
 * @author Evgeniy Surovskiy
 */
public interface BucketsStorage
{
    void saveSingleBucket(AbstractGPSBucket bucket);

    @Deprecated
    void saveBucketsList(List<AbstractGPSBucket> buckets);
}
