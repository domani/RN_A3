/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adp.a2.dateiverwaltung;

import adp.a2.Util;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Domani
 */
public class Band {

    //TODO output / FileOutputStrean rausnehmen
    int name;
    private File f;
    private RandomAccessFile rFile;
    //private FileOutputStream output;
    private Band.Run aktRun;
    private List<Band.Run> runQueue;
    private String path;
    private boolean linkerPuffer = true;
    private final List<Byte> readBuffer1 = new ArrayList<Byte>();
    private final List<Byte> readBuffer2 = new ArrayList<Byte>();
    private final List<Byte> writeBuffer1 = new ArrayList<Byte>();
    private final List<Byte> writeBuffer2 = new ArrayList<Byte>();
    private static final int ANZAHL_ZAHLEN_IM_PUFFER = 4;
    private static final int BUFFER_SIZE = ANZAHL_ZAHLEN_IM_PUFFER * 4;

    public Band(int name, String path) {
        this.name = name;
        this.f = new File(path);
        try {
            this.rFile = new RandomAccessFile(f, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.path = path;

        runQueue = new ArrayList();

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



    }

    public boolean leer() {
        return runQueue.isEmpty() && aktRun == null;
    }

    public void addRun() {
        aktRun = new Band.Run(0, f.length(), rFile);
        runQueue.add(aktRun);
    }

    public void endRun() {
        aktRun = null;
    }

//    private void writeBuffer(List<Byte> buffer) {
//        synchronized (buffer) {
//            while (buffer.isEmpty()) {
//                try {
//                    buffer.wait();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            try {
//                rFile.seek(aktRun.endRun);
//                byte[] bAry = new byte[BUFFER_SIZE];
//                for (int i = 0; i < BUFFER_SIZE; ++i) {
//                    bAry[i] = buffer.remove(0);
//                }
//                rFile.write(bAry);
//            } catch (IOException ex) {
//                Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            buffer.notifyAll();
//        }
//    }

    /**
     * @param number
     */
    public void addNumber(int number) {
        aktRun.addNumber(number);


        /*
         * if (writeBuffer1.size() == BUFFER_SIZE) { linkerPuffer = false;
         * writeBuffer(writeBuffer1); } if (writeBuffer2.size() == BUFFER_SIZE)
         * { linkerPuffer = true; writeBuffer(writeBuffer2); } if (linkerPuffer)
         * { synchronized (writeBuffer1) { while (writeBuffer1.size() ==
         * BUFFER_SIZE) { try { writeBuffer1.wait(); } catch
         * (InterruptedException ex) {
         * Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex); }
         * } byte bAry[] = Util.intToByte(number); for (int i = 0; i < 4; ++i) {
         * writeBuffer1.add(bAry[i]); } writeBuffer1.notifyAllAll(); } } else {
         * synchronized (writeBuffer2) { while (writeBuffer2.size() ==
         * BUFFER_SIZE) { try { writeBuffer2.wait(); } catch
         * (InterruptedException ex) {
         * Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex); }
         * } byte bAry[] = Util.intToByte(number); for (int i = 0; i < 4; ++i) {
         * writeBuffer1.add(bAry[i]); } writeBuffer2.notifyAllAll(); }
        }
         */
    }

    public long size() {
        int x = 0;
        for (Band.Run r : runQueue) {
            x += r.size;
        }
        if (aktRun != null) {
            x += aktRun.size;
        }
        return x;
    }

    public int getRunCount() {
        return runQueue.size() + ((aktRun != null) ? 1 : 0);
    }

    private void setNextRun() {
        aktRun = (runQueue.size() > 0) ? runQueue.remove(0) : null;
    }

    public int getNumber() {
        if (runFinished()) {
            setNextRun();
        }
        if (aktRun == null) {
            return -1;
        }
        return aktRun.getNumber();
    }

//    /**
//     * @param countNumber
//     * @return
//     */
//    public void getNumber4Buffer(List<Byte> buffer) {
//
//        synchronized (buffer) {
//            while (!buffer.isEmpty()) {
//                try {
//                    buffer.wait();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (runFinished()) {
//                setNextRun();
//            }
//            //if(aktRun == null) return -1;
//            try {
//                rFile.seek(aktRun.position);
//                if (buffer.isEmpty()) {
//                    for (int i = 0; i < BUFFER_SIZE; ++i) {
//                        try {
//                            buffer.add(rFile.readByte());
//                        } catch (EOFException ex) {
//                            break;
//                        }
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            buffer.notifyAllAll();
//        }
//    }
//    public int getNumber(boolean deleteReadedNumbers) {
//        if (readBuffer1.isEmpty()) {
//            linkerPuffer = false;
//            getNumber4Buffer(readBuffer1);
//        }
//        if (readBuffer2.isEmpty()) {
//            linkerPuffer = true;
//            getNumber4Buffer(readBuffer2);
//        }
//        if (runFinished()) {
//            setNextRun();
//        }
//        if (aktRun == null) {
//            return -1;
//        }
//        byte[] buffer = new byte[4];
//        if (linkerPuffer) {
//            synchronized (readBuffer1) {
//                while (readBuffer1.isEmpty()) {
//                    try {
//                        readBuffer1.wait();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                for (int i = 0; i < 4; ++i) {
//                    buffer[i] = readBuffer1.remove(0);
//                }
//                readBuffer1.notifyAllAll();
//            }
//        } else {
//            synchronized (readBuffer2) {
//                while (readBuffer2.isEmpty()) {
//                    try {
//                        readBuffer2.wait();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//                for (int i = 0; i < 4; ++i) {
//                    buffer[i] = readBuffer2.remove(0);
//                }
//                readBuffer2.notifyAllAll();
//            }
//
//        }
//        if (deleteReadedNumbers) {
//            aktRun.size--;
//            aktRun.position += 4;
//        }
//        return Util.byteAryToInt(buffer);
//    }
    public int getRunSize() {
        if (aktRun == null) {
            setNextRun();
        }
        return (aktRun == null) ? 0 : aktRun.size;
    }

    public void skipRun() {
        aktRun = null;
        setNextRun();
    }

    public boolean runFinished() {
        if (aktRun == null && runQueue.size() > 0) {
            setNextRun();
        }

        if (aktRun != null && aktRun.size == 0) {
            setNextRun();
            return true;
        } else if (aktRun == null) {
            return true;
        }
        return false;
    }

    public void printBand() {
        System.out.println("Band " + name + " - [" + getRunCount() + "]");
        int counter = 0;
        List<Band.Run> _tmpQueue = new ArrayList();
        _tmpQueue.addAll(runQueue);
        if (aktRun != null) {
            _tmpQueue.add(aktRun);
        }
        for (Band.Run r : _tmpQueue) {

            long runPos = r.position;
            int runSize = r.size;
            int[] runNumbers = new int[runSize];
            for (int a = 0; a < runSize; ++a) {
                try {
                    rFile.seek(runPos);
                    byte[] buffer = new byte[4];
                    for (int i = 0; i < 4; ++i) {
                        buffer[i] = rFile.readByte();
                    }
                    runNumbers[a] = Util.byteAryToInt(buffer);

                    runPos += 4;
                } catch (IOException ex) {
                    Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println("Run " + counter++ + " " + Arrays.toString(runNumbers));
        }
    }

    public void clearBand() {
        runQueue.clear();
        aktRun = null;
        try {
            f.delete();


            f = null;


            f = new File(path);
            f.createNewFile();

        } catch (IOException ex) {
            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class Run {

        public int size;
        public long position;
        public long endRun;
        private boolean linkerBuffer = true;
        private final RandomAccessFile rfile;
        private final List<Byte> buffer1 = new ArrayList<Byte>();
        private final List<Byte> buffer2 = new ArrayList<Byte>();

        public int getNumber() {
            synchronized (buffer1) {
                if (buffer1.isEmpty()) {
                    linkerBuffer = false;
                    readNumberFromFile(buffer1);
                }
                buffer1.notifyAll();
            }
            synchronized (buffer2) {
                if (buffer2.isEmpty()) {
                    linkerBuffer = true;
                    readNumberFromFile(buffer2);
                }
                buffer2.notifyAll();
            }
            if (buffer1.isEmpty() && buffer2.isEmpty()) {
                return -1;
            }
            byte[] bAry = new byte[4];


            if (linkerBuffer) {
                synchronized (buffer1) {
                    for (int i = 0; i < 4; i++) {
                        bAry[i] = buffer1.remove(0);
                    }
                    buffer1.notifyAll();
                }
            } else {
                synchronized (buffer2) {
                    for (int i = 0; i < 4; i++) {
                        bAry[i] = buffer2.remove(0);
                    }
                    buffer2.notifyAll();
                }
            }
            size--;
            position += 4;

            return Util.byteAryToInt(bAry);
        }

        private void readNumberFromFile(final List<Byte> buff) {
            new Thread() {

                @Override
                public void run() {
                    int buffSize = (size < BUFFER_SIZE) ? size : BUFFER_SIZE;
                    buffSize = (buffSize > (size - buffer1.size() - buffer2.size())) ? size - buffer1.size() - buffer2.size() : buffSize;
                    if (buffSize < 0) {
                        return;
                    }
                    synchronized (buff) {
                        while (!buff.isEmpty()) {
                            try {
                                buff.wait();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        byte bAry[] = new byte[buffSize];
                        try {
                            rfile.seek(position);
                            rfile.read(bAry);//TODO Schauen ob es wirklich so viele Zahlen liest!!
                        } catch (IOException ex) {
                            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int i = 0; i < buffSize; i++) {
                            buff.add(bAry[i]);
                        }
                        buff.notifyAll();
                    }
                }
            }.start();
        }

        public void addNumber(int number) {
            int buffSize = (size < BUFFER_SIZE) ? size : BUFFER_SIZE;
            synchronized (buffer1) {
                if (buffer1.size() == BUFFER_SIZE) {
                    linkerBuffer = false;
                    writeNumberToFile(buffer1);                    
                }
                buffer1.notifyAll();
            }
            synchronized (buffer2) {
                if (buffer2.size() == BUFFER_SIZE) {
                    linkerBuffer = true;
                    writeNumberToFile(buffer2);
                }
                buffer2.notifyAll();
            }
            byte[] bAry = Util.intToByte(number);
            if (linkerBuffer) {
                synchronized (buffer1) {
                    while (buffer1.size()==BUFFER_SIZE) {
                        try {
                            buffer1.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        buffer1.add(bAry[i]);
                    }
                    buffer1.notifyAll();
                }
            } else {
                synchronized(buffer2)
                {
                 while(buffer2.size()==BUFFER_SIZE)   {
                        try {
                            buffer2.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                        }
                 }                
                    for (int i = 0; i < 4; i++) {
                        buffer2.add(bAry[i]);
                    }
                }
                buffer2.notifyAll();
            }
            size++;
            calcEndPosition();
        }

        private void writeNumberToFile(final List<Byte> buff) {
            new Thread() {
                //int buffSize = (size<BUFFER_SIZE)?size:BUFFER_SIZE;                
                int buffSize = buff.size();
                @Override
                public void run() {
                    synchronized (buff) {
                        try {
                            while (buff.isEmpty()) {
                                try {
                                    buff.wait();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            byte[] bAry = new byte[buffSize];
                            for (int i = 0; i < buffSize; i++) {
                                bAry[i] = buff.remove(0);
                            }
                            rfile.seek(endRun - buffer1.size() - buffer2.size());
                            rfile.write(bAry);
                        } catch (IOException ex) {
                            Logger.getLogger(Band.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        buff.notifyAll();
                    }
                }
            }.start();
        }

        Run(int size, long position, RandomAccessFile rFile) {
            this.rfile = rFile;
            this.size = size;
            this.position = position;
            calcEndPosition();
        }

        public void calcEndPosition() {
            this.endRun = position + (4 * size);
        }
    }
}