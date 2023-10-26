import TelecomComp.BillingAccount;
import TelecomComp.ChargingHandler;
import TelecomComp.ChargingRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargingHandlerTest {
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }
    @Test
    void TestAlpha1(){
        //Testing for roaming
        var bAcc = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, true, 2, "A");
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(6, bAcc.getBucketB());
        assertEquals(2,bAcc.getCounterA());
        assertEquals(1,bAcc.getCounterC());
        // Testing for local daytime
        var bAcc_local = new BillingAccount(922788677, "Alpha1", "Beta1", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 2, "A");
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(8, bAcc_local.getBucketA());
        assertEquals(2,bAcc_local.getCounterA());

    }
    @Test
    void TestAlpha2() {
        //Testing with roaming active
        var bAcc = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, true, 2, "A");
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(10,bAcc.getBucketB());
        //Testing without roaming
        var bAcc_local = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 2, "A");
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(9,bAcc_local.getBucketB());
        //Testing applying -0.05 discount because bucket B > 15
        var bAcc_local_1 = new BillingAccount(922788677, "Alpha2", "Beta1", 10, 20, 10);
        var cReq_local_1 = new ChargingRequest(922788677, false, 2, "A");
        var cHand_local_1 = new ChargingHandler(cReq_local_1, bAcc_local_1);
        assertEquals(19.10,bAcc_local_1.getBucketB());
    }
    @Test
    void TestAlpha3() {
        //Testing if it charges nothing if local call
        var bAcc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, false, 2, "A");
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(10,bAcc.getBucketC());
        //Testing if roaming charges
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 3, "A");
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(7,bAcc_roaming.getBucketC());
        //Testing if discount for bucket B > 15 charges
        var bAcc_roaming_1 = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 20, 10);
        var cReq_roaming_1 = new ChargingRequest(922788677, true, 3, "A");
        var cHand_roaming_1 = new ChargingHandler(cReq_roaming_1, bAcc_roaming_1);
        assertEquals(10-(0.95*3),bAcc_roaming_1.getBucketC());
    }
    @Test
    void TestBeta1() {
        //Testing if it charges on local calls
        var bAcc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq = new ChargingRequest(922788677, false, 10, "B");
        var cHand = new ChargingHandler(cReq, bAcc);
        assertEquals(9,bAcc.getBucketA());
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B");
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(8,bAcc_roaming.getBucketC());
        //Testing if it charges on roaming calls and applies discount
        var bAcc_roaming_disc = new BillingAccount(922788677, "Alpha3", "Beta1", 10, 10, 55);
        var cReq_roaming_disc = new ChargingRequest(922788677, false, 10, "B");
        var cHand_roaming_disc = new ChargingHandler(cReq_roaming_disc, bAcc_roaming_disc);
        assertEquals(10 - 10*(0.1-0.010),bAcc_roaming_disc.getBucketA());
    }
    @Test
    void TestBeta2() {
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B");
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(10,bAcc_roaming.getBucketB());
        //Testing if it charges on local calls
        var bAcc_local = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 10, "B");
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(9.5,bAcc_local.getBucketB());
        //Testing if it charges on local calls and applies discounts if bucket B > 15
        var bAcc_local_disc = new BillingAccount(922788677, "Alpha3", "Beta2", 10, 20, 10);
        var cReq_local_disc = new ChargingRequest(922788677, false, 10, "B");
        var cHand_local_disc = new ChargingHandler(cReq_local_disc, bAcc_local_disc);
        assertEquals(19.55,bAcc_local_disc.getBucketB());
    }
    @Test
    void TestBeta3() {
        //Testing if it charges on local calls
        var bAcc_local = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 10, 10);
        var cReq_local = new ChargingRequest(922788677, false, 10, "B");
        var cHand_local = new ChargingHandler(cReq_local, bAcc_local);
        assertEquals(10,bAcc_local.getBucketC());
        //Testing if it charges on roaming calls
        var bAcc_roaming = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 10, 10);
        var cReq_roaming = new ChargingRequest(922788677, true, 10, "B");
        var cHand_roaming = new ChargingHandler(cReq_roaming, bAcc_roaming);
        assertEquals(9,bAcc_roaming.getBucketC());
        //Testing if it charges on roaming calls and discounts when bucket B > 15
        var bAcc_roaming_disc = new BillingAccount(922788677, "Alpha3", "Beta3", 10, 20, 10);
        var cReq_roaming_disc = new ChargingRequest(922788677, true, 10, "B");
        var cHand_roaming_disc = new ChargingHandler(cReq_roaming_disc, bAcc_roaming_disc);
        assertEquals(10-(10*(0.1-0.005)),bAcc_roaming_disc.getBucketC());
    }
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
}