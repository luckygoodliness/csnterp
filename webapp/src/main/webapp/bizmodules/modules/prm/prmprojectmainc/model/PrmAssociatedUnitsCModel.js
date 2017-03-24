Ext.define("Prmprojectmainc.model.PrmAssociatedUnitsCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'associatedUnitsName', 'associatedType', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'remark', 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);