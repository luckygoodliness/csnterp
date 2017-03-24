Ext.define("Invoice.model.ScmInvoiceMaterialModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractInvoiceId','scmContracDetailId','materialName','model','storageValue','price','totalValue','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);