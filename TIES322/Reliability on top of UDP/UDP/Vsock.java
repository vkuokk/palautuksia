import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;

class Vsock extends DatagramSocket{

  private static double p_drop = 0.5;
  private static double p_delay = 0.5;
  private static double p_berror = 0.5;

  private boolean isDropped = false;
  private boolean isDelayed = false;
  private int portti;
  private DatagramPacket receive;

  public Vsock() throws SocketException{
    super();
  }

  public Vsock(int port) throws SocketException{
    super(port);
  }

  // alump void
  @Override
  public void receive(DatagramPacket p) throws IOException{
    while(true){
      Random r = new Random();
      // Pudotetaanko paketti, jonka jälkeen palataan silmukkaan, jotta
      // muita operaatioita ei enää tehdä pudotetulle paketille
      if(r.nextDouble() <= p_drop){

        //super.receive(p);
        System.out.println("Packet dropped");
        super.receive(p);
        //continue;
      }
      else{

        //bittivirheen asettaminen
        //===========================
        if(r.nextDouble() <= p_berror){
          System.out.println("tekemässä bittivirhettä :D");
          super.receive(p);

          byte[] rec = new byte[256];

          byte[] data = p.getData();
          int wbyte = r.nextInt(data.length);
          byte change_b = data[wbyte];


          int wbit = r.nextInt(7);

          change_b = (byte) (change_b ^ (1 << wbit));

          p.setData(data, 0, data.length-1);
          //continue;
          //===========================
        }
        else{
          // Tehdään viivettä pakettiin
          if(r.nextDouble() <= p_delay){
            System.out.println("viivästytetään :D");
            super.receive(p);
            try{
              TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e){
              System.out.println(e);
            }
            //aiemmin
          }
          else super.receive(p);
        }
      }
      return;
    }
  }
}
