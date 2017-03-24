Ext.define("Invoice.model.FadInvoiceMaterialModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'fadInvoiceId', 'materialName', 'model', 'units', 'receivableNum', 'actualNum', 'unitPrice', 'amount', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {name: 'isVoid', defaultValue: 0}, 'seqNo']
    }
);