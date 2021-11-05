import java.io.*;
import java.net.*;

class Main{
  //private static DatagramSocket s = null;
  // private static e_detection s = null;
  //private static rdt_pos_neg s = null;
  private static rdt_pos s = null;
  public static void main(String[] args) throws IOException {
    //s = new DatagramSocket(1234);
    // alkup vsock, ilman reliability kerrosta
    //s = new Vsock(1234);
    //s = new e_detection(1234);
    s = new rdt_pos(1234);
    boolean on = true;

    while(on){

      //try{
        int ackcrc = new Crc8("testiviesti".getBytes()).calcCrc8();


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //out.write((byte)1);
        out.write("testiviesti".getBytes());
        out.write(ackcrc);
        byte[] tosend = out.toByteArray();

        InetAddress server = InetAddress.getByName("localhost");
        DatagramPacket testi = new DatagramPacket(tosend, tosend.length,server,62640);
        //s.send(testi);




        byte[] rec = new byte[256];
        DatagramPacket p = new DatagramPacket(rec,rec.length);
        s.receive(p);
        boolean crcRight = s.checkCrc();
        // alkup vsock, ilman reliability kerrosta
        // s.receive(p);
        // Tämä alla oleva ei jostain syystä toiminu, jos dataa muutti
        System.out.println(new String(rec, 0, p.getLength()-1));
        if(!crcRight) System.out.println("wrong crc in previous message");
        //System.out.println(new String(p.getData(), 0, p.getLength()-1));
      /*
      }
      catch(IOException e){
        on = false;
        System.out.println("virhe");
        break;
      }
      */
    }
  }
}
