Ext.define("Budget.model.NonProjectBudgetCD2Model", {
        extend: 'Ext.data.Model',
fields: ['uuid','cid','item','amount','price','totalValue','descp','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);