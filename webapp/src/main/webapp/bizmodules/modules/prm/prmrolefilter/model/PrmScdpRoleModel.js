Ext.define("Prmrolefilter.model.PrmRoleFilterModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','roleName','roleDesc','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);