Ext.define("Scmsupplierlimitchange.model.ScmSupplierLimitChangeDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSupplierLimitChangeId','scmSupplierId','maxVolume','maxAmount','supplierType','curVolume','curAmount','remark','scmSupplierName','scmContractDto','scmContractDto_c']
	}
);