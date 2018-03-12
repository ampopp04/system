/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

package db.migration;


import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.ui.base.UiBase;
import com.system.ui.base.component.UiComponent;
import com.system.ui.base.component.assignment.UiComponentAssignment;
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.container.UiContainer;
import com.system.ui.base.component.container.panel.UiPanel;
import com.system.ui.base.component.definition.UiComponentDefinition;
import com.system.ui.base.component.type.UiComponentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V1__initial_schema</class> defines the initial
 * Ui schema creation.
 * <p>
 * This defines various tables that manage the Ui.
 * <p>
 * It then performs data insertion into these newly created tables.
 *
 * @author Andrew
 * @see UiBase
 * @see UiComponent
 * @see UiContainer
 * @see UiPanel
 * @see UiComponentConfig
 * @see UiComponentConfigAttribute
 */
public class V11__initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<UiComponentType> uiComponentTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(UiComponentType.class,
                UiComponentDefinition.class, UiComponent.class,
                UiComponentConfigAttribute.class, UiComponentConfig.class,
                UiComponentAssignment.class);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////


    @Override
    protected void insertData() {
        getUiComponentTypeRepository().saveAll(getDefinitionTypeList());
    }

    private List<UiComponentType> getDefinitionTypeList() {
        return asList(
                createUiComponentType("Window", "This type represents a window in the system."),
                createUiComponentType("Panel", "This type represents a panel in the system."),
                createUiComponentType("Detail Screen", "This type represents an entities detail screen in the system."),
                createUiComponentType("Toolbar", "This type represents an toolbar in the system."),
                createUiComponentType("Items", "This type represents items within another component in the system."),
                createUiComponentType("Toolbar Menu", "This type represents a menu within a toolbar in the system.")
        );
    }

    private UiComponentType createUiComponentType(String name, String description) {
        UiComponentType uiComponentType = new UiComponentType();
        uiComponentType.setName(name);
        uiComponentType.setDescription(description);
        return uiComponentType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<UiComponentType> getUiComponentTypeRepository() {
        return uiComponentTypeRepository;
    }

    public void setUiComponentTypeRepository(NamedEntityRepository<UiComponentType> uiComponentTypeRepository) {
        this.uiComponentTypeRepository = uiComponentTypeRepository;
    }

}