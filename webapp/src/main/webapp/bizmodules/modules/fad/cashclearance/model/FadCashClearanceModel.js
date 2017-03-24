Ext.define("Cashclearance.model.FadCashClearanceModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','projectName','totalMoney','runningNo','clearancePerson','clearancePersonName','officeId','state','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);