package com.system;

import com.system.inversion.InversionContainer;
import net.bytebuddy.agent.ByteBuddyAgent;


public class SystemRunner {

    public static void main(String[] args) {
        EntityAgent.install(ByteBuddyAgent.install());
        InversionContainer.startInversion(args);
    }
}