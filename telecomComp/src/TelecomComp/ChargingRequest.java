package TelecomComp;

import java.text.SimpleDateFormat;  
import java.util.Date;

public class ChargingRequest {
    //Private class variables
    private int MSISDN, RSU;
    private static int counter = 1;
    private int ID;
    private String service;
    private boolean roaming;
    private Date timeStamp;
    private String timeStampString;
    //Charging request constructor
    public ChargingRequest(Integer newMSISDN, boolean isRoaming,int RSUvalue, String service) {
        this.MSISDN = newMSISDN;
        this.ID =counter ++; //Set a sequential ID value;
        this.roaming = isRoaming;
        this.RSU = RSUvalue;
        this.timeStamp =  new Date();
        this.timeStampString = setDate();
        this.service = service;
    }
    //Getter for MSISDN
    public int getMSISDN() {return this.MSISDN;}
    // Return the date of the request as a String
    private String setDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return formatter.format(new Date());
    }
    //Getter for timestamp as a string
    public String getTimestampString() {return this.timeStampString;}
    //Getter for timestamp as a Date
    public Date getTimestampDate() {return this.timeStamp;}
    //Getter for ID
    public int getID() {return this.ID;}
    //Getter for Service 
    public String getService() {return this.service;}
    //Getter for roaming
    public boolean getRoaming() {return this.roaming;}
    //Getter for RSU
    public int getRSU() {return this.RSU;}
}
