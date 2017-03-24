Ext.define("Projectmain.model.PrmAssociatedUnitsModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'associatedUnitsName', 'associatedType', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);