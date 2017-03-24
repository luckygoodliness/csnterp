Ext.define("Ncperson.model.FadNcPersonModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','ncPersonCode','ncPersonName','ncOrgCode','ncOrgName','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);