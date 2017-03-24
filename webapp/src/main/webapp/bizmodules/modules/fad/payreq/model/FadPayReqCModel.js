Ext.define("Payreq.model.FadPayReqCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'puuid',
            'orgName', 'supplierId', 'supplierName', 'projectMainId', 'projectCode', 'projectMainName',
            'prmMoney', 'prmActualMoney', 'prmReceiptRate', 'isBlackList', 'scmContractThenPaid', 'auditMoneyRate',
            'scmContractId', 'scmContractCode', 'scmContractAmount', 'accountsPayable', 'cashReqValue', 'scmContractExpectPaid',
            'scmContractPaidMoney', 'scmPaidRate', 'scmContractCheckedAmount', 'scmContractFadInvoiceMoney',
            'scmContractSupplierUnPaidMoney', 'scmContractUnPaidMoney', 'scmContractNeedToPay','invoiceScmNeedCheck',
            'reqMoney', 'reqMoneyRate', 'approveMoney', 'approveMoneyRate', 'purchaseManagerMoney', 'purchaseManagerMoneyRate',
            'purchaseChiefMoney', 'purchaseChiefMoneyRate', 'auditMoney', 'fadSubjectCode',
            'payReason', 'state', 'remark', 'payType', 'expectDate', 'certificateNo', 'certificateCreateTime', 'departmentCode',
            'companyCode', 'companyName', 'createBy', 'createByName' , 'createTime', 'seqNo', 'updateBy','departmentName',
            'updateTime', {name: 'isVoid', defaultValue: 0},
            {
                name: 'state',
                sortType: Erp.Util.stateSortFn
            },
            'locTimezone', 'recordVersion','contractNature']
    }
);