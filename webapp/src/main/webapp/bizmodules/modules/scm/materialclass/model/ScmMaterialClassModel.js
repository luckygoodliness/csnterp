Ext.define("Materialclass.model.ScmMaterialClassModel", {
        extend: 'Ext.data.Model',
fields: ['uuid','code','shortCode','name','operativeSegments','itemClass','itemClassName','classLevel','classLevelCombo','cycle','isRigid','isRigidCombo','remark','companyCode','projectId','materialType','departmentCode','createBy','createOffice','createTime','seqNo','updateBy','updateOffice','updateTime','locTimezone','tblVersion',{name: 'isVoid', defaultValue: 0}]
	}
);