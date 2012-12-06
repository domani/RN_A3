package filecopy;

/* FileCopyClient.java
 Version 0.1 - Muss erg�nzt werden!!
 Praktikum 3 Rechnernetze BAI4-SS2012 HAW Hamburg
 Autoren:
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class FileCopyClient extends Thread {

    // -------- Constants
    public final static boolean TEST_OUTPUT_MODE = false;
    public final int SERVER_PORT = 23000;
    public final int UDP_PACKET_SIZE = 1024;
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
    private long nextSeqNum = 0L;
    // -------- Buffer
    private byte[] sendData;
    //sendBuf.size = windowSize
    private List<FCpacket> sendBuf;

    // Constructor
    public FileCopyClient(String serverArg, String sourcePathArg,
            String destPathArg, String windowSizeArg, String errorRateArg) {
        servername = serverArg;
        sourcePath = sourcePathArg;
        destPath = destPathArg;
        windowSize = Integer.parseInt(windowSizeArg);
        serverErrorRate = Long.parseLong(errorRateArg);

    }

    public void runFileCopyClient() throws FileNotFoundException, IOException {
        //1. Buffer mit der Datei befüllen & daraus Pakete extrahieren
        byte[] buf = makeFile();
        sendBuf = new ArrayList<FCpacket>();
        //Steuerpaket an den Anfang stellen
        sendBuf.add(makeControlPacket());
        
        
        //mit Paketen befüllen
        for (int i = 0; i < windowSize; ++i) {
          //müsste ne andere größe sein
          sendData = new byte[UDP_PACKET_SIZE];
          //mit daten aufm buf füllen
          
          FCpacket packet = new  FCpacket(nextSeqNum, sendData, UDP_PACKET_SIZE);
          sendBuf.add(packet);
          
          //UDP Paket aufbereiten
          FCpacket.writeBytes(packet.getSeqNum(), sendData, 0, 8);
          DatagramPacket udpSendPacket = new DatagramPacket(packet.getData(), UDP_PACKET_SIZE);

        }


    }
    
    

    public byte[]  makeFile() throws FileNotFoundException, IOException {
        File f = new File(sourcePath);
        FileInputStream in = new FileInputStream(f);
        int len = (int) f.length();
        byte buf[] = new byte[len];
        in.read(buf, 0, len);
        in.close();
        return buf;
    }
    


    
    
    public void retrieveAck() throws SocketException {
        DatagramPacket udpReceivePacket;
        clientSocket = new DatagramSocket(SERVER_PORT);
        /*
         udpReceivePacket = new DatagramPacket(receiveData,
         UDP_PACKET_SIZE);
         // Wait for data packet
         serverSocket.receive(udpReceivePacket);
         receivedIPAddress = udpReceivePacket.getAddress();
         receivedPort = udpReceivePacket.getPort();
         udpReceivePacket = new DatagramPacket(receiveData,
         UDP_PACKET_SIZE);
         // Wait for data packet
         clientSocket.receive(udpReceivePacket);
         * */
    }

    /**
     *
     * Timer Operations
     */
    public void startTimer(FCpacket packet) {
        /* Create, save and start timer for the given FCpacket */
        FC_Timer timer = new FC_Timer(timeoutValue, this, packet.getSeqNum());
        packet.setTimer(timer);
        timer.start();
    }

    public void cancelTimer(FCpacket packet) {
        /* Cancel timer for the given FCpacket */
        testOut("Cancel Timer for packet" + packet.getSeqNum());

        if (packet.getTimer() != null) {
            packet.getTimer().interrupt();
        }
    }

    /**
     * Implementation specific task performed at timeout
     */
    public void timeoutTask(long seqNum) {
        // ToDo
        //Starten Sie für jeden Timer einen Thread der mitgegebenen Klasse FC_Timer und passen Sie
        //den Code für Ihre Implementierung an
    }

    /**
     *
     * Computes the current timeout value (in nanoseconds)
     */
    public void computeTimeoutValue(long sampleRTT) {
        // ToDo
    }

    /**
     *
     * Return value: FCPacket with (0 destPath;windowSize;errorRate)
     */
    public FCpacket makeControlPacket() {
        /* Create first packet with seq num 0. Return value: FCPacket with
         (0 destPath ; windowSize ; errorRate) */
        String sendString = destPath + ";" + windowSize + ";" + serverErrorRate;
        byte[] sendData = null;
        try {
            sendData = sendString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new FCpacket(0, sendData, sendData.length);
    }

    public void testOut(String out) {
        if (TEST_OUTPUT_MODE) {
            System.err.printf("%,d %s: %s\n", System.nanoTime(), Thread
                    .currentThread().getName(), out);
        }
    }

    public static void main(String argv[]) throws Exception {
        //hier noch das einfügen, was übergeben werden muss
        //Konstruktor: new FileCopyClient("CowboyBebop", String sourcePath, String destPath, String windowSize, String errorRate)
        //zu testen (Werte ermitteln): windowsize = 1,8,128; error_rate: 10,100,1000                           
        FileCopyClient myClient = new FileCopyClient(argv[0], argv[1], argv[2],
                argv[3], argv[4]);
        myClient.runFileCopyClient();
    }
}
