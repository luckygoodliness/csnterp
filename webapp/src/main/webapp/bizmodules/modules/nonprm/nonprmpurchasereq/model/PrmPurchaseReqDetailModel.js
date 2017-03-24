Ext.define("Nonprmpurchasereq.model.PrmPurchaseReqDetailModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmPurchaseReqId','prmPurchasePlanDetailId','purchaseType','price','purchaseBudgetMoney','name','model','factory','amount','budgetPrice','supplierId','supplierProperty','arriveDate','technicalDrawing','arriveLocation','consignee','contactWay','remark','prmBudgetRefId','prmBudgetType','companyCode','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'scmPurchaseReqId','scmContractId','handleAmount','isfallback','fallbackReason','puuid','serialNumber','seqNo','expectedPrice','scmContractCode','purchasePackageId']
	}
);