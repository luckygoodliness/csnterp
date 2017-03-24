Ext.define("Weekly.model.PrmGoodsArrivalModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','prmProjectMainId','scmContractDetailId','materialName','model','units','prmWeeklyId','amount','quality','arriveDate','registrant','registrantName','remark','state','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);