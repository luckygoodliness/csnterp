Ext.define("Prmblacklistmonth.controller.PrmblacklistmonthController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmblacklistmonth.view.PrmblacklistmonthView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.prmblacklistmonth.dto.PrmBlacklistMonthDto',
    queryAction: 'prmblacklistmonth-query',
    loadAction: 'prmblacklistmonth-load',
    addAction: 'prmblacklistmonth-add',
    modifyAction: 'prmblacklistmonth-modify',
    deleteAction: 'prmblacklistmonth-delete',
    exportXlsAction: "prmblacklistmonth-exportxls",
    afterInit: function () {
        var me = this;
//        //        设为当前年月
//        var date = new Date();
//        var time=Ext.Date.format(date, 'Y-m');
//        me.view.getCmp("monthFrom").sotValue(time);
//        me.view.getCmp("monthTo").sotValue(time);
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
        var monthFrom = me.view.getCmp('prmBlacklistMonthDto->monthFrom').gotValue();
        var monthTo = me.view.getCmp('prmBlacklistMonthDto->monthTo').gotValue();
        if (monthFrom > monthTo) {
            Scdp.MsgUtil.info("开始年月不能大于结束年月!");
            return false;
        } else {
            return true;
        }
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
    doAdd: function () {
        var me = this;
//    //设置当前月份
//    var curDate = new Date()
//    var month = curDate.getMonth()+1;
//    me.view.getCmp("month").setValue(month);
        me.callParent(arguments);
//    //        设为当前年月
//    var date = new Date();
//    var time=Ext.Date.format(date, 'Y-m');
//    me.view.getCmp("monthFrom").sotValue(time);
    }
});