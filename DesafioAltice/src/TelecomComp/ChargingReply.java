package TelecomComp;

public class ChargingReply {
    private int ID,GSU = 0;
    private String status;
    public ChargingReply(int ID) {
        this.ID = ID;

    }
    //Getter and setter for status
    public String getStatus() {return this.status;}
    public void setStatus(String val) {this.status=val;}
    //Getter and setter for GSU
    public int getGSU() {return this.GSU;}
    public void setGSU(int val) {this.GSU=val;}
}
