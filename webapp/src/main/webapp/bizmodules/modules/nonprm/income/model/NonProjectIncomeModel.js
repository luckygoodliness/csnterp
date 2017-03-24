Ext.define("Income.model.NonProjectIncomeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','year','tit','subject','lastAppliedValue', 'lastOccurredValue','subjectOfficeId', 'subjectOfficeName','appliedValue','firstInstance','assignedValue','occurredValue','companyCode','companyName','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion']
	}
);