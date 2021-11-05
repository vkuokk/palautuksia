import java.io.*;
import java.net.*;


class rlayer_pn{

    private int port;
    private Vsock soketti;


    public rlayer_pn(int port){
        this.port = port;
        this.Vsock = new Vsock(port);
    }

    public void receive(DatagramPacket p){
        soketti.receive(p);
    }


}