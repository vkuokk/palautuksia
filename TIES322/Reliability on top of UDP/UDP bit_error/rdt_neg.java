import java.io.*;
import java.net.*;


// Luokka, jossa pelkkä negatiivinen kuittaus
class rdt_neg {

    private Vsock soketti;
    private byte[] data;

    public rdt_neg(int port) throws SocketException {
        this.soketti = new Vsock(port);
    }

    public void receive(DatagramPacket p) throws IOException {
        try {
            soketti.receive(p);
            data = p.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        InetAddress server = InetAddress.getByName("localhost");
        String result = "NAK";

        int nakcrc = new Crc8(result.getBytes()).calcCrc8();


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result.getBytes());
        out.write(nakcrc);
        byte[] tosend = out.toByteArray();

        DatagramPacket nak = new DatagramPacket(tosend, tosend.length,server,62640);
        System.out.println(result);

            try {
                if(!checkCrc()) soketti.send(nak);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public boolean checkCrc(){
        
        int truecrc = data[data.length-1];

        Crc8 crc8 = new Crc8(data);

        int calccrc = crc8.calcCrc8();

        if(truecrc == calccrc) return true;
        else return false;
    }


}
