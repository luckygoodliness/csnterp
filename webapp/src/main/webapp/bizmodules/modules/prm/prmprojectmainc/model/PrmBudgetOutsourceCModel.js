Ext.define("Prmprojectmainc.model.PrmBudgetOutsourceCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'supplierCode', 'unit','isStamp', 'amount', 'price', 'totalValue', 'content', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
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