Ext.define("Ztestcode.model.ZtrainMainContentModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','travelCity','travelTime','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);