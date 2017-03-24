Ext.define("Scmsaeappraisercase.model.ScmSaeAppraiserCaseModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','caseName',{name: 'isactive', defaultValue: 0},'remark']
	}
);