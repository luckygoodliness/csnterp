Ext.define("Companyplan.model.OperateCompanyPlanHModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','startYear','jhsr','jhjslr','jhlxlr','jhjssr','jhhte','planName','remark']
	}
);