package com.system.manipulator.interceptor.util;

import com.system.manipulator.transformer.util.TransformerUtils;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import static com.system.manipulator.util.ManipulatorUtils.getDefaultByteBuddy;

/**
 * The <class>InterceptorUtils</class> defines
 * interceptor related utility methods
 *
 * @author Andrew
 */
public class InterceptorUtils {

    /**
     * Builds an interceptor against the from class for the specified method.
     * It will intercept calls to this method and send it to the first statically defined method within
     * the interceptor class (Usually defined as the same method signature of intercepted method)
     *
     * @param fromClass
     * @param fromClassMethodName
     * @param interceptorClass
     */
    public static void methodInterceptor(Class fromClass, String fromClassMethodName, Class interceptorClass) {
        TransformerUtils.transform(ElementMatchers.isSubTypeOf(fromClass).and(ElementMatchers.not(ElementMatchers.isAbstract().or(ElementMatchers.isInterface()))),
                (builder, typeDescription, classLoader, module) -> builder.method(ElementMatchers.named(fromClassMethodName)).intercept(MethodDelegation.to(interceptorClass)));
    }

    /**
     * Effectively replace all instances of fromClass
     * to be toClass
     *
     * @param fromClass
     * @param toClass
     */
    public static void classInterceptor(Class fromClass, Class toClass) {
        getDefaultByteBuddy()
                .with(Implementation.Context.Disabled.Factory.INSTANCE)
                .rebase(toClass)
                .name(fromClass.getName()).make()
                .load(fromClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }
}
