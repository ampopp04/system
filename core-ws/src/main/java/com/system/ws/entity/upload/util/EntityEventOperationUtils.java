package com.system.ws.entity.upload.util;

import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.logging.exception.util.ExceptionUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.*;

import static com.system.ws.entity.upload.util.EntityPropertyUtils.copyNonNullProperties;

/**
 * The <class>EntityEventOperationUtils</class> defines
 * utility methods for executing entity event types.
 *
 * @author Andrew
 */
public class EntityEventOperationUtils {


    public static <T extends BaseEntity> boolean doHandleUpdate(T entity, T existingEntity, SystemRepository<T, Integer> entityRepository, ApplicationEventPublisher publisher) {

        if (existingEntity == null) {
            return false;
        } else {
            copyNonNullProperties(entity, existingEntity);
        }
        publisher.publishEvent(new BeforeSaveEvent(existingEntity));
        T savedEntity =entityRepository.save(existingEntity);
        publisher.publishEvent(new AfterSaveEvent(savedEntity));


        return true;
    }

    public static <T extends BaseEntity> void doHandleInsert(T entity, SystemRepository<T, Integer> entityRepository, ApplicationEventPublisher publisher) {
        publisher.publishEvent(new BeforeCreateEvent(entity));
        T savedEntity = entityRepository.save(entity);
        publisher.publishEvent(new AfterCreateEvent(savedEntity));
    }

    public static <T extends BaseEntity> void doHandleDelete(T entity, T existingEntity, SystemRepository<T, Integer> entityRepository, ApplicationEventPublisher publisher) {

        if (existingEntity != null) {
            publisher.publishEvent(new BeforeDeleteEvent(entity));
            entityRepository.delete(existingEntity);
            publisher.publishEvent(new AfterDeleteEvent(existingEntity));

        } else {
            ExceptionUtils.throwSystemException("Cannot delete entity because the entity cannot be found.");
        }

    }

}
