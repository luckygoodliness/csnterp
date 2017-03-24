Ext.define("PrmExamPeriod.controller.PrmExamPeriodController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'PrmExamPeriod.view.PrmExamPeriodView',
    uniqueValidateFields: [],
    extraEvents: [

    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmexamperiod.dto.PrmExamPeriodDto',
    queryAction: 'prmexamperiod-query',
    loadAction: 'prmexamperiod-load',
    addAction: 'prmexamperiod-add',
    modifyAction: 'prmexamperiod-modify',
    deleteAction: 'prmexamperiod-delete',
    exportXlsAction: "prmexamperiod-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var beginDate = view.getCmp("prmExamPeriodDto->beginDate").gotValue();
        var endDate = view.getCmp("prmExamPeriodDto->endDate").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(beginDate) && Scdp.ObjUtil.isNotEmpty(endDate)) {
            if (beginDate > endDate) {
                Scdp.MsgUtil.warn("开始日期不能大于结束日期！");
                return false;
            }
        }
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.view.afterChangeUIStatus();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var isActive = view.getCmp("prmExamPeriodDto->isActive").gotValue();
        if (isActive == 1) {
            Scdp.MsgUtil.warn("有效状态的数据不能删除！");
            return false;
        }
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