/*
 * Copyright 2018, Andrew Popp, All rights reserved. Email ampopp04@gmail.com for commercial licensing use and pricing
 */

/**
 * The <class>System.view.component.field.SystemEntityFilter</class> extends
 * the standard string filter to support system entities
 * that are actually objects and not raw strings.
 *
 * This class will override the functionality that retrieves the value of the object
 * to filter it as a string.
 *
 * @author Andrew
 */
Ext.define('System.view.component.field.filter.SystemEntityFilter', {
    extend: 'Ext.grid.filters.filter.String',
    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                      //////////
    //////////////////////////////////////////////////////////////////////

    alias: 'grid.filter.entity',

    type: 'entity',

    /**
     * Allows you to filter a record by
     * topLevelRoot.defaultRoot.defaultSubRoot
     *
     * Given a record layout with
     * _schemaTable.data.name
     * the set defaults would work assuming
     * that the dataindex of this filter is
     * set to _schemeTable.
     *
     * topLevelRoot of undefined value defaults to
     * this.dataIndex which is usually the stores
     * specific model
     *
     */
    topLevelRoot: undefined,
    defaultRoot: 'data',
    defaultSubRoot: 'name',


    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Methods                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Override default filter configuration capabilities
     * to support a deeper level of
     * granularity to resolve the data we want to filter against.
     *
     */
    getFilterConfig: function (config, key) {
        config.id = this.getBaseIdPrefix();


        if (!config.root) {
            config.root = (this.topLevelRoot ? ("_" + this.topLevelRoot) : this.dataIndex);
        }

        if (!config.property) {
            config.property = this.defaultRoot;
        }

        if (!config.defaultSubRoot) {
            config.defaultSubRoot = this.defaultSubRoot;
        }

        if (!this.topLevelRoot && config.root == undefined) {
            config.root = config.property;
            config.property = config.defaultSubRoot;
            config.defaultSubRoot = undefined;
        }

        if (key) {
            config.id += '-' + key;
        }

        config.getCandidateValue = function (candidate, v, preventCoerce) {
            var me = this,
                convert = me._convert,
                result = me.getPropertyValue(candidate);
            if (me.defaultSubRoot) {
                result = result[me.defaultSubRoot];
            }
            if (convert) {
                result = convert.call(me.scope || me, result);
            } else if (!preventCoerce) {
                result = Ext.coerce(result, v);
            }
            return result;
        }

        config.getPropertyValue = function (item) {
            try {
                var root = this._root,
                    value = (root == null) ? item : item[root];

                if (value == undefined) {
                    value = item['_' + root];
                }

                if (value) {
                    return value[this._property];
                }
            } catch (e) {
                return "";
            }
            return "";
        }

        return config;
    }

});