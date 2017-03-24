Ext.define("Scmsaecase.controller.ScmsaecaseController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmsaecase.view.ScmsaecaseView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmsaecase.dto.ScmSaeCaseDto',
    queryAction: 'scmsaecase-query',
    loadAction: 'scmsaecase-load',
    addAction: 'scmsaecase-add',
    modifyAction: 'scmsaecase-modify',
    deleteAction: 'scmsaecase-delete',
    exportXlsAction: "scmsaecase-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("scmSaeCaseDto->isactive").sotValue(1);

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
        var view = me.view;
        var scmSaeCaseId = view.getCmp("scmSaeCaseDto->uuid").gotValue();
        var postData = {scmSaeCaseId: scmSaeCaseId};
        var flag = true;
        Scdp.doAction("scmsaecase-checkcaseuse", postData, function (result) {
            if (result.success) {
                if ("FALSE" == result.message) {
                    Scdp.MsgUtil.info("该考评方案已被考评管理引用,不允许修改！");
                    flag = false;
                }
            }
        },false,false,false,null);

        return flag;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var scmSaeCaseDetailGrid = view.getCmp("scmSaeCaseDDto");
        var sumRatio = 0.0;
        Ext.Array.each(scmSaeCaseDetailGrid.getStore().data.items, function (record) {
            sumRatio = sumRatio + record.data.ratio;
        });

        if (sumRatio > 100.00) {
            Scdp.MsgUtil.info("占比不能超过100%！");
            return false;
        }

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
    }
});