Ext.define("Scmoverduereceivables.model.ScmOverdueReceivablesModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContract','supplier','totalValue','targetValue','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);