Ext.define("Cdmfile.model.CdmFileRelationModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'dataId', 'fileId', 'fileType', 'module', 'remark', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createTime', 'updateBy', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'seqNo', 'fileName', 'fileSize', 'cdmFileType', 'fileClassify', 'begindate', 'enddate',
            'convertedFileSize'
        ]
    }
);