package com.system.ui.base;

import com.system.db.entity.named.NamedEntity;

import javax.persistence.MappedSuperclass;

/**
 * The <class>UiBase</class> defines
 * the base UI entity
 *
 * @author Andrew
 */
@MappedSuperclass
public abstract class UiBase<ID extends Number> extends NamedEntity<ID> {
}