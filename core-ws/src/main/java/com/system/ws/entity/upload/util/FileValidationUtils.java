package com.system.ws.entity.upload.util;

import com.system.logging.exception.util.ExceptionUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * The <class>FileValidationUtils</class> defines
 * utility methods for validating files.
 *
 * @author Andrew
 */
public class FileValidationUtils {

    public static void validateFileUpload(MultipartFile uploadfile, String contentType) {
        if (uploadfile == null || uploadfile.isEmpty()) {
            ExceptionUtils.throwSystemException("File is either missing or empty. Please select a file to upload.");
        }

        if (!"text/csv".equals(contentType) && !"text/tab-separated-values".equals(contentType)) {
            //Error, wrong content type
            ExceptionUtils.throwSystemException("File type [" + contentType + "] not accepted. Please upload files ending in CSV or TSV. (Comma/Tab Separated Values files with the first row defining property headers.)");
        }
    }

}
