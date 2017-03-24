Ext.define("Prmrolefilter.model.ScdpQueryFilterModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'tableNames', 'beanName', 'schemeTypes', 'subtypeFieldNames', 'createBy', 'createTime', 'updateBy', 'updateTime', {
            name: 'isVoid',
            defaultValue: 0
        }, 'locTimezone', 'tblVersion']
    }
);