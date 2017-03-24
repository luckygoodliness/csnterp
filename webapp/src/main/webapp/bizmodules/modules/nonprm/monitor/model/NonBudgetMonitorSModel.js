Ext.define("Monitor.model.NonBudgetMonitorSModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','year','season','title','budgetOfficeId','inOutType','budgetType','assignedMoney','occuredMoney','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);