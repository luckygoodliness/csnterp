Ext.define("Purchaseplan.model.PrmPurchasePackageRecordModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmPurchasePackageId','revisedBy','revisedTime','packageNo','packageBudgetMoney','packageState','state','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'purchaseLevel','subjectProperty','description','seqNo','packageName','pendingMoney','materialClassCode','materialClassCodeDesc','arriveDate','revisedByDesc']
	}
);