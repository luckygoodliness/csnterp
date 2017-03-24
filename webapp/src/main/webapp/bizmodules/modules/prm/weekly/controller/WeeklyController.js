Ext.define("Weekly.controller.WeeklyController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Weekly.view.WeeklyView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'extBtnFill', name: 'click', fn: 'doExtBtnFill'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.weekly.dto.PrmWeeklyDto',
    queryAction: 'weekly-query',
    loadAction: 'weekly-load',
    addAction: 'weekly-add',
    modifyAction: 'weekly-modify',
    deleteAction: 'weekly-delete',
    exportXlsAction: "weekly-exportxls",
    fillAction: 'weekly-fill',


    afterInit: function () {
        var me = this;

    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    //添加完之后不可以点击自动填充按钮
    afterAdd: function () {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    //复制新增后不可以可以点击自动填充按钮
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    //修改后不可以点击自动填充按钮
    afterModify: function () {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    //如果没有选中记录不能点击自动填充按钮，有选中的记录可以点
    afterSave: function (retData) {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
        var selectRecord = me.view.getResultPanel().getCurRecord();
        if(selectRecord){
            me.view.getCmp("extBtnFill").setDisabled(false);
        }else{
            me.view.getCmp("extBtnFill").setDisabled(true);
        }

    },
    beforeLoadItem: function () {
        var me = this;
        return true;

    },
    //如果没有选中记录不能点击自动填充按钮，有选中的记录可以点
    afterLoadItem: function () {
        var me = this;
        var selectRecord = me.view.getResultPanel().getCurRecord();
        if(selectRecord){
            me.view.getCmp("extBtnFill").setDisabled(false);
        }else{
            me.view.getCmp("extBtnFill").setDisabled(true);
        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    //如果没有选中记录不能点击自动填充按钮，有选中的记录可以点
    afterCancel: function () {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
        var selectRecord = me.view.getResultPanel().getCurRecord();
        if(selectRecord){
            me.view.getCmp("extBtnFill").setDisabled(false);
        }else{
            me.view.getCmp("extBtnFill").setDisabled(true);
        }
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    //删除后自动填充按钮不能点
    afterDelete: function () {
        var me = this;
        me.view.getCmp("extBtnFill").setDisabled(true);
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
    //新增时周报日期默认为当期时间
    doAdd: function () {
        var me = this;
        me.callParent(arguments);
        var curDate = new Date()
        var time = Ext.Date.format(curDate, 'Y-m-d');
        me.view.getCmp("weekleDate").sotValue(time);
    },
    doExtBtnFill: function () {
        var me = this;
        var selectRecord = me.view.getResultPanel().getCurRecord();
        var uuid = this.view.getCmp('prmWeeklyDto->uuid').gotValue();
        var postData = {uuid: uuid};
        Scdp.doAction("weekly-fill", postData, function () {
            me.loadItem(selectRecord.data.uuid);
        })
    },
//项目简报  删除界面上选中的数据
    deleteBrief: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmBriefDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmBriefDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
//    this.view.getCmp('prmBriefDto').getSelectionModel().getSelection()[0].set('prmWeeklyId','');
//    //删除所有
//    var store = me.view.getCmp("prmBriefDto").getStore();
//    store.filterBy(function (record) {
//            if (record.data.prmWeeklyId!= null) {
//                return false;
//            } else {
//                return true;
//            }
//
//    })

    },
    //人员动向   删除界面上选中的数据
    deleteMemberTrend: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmMemberTrendDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmMemberTrendDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    },
    //到货确认  删除界面上选中的数据
    deleteGoodsArrival: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmGoodsArrivalDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmGoodsArrivalDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    },
    //收款计量  删除界面上选中的数据
    deleteCollectionMeasure: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmCollectionMeasureDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmCollectionMeasureDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    },
    //工期进度  删除界面上选中的数据
    deleteProgress: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmProgressDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmProgressDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    },
    //问题申报  删除界面上选中的数据
    deleteProblem: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmProblemDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmProblemDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    },
    //会议纪要  删除界面上选中的数据
    deleteMeetingSummary: function () {
        var me = this;
        var curRecord = this.view.getCmp('prmMeetingSummaryDto').getCurRecord();
        if (curRecord) {
            curRecord.set('prmWeeklyId', '');
            var store = me.view.getCmp("prmMeetingSummaryDto").getStore();
            store.filterBy(function (record) {
                return Scdp.ObjUtil.isNotEmpty(record.get("prmWeeklyId"))

            })
        }
    }
});