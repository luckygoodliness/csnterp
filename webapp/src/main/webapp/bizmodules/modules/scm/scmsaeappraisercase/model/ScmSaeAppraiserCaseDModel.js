Ext.define("Scmsaeappraisercase.model.ScmSaeAppraiserCaseDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},
    'locTimezone','tblVersion','seqNo','scmSaeAppraiserCaseId','appraiser','appraiserDesc','officeId','officeIdDesc','remark']
	}
);