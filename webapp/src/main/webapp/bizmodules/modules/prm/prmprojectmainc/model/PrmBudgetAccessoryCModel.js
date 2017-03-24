Ext.define("Prmprojectmainc.model.PrmBudgetAccessoryCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'accessoryName','isStamp', 'accessoryModel', 'amount', 'price', 'totalValue', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
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