Ext.define("Fadsupplieradjust.model.FadSupplierAdjustModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','supplierName','projectCode','projectName','ajustMoney','remark','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo']
	}
);