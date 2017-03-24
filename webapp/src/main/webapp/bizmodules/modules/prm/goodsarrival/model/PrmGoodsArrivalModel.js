Ext.define("Goodsarrival.model.PrmGoodsArrivalModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','registrantName','scmContractDetailId','materialName','model','units','prmWeeklyId','amount','quality','arriveDate','registrant','remark','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);