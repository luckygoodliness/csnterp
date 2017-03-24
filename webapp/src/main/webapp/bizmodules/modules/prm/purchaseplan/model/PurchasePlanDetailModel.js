Ext.define("Purchaseplan.model.PurchasePlanDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmBudgetRefId','isStamp', 'prmBudgetType', 'subPackageNo', 'purchaseType', 'purchaseBudgetMoney', 'purchaseLevel', 'subjectCode', 'subjectProperty', 'name', 'model', 'factory', 'amount', 'budgetPrice', 'supplierId', 'supplierProperty', 'arriveDate', 'technicalDrawing', 'arriveLocation', 'consignee', 'contactWay', 'remark', 'prmContractId', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'seqNo', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion',
            {
                name: 'isVoid',
                defaultValue: 0
            },
            {
                name: 'serialNumber',
                sortType: Erp.Util.serialNoSortFn
            },
            'isBudget', 'isReq', 'isClose', 'prmProjectMainId', 'packageName', 'planAmount', 'purchasePackageId', 'appliedAmount', 'remainAmount', 'unit',
            'originalAmount', 'oriBudgetAmount', 'oriBudgetPrice', 'oriBudgetTotalValue','availableAmount','isRunApply'
        ]
    }
);