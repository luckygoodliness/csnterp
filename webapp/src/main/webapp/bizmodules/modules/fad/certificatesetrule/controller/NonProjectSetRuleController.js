Ext.define("Certificatesetrule.controller.NonProjectSetRuleController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Certificatesetrule.view.NonProjectSetRuleView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'orgName', name: 'blur', fn: 'queryOfficeChange'},
        {cid: 'fadNonProjectSetRuleDto->orgName', name: 'blur', fn: 'editOfficeChange'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.certificatesetrule.dto.FadNonProjectSetRuleDto',
    queryAction: 'nonprojectsetrule-query',
    loadAction: 'nonprojectsetrule-load',
    addAction: 'certificatesetrule-add',
    modifyAction: 'certificatesetrule-modify',
    deleteAction: 'certificatesetrule-delete',
    exportXlsAction: "certificatesetrule-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    queryOfficeChange: function () {
        var me = this;
        Erp.Util.SetCookie("queryOfficeChangeNonProjectSubjectSubjectOfficeId", Erp.Util.isNullReturnEmpty(me.view.getCmp("office").gotValue()));
    },
    editOfficeChange: function () {
        var me = this;
        Erp.Util.SetCookie("editOfficeChangeNonProjectSubjectSubjectOfficeId", Erp.Util.isNullReturnEmpty(me.view.getCmp("fadNonProjectSetRuleDto->office").gotValue()));
    }
});