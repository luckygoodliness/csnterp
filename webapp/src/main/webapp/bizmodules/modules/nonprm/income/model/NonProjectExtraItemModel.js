Ext.define("Extraitem.model.NonProjectExtraItemModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','type','name','remark','companyCode','companyName','departmentCode','createBy','createOffice','createTime','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo']
	}
);