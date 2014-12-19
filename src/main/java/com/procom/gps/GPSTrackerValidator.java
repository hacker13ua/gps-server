package com.procom.gps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Evgeniy Surovskiy
 */
public class GPSTrackerValidator implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(GPSTrackerValidator.class);
    private final static Set<String> availableTrackerUIDs = new HashSet<>();
    private final static ReentrantReadWriteLock readWriteIDsLock = new ReentrantReadWriteLock();
    private final static Lock readIDsLock = readWriteIDsLock.readLock();
    private final static Lock writeIDsLock = readWriteIDsLock.writeLock();
    public final static String AVAILABLE_UID_FILE_PATH = System.getProperty("available-uid", "");

    @Override
    public void run()
    {
        while(true)
        {
            updateValidationInfo();
            try
            {
                Thread.sleep(5 * 60 * 1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void updateValidationInfo()
    {
        LOG.info("Updating available tracker ids");
        final BufferedReader reader;
        writeIDsLock.lock();
        try
        {
            reader = new BufferedReader(new FileReader(AVAILABLE_UID_FILE_PATH));
            String line;
            availableTrackerUIDs.clear();
            while((line = reader.readLine()) != null)
            {
                availableTrackerUIDs.add(line);
            }
            LOG.debug("Available ids: " + availableTrackerUIDs);
        }
        catch(FileNotFoundException e)
        {
            LOG.error("No such file - " + AVAILABLE_UID_FILE_PATH, e);
        }
        catch(IOException e)
        {
            LOG.error("Error while read file " + AVAILABLE_UID_FILE_PATH, e);
        }
        finally
        {
            writeIDsLock.unlock();
        }
    }

    public static boolean isBucketValid(final String bucketUID)
    {
        readIDsLock.lock();
        try
        {
            return AVAILABLE_UID_FILE_PATH.isEmpty() || availableTrackerUIDs.contains(bucketUID);
        }
        finally
        {
            readIDsLock.unlock();
        }
    }

    /*public static Set<String> getAvailableTrackerUIDs()
    {
        readIDsLock.lock();
        try
        {
            return availableTrackerUIDs;
        }
        finally
        {
            readIDsLock.unlock();
        }
    }*/
}
