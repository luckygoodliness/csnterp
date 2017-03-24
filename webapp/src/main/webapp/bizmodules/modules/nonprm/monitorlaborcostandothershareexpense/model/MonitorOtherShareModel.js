Ext.define("Monitorlaborcostandothershareexpense.model.MonitorOtherShareModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','monitorBaseId','year','month','officeId','subjectName','money','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);