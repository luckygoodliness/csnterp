Ext.define("Projectmain.model.PrmContractDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'prmContractId', 'customerId', 'contractNowMoney',
            'contractName', 'customerName', 'innerProjectCode', 'contractSignMoney',
            'createBy', 'createTime', 'seqNo', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
                name: 'isVoid',
                defaultValue: 0
            }]
    }
);