package filecopy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendBuffer {
	private List<FCpacket> sendBuf;
	private int windowSize;

	public SendBuffer(int windowSize) {
		sendBuf = new ArrayList<FCpacket>();
		this.windowSize = windowSize;

	}

	public synchronized void add(FCpacket packet) {
		while(isFull()){
                try
                {
                    wait();
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(SendBuffer.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
		sendBuf.add(packet);
                notifyAll();

	}

	public boolean isFull() {
		return (sendBuf.size() >= windowSize);
	}
        
        public boolean isEmpty(){
            return sendBuf.isEmpty();
        }

	// Autom. Verschieben des Fensters, sobald ack f�r �ltestes Paket da
	public synchronized void removePacket() {
            while(isEmpty()) try
            {
                wait();
            } catch (InterruptedException ex)
            {
                Logger.getLogger(SendBuffer.class.getName()).log(Level.SEVERE, null, ex);
            }
		sendBuf.remove(0);
                notifyAll();
	}
	
	public void setAckValid(long seqNum){
		for(FCpacket packet : sendBuf){
		if(seqNum == packet.getSeqNum()) packet.setValidACK(true);
		}
	}
	
	public void setAckInvalid(long seqNum){
		for(FCpacket packet : sendBuf){
		if(seqNum == packet.getSeqNum()) packet.setValidACK(false);
		}
	}
	
	//muss anders, weil irgendwie sagen, dass seqNum nicht enthalten
	public boolean getAck(long seqNum){
		for(FCpacket packet : sendBuf){
		if(seqNum == packet.getSeqNum()) return packet.isValidACK();
		}
		 return false;
	}
	
	
	public FCpacket getPacket(int pos){
		return sendBuf.get(pos);	
	}
	
	public int size(){
		return sendBuf.size();
	}
        
        
	
	
	
}
