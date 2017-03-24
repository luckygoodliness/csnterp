Ext.define("Projectmain.model.ReceiptsDetailPModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'prmReceiptsMainPId', 'projectStage', 'schemingReceiptsDate', 'schemingReceiptsMoney', 'explanation', 'companyCode', 'projectId', 'departmentCode', 'createBy', 'createOffice', 'createTime', 'seqNo', 'updateBy', 'updateOffice', 'updateTime', 'locTimezone', 'tblVersion', {
            name: 'isVoid',
            defaultValue: 0
        }, 'latestUpdateTime', 'changeStatus', 'changeVersionDesc', 'latestUpdateBy', 'latestUpdateByDesc']
    }
);