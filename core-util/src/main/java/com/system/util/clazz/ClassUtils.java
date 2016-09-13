package com.system.util.clazz;

import org.objectweb.asm.ClassReader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import static com.system.util.collection.CollectionUtils.*;
import static com.system.util.string.StringUtils.lineSeparatorToPeriod;
import static org.apache.commons.lang3.reflect.TypeUtils.getTypeArguments;

/**
 * The <class>ClassUtils</class> defines
 * class related utility methods
 *
 * @author Andrew
 */
public class ClassUtils {

    /**
     * Arrays of primitive wrapper objects. Do not reorder this array
     */
    private static final Class[] wrappers = {
            Integer.class, Double.class, Byte.class,
            Boolean.class, Character.class, Void.class,
            Short.class, Float.class, Long.class
    };

    /**
     * Convert a primitive object to it's boxed Object representation.
     * If this is not a primitive it simply returns it.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrapPrimitive(final Class<T> clazz) {
        if (!clazz.isPrimitive()) return clazz;
        final String name = clazz.getName();
        final int c0 = name.charAt(0);
        final int c2 = name.charAt(2);
        final int mapper = (c0 + c0 + c0 + 5) & (118 - c2);
        return (Class<T>) wrappers[mapper];
    }

    /**
     * Determines if the cls is assignable to the toClass
     *
     * @param cls
     * @param toClass
     * @return
     */
    public static boolean isAssignable(final Class<?> cls, final Class<?> toClass) {
        return org.apache.commons.lang3.ClassUtils.isAssignable(cls, toClass);
    }

    /**
     * Converts a clazz to it's fully qualified name.
     * Primitives return the wrapped Objects class name.
     *
     * @param clazz
     * @return
     */
    public static String toClassName(Class clazz) {
        return wrapPrimitive(clazz).getName();
    }

    /**
     * Extract Class name from an {@link InputStream} representing a Class object
     *
     * @param classFileInputStream
     * @return
     * @throws IOException
     */
    public static String toClassName(InputStream classFileInputStream) throws IOException {
        return lineSeparatorToPeriod(getClassReader(classFileInputStream).getClassName());
    }

    /**
     * Returns back a classes runtime generic type arguments
     * for a specific type identifier
     *
     * @param clazz
     * @return
     */
    public static Type getGenericTypeArgument(Class<?> clazz, String typeName) {
        Map<String, Type> typeMap = getAllGenericTypeArguments(clazz);
        return typeMap.get(typeName);
    }

    public static Map<String, Type> getAllGenericTypeArguments(Class<?> clazz) {
        final Map<String, Type> typeMap = new HashMap<>();

        if (clazz == Object.class) {
            return typeMap;
        }
        typeMap.putAll(getAllGenericTypeArguments(clazz.getSuperclass()));
        typeMap.putAll(getGenericTypeArguments(clazz));

        return typeMap;
    }

    /**
     * Returns the generic type arguments of a given class
     *
     * @param clazz
     * @return
     */
    public static Map<String, Type> getGenericTypeArguments(Class<?> clazz) {
        final Map<String, Type> typeMap = newMap();
        if (clazz == null) {
            return typeMap;
        } else if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
            Map<TypeVariable<?>, Type> typeVarMap = getTypeArguments((ParameterizedType) clazz.getGenericSuperclass());
            iterate(iterable(typeVarMap), (k) -> typeMap.put(k.getName(), typeVarMap.get(k)));
        } else {
            iterate(iterable(clazz.getGenericInterfaces()), (type) -> {
                if (type instanceof ParameterizedType) {
                    Map<TypeVariable<?>, Type> typeVarMap = getTypeArguments((ParameterizedType) type);
                    iterate(iterable(typeVarMap), (k) -> typeMap.put(k.getName(), typeVarMap.get(k)));
                }
            });
        }
        return typeMap;
    }

    /**
     * Returns back a classes runtime generic type arguments
     * for a specific type identifier
     *
     * @param clazz
     * @return
     */

    public static Type getGenericTypeArgument(Class<?> clazz, TypeVariable<?> type) {
        return getGenericTypeArgument(clazz, type.getTypeName());
    }

    /**
     * Converts a class object {@link InputStream} into a {@link ClassReader}
     *
     * @param classFileInputStream
     * @return
     * @throws IOException
     */
    public static ClassReader getClassReader(InputStream classFileInputStream) throws IOException {
        try {
            return new ClassReader(classFileInputStream);
        } finally {
            classFileInputStream.close();
        }
    }

    /**
     * Loads the {@link Class} represented by the className param.
     * <p>
     * If this class cannot be found it will return back Object.class
     * instead of throwing an exception
     *
     * @param className
     * @return
     */
    public static Class forNameSafe(String className) {
        Class clazz = forNameOrNull(className);
        if (clazz == null) {
            clazz = Object.class;
        }
        return clazz;
    }

    /**
     * Loads the {@link Class} represented by the className param.
     * <p>
     * If this class cannot be found it will return back null
     * instead of throwing an exception
     *
     * @param className
     * @return
     */
    public static Class forNameOrNull(String className) {
        try {
            return Class.forName(className);
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Convert a {@link Class} object to an {@link InputStream}
     *
     * @param clazz
     * @return
     */
    public static InputStream toInputStreamSafe(Class clazz) {
        try {
            return objectToStream(clazz);
        } catch (Throwable e) {
            return emptyStream();
        }
    }

    /**
     * Converts a {@link Object} to an {@link InputStream}
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static InputStream objectToStream(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Return an empty {@link InputStream}
     *
     * @return
     */
    public static InputStream emptyStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    /**
     * Return all declared fields on a class
     *
     * @param clazz
     * @return
     */
    public static Field[] getClassFields(Class clazz) {
        return clazz.getDeclaredFields();
    }

    /**
     * Returns the first direct interface of the given class or null if it doesn't exist
     *
     * @param clazz
     * @return
     */
    public static Class getFirstInterfaceOfClass(Class clazz) {
        return clazz == null || clazz.getInterfaces() == null ? null : clazz.getInterfaces()[0];
    }
}
