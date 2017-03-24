Ext.define("Weeklypay.model.WeeklyPayModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','prmWeeklyId','prmPayReqId','scmContractId','accountsPayable','paidMoney','reqMoney','approveMoney','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);