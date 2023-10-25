import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import TelecomComp.*;
public class Teste {
    public static void main(String[] args) {/*
        BillingAccount firstNumber = new BillingAccount(939716057,"Alpha1", "Beta2",20,20,20);
        firstNumber.setMSISDN(349999999);
        
        ChargingRequest charge = new ChargingRequest(939716057, false, 20,"A");
        System.out.println(charge.getTimestampString() + "; ID: " + charge.getID());
        
        firstNumber.setCounterA(90);
        firstNumber.setBucketA(30);
        System.out.println(firstNumber.getCounterA());
        ChargingHandler ch = new ChargingHandler(charge,firstNumber);
      
        
        ClientDataRecord crd = ch.getCDR();
        System.out.println(crd.toString());
        */

        Operator operadora = new Operator();
        operadora.setBillingAccount(939716057, "Alpha1", "Beta2",20,20,20);
        operadora.setBillingAccount(939716044, "Alpha3", "Beta2",40,40,40);
        System.out.println(operadora.BillingAccountsToString());
        
        ChargingRequest chargingRequest = new ChargingRequest(939716057, false, 10, "A");
        operadora.makeRequest(chargingRequest);
        ChargingRequest chargingRequest2 = new ChargingRequest(939716044, true, 5, "A");
        operadora.makeRequest(chargingRequest2);
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChargingRequest chargingRequest3 = new ChargingRequest(939716057, false, 10, "A");
        operadora.makeRequest(chargingRequest3);
         try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChargingRequest chargingRequest4 = new ChargingRequest(939716057, false, 2, "A");
        operadora.makeRequest(chargingRequest4);
        System.out.println(operadora.CRDStoString());
    }
}
