Ext.define("Purchaseplan.model.PrmPurchasePackageModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid','remainBudget', 'prmProjectMainId', 'packageNo', 'packageBudgetMoney', 'packageState', 'state', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'purchaseLevel', 'subjectProperty', 'description', 'seqNo', 'packageName', 'pendingMoney',
            'materialClassCode', 'materialClassCodeDesc','appliedMoney','completePercent','packageBalance','arriveDate']
    }
);