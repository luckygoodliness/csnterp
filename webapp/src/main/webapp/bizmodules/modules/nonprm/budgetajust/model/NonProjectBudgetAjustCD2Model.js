Ext.define("Budgetajust.model.NonProjectBudgetAjustCD2Model", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'cid', 'item', 'orrigalBudgetAssigned', 'appliedAmount', 'budgetCdUuid', 'price', 'changedValie', 'descp', 'companyCode', 'companyName', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion']
    }
);