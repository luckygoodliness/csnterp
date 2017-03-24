Ext.define("Scmcontractchange.model.ScmContractChangeDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',
    {name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','prmPurchaseReqDetailId',
    'prmPurchaseReqId','prmPurchasePlanDetailId','purchaseType','purchaseBudgetMoney','name',
    'model','factory','amount','budgetPrice','supplierId','supplierProperty','arriveDate',
    'technicalDrawing','arriveLocation','consignee','contactWay','remark','prmBudgetRefId',
    'prmBudgetType','scmPurchaseReqId','scmContractId','handleAmount','fallbackReason','puuid',
    'serialNumber','expectedPrice','scmContractCode','purchasePackageId','unit',
    {name: 'isfallback', defaultValue: 0},'changeState','packageName','subTotal','planAmount'
]
	}
);