Ext.define("Ncorg.model.FadNcOrgModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','ncOrgCode','ncOrgName','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);