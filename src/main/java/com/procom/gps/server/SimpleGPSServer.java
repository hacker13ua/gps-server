package com.procom.gps.server;

import com.procom.gps.GL300ResponseParser;
import com.procom.gps.GPSBucketsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Very simple socket server
 * @author Evgeniy Surovskiy
 */
public class SimpleGPSServer extends Thread implements GPSServer
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleGPSServer.class);
    private final static GPSBucketsCollector gpsBucketsCollector = new GPSBucketsCollector();


    Socket socket;
    int num;

    private SimpleGPSServer(final int num, final Socket socket)
    {
        // копируем данные
        this.num = num;
        this.socket = socket;

        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    public SimpleGPSServer()
    {
        // empty constructor
    }

    public void run()
    {
        try
        {
            final InputStream is = socket.getInputStream();

            final byte buffer[] = new byte[1024]; //buffer 1 kB
            final int messageLength = is.read(buffer);
            if (messageLength < 0)
            {
                LOG.warn("Socket close");
                return;
            }
            final String messageContent = new String(buffer, 0, messageLength);

            LOG.info(num + ": " + messageContent);
            gpsBucketsCollector.processNewBucket(messageContent);
            socket.close();
        }
        catch(Exception e)
        {
            LOG.error("Init error: ", e);
        }
    }

    @Override
    public void startServer(int port)
    {
        try
        {
            int i = 0;
            ServerSocket server = new ServerSocket(port);

            LOG.info("GPS Server started on port " + port);

            while(true)
            {
                new SimpleGPSServer(i, server.accept());
                i++;
            }
        }
        catch(Exception e)
        {
            System.out.println("init error: " + e);
        }
    }
}
