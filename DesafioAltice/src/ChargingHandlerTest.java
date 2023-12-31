import TelecomComp.BillingAccount;
import TelecomComp.ChargingHandler;
import TelecomComp.ChargingRequest;
import org.junit.jupiter.api.Test;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ChargingHandlerTest {

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
    public Date weekday = formatter.parse("7-Jun-2013 10:13:54"); // Friday morning
    public Date weekdayNight = formatter.parse("7-Jun-2013 23:13:54"); // Friday night
    public Date weekendDay = formatter.parse("8-Jun-2013 10:13:54"); // Saturday morning
    public Date weekendNight = formatter.parse("9-Jun-2013 23:13:54"); // Saturday morning

    ChargingHandlerTest() throws ParseException {
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }
    @Test
    void TestAlpha1() throws ParseException {
        //Tests if it's not a day off the week
        var bAcc_dated = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq_dated = new ChargingRequest(922788677, true, 2, "A",this.weekendDay);
        var cHand_dated = new ChargingHandler(cReq_dated, bAcc_dated);
        assertEquals(10, bAcc_dated.getBucketB());
        //Testing for roaming
        var bAcc = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, true, 2, "A",this.weekday);
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(6, bAcc.getBucketB());
        assertEquals(2,bAcc.getCounterA());
        assertEquals(1,bAcc.getCounterC());
        // Testing for local daytime
        var bAcc_local = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 2, "A",this.weekday);
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(8, bAcc_local.getBucketA());
        assertEquals(2,bAcc_local.getCounterA());
        // Testing for local nighttime
        var bAcc_local_night = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq_local_night = new ChargingRequest(922788677, false, 2, "A",this.weekdayNight);
        var cHand_local_night = new ChargingHandler(cReq_local_night, bAcc_local_night);
        assertEquals(9, bAcc_local_night.getBucketA());


    }
    @Test
    void TestAlpha2() {
        //Testing for nightime
        var bAcc_night = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 10, 10);
        var cReq_night = new ChargingRequest(922788677, false, 2, "A",this.weekdayNight);
        var cHand_night = new ChargingHandler(cReq_night, bAcc_night);
        assertEquals(9.5,bAcc_night.getBucketB());
        //Testing with roaming active
        var bAcc = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, true, 2, "A",this.weekday);
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(10,bAcc.getBucketB());
        //Testing without roaming
        var bAcc_local = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 2, "A",this.weekday);
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(9,bAcc_local.getBucketB());
        //Testing applying -0.05 discount because bucket B > 15
        var bAcc_local_1 = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 20, 10);
        var cReq_local_1 = new ChargingRequest(922788677, false, 2, "A",this.weekday);
        var cHand_local_1 = new ChargingHandler(cReq_local_1, bAcc_local_1);
        assertEquals(19.10,bAcc_local_1.getBucketB());
    }
    @Test
    void TestAlpha3() {
        //Tests if weekends
        var bAcc_wEnd = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_wEnd = new ChargingRequest(922788677, true, 2, "A",this.weekendDay);
        var cHand_wEnd = new ChargingHandler(cReq_wEnd, bAcc_wEnd);
        assertEquals(9.5,bAcc_wEnd.getBucketC());
        //Testing if it charges nothing if local call
        var bAcc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, false, 2, "A",this.weekday);
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(10,bAcc.getBucketC());
        //Testing if roaming charges
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 3, "A",this.weekday);
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(7,bAcc_roaming.getBucketC());
        //Testing if discount for bucket B > 15 charges
        var bAcc_roaming_1 = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 20, 10);
        var cReq_roaming_1 = new ChargingRequest(922788677, true, 3, "A",this.weekday);
        var cHand_roaming_1 = new ChargingHandler(cReq_roaming_1, bAcc_roaming_1);
        assertEquals(10-(0.95*3),bAcc_roaming_1.getBucketC());
    }
    @Test
    void TestBeta1() {
        //Testing for weekend day
        var bAcc_wEndDay = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_wEndDay = new ChargingRequest(922788677, false, 10, "B",this.weekendDay);
        var cHand_wEndDay = new ChargingHandler(cReq_wEndDay, bAcc_wEndDay);
        assertEquals(10,bAcc_wEndDay.getBucketA());
        //Testing if it charges on local calls
        var bAcc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, false, 10, "B",this.weekday);
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(9,bAcc.getBucketA());
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B",this.weekday);
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(8,bAcc_roaming.getBucketC());
        //Testing if it charges on roaming calls and applies discount
        var bAcc_roaming_disc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 55);
        var cReq_roaming_disc = new ChargingRequest(922788677, false, 10, "B",this.weekday);
        var cHand_roaming_disc = new ChargingHandler(cReq_roaming_disc, bAcc_roaming_disc);
        assertEquals(10 - 10*(0.1-0.010),bAcc_roaming_disc.getBucketA());
        //Testing for nightime
        var bAcc_night = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_night = new ChargingRequest(922788677, false, 10, "B",this.weekdayNight);
        var cHand_night = new ChargingHandler(cReq_night, bAcc_night);
        assertEquals(10 - 10*0.05,bAcc_night.getBucketA());

    }
    @Test
    void TestBeta2() {
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B",this.weekday);
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(10,bAcc_roaming.getBucketB());
        //Testing for nightime
        var bAcc_night = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 10, 10);
        var cReq_night = new ChargingRequest(922788677, false, 10, "B",this.weekdayNight);
        var cHand_night = new ChargingHandler(cReq_night, bAcc_night);
        assertEquals(10- 10*0.025,bAcc_night.getBucketB());
        //Testing if it charges on local calls
        var bAcc_local = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 10, "B",this.weekday);
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(9.5,bAcc_local.getBucketB());
        //Testing if it charges on local calls and applies discounts if bucket B > 15
        var bAcc_local_disc = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 20, 10);
        var cReq_local_disc = new ChargingRequest(922788677, false, 10, "B",this.weekday);
        var cHand_local_disc = new ChargingHandler(cReq_local_disc, bAcc_local_disc);
        assertEquals(19.55,bAcc_local_disc.getBucketB());
    }
    @Test
    void TestBeta3() {
        //Testing if it charges on local calls
        var bAcc_local = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 10, "B",this.weekdayNight);
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(10,bAcc_local.getBucketC());
        //testing for weekend prices
        var bAcc_wEnd = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 10, 10);
        var cReq_wEnd = new ChargingRequest(922788677, true, 10, "B",this.weekendDay);
        var cHand_wEnd = new ChargingHandler(cReq_wEnd, bAcc_wEnd);
        assertEquals(10-10*0.025,bAcc_wEnd.getBucketC());
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B",this.weekday);
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(9,bAcc_roaming.getBucketC());
        //Testing if it charges on roaming calls and discounts when bucket B > 15
        var bAcc_roaming_disc = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 20, 10);
        var cReq_roaming_disc = new ChargingRequest(922788677, true, 10, "B",this.weekdayNight);
        var cHand_roaming_disc = new ChargingHandler(cReq_roaming_disc, bAcc_roaming_disc);
        assertEquals(10-(10*(0.1-0.005)),bAcc_roaming_disc.getBucketC());
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
}