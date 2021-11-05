import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class Vsock extends DatagramSocket{

  private static double p_drop = 0.0;
  private static double p_delay = 0.0;
  private static double p_berror = 0.5;

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
      super.receive(p);
      // Pudotetaanko paketti, jonka jälkeen palataan silmukkaan, jotta
      // muita operaatioita ei enää tehdä pudotetulle paketille
      if(r.nextDouble() <= p_drop){

        //super.receive(p);
        System.out.println("Packet dropped");
        //continue;
        continue;
      }
      else{

        //bittivirheen asettaminen
        //===========================
        if(r.nextDouble() <= p_berror){
          //System.out.println("tekemässä bittivirhettä :D");

          byte[] rec = new byte[256];

          byte[] data = p.getData();
          int wbyte = r.nextInt(data.length);
          byte change_b = data[wbyte];


          int wbit = r.nextInt(7);

          change_b = (byte) (change_b ^ (1 << wbit));
          data[wbyte] = change_b;

          p.setData(data, 0, data.length-1);
          return;
          //continue;
          //===========================
        }
        else{
          // Tehdään viivettä pakettiin
          if(r.nextDouble() <= p_delay){
            //System.out.println("viivästytetään :D");
            try{
              TimeUnit.SECONDS.sleep(2);
              //Thread.sleep(1000);
            }catch (InterruptedException e){
              System.out.println(e);
            }
            return;
            //aiemmin
          }
          //else super.receive(p);
        }
      }
      return;
    }
  }
}
