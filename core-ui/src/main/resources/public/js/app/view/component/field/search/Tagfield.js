Ext.define('System.view.component.field.search.Tagfield', {
    extend: 'Ext.form.field.Tag',

    xtype: 'system-tagfield',

    fieldSubTpl:
        [
            '<div id="{cmpId}-listWrapper" data-ref="listWrapper" class="' + Ext.baseCSSPrefix + 'tagfield {fieldCls} {typeCls} {typeCls}-{ui} style="{wrapperStyle}">',
            '<ul id="{cmpId}-itemList" data-ref="itemList" class="' + Ext.baseCSSPrefix + 'tagfield-list{itemListCls}">',
            '<li id="{cmpId}-inputElCt" data-ref="inputElCt" class="' + Ext.baseCSSPrefix + 'tagfield-input">',
            '<input id="{cmpId}-inputEl" data-ref="inputEl" type="{type}" ',
            '<tpl if="name">name="{name}" </tpl>',
            '<tpl if="value"> value="{[Ext.util.Format.htmlEncode(values.value)]}"</tpl>',
            '<tpl if="size">size="{size}" </tpl>',
            '<tpl if="tabIdx != null">tabindex="{tabIdx}" </tpl>',
            '<tpl if="disabled"> disabled="disabled"</tpl>',
            'class="' + Ext.baseCSSPrefix + 'tagfield-input-field {inputElCls}" autocomplete="off">',
            '</li>',
            '</ul>',
            '</div>',
            {
                disableFormats: true
            }
        ],

    childEls:
        ['listWrapper', 'itemList', 'inputEl', 'inputElCt'],

    getSubTplData:

        function (fieldData) {
            var me = this,
                data = me.callParent(arguments),
                emptyText = me.emptyText,
                emptyInputCls = me.emptyInputCls,
                isEmpty = emptyText && data.value.length < 1,
                growMin = me.growMin,
                growMax = me.growMax;

            data.value = '';
            data.emptyText = isEmpty ? emptyText : '';
            data.emptyCls = me.emptyCls;
            data.inputElCls = '';
            data.wrapperStyle = '';
            data.itemListCls = '';

            if (me.grow) {
                if (Ext.isNumber(growMin) && growMin > 0) {
                    wrapperStyle += 'min-height:' + growMin + 'px;';
                }
                if (Ext.isNumber(growMax) && growMax > 0) {
                    wrapperStyle += 'max-height:' + growMax + 'px;';
                }
            }

            if (me.stacked === true) {
                data.itemListCls += ' ' + Ext.baseCSSPrefix + 'tagfield-stacked';
            }

            if (!me.multiSelect) {
                data.itemListCls += ' ' + Ext.baseCSSPrefix + 'tagfield-singleselect';
            }

            return data;
        },

    afterRender: function () {
        var me = this,
            inputEl = me.inputEl;

        if (Ext.supports.Placeholder && inputEl && me.emptyText) {
            inputEl.dom.setAttribute('placeholder', me.emptyText);
        }

        me.applyMultiselectItemMarkup();

        me.callSuper(arguments);

        me.applyEmptyText();
    }
    ,

    applyEmptyText: function () {
        var me = this,
            emptyText = me.emptyText,
            inputEl = me.inputEl,
            listWrapper = me.listWrapper,
            emptyCls = me.emptyCls,
            emptyInputCls = me.emptyInputCls,
            isEmpty;

        if (me.rendered && emptyText) {
            isEmpty = Ext.isEmpty(me.value) && !me.hasFocus;

            inputEl.removeCls(emptyInputCls);
            inputEl.dom.setAttribute('placeholder', emptyText);

            if (isEmpty) {
                inputEl.dom.value = '';
                listWrapper.addCls(emptyCls);
            } else {
                listWrapper.removeCls(emptyCls);
            }
            me.autoSize();
        }
    }
    ,

    preFocus: function () {
        var me = this,
            inputEl = me.inputEl,
            isEmpty = inputEl.dom.value === '';

        me.listWrapper.removeCls(me.emptyCls);
        me.inputEl.removeCls(me.emptyInputCls);

        if (me.selectOnFocus || isEmpty) {
            inputEl.dom.select();
        }
    }
})
;