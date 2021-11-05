import java.io.*;
import java.net.*;

class Main{
  private static DatagramSocket s = null;
  public static void main(String[] args) throws IOException {
    //s = new DatagramSocket(1234);
    s = new rlayer_pn(1234);
    // alkup vsock, ilman reliability kerrosta
    // s = new Vsock(1234);
    boolean on = true;

    while(on){
      try{
        byte[] rec = new byte[256];
        DatagramPacket p = new DatagramPacket(rec,rec.length);
        p = s.receive(p);
        // alkup vsock, ilman reliability kerrosta
        // s.receive(p);
        // Tämä alla oleva ei jostain syystä toiminu, jos dataa muutti
        System.out.println(new String(rec, 0, p.getLength()-1));
        //System.out.println(new String(p.getData(), 0, p.getLength()-1));
      }
      catch(IOException e){
        on = false;
        System.out.println("virhe");
        break;
      }
    }
  }
}
