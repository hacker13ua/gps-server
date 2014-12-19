package com.procom.gps;

import com.procom.gps.server.GPSServer;
import com.procom.gps.server.NettyGPSServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Evgeniy Surovskiy
 */
public class Main
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final int PORT = Integer.parseInt(System.getProperty("port", "5455"));

    public static void main(String[] args)
    {
        final GPSServer gpsServer = new NettyGPSServer();
        if (!GPSTrackerValidator.AVAILABLE_UID_FILE_PATH.isEmpty())
        {
            new Thread(new GPSTrackerValidator()).start();
        }
        gpsServer.startServer(PORT);
        System.out.print("YYYY hhhh");
    }
}
