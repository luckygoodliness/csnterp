Ext.define("Scmsupplierlimitchange.model.ScmSupplierLimitChangeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','year','title','state','remark']
	}
);