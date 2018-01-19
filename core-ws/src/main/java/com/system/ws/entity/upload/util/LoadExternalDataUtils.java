package com.system.ws.entity.upload.util;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.event.types.SystemRepositoryEventTypes;
import com.system.logging.exception.util.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.webmvc.RootResourceInformation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.system.db.repository.event.types.SystemRepositoryEventTypes.INSERT_OR_DELETE;
import static com.system.ws.entity.upload.util.EntityEventOperationUtils.*;
import static com.system.ws.entity.upload.util.EntityLocateUtils.findExistingEntityIfRequiredByUploadType;
import static com.system.ws.entity.upload.util.EntityUtils.parseEntity;
import static com.system.ws.entity.upload.util.FileParsingUtils.getFileHeaders;
import static com.system.ws.entity.upload.util.FileParsingUtils.validatePropertyHeadersForEntity;

/**
 * The <class>LoadExternalDataUtils</class> defines
 * utility methods for loading external data into the system.
 *
 * @author Andrew
 */
public class LoadExternalDataUtils {

    @javax.transaction.Transactional
    public static <T extends BaseEntity> void loadExternalData(InputStream externalData, SystemRepository<T, Integer> entityRepository, Class<T> entityClass, SystemRepositoryEventTypes uploadType, String contentType, RootResourceInformation resourceInformation, Repositories repositories, ApplicationEventPublisher publisher) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(externalData));
            String line;

            String[] filePropertyHeaders = getFileHeaders(reader.readLine(), contentType);

            //Ensure these headers match a property name on the entity
            validatePropertyHeadersForEntity(entityClass, filePropertyHeaders);

            //Perform the delete portion of this event type before inserting new entities.
            if (uploadType == INSERT_OR_DELETE) {
                entityRepository.deleteAllInBatch();
            }

            while ((line = reader.readLine()) != null) {

                try {

                    T newEntity = parseEntity(entityClass, filePropertyHeaders, line, contentType, repositories);
                    T existingEntity = findExistingEntityIfRequiredByUploadType(newEntity, entityRepository, uploadType, resourceInformation);

                    if (newEntity != null) {

                        switch (uploadType) {
                            case UPDATE:
                                doHandleUpdate(newEntity, existingEntity, entityRepository, publisher);
                                break;
                            case INSERT:
                                doHandleInsert(newEntity, entityRepository, publisher);
                                break;
                            case SAVE:
                                //Update existing entity, if there is not existing entity
                                // then insert this as a new entity
                                if (!doHandleUpdate(newEntity, existingEntity, entityRepository, publisher)) {
                                    doHandleInsert(newEntity, entityRepository, publisher);
                                }
                                break;
                            case INSERT_OR_DELETE:
                                doHandleInsert(newEntity, entityRepository, publisher);
                                break;
                            case DELETE:
                                doHandleDelete(newEntity, existingEntity, entityRepository, publisher);
                                break;
                            default:
                                ExceptionUtils.throwSystemException("Unsupported Upload Type [" + uploadType.name() + "].");
                                break;
                        }

                    } else {
                        ExceptionUtils.throwSystemException("Could not generate an entity from file line [" + line + "]");
                    }

                } catch (Exception e) {
                    ExceptionUtils.throwSystemException("Failed to process entity, error reading file line [" + line + "]", e);
                }
            }

            reader.close();

        } catch (Exception e) {
            ExceptionUtils.throwSystemException("Error processing file.", e);
        }
    }

}
