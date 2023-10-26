package TelecomComp;

import java.util.Calendar;
import java.util.Objects;

public class ChargingHandler {

    private ChargingReply charRep;
    private double cost;
    private Calendar cal = Calendar.getInstance();
    private ClientDataRecord CDR = null;

    public ChargingHandler(ChargingRequest request, BillingAccount billingAccount) {
        cal.setTime(request.getTimestampDate());
        this.charRep = new ChargingReply(request.getID());
        this.charRep.setGSU(0);
        switch(request.getService()) {
            case "A":
                switch(billingAccount.getServiceAPlan()) {
                    case "Alpha1":
                        HandleAlpha1(request, billingAccount);
                        break;
                    case "Alpha2":
                        HandleAlpha2(request, billingAccount);
                        break;
                    case "Alpha3":
                        HandleAlpha3(request, billingAccount);
                        break;
                    default:
                        this.charRep.setStatus("Not admissible ! Your plan was not found.");
                        break;
                }
                break;
            case "B":
                switch(billingAccount.getServiceBPlan()) {
                    case "Beta1":
                        HandleBeta1(request, billingAccount);
                        break;
                    case "Beta2":
                        HandleBeta2(request,billingAccount);
                        break;
                    case "Beta3":
                        HandleBeta3(request,billingAccount);
                        break;
                    default:
                        this.charRep.setStatus("Not admissible ! Your plan was not found.");
                        break;
                }
                break;
            default:
                this.charRep.setStatus("Not admissible ! Your plan was not found.");
                break;
        }
        //Creates a CDR if the charging was complete
        String response = this.charRep.getStatus();
        if(Objects.equals(request.getService(), "A")) {
                this.CDR = new ClientDataRecord(request.getTimestampDate(), billingAccount.getMSISDN(),
                        request, this.charRep, billingAccount.getCounterA(), billingAccount.getCounterB(), billingAccount.getCounterC(), billingAccount.getCounterD(), request.getService(), billingAccount.getServiceAPlan(), billingAccount.getBucketA(), billingAccount.getBucketB(),billingAccount.getBucketC());
            }
            else if(Objects.equals(request.getService(), "B")) {
            this.CDR = new ClientDataRecord(request.getTimestampDate(), billingAccount.getMSISDN(),
                    request, this.charRep, billingAccount.getCounterA(), billingAccount.getCounterB(), billingAccount.getCounterC(), billingAccount.getCounterD(), request.getService(), billingAccount.getServiceBPlan(), billingAccount.getBucketA(), billingAccount.getBucketB(),billingAccount.getBucketC());
        }
    }
    //Getter for CRD
    public ClientDataRecord getCDR() {return this.CDR;}

