Ext.define("Scmcontract.model.ScmContractAttachmentModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','scmContractId','attachmentType','orderNo','fileName','url','remark','companyCode','projectId','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);