Ext.define("Budgetajust.model.NonProjectBudgetAjustCDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','cid','item','orrigalBudgetAssigned','appliedAmount','price',
    'changedValie','descp','companyCode','companyName','departmentCode','createBy',
    'createOffice','createTime','seqNo','updateBy','updateOffice','budgetCdUuid','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);