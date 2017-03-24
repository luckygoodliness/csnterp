Ext.define("Supplierinfor.model.ScmSupplierContactsModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierId','contacts','post','phone','mobilePhone','qq','weixin','email','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);