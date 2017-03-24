Ext.define("Prmpurchasereq.model.PrmPurchaseReqDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmPurchaseReqId', 'isStamp', 'stampProjectUuid', 'needUpdate', 'chkAmount', 'chkBudgetPrice', 'packageNo', 'chkPackageId', 'chkPackageName', 'packageId', 'costControlMoney', 'lockedBudget', 'remainBudget', 'budgetCode', 'budgetName', 'prmPurchasePlanDetailId', 'subPackageNo', 'projectName', 'purchaseType', 'purchaseBudgetMoney', 'scmContractCode', 'name', 'model', 'factory', 'amount', 'budgetPrice', 'supplierId', 'supplierProperty', 'arriveDate', 'technicalDrawing', 'arriveLocation', 'consignee', 'contactWay', 'remark', 'prmBudgetRefId', 'prmBudgetType', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'scmPurchaseReqId', 'projectCode', 'scmContractId', 'handleAmount', 'isfallback', 'fadSubjectCode', 'fallbackReason', 'puuid', 'serialNumber', 'packageName', 'planAmount', 'price', 'contractState', 'undertaker', 'isUploaded', 'unit', 'expectedPrice', 'purchasePackageId', 'withAttach', 'isStamp']
    }
);