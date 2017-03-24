Ext.define("Scmcontract.model.ScmContractDetailModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmContractId', 'materialName', 'model', 'units', 'amount', 'unitPriceTalk', 'unitPriceTrue', 'factory', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },{
            name: 'isRepair',
            defaultValue: 0
        }, 'seqNo', 'materialNumber','totalPriceTalk', 'totalPriceTrue', 'arrived']
    }
);