Ext.define("Supplierinfor.model.ScmSupplierEvaluationModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','companyCode','companyName','departmentCode','createBy','createTime','updateBy','updateTime',{name: 'isVoid', defaultValue: 0},'locTimezone','tblVersion','seqNo','scmSupplierId','evaluateFrom','price','business','personQuality','organizingCapability','compliance','securityManagement','finalEstimate','arrivalTime','equipmentQuality','service','comprehensive','remark','scmContractCode','scmContractId']
	}
);