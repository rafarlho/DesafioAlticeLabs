package TelecomComp;

import java.util.Date;

public class BillingAccount {
    private double bucketA= 0,bucketB= 0,bucketC = 0;
    private int MSISDN;
    private int counterA=0, counterB=0, counterC=0;
    private String planA;
    private String planB;
    private Date counterD;

    public BillingAccount(int MSISDN ,String planA, String planB, double bucketA, double bucketB , double bucketC) {
        this.MSISDN = MSISDN;
        this.planA = planA;
        this.planB = planB;  
        this.bucketA = bucketA;
        this.bucketB = bucketB;
        this.bucketC = bucketC;
    }
    //Returns phone number associated with billing account
    public Integer getMSISDN() {
        return this.MSISDN;
    }
    //Changes phone number value and validates it
    public void setMSISDN(Integer newMSISDN) {
        assert ((String.valueOf(newMSISDN).length()) == 9) : "Phone number must contain 9 numbers!";
        this.MSISDN = newMSISDN;
    }
    //Method to print billing account
    public String toString() {
        return " MSISDN : " + this.MSISDN+
                "\n Service A Plan : " + this.planA+ 
                "\n Service B Plan : " + this.planB + 
                "\n Bucket A : " + this.bucketA +
                "\n Bucket B : " + this.bucketB +
                "\n Bucket C : " + this.bucketC +
                "\n Counter A : " + this.counterA+
                "\n Counter B : " + this.counterB+
                "\n Counter C : " + this.counterC+
                "\n Counter D : " + this.counterD;
    }
    //Setters and getters for buckets
    public double getBucketA() {return this.bucketA;}
    public void setBucketA(double val){this.bucketA = val;}
    public double getBucketB() {return this.bucketB;}
    public void setBucketB(double val){this.bucketB = val;}
    public double getBucketC() {return this.bucketC;}
    public void setBucketC(double val){this.bucketC = val;}


    //Setters and getters for counters
    public int getCounterA() {return this.counterA;}
    public void setCounterA(int val){this.counterA = val;}

    public int getCounterB() {return this.counterB;}
    public void setCounterB(int val){this.counterB = val;}


    public int getCounterC() {return this.counterC;}
    public void setCounterC(int val){this.counterC = val;}

    public Date getCounterD() {return this.counterD;}
    public void setCounterD(Date val){this.counterD = val;}

    //Getter for service A plan
    public String getServiceAPlan() {return this.planA;}

    //Getter for service B plan
    public String getServiceBPlan() {return this.planB;}
}

