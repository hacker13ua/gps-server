package com.procom.gps.storage;

import com.procom.gps.AbstractGPSBucket;
import com.procom.gps.GPSTrackerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Evgeniy Surovskiy
 */
public class FileBucketsStorage implements BucketsStorage
{
    private static final Logger LOG = LoggerFactory.getLogger(FileBucketsStorage.class);

    private static final String BASE_PATH = System.getProperty("file-storage.base.path", "");

    @Override
    public void saveSingleBucket(final AbstractGPSBucket bucket)
    {
        if(isPossibleToSave(bucket))
        {
            try (FileWriter out = new FileWriter(BASE_PATH.isEmpty() ? bucket.getTrackerUniqueId() : BASE_PATH + File.separator + bucket.getTrackerUniqueId(), true))
            {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
                out.write(simpleDateFormat.format(bucket.getDate()) + " " + bucket.getLatitude() + " " + bucket.getLongitude() + "\n");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            LOG.warn("Drop message from tracker " + bucket.getTrackerUniqueId());
        }
    }

    @Override
    public void saveBucketsList(final List<AbstractGPSBucket> buckets)
    {
        if (buckets == null || buckets.isEmpty())
        {
            LOG.warn("Can't save empty list");
            return;
        }
        Collections.sort(buckets, new Comparator<AbstractGPSBucket>()
        {
            @Override
            public int compare(AbstractGPSBucket o1, AbstractGPSBucket o2)
            {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        if (isPossibleToSave(buckets.get(0)))
        {
            LOG.info("Starting save " + buckets.size() + " buckets to file");
            final SimpleDateFormat simpleDateFormatFile = new SimpleDateFormat("ddMMyyyy-hh:mm:ss");
            final String fileName = buckets.get(0).getTrackerUniqueId() + "-" + simpleDateFormatFile.format(buckets.get(0).getDate());
            try (FileWriter out = new FileWriter(BASE_PATH.isEmpty() ? fileName : BASE_PATH + File.separator + fileName, true))
            {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm:ss");
                for (final AbstractGPSBucket bucket : buckets)
                {
                    out.write(simpleDateFormat.format(bucket.getDate()) + " " + bucket.getLatitude() + " " + bucket.getLongitude() + "\n");
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            LOG.warn("Drop message from tracker " + buckets.get(0).getTrackerUniqueId());
        }
    }

    private boolean isPossibleToSave(final AbstractGPSBucket bucket)
    {
        return GPSTrackerValidator.isBucketValid(bucket.getTrackerUniqueId());
    }

}
