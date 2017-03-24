Ext.define("Collectionmeasure.controller.CollectionmeasureController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Collectionmeasure.view.CollectionmeasureView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'measureMoney', name: 'blur', fn: 'equalZero'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.collectionmeasure.dto.PrmCollectionMeasureDto',
    queryAction: 'collectionmeasure-query',
    loadAction: 'collectionmeasure-load',
    addAction: 'collectionmeasure-add',
    modifyAction: 'collectionmeasure-modify',
    deleteAction: 'collectionmeasure-delete',
    exportXlsAction: "collectionmeasure-exportxls",
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
        var prmWeeklyId = this.view.getCmp('prmCollectionMeasureDto->prmWeeklyId').gotValue();
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
    equalZero: function () {
        var me = this;
        //金额不能小于0
        var measureMoney = me.view.getCmp("measureMoney").gotValue();
        if (measureMoney < 0) {
            Scdp.MsgUtil.info("金额不能小于0！");
        }
    }
});