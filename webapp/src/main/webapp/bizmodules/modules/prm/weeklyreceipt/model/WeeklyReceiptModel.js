Ext.define("Weeklyreceipt.model.WeeklyReceiptModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','prmWeeklyId','prmReceiptsId','payer','customerId','estimateMoney','actualMoney','moneyType','estimateDate','actualDate','prmUnknownReceiptsId','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);