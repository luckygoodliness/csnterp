Ext.define("Subjectsubject.controller.SubjectsubjectController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Subjectsubject.view.SubjectsubjectView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.subjectsubject.dto.NonProjectSubjectSubjectDto',
    queryAction: 'subjectsubject-query',
    loadAction: 'subjectsubject-load',
    addAction: 'subjectsubject-add',
    modifyAction: 'subjectsubject-modify',
    deleteAction: 'subjectsubject-delete',
    exportXlsAction: "subjectsubject-exportxls",
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
//        me.view.getCmp("nonProjectSubjectSubjectDto->financialSubjectCode").sotEditable(false);
//        me.view.getCmp("nonProjectSubjectSubjectDto->officeId").sotEditable(false);
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("nonProjectSubjectSubjectDto->financialSubject").sotEditable(false);
        me.view.getCmp("nonProjectSubjectSubjectDto->officeId").sotEditable(false);
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
//    保存之后回调
    afterSave: function (result) {
        var me = this;
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(result.financialSubjectCode)) {
            me.view.getCmp("nonProjectSubjectSubjectDto->financialSubjectCode")
                .sotValue(result.financialSubjectCode);
        }
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
    }
});