package com.api.tests;

import org.testng.annotations.Test;

public class UserTests extends LoginScenarioTest {

    @Test
    public void token(){
        assert accessToken != null;
        System.out.println(accessToken);
    }
}
