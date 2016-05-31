package com.system.manipulator.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;


public class ManipulatorUtils {

    //Add Agent as static builder here to enclose and optimize it's use

    public static ByteBuddy getDefaultByteBuddy() {
        ByteBuddyAgent.install();
        return new ByteBuddy();
    }

    public static AgentBuilder getDefaultAgentBuilder() {
        return new AgentBuilder.Default()
                //.with(toSystemError())
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.InitializationStrategy.SelfInjection.EAGER)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE);
    }

    public static AgentBuilder setDefaultAgentIgnores(AgentBuilder agent) {
        return agent;//.ignore(ElementMatchers.not(ElementMatchers.nameStartsWith("com.system")));
    }

    public static void installAgent(AgentBuilder agent) {
        agent.installOn(ByteBuddyAgent.install());
    }

}
