Ext.define("Fadsuppliermapping.model.FadSupplierMappingModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'supplierFromName', 'supplierToUuid', 'supplierToUuidDesc', 'supplierToUuidDesc', 'remark', 'companyCode', 'companyName', 'departmentCode', 'completeName',
            'createBy', 'createTime', 'updateBy', 'updateTime', {name: 'isVoid', defaultValue: 0}, 'locTimezone', 'tblVersion', 'seqNo', 'supplierToLastUuid', {name: 'dataFrom', defaultValue: 1}]
    }
);