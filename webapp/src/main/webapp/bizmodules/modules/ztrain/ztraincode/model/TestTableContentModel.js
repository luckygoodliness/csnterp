Ext.define("Ztraincode.model.TestTableContentModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','bankcardNo','bankName','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);