Ext.define("Companyplan.model.OperateCompanyPlanDModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','operateCompanyPlanCId','objectType','jhsr','jhjslr','jhlxlr','jhjssr','jhhte','jzhte','jzlr','jhszyy','lxyxexz','lxlrxz','jzsr','htexz','bmjslrxz','bmjssrxz','bmsrxz']
	}
);