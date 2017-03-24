Ext.define("Budgetajust.model.NonProjectBudgetAjustCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'hid', 'financialSubjectCode', 'orrigalBudgetAssigned', 'budgetChanged', 'changedReason', 'companyCode', 'companyName',
            'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'subjectCode', 'subjectName', 'updateBy',
            'updateOffice', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'afterAdjustMoney',
            'nonProjectBudgetAjustCDDto', 'nonProjectBudgetAjustCDDto_c', 'nonProjectBudgetAjustCD2Dto',
            'nonProjectBudgetAjustCD2Dto_c']
    }
);
