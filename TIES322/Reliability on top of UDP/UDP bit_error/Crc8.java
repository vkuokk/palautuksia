


class Crc8{

    private byte[] data;
    private int polyn = 0b100000111;
    private int[] maski = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};


    public Crc8(byte[] data){
        this.data = data;
    }

    public int calcCrc8(){
        int crc = 0;
        for(int i = 0; i<data.length; i++){
            crc^= data[i];
            for(int j = 0; j< 8; j++){
                if((crc & maski[7]) != 0){
                    crc = ((crc << 1)^ polyn);
                }
                else{
                    crc <<= 1;
                }
            }
        }
        return crc;

    }
}