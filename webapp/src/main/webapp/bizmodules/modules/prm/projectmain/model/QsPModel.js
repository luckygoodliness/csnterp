Ext.define("Projectmain.model.QsPModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'state', 'safePrincipal', 'safeContact', 'safeDocument', 'safeMeasure', 'qualityPrincipal', 'qualityContact', 'qualityDocument', 'qualityMeasure', 'outerNo', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'safePrincipalDesc', 'safeContactDesc', 'qualityPrincipalDesc', 'qualityContactDesc',
            'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);