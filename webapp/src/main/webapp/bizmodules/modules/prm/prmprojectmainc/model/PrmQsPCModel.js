Ext.define("Prmprojectmainc.model.PrmQsPCModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainCId', 'state', 'safePrincipal', 'safeContact', 'safeDocument', 'safeMeasure', 'qualityPrincipal', 'qualityContact', 'qualityDocument', 'qualityMeasure', 'outerNo', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'prmProjectMainId', 'seqNo'
            , 'safePrincipalDesc', 'safeContactDesc', 'qualityPrincipalDesc', 'qualityContactDesc', 'lastUuid', 'isRevised', 'changedFields', 'changeStatus']
	}
);