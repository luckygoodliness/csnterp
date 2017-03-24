Ext.define("Goodsarrival.model.ScmContractDetailModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractId','confirmState','actualAmount','materialName','model','cdmUnitCombo','amount','unitPriceTalk','unitPriceTrue','factory','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);