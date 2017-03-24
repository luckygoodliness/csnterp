Ext.define("Prmbilling.model.PrmBillingDetailModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmBillingId','content','contentEn','unit','amount','price','totalValue','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);