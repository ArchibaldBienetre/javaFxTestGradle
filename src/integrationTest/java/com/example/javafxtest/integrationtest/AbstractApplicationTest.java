package com.example.javafxtest.integrationtest;

import org.junit.jupiter.api.BeforeAll;

public class AbstractApplicationTest {

    // headless testing: no UI will pop up, this should work on CI
    @BeforeAll
    public static void setupForHeadlessTesting() {
        // https://circleci.com/docs/2.0/env-vars/#built-in-environment-variables
        String isRunningOnCi = System.getProperty("CI");
        if ("true".equalsIgnoreCase(isRunningOnCi)) {
            System.setProperty("monocle.platform", "Headless");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("glass.platform", "Monocle");
            System.setProperty("embedded", "monocle");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");

            // System.setProperty("prism.text", "t2k");
            System.setProperty("prism.text", "native");

            System.setProperty("java.awt.headless", "true");
        }
    }
}