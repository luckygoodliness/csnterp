Ext.define("Responsibledepartment.model.ResponsibleDepartmentModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'principal', 'attache', 'responsibleDepartment', 'responsibleDepartmentName', 'isProject', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        },'responsibleDepartmentDesc']
    }
);