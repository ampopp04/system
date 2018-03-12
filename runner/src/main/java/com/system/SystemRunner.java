/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package com.system;


import com.system.inversion.InversionContainer;

/**
 * The <class>SystemRunner</class> runs the application
 *
 * @author Andrew
 */
public class SystemRunner {
    /**
     * Run the application
     *
     * @param args
     */
    public static void main(String[] args) {
        InversionContainer.startInversion(args);
    }
}