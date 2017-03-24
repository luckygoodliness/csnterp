Ext.define("Prmorgrolefilter.model.PrmOrgRoleFilterModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','roleId','tableName','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);