package com.system.util.clazz;

import org.objectweb.asm.ClassReader;

import java.io.*;

import static com.system.util.string.StringUtils.lineSeparatorToPeriod;

/**
 * The <class>ClassUtils</class> defines
 * class related utility methods
 *
 * @author Andrew
 */
public class ClassUtils {

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
        try {
            return Class.forName(className);
        } catch (Throwable e) {
            return Object.class;
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
}
