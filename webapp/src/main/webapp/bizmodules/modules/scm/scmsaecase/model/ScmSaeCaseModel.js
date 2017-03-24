Ext.define("Scmsaecase.model.ScmSaeCaseModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','caseName',{name: 'caseType', defaultValue: 0},{name: 'isactive', defaultValue: 0},'remark']
	}
);