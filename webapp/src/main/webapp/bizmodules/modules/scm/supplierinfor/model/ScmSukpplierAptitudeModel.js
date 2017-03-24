Ext.define("Supplierinfor.model.ScmSukpplierAptitudeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmSupplierId','aptitudeType','beginDate','endDate','url','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);