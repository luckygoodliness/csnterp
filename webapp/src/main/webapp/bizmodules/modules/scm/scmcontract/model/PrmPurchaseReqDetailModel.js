Ext.define("Scmcontract.model.PrmPurchaseReqDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmPurchaseReqId', 'prmPurchasePlanDetailId', 'purchaseType', 'purchaseBudgetMoney', 'name', 'model', 'factory', 'amount', 'budgetPrice', 'supplierId', 'supplierProperty', 'arriveDate', 'technicalDrawing', 'arriveLocation', 'consignee', 'contactWay', 'remark', 'prmBudgetRefId', 'prmBudgetType', 'companyCode', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'scmPurchaseReqId','purchaseReqNo','isProject', 'scmContractId', 'handleAmount', 'isfallback', 'fallbackReason', 'puuid', 'serialNumber', 'totalBudgetMoney', 'packageName', 'totalExpectedMoney', 'expectedPrice', 'seqNo', 'expectedPrice', 'scmContractCode', 'purchasePackageId', 'unit']
    }
);
