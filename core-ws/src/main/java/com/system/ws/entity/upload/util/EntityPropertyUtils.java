package com.system.ws.entity.upload.util;

import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.util.entity.EntityUtils;
import com.system.logging.exception.util.ExceptionUtils;
import com.system.util.clazz.ClassUtils;
import com.system.ws.entity.property.editor.EntityPropertyEditor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.repository.support.Repositories;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.string.StringUtils.isEmpty;

/**
 * The <class>FileUtils</class> defines
 * utility methods for manipulating
 * files
 *
 * @author Andrew
 */
public class EntityPropertyUtils {

    public static Object injectPropertyValues(Object entity, String[] propertyNames, Object[] propertyValues) {
        return injectPropertyValues(entity, propertyNames, propertyValues, null);
    }

    public static Object injectPropertyValues(Object entity, String[] propertyNames, Object[] propertyValues, Repositories repositories) {
        //Map each property to it's value
        Map<String, Object> propertyValueMap = IntStream.range(0, propertyNames.length).boxed()
                .collect(Collectors.toMap(i -> propertyNames[i], i -> propertyValues[i]));

        BeanWrapper entityWrapper = getEntityBeanPropertyWrapper(entity);
        if (repositories != null) {
            registerBeanWrapperCustomEntityPropertyEditors(entityWrapper, repositories);
        }
        entityWrapper.setPropertyValues(propertyValueMap);

        return entityWrapper.getWrappedInstance();
    }

    public static BeanWrapper getEntityBeanPropertyWrapper(Object entity) {
        BeanWrapper entityWrapper = PropertyAccessorFactory.forBeanPropertyAccess(entity);
        entityWrapper.setAutoGrowNestedPaths(true);
        return entityWrapper;
    }

    /**
     * For a given bean wrapper return back the value of all non-null objects within
     * the object the bean wrapper is wrapping.
     * <p>
     * This will be a map of the the wrapped beans property descriptors
     * and their associated values on the wrapped entity
     */
    public static <T extends BaseEntity> Map<PropertyDescriptor, Object> getEntityPropertyDescriptorValueMap(T entity) {
        BeanWrapper entityWrapper = getEntityBeanPropertyWrapper(entity);
        Map<PropertyDescriptor, Object> entityPropertyDescriptorValueMap = newMap();

        PropertyDescriptor[] propertyDescriptors = entityWrapper.getPropertyDescriptors();
        iterate(iterable(propertyDescriptors), propertyDescriptor -> {
            Object propertyValue = entityWrapper.getPropertyValue(propertyDescriptor.getName());
            if (propertyValue != null) {
                entityPropertyDescriptorValueMap.put(propertyDescriptor, propertyValue);
            }
        });

        return entityPropertyDescriptorValueMap;
    }

    /**
     * For a given entity return back all property names and their current object value
     */
    public static <T extends BaseEntity> Map<String, Object> getEntityBeanPropertyNameValueMap(T entity) {
        Map<String, Object> entityBeanPropertyNameValueMap = newMap();

        Map<PropertyDescriptor, Object> entityPropertyDescriptorValueMap = getEntityPropertyDescriptorValueMap(entity);

        iterate(iterable(entityPropertyDescriptorValueMap), propertyDescriptor -> {
            Object value = entityPropertyDescriptorValueMap.get(propertyDescriptor);

            if (EntityUtils.isEntityClass(value.getClass())) {
                value = getEntityBeanPropertyNameValueMap((BaseEntity) value);
            }

            entityBeanPropertyNameValueMap.put(propertyDescriptor.getName(), value);
        });

        return entityBeanPropertyNameValueMap;
    }

    public static void registerBeanWrapperCustomEntityPropertyEditors(BeanWrapper entityWrapper, Repositories repositories) {
        registerCustomEntityPropertyEditors(entityWrapper, repositories);
    }

    /**
     * Register editors that can handle resolving our database entities that are nested within these entity objects
     */
    public static void registerCustomEntityPropertyEditors(BeanWrapper entityWrapper, Repositories repositories) {
        iterate(iterable(entityWrapper.getPropertyDescriptors()), propertyDescriptor -> {
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            if (com.system.db.util.entity.EntityUtils.isEntityClass(propertyType)) {
                entityWrapper.registerCustomEditor(propertyType, new EntityPropertyEditor((SystemRepository) repositories.getRepositoryFor(propertyType).get(), (Class<? extends Entity>) propertyType));
            } else if (ClassUtils.isAssignable(propertyType, LocalDate.class)) {
                entityWrapper.registerCustomEditor(propertyType, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        try {
                            if (!isEmpty(text)) {
                                setValue(LocalDate.parse(StringUtils.substringBeforeLast(text, "00:00:00").trim(), DateTimeFormatter.ofPattern("E MMM dd yyyy")
                                        .withLocale(Locale.US))
                                );
                            }
                        } catch (Exception e) {
                            ExceptionUtils.throwSystemException("Cannot parse date [" + text + "]", e);
                        }
                    }
                });
            }
        });
    }

    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
