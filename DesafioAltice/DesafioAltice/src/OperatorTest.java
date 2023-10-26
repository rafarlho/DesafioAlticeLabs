import TelecomComp.BillingAccount;
import TelecomComp.ChargingRequest;
import TelecomComp.Operator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class OperatorTest {

    @BeforeEach
    void setUp() {
    }
    //Checks if creating operator all the map are empty
    @Test
    void TestOperatorConstructor() {
        Operator op = new Operator();
        assertEquals(Collections.emptyMap(),op.getBillingAccount());
        assertEquals(Collections.emptyMap(),op.getReportsByNumber());
    }
    //Tests if an added billing accounts are correctly added to data structure
    @Test
    void TestBillingMap() {
        Operator op = new Operator();
        var billing1 = new BillingAccount(977455676, "Alpha1" ,"Beta1",5,10,20);
        op.setBillingAccount(billing1);
        assertNotEquals(Collections.EMPTY_MAP, op.getBillingAccount());
        var billing2 = new BillingAccount(977455976, "Alpha2" ,"Beta2",50,100,200);
        op.setBillingAccount(billing2);
        assertEquals(2,op.getBillingAccount().size());

    }
    //Tests if upon making a request everything goes correctly into the data structure and properly indexed
    @Test
    void TestMakingRequest() {
        Operator op = new Operator();
        ChargingRequest cReq1 = new ChargingRequest(977455676, false, 2,"A");
        op.makeRequest(cReq1);
        assertEquals(Collections.emptyMap(),op.getReportsByNumber());
        BillingAccount bAcc1 = new BillingAccount(977455676,"Alpha1", "Beta2" , 10,10,10);
        op.setBillingAccount(bAcc1);
        op.makeRequest(cReq1);
        assertNotEquals(Collections.emptyMap(),op.getReportsByNumber());
        ChargingRequest cReq2 = new ChargingRequest(977455676, false, 2,"A");
        op.makeRequest(cReq2);
        assertEquals(1,op.getReportsByNumber().size());

    }
    @AfterEach
    void tearDown() {
    }
}