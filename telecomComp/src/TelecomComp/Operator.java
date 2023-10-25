package TelecomComp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Operator {
    
    private HashMap<Integer,List<ClientDataRecord>> reportsByNumber;
    private HashMap<Integer,BillingAccount> billingHashMap;

    public Operator() {
        this.reportsByNumber = new HashMap<>();
        this.billingHashMap = new HashMap<>();
    }
    public void setBillingAccount(Integer MSISDN, String planA, String planB,double bucketA, double bucketB, double bucketC) {
        BillingAccount billingAccount = new BillingAccount(MSISDN, planA, planB, bucketA,bucketB,bucketC);
        billingHashMap.put(MSISDN, billingAccount);
    }
  
    public void makeRequest(ChargingRequest request) {
        if(this.billingHashMap.containsKey(request.getMSISDN())) {
            ChargingHandler handler = new ChargingHandler(request, this.billingHashMap.get(request.getMSISDN()));
            List<ClientDataRecord> temp = new ArrayList<ClientDataRecord>();
            if(reportsByNumber.containsKey(request.getMSISDN()) && reportsByNumber.get(request.getMSISDN()) != null) {
                temp = reportsByNumber.get(request.getMSISDN());
                temp.add(handler.getCDR());
                Collections.sort(temp, new Comparator<ClientDataRecord>() {
                    @Override
                    public int compare(ClientDataRecord cdr1, ClientDataRecord cdr2) {
                        return cdr2.getTimestamp().compareTo(cdr1.getTimestamp());
                    }
                });
                reportsByNumber.put(request.getMSISDN(), temp);
            } else {
                temp.add(handler.getCDR());
                Collections.sort(temp, new Comparator<ClientDataRecord>() {
                    @Override
                    public int compare(ClientDataRecord cdr1, ClientDataRecord cdr2) {
                        return cdr2.getTimestamp().compareTo(cdr1.getTimestamp());
                    }
                });
                reportsByNumber.put(request.getMSISDN(),temp);

            }
        }

    }
    //Prints all the saved CDRS
    public String CRDStoString() {
        if(this.reportsByNumber.isEmpty()) return "No Client Data Records in the system.";
        String toPrint = "\nList of all CRDS";
        for(HashMap.Entry<Integer,List<ClientDataRecord>> entry : this.reportsByNumber.entrySet()) {
            toPrint+= "\nCRDs for number " + entry.getKey() + ":";
            for(ClientDataRecord cdr :entry.getValue()) {
                if(cdr == null) {
                    System.out.println("Something went wront printing the CDRS");
                }
                else toPrint += ("\n"+ cdr.toString() + "\n");
            }
        }
        return toPrint;
    }
    //Prints all saved billing accounts
    public String BillingAccountsToString() {
        if(this.billingHashMap.isEmpty()) return "No Billing Accounts found in the system.";
        String toPrint = "\nList of all billing accounts\n";
        for(HashMap.Entry<Integer,BillingAccount> entry : this.billingHashMap.entrySet()) {
            toPrint += "\nBilling information for number : " + entry.getKey() + "\n" + entry.getValue().toString();
        }
        return toPrint;
    }
    
}
