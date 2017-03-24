Ext.define("Receipts.model.PrmReceiptsScmInvoiceModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid','prmReceiptsUuid','scmContractUuid','hedgeMoney','remark','companyCode','companyName',
            'departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},
            'locTimezone','tblVersion','seqNo','departmentCodeDesc','projectCode','projectName','scmContractUuidDesc',
            'needPayMoney','needPayMoneyLock','officeId','officeIdDesc']
    }
);