Ext.define("Income.model.NonProjectIncomeInModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'year', 'subject', 'completion','lastOccurredValue', 'lastAppliedValue', 'subjectOfficeId', 'subjectOfficeName', 'appliedValue', 'firstInstance', 'assignedValue', 'occurredValue', 'companyCode', 'companyName', 'departmentCode', 'createBy','createOffice', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'seqNo', 'changedValue']
    }
);