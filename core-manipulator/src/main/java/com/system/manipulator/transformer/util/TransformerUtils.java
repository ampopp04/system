package com.system.manipulator.transformer.util;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.annotation.Inherited;

import static com.system.manipulator.util.ManipulatorUtils.*;

/**
 * The <class>TransformerUtils</class> defines
 * byte code transformer related utility methods
 *
 * @author Andrew
 */
public class TransformerUtils {

    /**
     * Adds @Inherited to any annotation
     *
     * @param annotationToAddTo
     */
    public static void addInheritedAnnotationToAnnotation(Class annotationToAddTo) {
        transform(ElementMatchers.isSubTypeOf(annotationToAddTo), (builder, typeDescription, classloader) -> builder.annotateType(AnnotationDescription.Builder.ofType(Inherited.class).build()));
    }

    /**
     * Adds an annotation to a class
     *
     * @param annotationToAdd
     * @param subTypeClass
     */
    public static void addAnnotationToClass(Class annotationToAdd, Class subTypeClass) {
        transform(ElementMatchers.isSubTypeOf(subTypeClass).and(ElementMatchers.not(ElementMatchers.isAbstract())), (builder, typeDescription, classloader) -> builder.annotateType(AnnotationDescription.Builder.ofType(annotationToAdd).build()));
    }

    /**
     * Transform an object given a matcher and a transformer
     *
     * @param classMatcher
     * @param transformer
     */
    public static void transform(ElementMatcher.Junction<TypeDescription> classMatcher, AgentBuilder.Transformer transformer) {
        installAgent(setDefaultAgentIgnores(getDefaultAgentBuilder()
                .type(classMatcher).transform(transformer)));
    }
}