    //Method to handle Alpha 1
    private void HandleAlpha1(ChargingRequest request, BillingAccount billingAccount) {
        cost =0;

        //Checks if its a day of the week
        if((cal.get(Calendar.DAY_OF_WEEK)!= 1 && cal.get(Calendar.DAY_OF_WEEK)!= 7))
        {
            //Cheks if counter A is inferior to 100
            if(billingAccount.getCounterA() < 101)
            {

                //Cheks if discounts are avaiable
                if(billingAccount.getCounterA()>10) cost -= 0.25;
                if(billingAccount.getBucketC() > 50) cost -=0.10;

                //Checks if Roaming is active
                if(request.getRoaming())
                {
                    //Standard roaming cost
                    cost += 2;
                    cost *= request.getRSU();
                    if(billingAccount.getBucketB() > 5) {
                        if(billingAccount.getBucketB()-cost>= 0) {
                            billingAccount.setBucketB(billingAccount.getBucketB()-cost);
                            charRep.setStatus("OK");
                            this.charRep.setGSU(request.getRSU());;
                            billingAccount.setCounterA(billingAccount.getCounterA()+request.getRSU());// incrementa Counter A
                            //Increments roaming counter
                            billingAccount.setCounterC(billingAccount.getCounterC()+1);
                            //Sets counter D
                            billingAccount.setCounterD(request.getTimestampDate());
                        }
                        else {this.charRep.setStatus("Credit limit reached");}
                    }
                    else {
                        if(billingAccount.getBucketC()-cost>= 0) {
                            billingAccount.setBucketC(billingAccount.getBucketC()-cost);
                            this.charRep.setStatus("OK");
                            this.charRep.setGSU(request.getRSU());;
                            billingAccount.setCounterA(billingAccount.getCounterA()+request.getRSU());// incrementa Counter A
                            //Increments roaming counter
                            billingAccount.setCounterC(billingAccount.getCounterC()+1);
                            //Sets counter D
                            billingAccount.setCounterD(request.getTimestampDate());
                        }
                        else {this.charRep.setStatus("Credit limit reached");}
                    }
                }
                //If Roaming is false

                else {

                    //Checks if it nighttime (Assuming nighttime is between 22 and 7 in the morning of the next day)

                    if(cal.get(Calendar.HOUR_OF_DAY)< 7 && cal.get(Calendar.HOUR_OF_DAY) > 22) cost += 0.50;
                    else cost+=1;

                    cost *= request.getRSU();
                    if(billingAccount.getBucketA() - cost >=0) {
                        billingAccount.setBucketA(billingAccount.getBucketA() - cost);
                        this.charRep.setStatus("OK");
                        this.charRep.setGSU(request.getRSU());;
                        billingAccount.setCounterA(billingAccount.getCounterA()+request.getRSU());// incrementa Counter A
                        //Sets counter D
                        billingAccount.setCounterD(request.getTimestampDate());
                    }else{
                        this.charRep.setStatus("Credit limit reached");
                    }
                }

            } else this.charRep.setStatus("Not admissible! You have overpassed the 100 limit");
        } else this.charRep.setStatus("Not admissible! You can only use on week days.");
    }
    //Method to handle Alpha 2
    private void HandleAlpha2(ChargingRequest request, BillingAccount billingAccount) {
        cost =0;
        //Checks if roaming is active and bucket b value > 10
        if(request.getRoaming() &&( billingAccount.getBucketB()>10)) {
            this.charRep.setStatus("Not admissible! Your plan does not allow you to use roaming while bucketB is not bigger then 100.");
        }else {
            //Apllying discounts

            if(billingAccount.getCounterB() > 10) cost-=0.2;
            if(billingAccount.getBucketB() > 15) cost -=0.05;

            //Checks if it's not roaming
            if(!request.getRoaming()){
                //Checks if nighttime
                if(cal.get(Calendar.HOUR_OF_DAY)< 7 && cal.get(Calendar.HOUR_OF_DAY) > 22) cost += 0.25;
                else cost +=0.5;
                cost *= request.getRSU();
                // Checks if sufficient funds
                if(billingAccount.getBucketB()-cost >= 0) {
                    billingAccount.setBucketB(billingAccount.getBucketB()-cost);
                    this.charRep.setStatus("OK");
                    this.charRep.setGSU(request.getRSU());
                    billingAccount.setCounterA(billingAccount.getCounterA()+request.getRSU());// incrementa Counter A
                    //Sets counter D
                    billingAccount.setCounterD(request.getTimestampDate());
                }
                else this.charRep.setStatus("Credit limit reached");
            }
            //Condição não verificada na tabela
            else{this.charRep.setStatus("Not admissible! You cannot use roaming.");}
        }
    }
    //Method to handle Alpha 3
    private void HandleAlpha3(ChargingRequest request, BillingAccount billingAccount) {
        cost = 0;
        //Checks if not roaming and bucket C > 10
        if(request.getRoaming() && billingAccount.getBucketC()<=10) {
            //Checks for discounts
            if(billingAccount.getCounterB()>10) cost -= 0.2;
            if(billingAccount.getBucketB()>15) cost -= 0.05;
            //checks if it's week day
            if((cal.get(Calendar.DAY_OF_WEEK)!= 1 && cal.get(Calendar.DAY_OF_WEEK)!= 7)) {
                cost+=1;
            }else {
                cost+=0.25;
            }
            //Checks if there are enough funds on bucketC and if so discounts from it
            cost *= request.getRSU();
            if(billingAccount.getBucketC()-cost >=0) {
                billingAccount.setBucketC(billingAccount.getBucketC()-cost);
                charRep.setStatus("OK");
                this.charRep.setGSU(request.getRSU());;
                billingAccount.setCounterA(billingAccount.getCounterA()+request.getRSU()); // incrementa Counter A
                //Sets counter D
                billingAccount.setCounterD(request.getTimestampDate());
            }
            else this.charRep.setStatus("Credit Limit Reached");
        }
        else this.charRep.setStatus("Not admissible! Your plan only allows you to use roaming");
    }
    //Method to handle Beta 1
    private void HandleBeta1(ChargingRequest request, BillingAccount billingAccount) {
        cost = 0;
        //Checks if it is a week day or a night in weekend
        if((cal.get(Calendar.DAY_OF_WEEK)!= 1 && cal.get(Calendar.DAY_OF_WEEK)!= 7) ||
                ((cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK)== 7) && (cal.get(Calendar.HOUR_OF_DAY)< 7 && cal.get(Calendar.HOUR_OF_DAY) > 22))) {

            //Checks for discounts
            if (billingAccount.getCounterA() > 10) cost -= 0.025;
            if (billingAccount.getBucketC() > 50) cost -= 0.010;

            //Checks if is roaming

            if (request.getRoaming()) cost += 0.2;
            //Checks if its nightime
            else if (cal.get(Calendar.HOUR_OF_DAY) < 7 && cal.get(Calendar.HOUR_OF_DAY) > 22) cost += 0.05;
            else cost += 0.1;
            cost *= request.getRSU();
            //Verifies if roaming
            if (request.getRoaming()) {
                //Checks if counter B > 5
                if (billingAccount.getCounterB() > 5) {
                    //Checks if enough funds
                    if (billingAccount.getBucketB() - cost > 0) {
                        billingAccount.setBucketB(billingAccount.getBucketB() - cost);
                        this.charRep.setStatus("OK");
                        this.charRep.setGSU(request.getRSU());

                        billingAccount.setCounterB(billingAccount.getCounterB() + 1);
                        //Increments roaming counter
                        billingAccount.setCounterC(billingAccount.getCounterC() + 1);
                        //Sets counter D
                        billingAccount.setCounterD(request.getTimestampDate());
                    } else {
                        this.charRep.setStatus("Credit limit reached");
                    }
                } else {
                    //Checks if enough funds

                    if (billingAccount.getBucketC() - cost > 0) {
                        billingAccount.setBucketC(billingAccount.getBucketC() - cost);
                        this.charRep.setStatus("OK");
                        this.charRep.setGSU(request.getRSU());
                        //Increments roaming counter
                        billingAccount.setCounterC(billingAccount.getCounterC() + 1);
                        //Sets counter D
                        billingAccount.setCounterD(request.getTimestampDate());
                    } else this.charRep.setStatus("Credit limit reached");
                }

            } else {
                //If not roaming
                if (billingAccount.getBucketA() - cost > 0) {
                    billingAccount.setBucketA(billingAccount.getBucketA() - cost);
                    this.charRep.setStatus("OK");
                    this.charRep.setGSU(request.getRSU());
                    ;
                    //Sets counter D
                    billingAccount.setCounterD(request.getTimestampDate());
                } else this.charRep.setStatus("Credit limit reached");
            }
        }
    }
    //Method to handle Beta 2
    private void HandleBeta2(ChargingRequest request, BillingAccount billingAccount) {
        cost = 0;
        //Checks if roaming is active and bucket b value > 10
        if(request.getRoaming() && billingAccount.getBucketB()>10) {
            this.charRep.setStatus("Not admissible! Your plan does not alow you to use roaming or have less then 10 on bucket B.");
        }else {
            //Apllying discounts
            if(billingAccount.getCounterB() > 10) cost-=0.02;
            if(billingAccount.getBucketB() > 15) cost -=0.005;

            //Checks if its not roaming
            if(!request.getRoaming()){
                //Checks if night time
                if(cal.get(Calendar.HOUR_OF_DAY)< 7 && cal.get(Calendar.HOUR_OF_DAY) > 22) cost += 0.025;
                else cost +=0.05;
                cost *= request.getRSU();
                // Checks if suficient funds
                if(billingAccount.getBucketB()-cost >= 0) {
                    billingAccount.setBucketB(billingAccount.getBucketB()-cost);
                    this.charRep.setStatus("OK");
                    this.charRep.setGSU(request.getRSU());
                    //Sets counter D
                    billingAccount.setCounterD(request.getTimestampDate());
                }
                else this.charRep.setStatus("Credit limit reached");
            }
            //Condição não verificada na tabela
            else{this.charRep.setStatus("Not admissible! Something went wrong... This was not supposed to happen.");}
        }
    }
    private void HandleBeta3(ChargingRequest request, BillingAccount billingAccount) {
        cost = 0;
        //Checks if not roaming and bucket C > 10
        if(request.getRoaming() && billingAccount.getBucketC()<=10) {
            //Checks for discounts
            if(billingAccount.getCounterB()>10) cost -= 0.02;
            if(billingAccount.getBucketB()>15) cost -= 0.005;
            //checks if it's week day
            if((cal.get(Calendar.DAY_OF_WEEK)!= 1 && cal.get(Calendar.DAY_OF_WEEK)!= 7)) {
                cost+=0.1;
            }else {
                cost+=0.25;
            }
            cost *= request.getRSU();

            //Checks if there are enough funds on bucketC and if so discounts from it
            if(billingAccount.getBucketC()-cost >=0) {
                billingAccount.setBucketC(billingAccount.getBucketC()-cost);
                charRep.setStatus("OK");
                this.charRep.setGSU(request.getRSU());;
                //Sets counter D
                billingAccount.setCounterD(request.getTimestampDate());
            }
            else this.charRep.setStatus("Credit Limit Reached");
        }
        else this.charRep.setStatus("Not admissible! Your plan does not allow you to use roaming");
    }
}