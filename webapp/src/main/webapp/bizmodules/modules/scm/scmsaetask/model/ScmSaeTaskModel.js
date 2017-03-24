Ext.define("Scmsaetask.model.ScmSaeTaskModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','curYear','title',{name: 'materialType', defaultValue: 0},'beginTime','endTime','userCode','state','remark']
	}
);