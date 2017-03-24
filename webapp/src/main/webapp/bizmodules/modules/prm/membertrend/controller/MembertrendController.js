Ext.define("Membertrend.controller.MembertrendController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Membertrend.view.MembertrendView',
    uniqueValidateFields: [],
    extraEvents: [{cid: 'beginDate', name: 'blur', fn: 'checkDate'},
                  {cid: 'endDate', name: 'blur', fn: 'checkDate'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.membertrend.dto.PrmMemberTrendDto',
    queryAction: 'membertrend-query',
    loadAction: 'membertrend-load',
    addAction: 'membertrend-add',
    modifyAction: 'membertrend-modify',
    deleteAction: 'membertrend-delete',
    exportXlsAction: "membertrend-exportxls",
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
    //时间的判断
    beforeSave: function () {
        var me = this;
        var beginDate = me.view.getCmp('prmMemberTrendDto->beginDate').gotValue();
        var endDate = me.view.getCmp('prmMemberTrendDto->endDate').gotValue();
        var percent = me.view.getCmp("prmMemberTrendDto->percent").gotValue();
        if (beginDate > endDate) {
            Scdp.MsgUtil.info("开始时间不得大于结束时间！")
            return  false;
        } else if (percent < 0) {
            Scdp.MsgUtil.info("投入百分比不能小于0！")
            return  false;
        } else {
            return  true;
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
        //有关联关系的不能修改删除
        var prmWeeklyId = this.view.getCmp('prmMemberTrendDto->prmWeeklyId').gotValue();
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
    checkDate: function(obj, isChanged){
        var me = this;
        var endDate = me.view.getCmp("endDate").gotValue();
        var beginDate = me.view.getCmp("beginDate").gotValue();
        if(endDate && beginDate && endDate < beginDate){
            obj.sotValue(null);
            Scdp.MsgUtil.info("结束时间不能比开始时间早！")
        }
    }
});