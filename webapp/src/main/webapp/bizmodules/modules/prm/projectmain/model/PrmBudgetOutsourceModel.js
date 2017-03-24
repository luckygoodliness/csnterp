Ext.define("Projectmain.model.PrmBudgetOutsourceModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetMainId','isStamp', 'supplierCode', 'unit', 'amount', 'price', 'totalValue', 'content', 'packageName', 'packageNo', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },
            {
                name: 'serialNumber',
                sortType: Erp.Util.serialNoSortFn
            }, 'prmProjectMainId', 'latestUpdateTime', 'splitFromUuid',
            'purchaseLimitLeft', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);