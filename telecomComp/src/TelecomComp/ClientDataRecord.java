package TelecomComp;

import java.util.Date;

public class ClientDataRecord  {
    private Date timestamp,counterD;
    private int MSISDN, counterA, counterB, counterC;
    private ChargingRequest charReq;
    private ChargingReply charRep;
    private String service, plan;
    private double bucketA,bucketB,bucketC;

    public ClientDataRecord(Date timeStamp, int MSISDN, ChargingRequest cR, ChargingReply cRp, 
                            int counterA,int counterB, int counterC, Date counterD,String service,String plan,double bucketA, double bucketB, double bucketC   ) {
        this.timestamp = timeStamp;
        this. MSISDN = MSISDN;
        this.charReq = cR;
        this.charRep=cRp;
        this.counterA =counterA;
        this.counterB = counterB;
        this.counterC = counterC;
        this.counterD = counterD;
        this.service=service;
        this.plan = plan;
        this.bucketA = bucketA;
        this.bucketB = bucketB;
        this.bucketC=bucketC;
    }
    public Date getTimestamp() {return this.timestamp;}
    @Override
    public String toString(){
        return " Timestamp : " + this.timestamp + 
        "\n MSISDN : " + this.MSISDN +
        "\n Service : " + this.service +
        "\n Plan : " + this.plan + 
        "\n Charging request ID : " +this.charReq.getID()+
        "\n Charging reply status :" +this.charRep.getStatus() +
        "\n Counter A : " + this.counterA+
        "\n Counter B : " + this.counterB+
        "\n Counter C : " + this.counterC+
        "\n Counter D : " + this.counterD+
        "\n Bucket A : " +this.bucketA+
        "\n Bucket B : " +this.bucketB+
        "\n Bucket C : " +this.bucketC;
    }
}
