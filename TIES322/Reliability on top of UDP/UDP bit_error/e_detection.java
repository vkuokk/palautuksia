import java.io.*;
import java.net.*;

class e_detection {

    private Vsock soketti;
    private byte[] data;

    public e_detection(int port) throws SocketException {
        this.soketti = new Vsock(port);
    }

    public void receive(DatagramPacket p) {
        try {
            soketti.receive(p);
            data = p.getData();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean checkCrc(){
        
        int truecrc = data[data.length-1];

        Crc8 crc8 = new Crc8(data);

        int calccrc = crc8.calcCrc8();

        if(truecrc == calccrc) return true;
        else return false;

        //byte[] crc8 = new Crc8(data);
    }


}
