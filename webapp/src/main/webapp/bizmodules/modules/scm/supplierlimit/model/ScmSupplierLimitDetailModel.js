Ext.define("Supplierlimit.model.ScmSupplierLimitDetailModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSupplierLimitId','scmSupplierId','maxVolume','maxAmount','remark','supplierType','curVolume','curAmount','scmSupplierName']
	}
);