Ext.define("Prmprojectmainc.model.PrmBudgetRunCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'financialSubjectCode', 'unit', 'amount', 'price', 'totalValue', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmProjectMainCId', 'serialNumber', 'lastUuid', 'isRevised', 'changedFields', 'changeStatus', 'lockedMoney']
    }
);