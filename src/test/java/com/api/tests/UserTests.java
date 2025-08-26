package com.api.tests;

import org.testng.annotations.Test;

import static com.api.tests.RunAllPossibleLoginScenarioTest.accessToken;

public class UserTests extends  RunAllPossibleLoginScenarioTest {

    @Test
    public void token(){
        assert accessToken != null;
        System.out.println(accessToken);
    }
}
