Ext.define("Monitorm.model.NonProjectIncomeMonModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'puuid', 'subject', 'assignedValue','month', 'occurredValue', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'seqNo']
    }
);