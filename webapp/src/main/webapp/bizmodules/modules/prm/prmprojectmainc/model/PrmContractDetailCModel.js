Ext.define("Prmprojectmainc.model.PrmContractDetailCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'prmContractId', 'customerId', 'contractNowMoney',
            'contractName', 'customerName', 'contractSignMoney', 'taxType', 'prmCodeType', 'isBusinessTax',
            'projectName', 'innerProjectCode', 'contractDuration', 'designerId', 'managementId', 'projectManager', 'innerPurchaseReqId',
            'createBy', 'createTime', 'seqNo', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
                name: 'isVoid',
                defaultValue: 0
            }, 'isRevised', 'changedFields', 'changeStatus']
    }
);