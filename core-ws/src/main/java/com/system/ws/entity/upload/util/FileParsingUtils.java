package com.system.ws.entity.upload.util;

import com.system.db.entity.base.BaseEntity;
import com.system.logging.exception.SystemException;
import com.system.logging.exception.util.ExceptionUtils;
import com.system.util.stream.StreamUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.beans.PropertyUtil;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>FileUtils</class> defines
 * utility methods for manipulating
 * files
 *
 * @author Andrew
 */
public class FileParsingUtils {

    /**
     * Returns the headers of a file given that it is provided
     * as a single string with a provided fileContentType.
     * <p>
     * The fileContentType is used to determine how to parse the headers
     * out of the provided headerLine string
     */
    public static String[] getFileHeaders(String headerLine, String fileContentType) {
        if (StringUtils.isEmpty(headerLine)) {
            throw SystemException.newInstance("Please specify headers on your file. They are missing.");
        }

        String separator = "text/csv".equals(fileContentType) ? "," : "\t";
        return StringUtils.splitPreserveAllTokens(headerLine, separator);
    }

    /**
     * Iterates over each of the headers defined from a file to ensure that they
     * match a property on the entity class that values will be injected into.
     */
    public static <T extends BaseEntity> void validatePropertyHeadersForEntity(Class<T> entityClass, String[] filePropertyHeaders) {
        try {
            T entityInstanceValidator = entityClass.newInstance();
            Set<String> allowedHeaderPropertyValueSet = StreamUtils.stream(iterable(PropertyUtil.propertyDescriptorsFor(entityInstanceValidator, Object.class))).map(propertyDescriptor -> propertyDescriptor.getName()).collect(Collectors.toSet());

            iterate(iterable(filePropertyHeaders), propertyName -> {

                if (!allowedHeaderPropertyValueSet.contains(propertyName)) {
                    ExceptionUtils.throwSystemException("Header property [" + propertyName + "] does not exist on entity, possible case sensitive values are [" + StringUtils.join(allowedHeaderPropertyValueSet, ",") + "].");
                }

            });

        } catch (Exception e) {
            ExceptionUtils.throwSystemException(e.getMessage(), e);
        }
    }

    public static String[] parseFileLine(String[] filePropertyHeaders, String customerLine, String contentType) {
        if (StringUtils.isEmpty(customerLine)) {
            return new String[0];
        }

        String separator = "text/csv".equals(contentType) ? "," : "\t";
        String[] fileLineValues = Arrays.stream(StringUtils.splitPreserveAllTokens(customerLine, separator)).map(String::trim).toArray(String[]::new);

        if (filePropertyHeaders.length != fileLineValues.length) {
            ExceptionUtils.throwSystemException("File contains [" + filePropertyHeaders.length + "] header property names while current line contains [" + fileLineValues.length + "] property values. File lines must contain the same amount of values as there are headers. Line with error [" + customerLine + ']');
        }

        return fileLineValues;
    }

}
