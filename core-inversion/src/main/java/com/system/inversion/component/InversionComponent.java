package com.system.inversion.component;

import com.system.inversion.util.InversionUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * A spring application can be configured to recognize other annotations as if they
 * denote components.  Traditionally you would have to annotate your object with {@linkComponent}
 * for spring component scanning to pick it up.
 * <p>
 * Ex. @ComponentScan(basePackages = {@link InversionUtils#SYSTEM_PACKAGE_ROOT},
 * includeFilters = @ComponentScan.Filter(InversionComponent.class))
 * <p>
 * Allows for defining {@link InversionComponent} as if it were equal to {@link Component}
 * the major difference though is that {@link InversionComponent} is denoted with {@link Inherited}
 * <p>
 * This means that this annotation can be placed along any part of the inheritance graph of the object and
 * all subtypes will inherit it.
 * <p>
 * Instead of having to place {@link Component} directly on each object implementation you can place
 * {@link InversionComponent} on a parent class and have it's effect inherited to it.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component()
@Inherited
public @interface InversionComponent {
}
