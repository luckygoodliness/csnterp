Ext.define("Monitory.model.NonBudgetMonitorYModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'year', 'subjectCode', 'inOutType', 'budgetType', 'assignedMoney', 'occurredMoney', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'seqNo', 'subject']
    }
);