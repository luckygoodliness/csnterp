Ext.define("Scmsae.model.ScmSaeModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','saiCaseName','waiCaseName','companyName',
    'departmentCode','createBy','createTime','updateBy','updateTime',
    {name: 'isVoid', defaultValue: 0},
    'locTimezone','tblVersion','seqNo','curYear',
    'title','saiCase','waiCase','appraiserCase','saiCaseDesc','waiCaseDesc','appraiserCaseDesc','beginTime','endTime','remark']
	}
);