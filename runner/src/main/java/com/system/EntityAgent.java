package com.system;

import com.system.db.entity.base.BaseEntity;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import javax.persistence.Entity;
import java.lang.instrument.Instrumentation;

/**
 * Transform all Non-Abstract Classes which extend BaseEntity
 * to have the annotation Entity
 */
public class EntityAgent {

    /**
     * Installs the agent builder to the instrumentation API.
     */
    public static void install(Instrumentation inst) {
        createAgentBuilder().installOn(inst);
    }

    /**
     * Creates the AgentBuilder that will redefine any class extending BaseEntity
     */
    private static AgentBuilder createAgentBuilder() {
        return new AgentBuilder.Default()
                //.with(new AgentBuilder.Listener.StreamWriting(System.err))
                .type(getClassMatcher())
                .transform(getTransformer());
    }

    /**
     * Set Entity annotation on Class
     */
    private static AgentBuilder.Transformer getTransformer() {
        return (builder, typeDescription, classloader) -> builder.annotateType(AnnotationDescription.Builder.ofType(Entity.class).build());
    }

    /**
     * Find any non-abstract class that extends BaseEntity
     */
    private static ElementMatcher.Junction<TypeDescription> getClassMatcher() {
        return ElementMatchers.isSubTypeOf(BaseEntity.class).and(ElementMatchers.not(ElementMatchers.isAbstract()));
    }
}