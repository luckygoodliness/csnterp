Ext.define("Scmsaecase.model.ScmSaeCaseDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSaeCaseId','evaluationContent','ratio','remark']
	}
);