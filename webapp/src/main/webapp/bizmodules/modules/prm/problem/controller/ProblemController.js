Ext.define("Problem.controller.ProblemController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Problem.view.ProblemView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'btnProblem', name: 'click', fn: 'openNewWin'},
        {cid: 'colseProblem', name: 'click', fn: 'close'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.problem.dto.PrmProblemDto',
    queryAction: 'problem-query',
    loadAction: 'problem-load',
    addAction: 'problem-add',
    modifyAction: 'problem-modify',
    deleteAction: 'problem-delete',
    exportXlsAction: "problem-exportxls",
    afterInit: function () {
        var me = this;
        var obj = this.actionParams;
        var uuid = obj.uuid;
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            me.loadItem(uuid, me.view)
        }

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
        me.view.getCmp("state").sotValue("1");
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
        var prmWeeklyId = this.view.getCmp('prmProblemDto->prmWeeklyId').gotValue();
        var createBy = me.view.getCmp("createBy").gotValue();
        var postPerson = me.view.getCmp("postPerson").gotValue();
        var name = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
        var state = me.view.getCmp('prmProblemDto->state').gotValue();
        //状态为关闭时，所有按钮都不能点
        if (state == 4) {
            me.view.getCmp('editToolbar->addNew2Btn').setDisabled(true);
            me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
            me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
            me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
            me.view.getCmp("btnProblem").setDisabled(true);
            me.view.getCmp("colseProblem").setDisabled(true);
        } else if (prmWeeklyId || ( name != createBy && name != postPerson)) {
            //有关联关系的不能修改删除
            //只有创建人，提出人可以修改，删除和点击关闭按钮
            me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
            me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
            me.view.getCmp("colseProblem").setDisabled(true);
        } else {
            me.view.getCmp('editToolbar->modifyBtn').setDisabled(false);
            me.view.getCmp('editToolbar->deleteBtn').setDisabled(false);
            me.view.getCmp("colseProblem").setDisabled(false);
        }
//    if( prmWeeklyId ||( name!=createBy && name!=postPerson)){
//        me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
//        me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
//    }
        var prmProblemGrid = me.view.getCmp("prmProblemFeedbackDto");
        var count = prmProblemGrid.getStore().getCount();
        var provider = me.view.getCmp("provider").gotValue();
        //只有协助人和后续协助人可以点问题处理按钮 按钮
        if (name == provider) {
            me.view.getCmp("btnProblem").setDisabled(false);
        } else if (count > 0) {
            for (var i = 0; i < count; i++) {
                if (Scdp.ObjUtil.isNotEmpty(prmProblemGrid.getStore().getAt(i).get("dealPerson")) &&
                    prmProblemGrid.getStore().getAt(i).get("dealPerson") == name) {
                    me.view.getCmp("btnProblem").setDisabled(false);
                    return;
                } else {
                    me.view.getCmp("btnProblem").setDisabled(true);
                }
            }
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

    dobtnProblem: function () {
        var me = this;
        //页面跳转
        var companyCode = me.view.getCmp("prmProblemDto->companyCode").gotValue();
        var projectId = me.view.getCmp("prmProblemDto->projectId").gotValue();
        var departmentCode = me.view.getCmp("prmProblemDto->departmentCode").gotValue();
        var puuid = me.view.getCmp("prmProblemDto->uuid").gotValue();
        var state = me.view.getCmp("prmProblemDto->state").gotValue();
        var mot = me.view.getCmp("prmProblemDto->postDate").gotValue();
        var description = me.view.getCmp("prmProblemDto->description").gotValue();
        var mopId = me.view.getCmp("prmProblemDto->postPerson").gotValue();
        var remark = me.view.getCmp("prmProblemDto->remark").gotValue();
        var param = {companyCode: companyCode, projectId: projectId, departmentCode: departmentCode,
            puuid: puuid, state: state, mot: mot, description: description, mopId: mopId, remark: remark};

        var menuCode = 'RESOURCEREQUEST';
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },
    //点击按钮跳出提示框
    openNewWin: function () {
        var view = this.view;
        var callBack = function (subView) {
            var feedbacks = subView.getCmp("feedbacks").gotValue();

            var dealPersons = subView.getCmp("dealPersons").gotValue();
            var name = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
            var uuid = view.getCmp("uuid").gotValue();
            var postData = {feedbacks: feedbacks, dealPersons: dealPersons, name: name, uuid: uuid};
            Scdp.doAction("problem-deal", postData, function () {
                win.close();
            }, null, null, null, subView.getForm());
        };
        var form = Ext.widget("JForm", {
            height: 150,
            width: 500,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            items: [
                {
                    xtype: 'JUserGrid',
                    cid: 'dealPersons',
                    allowBlank: false,
                    fieldLabel: '后续协助人',
                    displayDesc: true,
                    valueField: 'userId',
                    descField: 'userName'
                },
                {
                    xtype: 'JTextArea',
                    allowBlank: true,
                    fieldLabel: '反馈信息',
                    cid: 'feedbacks'
                },
                {
                    xtype: 'JHidden',
                    cid: 'dealPersonsDesc'
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '反馈信息', "确认");
    },
    //已关闭的所用按钮都不能点
    close: function () {
        var me = this;
        me.view.getCmp('prmProblemDto->state').sotValue('4');
        me.view.getCmp('editToolbar->addNew2Btn').setDisabled(true);
        me.view.getCmp('editToolbar->copyAddBtn').setDisabled(true);
        me.view.getCmp('editToolbar->modifyBtn').setDisabled(true);
        me.view.getCmp('editToolbar->deleteBtn').setDisabled(true);
        me.view.getCmp("btnProblem").setDisabled(true);
        me.view.getCmp("colseProblem").setDisabled(true);
        var uuid = me.view.getCmp("prmProblemDto->uuid").gotValue();
        var postData = {uuid: uuid};
        Scdp.doAction("problem-change", postData, function () {

        })


    }
});