Ext.define("Prmprojectmainc.model.PrmBudgetDetailCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'budgetCode', 'contractMoney','costControlMoney', 'explanation', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmProjectMainCId', 'serialNumber', 'lastUuid', 'budgetRate', 'subjectComment', 'isRevised', 'changedFields', 'changeStatus', 'vatAmount', 'excludingVatAmount']
    }
);