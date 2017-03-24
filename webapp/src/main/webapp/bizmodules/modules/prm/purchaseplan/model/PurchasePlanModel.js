Ext.define("Purchaseplan.model.PurchasePlanModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','state','companyCode','projectId','departmentCode','createBy','createTime','seqNo','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'projectName','isPreProject','department','manager','customer','projectMoney','conferenceDate']
	}
);