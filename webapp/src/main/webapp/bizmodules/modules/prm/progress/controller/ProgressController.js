Ext.define("Progress.controller.ProgressController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Progress.view.ProgressView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'prmProjectMainId', name: 'change', fn: 'managementChange'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.progress.dto.PrmProgressDto',
    queryAction: 'progress-query',
    loadAction: 'progress-load',
    addAction: 'progress-add',
    modifyAction: 'progress-modify',
    deleteAction: 'progress-delete',
    changeAction: 'progress-change',
    exportXlsAction: "progress-exportxls",
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
        me.doQuery();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        //有关联关系的不能修改删除
        var prmWeeklyId = this.view.getCmp('prmProgressDto->prmWeeklyId').gotValue();
        if (prmWeeklyId) {
            me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
            me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
        }
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
    managementChange: function () {
        var me = this;
        var uuid = me.view.getCmp("prmProjectMainId").gotValue();
        var postData = {uuid: uuid};
        Scdp.doAction("progress-change", postData, function (out) {
            me.view.getCmp('management').sotValue(out.management);
        })
    }
});