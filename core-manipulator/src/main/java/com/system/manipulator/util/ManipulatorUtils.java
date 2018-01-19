package com.system.manipulator.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

/**
 * The <class>ManipulatorUtils</class> defines
 * byte code manipulation related utility methods
 *
 * @author Andrew
 */
public class ManipulatorUtils {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * VM Byte code instrumentation
     */
    private static final Instrumentation instrumentation = ByteBuddyAgent.install();

    /**
     * The byte code manipulation api
     */
    private static ByteBuddy BYTE_BUDDY = new ByteBuddy();

    /**
     * The byte code manipulation agent
     */
    private static AgentBuilder AGENT_BUILDER = new AgentBuilder.Default(BYTE_BUDDY)
            // .with(toSystemError())
            .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
            .with(new AgentBuilder.InitializationStrategy.SelfInjection.Eager())
            .with(AgentBuilder.TypeStrategy.Default.REDEFINE);

    /**
     * The  types to include for manipulation consideration
     */
    private static ElementMatcher.Junction INCLUDE_MATCHER = ElementMatchers.none();

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Builders                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Whenever manipulating bytecode we must specify an inclusion matcher.
     * This specifies which set of classes/objects are included to be considered for manipulation
     * <p>
     * This is a performance optimizer so we don't have to bother inspecting every class on the classpath
     *
     * @param inclusionMatcher
     * @return
     */
    public static synchronized void addInclusionMatcher(ElementMatcher inclusionMatcher) {
        setIncludeMatcher(getIncludeMatcher().or(inclusionMatcher));
    }

    /**
     * Add a bytecode manipulation transformation for a given type
     *
     * @param classMatcher
     * @param transformer
     */
    public static synchronized void addTransformation(ElementMatcher.Junction<TypeDescription> classMatcher, AgentBuilder.Transformer transformer) {
        addInclusionMatcher(classMatcher);
        setAgentBuilder(getDefaultAgentBuilder().type(classMatcher).transform(transformer));
        refreshAgent();
    }

    /**
     * Execute instrumentation of types with transformations
     */
    public static synchronized void refreshAgent() {
        setAgentBuilder(getDefaultAgentBuilder().ignore(ElementMatchers.not(getIncludeMatcher())));
        getDefaultAgentBuilder().installOn(getInstrumentation());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public static ElementMatcher.Junction getIncludeMatcher() {
        return INCLUDE_MATCHER;
    }

    public static void setIncludeMatcher(ElementMatcher.Junction includeMatcher) {
        INCLUDE_MATCHER = includeMatcher;
    }

    public static AgentBuilder getDefaultAgentBuilder() {
        return AGENT_BUILDER;
    }

    public static void setAgentBuilder(AgentBuilder agentBuilder) {
        AGENT_BUILDER = agentBuilder;
    }

    public static ByteBuddy getDefaultByteBuddy() {
        return BYTE_BUDDY;
    }

    public static void setByteBuddy(ByteBuddy byteBuddy) {
        BYTE_BUDDY = byteBuddy;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
