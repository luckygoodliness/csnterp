Ext.define("Scmcontract.model.ScmContractChangeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractId','runningNo','createByDesc','fadSubjectCode','fadSubjectName','supplierName','originalValue','newValue','changeReason','remark','companyCode','createBy','createTime','updateBy','updateTime','locTimezone','projectId','officeId','departmentCode','createOffice','updateOffice','tblVersion',{name: 'isVoid', defaultValue: 0},'contractname','seqNo','state','closeChange']
	}
);