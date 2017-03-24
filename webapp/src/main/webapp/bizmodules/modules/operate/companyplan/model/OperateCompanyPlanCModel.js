Ext.define("Companyplan.model.OperateCompanyPlanCModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','operateCompanyPlanHId','objectId','officeName','jhsr','jhjslr','jhlxlr','jhjssr','jhhte','jzhte','jzlr','jhszyy','lxyxexz','lxlrxz','jzsr','htexz','bmjslrxz','bmjssrxz','bmsrxz','state','operateCompanyPlanDDto','operateCompanyPlanDDto_c']
	}
);