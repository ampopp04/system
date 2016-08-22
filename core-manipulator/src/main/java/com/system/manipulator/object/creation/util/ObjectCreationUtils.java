package com.system.manipulator.object.creation.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.system.util.collection.CollectionUtils.iterable;

/**
 * The <class>ObjectCreationUtils</class> defines
 * object creation utils. This makes it easy to create
 * extensions of certain objects on the fly
 *
 * @author Andrew
 */
public class ObjectCreationUtils {

    /**
     * Allows creating a new interface that extends another
     *
     * @param interfaceName
     * @param interfaceClass
     * @param genericTypeVariable - allows specifying the bounds of an interface with a generic type
     * @return
     */
    public static Class<?> extendInterface(String interfaceName, Class interfaceClass, Class genericTypeVariable, AnnotationDescription... annotationDescriptions) {
        return new ByteBuddy()
                .makeInterface(TypeDescription.Generic.Builder.parameterizedType(interfaceClass, genericTypeVariable).build())
                .annotateType(annotationDescriptions)
                .name(interfaceName)
                .make()
                .load(interfaceClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();
    }

    /**
     * Create an interface by interface name with the provided set of public methods
     *
     * @param interfaceName
     * @param methodList
     * @param classLoader
     * @return
     */
    public static Class<?> createInterfaceWithPublicMethods(String interfaceName, List<java.lang.reflect.Method> methodList, ClassLoader classLoader) {
        DynamicType.Builder<?> byteBuddyBuilder = new ByteBuddy()
                .makeInterface();

        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition methodBuilder = null;

        for (Method method : methodList) {
            if (methodBuilder == null) {
                methodBuilder = byteBuddyBuilder.defineMethod(method.getName(), method.getReturnType(), method.getModifiers()).withoutCode();
            } else {
                methodBuilder = methodBuilder.defineMethod(method.getName(), method.getReturnType(), method.getModifiers()).withoutCode();
            }
        }
        return methodBuilder
                .name(interfaceName)
                .make()
                .load(classLoader, ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();
    }

    /**
     * Build an annotation description with values to inject into the created annotation in place of defaults or required fields
     *
     * @param annotationDef
     * @param annotationValueMap
     * @return
     */
    public static AnnotationDescription buildAnnotationDescription(Class annotationDef, Map<String, Class<?>> annotationValueMap) {
        AnnotationDescription.Builder builder = AnnotationDescription.Builder.ofType(annotationDef);
        for (String name : iterable(annotationValueMap)) {
            builder = builder.define(name, annotationValueMap.get(name));
        }
        return builder.build();
    }

    /**
     * Allows creating a new class that subclasses another
     *
     * @param className                - name of the new class
     * @param baseClass                - the base class to subclass from
     * @param interfaceImplementations - an interface we will implement for the new class
     * @param typeVariable             - if the interface specifies generic types this will define it's bounds for the concrete new class
     * @return
     */
    public static Class<?> subclass(String className, Class baseClass, Class interfaceImplementations, Class typeVariable) {
        return new ByteBuddy()
                .subclass(TypeDescription.Generic.Builder.parameterizedType(baseClass, typeVariable).build())
                .implement(interfaceImplementations)
                .name(className)
                .make()
                .load(baseClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();
    }
}
