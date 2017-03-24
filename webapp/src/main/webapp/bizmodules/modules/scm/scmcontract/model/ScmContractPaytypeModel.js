Ext.define("Scmcontract.model.ScmContractPaytypeModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'scmContractId', 'paytype', 'orderNo', 'title', 'value','actuallyPaid', 'companyCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'projectId', 'departmentCode', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo']
    }
);