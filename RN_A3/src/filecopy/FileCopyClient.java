package filecopy;

/* FileCopyClient.java
 Version 0.1 - Muss erg�nzt werden!!
 Praktikum 3 Rechnernetze BAI4-SS2012 HAW Hamburg
 Autoren:
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileCopyClient extends Thread {

    // -------- Constants
    public final static boolean TEST_OUTPUT_MODE = false;
    public final int SERVER_PORT = 23000;
    public final int UDP_PACKET_SIZE = 1024;
    public final int DATA_PACKET_SIZE = UDP_PACKET_SIZE - 8;
    // -------- Public parms
    public String servername;
    public String sourcePath;
    public String destPath;
    public int windowSize;
    public long serverErrorRate;
    // -------- Connection
    private DatagramSocket clientSocket;
    // -------- Variables
    // current default timeout in nanoseconds
    private long timeoutValue = 100000000L;
    private long nextSeqNum = 1L;
    private long bytesRemaining;
    private long estimatedRTT = 0;
    private long deviation = 0;
    // -------- Buffer
    private File f;
    private FileInputStream in;
    private final SendBuffer buffer;
    //sendBuf.size = windowSize

    // Constructor
    public FileCopyClient(String serverArg, String sourcePathArg,
            String destPathArg, String windowSizeArg, String errorRateArg)
    {
        servername = serverArg;
        sourcePath = sourcePathArg;
        destPath = destPathArg;
        windowSize = Integer.parseInt(windowSizeArg);
        serverErrorRate = Long.parseLong(errorRateArg);

        //Datei einlesen initialisieren
        f = new File(sourcePath);
        bytesRemaining = f.length();
        try
        {
            in = new FileInputStream(f);
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        buffer = new SendBuffer(windowSize);

    }

    public void runFileCopyClient() throws FileNotFoundException, IOException, InterruptedException
    {
        receivePackets();
        
        buffer.add(makeControlPacket());
        startTimer(buffer.getPacket(0));
        clientSocket = new DatagramSocket(SERVER_PORT);
        clientSocket.send(new DatagramPacket(buffer.getPacket(0).getSeqNumBytesAndData(), buffer.getPacket(0).getSeqNumBytesAndData().length));


        //wieder anfang
        while (bytesRemaining > 0)
        {
            buffer.add(makeFCPacket());
            long aktSeqNum = buffer.getPacket(buffer.size() - 1).getSeqNum();
            byte aktData[] = buffer.getPacket(buffer.size() - 1).getData();

            FCpacket.writeBytes(aktSeqNum, aktData, 0, 8);

            //vorm senden timer setzen
            startTimer(buffer.getPacket(buffer.size() - 1));
            DatagramPacket udpSendPacket = new DatagramPacket(aktData, UDP_PACKET_SIZE);
            clientSocket.send(udpSendPacket);

        }
        synchronized (buffer)
        {
            while (!buffer.isEmpty() && bytesRemaining > 0)
            {
                wait();
            }
        }
    }

    public void receivePackets()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (!buffer.isEmpty() && bytesRemaining > 0)
                {
                    byte[] receiveData = new byte[UDP_PACKET_SIZE];
                    DatagramPacket udpReceivePacket = new DatagramPacket(receiveData,
                            UDP_PACKET_SIZE);
                    try
                    {
                        // Wait for data packet
                        clientSocket.receive(udpReceivePacket);
                    } catch (IOException ex)
                    {
                        Logger.getLogger(FileCopyClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    FCpacket fcReceivePacket = new FCpacket(udpReceivePacket.getData(),
                            udpReceivePacket.getLength());
                    long seqNum = fcReceivePacket.getSeqNum();
                    buffer.setAckValid(seqNum);
                    cancelTimer(buffer.getPacket(seqNum));

                    while (buffer.getPacket(0).isValidACK())
                    {
                        buffer.removePacket();
                    }
                }
            }
        }).start();


    }

    public FCpacket makeFCPacket() throws FileNotFoundException, IOException
    {
        byte sendData[];

        if (bytesRemaining > DATA_PACKET_SIZE)
        {
            sendData = new byte[DATA_PACKET_SIZE];
        } else
        {
            sendData = new byte[(int) bytesRemaining];
        }

        in.read(sendData, 0, sendData.length);
        //schreibt trotzdem senddata mit crap voll
        //dateilänge - offset. falls d-o == 0 || rest < data_packet_size, dann nur restlänge reinschreiben
        bytesRemaining -= sendData.length;

        FCpacket packet = new FCpacket(++nextSeqNum, sendData, sendData.length);
        return packet;
    }

    /**
     *
     * Timer Operations
     */
    public void startTimer(FCpacket packet)
    {
        /* Create, save and start timer for the given FCpacket */
        FC_Timer timer = new FC_Timer(timeoutValue, this, packet.getSeqNum());
        packet.setTimer(timer);
        timer.start();
    }

    public void cancelTimer(FCpacket packet)
    {
        /* Cancel timer for the given FCpacket */
        testOut("Cancel Timer for packet" + packet.getSeqNum());

        if (packet.getTimer() != null)
        {
            packet.getTimer().interrupt();
        }
    }

    /**
     * Implementation specific task performed at timeout
     */
    public void timeoutTask(long seqNum) throws IOException
    {
        // ToDo
        //Starten Sie für jeden Timer einen Thread der mitgegebenen Klasse FC_Timer und passen Sie
        //den Code für Ihre Implementierung an
        startTimer(buffer.getPacket(seqNum));
       // DatagramPacket udpSendPacket = new DatagramPacket(buffer.getPacket(seqNum).getData(), UDP_PACKET_SIZE);
       // clientSocket.send(udpSendPacket);


    }

    /**
     *
     * Computes the current timeout value (in nanoseconds)
     */
    public void computeTimeoutValue(long sampleRTT)
    {
        //SampleRTT: gemessene Zeit vom Versenden eines Segments bis zur Bestätigung durch ACK

        //Typischer Wert von x: 0,1
        double x = 0.1;

        //erwartete Round Trip Time
        estimatedRTT = (long) ((1 - x) * estimatedRTT + x * sampleRTT);

        //Deviation = “sicherer Abstand”
        //Falls die EstimatedRTT starkt variieren -> muss der Sicherheitsabstand größer werden
        deviation = (long) ((1 - x) * deviation + x * Math.abs(sampleRTT - estimatedRTT));
        timeoutValue = estimatedRTT + 4 * deviation;


    }

    /**
     *
     * Return value: FCPacket with (0 destPath;windowSize;errorRate)
     */
    public FCpacket makeControlPacket()
    {
        /* Create first packet with seq num 0. Return value: FCPacket with
         (0 destPath ; windowSize ; errorRate) */
        String sendString = destPath + ";" + windowSize + ";" + serverErrorRate;
        byte[] sendData = null;
        try
        {
            sendData = sendString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        return new FCpacket(0L, sendData, sendData.length);
    }

    public void testOut(String out)
    {
        if (TEST_OUTPUT_MODE)
        {
            System.err.printf("%,d %s: %s\n", System.nanoTime(), Thread
                    .currentThread().getName(), out);
        }
    }

    public static void main(String argv[]) throws Exception
    {
        //hier noch das einfügen, was übergeben werden muss
        //Konstruktor: new FileCopyClient("CowboyBebop", String sourcePath, String destPath, String windowSize, String errorRate)
        //zu testen (Werte ermitteln): windowsize = 1,8,128; error_rate: 10,100,1000                           
        FileCopyClient myClient = new FileCopyClient("CowboyBebop", "C:/Users/Domani/Desktop/HAW/BSSicherheit.pdf", "C:/Users/Domani/Desktop/BSSicherheit.pdf",
                "128", "100");
        
        myClient.runFileCopyClient();
    }
}
