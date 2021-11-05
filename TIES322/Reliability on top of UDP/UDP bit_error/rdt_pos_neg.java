import java.io.*;
import java.net.*;


// Luokka, jossa positiiviset ja negatiiviset kuittaukset viesteille (ACK,NAK)
class rdt_pos_neg {

    private Vsock soketti;
    private byte[] data;

    public rdt_pos_neg(int port) throws SocketException {
        this.soketti = new Vsock(port);
    }
    public void send(DatagramPacket p) throws IOException {
        soketti.send(p);
    }

    public void receive(DatagramPacket p) throws IOException {
        try {
            soketti.receive(p);
            data = p.getData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        InetAddress server = InetAddress.getByName("localhost");
        String result = "ACK";
        if (!checkCrc()) result = "NAK";

        int ackcrc = new Crc8(result.getBytes()).calcCrc8();


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(result.getBytes());
        out.write(ackcrc);
        byte[] tosend = out.toByteArray();

        DatagramPacket ackornak = new DatagramPacket(tosend, tosend.length,server,62640);
        System.out.println(result);

            try {
                if(ackornak!=null) soketti.send(ackornak);
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
