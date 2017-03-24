Ext.define("Receipts.model.ReceiptsModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmContractId', 'prmContractIdDesc', 'payer', 'customerId', 'estimateMoney', 'actualMoney', 'moneyType', 'estimateDate', 'actualDate', 'prmUnknownReceiptsId', 'state', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'examDate', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'customerIdDesc', 'payerDesc', 'isPreProject', 'projectName', 'deptName', 'examDate',
            'customerName', 'moneyTypeDesc', 'departmentCodeDesc', 'isInternal', 'internalOffice']
    }
);