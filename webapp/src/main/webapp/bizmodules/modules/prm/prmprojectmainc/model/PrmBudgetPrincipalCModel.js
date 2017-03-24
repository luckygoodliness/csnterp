Ext.define("Prmprojectmainc.model.PrmBudgetPrincipalCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'arrivalDate', 'supplerProperty', 'isStamp','equipmentSubject', 'equipmentName', 'equipmentModel', 'factory', 'unit', 'amount', 'contractAmount', 'contractPrice', 'contractTotalValue', 'bidPrice', 'bidTotalValue', 'schemingPrice', 'schemingTotalValue', 'schemingGrossProfit', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },
            {
                name: 'serialNumber',
                sortType: Erp.Util.serialNoSortFn
            }, 'prmProjectMainId', 'lastUuid', 'splitFromUuid', 'splitFromUuidNo', 'isRevised',
            'changedFields', 'changeStatus', 'subjectProperty', 'preTotalValue', 'lockedAmount', 'lockedMoney', 'packageName', 'packageNo']
    }
);