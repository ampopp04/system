package com.system.ws.entity.property.editor;

import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.db.entity.named.NamedEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.logging.exception.util.ExceptionUtils;
import com.system.util.clazz.ClassUtils;
import com.system.util.string.StringUtils;

import java.beans.PropertyEditorSupport;


public class EntityPropertyEditor extends PropertyEditorSupport {

    private final SystemRepository systemRepository;
    private final Class<? extends Entity> entityType;

    public EntityPropertyEditor(SystemRepository systemRepository, Class<? extends Entity> entityType) {
        this.systemRepository = systemRepository;
        this.entityType = entityType;
    }

    public void setAsText(String text) {

        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else if (ClassUtils.isAssignable(entityType, NamedEntity.class)) {
            //NamedEntity, resolve by name
            setValue(((NamedEntityRepository) systemRepository).findByName(text.trim()));
        } else if (ClassUtils.isAssignable(entityType, BaseEntity.class)) {
            //BaseEntity, assume text is a existing integer entity ID
            setValue(systemRepository.findById(Integer.valueOf(text.trim())));
        } else {
            ExceptionUtils.throwSystemException("Cannot convert value [" + text + "] for entity type [" + entityType.getSimpleName() + "]");
        }

    }
}