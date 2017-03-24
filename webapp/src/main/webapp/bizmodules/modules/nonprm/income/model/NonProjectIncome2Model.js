Ext.define("Income.model.NonProjectIncome2Model", {
        extend: 'Ext.data.Model',
fields: ['uuid','year','subject','completion','lastAppliedValue','appliedValue','firstInstance','assignedValue','occurredValue','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);