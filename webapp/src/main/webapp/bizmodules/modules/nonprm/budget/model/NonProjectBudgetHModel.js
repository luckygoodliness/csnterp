Ext.define("Budget.model.NonProjectBudgetHModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','year','state','officeId','officeIdDesc','budgetType','budgetTotal','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);