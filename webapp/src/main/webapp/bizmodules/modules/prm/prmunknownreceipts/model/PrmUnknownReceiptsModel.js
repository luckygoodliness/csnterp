Ext.define("Prmunknownreceipts.model.PrmUnknownReceiptsModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','payer','money','moneyType','actualDate','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'receiptNo']
	}
);