Ext.define("Scmdepartmentbuyer.model.ScmDepartmentBuyerModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'buyer', 'officeCode', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo','officeCodeDesc']
    }
);