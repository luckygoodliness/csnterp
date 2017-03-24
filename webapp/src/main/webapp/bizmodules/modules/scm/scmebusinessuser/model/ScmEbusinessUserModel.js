Ext.define("Scmebusinessuser.model.ScmEbusinessUserModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierName','officeIdDesc','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSupplierId','ebusinessName','officeId','userCode','password','charge','remark']
	}
);