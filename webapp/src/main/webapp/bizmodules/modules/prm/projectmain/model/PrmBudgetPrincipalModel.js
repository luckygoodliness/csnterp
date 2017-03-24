Ext.define("Projectmain.model.PrmBudgetPrincipalModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetMainId','isStamp', 'serialNumber', 'arrivalDate', 'supplerProperty', 'equipmentSubject', 'equipmentName', 'equipmentModel', 'factory', 'unit', 'amount', 'contractAmount', 'contractPrice', 'contractTotalValue', 'bidPrice', 'bidTotalValue', 'schemingPrice', 'schemingTotalValue', 'schemingGrossProfit', 'packageName', 'packageNo', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },
            {
                name: 'serialNumber',
                sortType: Erp.Util.serialNoSortFn
            }, 'prmProjectMainId', 'latestUpdateTime',  'splitFromUuid',
            'purchaseLimitLeft', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);