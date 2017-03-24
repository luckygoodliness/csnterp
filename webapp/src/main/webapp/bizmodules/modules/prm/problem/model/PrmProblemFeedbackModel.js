Ext.define("Problem.model.PrmProblemFeedbackModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProblemId','dealPersonName','createByName','feedback','state','dealPerson','seqNo','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);