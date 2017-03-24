Ext.define("Nonprmpurchasereq.model.QueryBudgetModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','cid','item','amount','price','totalValue','descp','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo']
	}
);