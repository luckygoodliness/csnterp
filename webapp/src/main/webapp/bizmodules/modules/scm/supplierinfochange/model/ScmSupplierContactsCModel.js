Ext.define("Supplierinfochange.model.ScmSupplierContactsCModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','puuid','scmSupplierId','contacts','post','phone','mobilePhone','qq','weixin','email','remark','companyCode','projectId','departmentCode','createBy','createTime','updateBy','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0},'seqNo']
	}
);