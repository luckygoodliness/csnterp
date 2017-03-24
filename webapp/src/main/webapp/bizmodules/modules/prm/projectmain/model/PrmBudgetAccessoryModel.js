Ext.define("Projectmain.model.PrmBudgetAccessoryModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetMainId','isStamp', 'accessoryName', 'accessoryModel', 'amount', 'price', 'totalValue', 'packageName', 'packageNo', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },
            {
                name: 'serialNumber',
                sortType: Erp.Util.serialNoSortFn
            }, 'prmProjectMainId', 'latestUpdateTime', 'changeStatus', 'splitFromUuid',
            'purchaseLimitLeft', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);