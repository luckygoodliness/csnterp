Ext.define("Responsibleclass.model.ResponsibleclassModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'principal', 'responsibleClass', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }]
    }
);