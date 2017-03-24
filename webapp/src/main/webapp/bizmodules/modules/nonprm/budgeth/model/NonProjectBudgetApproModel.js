Ext.define("Budgeth.model.NonProjectBudgetApproModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','budgetCuuid','budgetSubjectCode','budgetSubjectName','appropriationBefore','appropriationAfter','descp','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo']
	}
);