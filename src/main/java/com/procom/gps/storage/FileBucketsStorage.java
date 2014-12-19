package com.procom.gps.storage;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.procom.gps.AbstractGPSBucket;
import com.procom.gps.GL300Bucket;
import com.procom.gps.GPSTrackerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
        if (GPSTrackerValidator.AVAILABLE_UID_FILE_PATH.isEmpty() || GPSTrackerValidator.getAvailableTrackerUIDs().contains(bucket.getTrackerUniqueId()))
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
    @Deprecated
    public void saveBucketsList(final List<AbstractGPSBucket> buckets)
    {
//        for(List<AbstractGPSBucket> buckets : gpsTrackerToBuckets.values())
//        {
        LOG.info("Starting save " + buckets.size() + " buckets to file");
        Collections.sort(buckets, new Comparator<AbstractGPSBucket>()
        {
            @Override
            public int compare(AbstractGPSBucket o1, AbstractGPSBucket o2)
            {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final YamlWriter yamlWriter = new YamlWriter(new OutputStreamWriter(outputStream));
        try
        {
            yamlWriter.getConfig().setClassTag("GL300", GL300Bucket.class);
            yamlWriter.write(buckets);
            yamlWriter.close();
        }
        catch(YamlException e)
        {
            LOG.error("Error while serialize by yaml ", e);
        }
        final SimpleDateFormat folderFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        StringBuilder filePath = new StringBuilder(BASE_PATH).append(File.separator)
                .append(buckets.get(0).getTrackerUniqueId())
                .append(File.separator)
                .append(folderFormat.format(buckets.get(0).getDate()));
        final File reportsDirectory = new File(filePath.toString().replaceAll("/", File.separator));
        if(!reportsDirectory.exists())
        {
            if(!reportsDirectory.mkdirs())
            {
                LOG.error("Can not create report directory: " + filePath);
            }
        }
        final File file = new File(filePath.append(File.separator).append(buckets.get(0).getDate()).append('-').append(buckets.get(buckets.size() - 1).getDate()).append(".txt").toString());
        try
        {
            final FileOutputStream fop = new FileOutputStream(file);

            // if file doesn't exists, then create it
            if(!file.exists())
            {
                file.createNewFile();
            }

            fop.write(outputStream.toByteArray());
            fop.flush();
            fop.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
//    }
}
