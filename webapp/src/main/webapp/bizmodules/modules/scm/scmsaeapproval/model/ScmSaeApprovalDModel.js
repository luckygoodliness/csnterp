Ext.define("Scmsaeapproval.model.ScmSaeApprovalDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','supplierName','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},
    'locTimezone','tblVersion','seqNo','scmSaeApprovalId','supplierId','supplierGenre','comprehensive','remark','addFrom']
	}
);