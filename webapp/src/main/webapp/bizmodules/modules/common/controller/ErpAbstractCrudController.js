Ext.define("ErpMvc.controller.ErpAbstractCrudController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
            var businessObj = me.actionParams.erp_msg_business_obj;
            if (businessObj && businessObj.uuid) {
                me.loadItem(businessObj.uuid);
            }
        }
    },
    doSave: function (obj, event, successFn) {
        var me = this;
        if (me.erpBeforeSave) {
            if (me.erpBeforeSave()) {
                me.doErpSave(obj, event, successFn);
            }
        } else {
            me.doErpSave(obj, event, successFn);
        }
    },
    doErpSave: function (obj, event, successFn) {
        var me = this;
        var parentClass = me.superclass;
        for (; parentClass && Ext.getClassName(parentClass) != "Scdp.mvc.AbstractCrudController"; parentClass = parentClass.superclass);
        if (parentClass) {
            parentClass.doSave.apply(me, arguments);
        }
    },
    loadItem: function (uuid, opt, callbackFn) {
        var me = this;
        var callArgs = arguments;
        callArgs[0] = {uuid: uuid, workFlowDefinitionKey: me.view.workFlowDefinitionKey};
        this.callParent(callArgs);
    },
    afterLoadItem: function (args) {
        var me = this;
        //this.callParent(arguments);
        if (args && args[0] && args[0].erpWfInitParams) {
            var erpWfInitParams = args[0].erpWfInitParams;
            refreshWorkFlowBarStatus(erpWfInitParams, me);
        }
    },
    doDelete: function () {
        var me = this;
        if (me.erpBeforeDelete && me.erpBeforeDelete() === false) {
            return;
        }
        me.callParent(arguments);
    },
    erpBeforeDelete: function () {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
            if (me.actionParams.deleteCheckCreateBy == '1') {
                if (Scdp.getCurrentUserId() !== me.view.createBy) {
                    Scdp.MsgUtil.info("只有创建人可以删除该数据！");
                    return false;
                }
            }
        }
        return true;
    },
    doModify: function () {
        var me = this;
        if (me.erpBeforeModify && me.erpBeforeModify() === false) {
            return;
        }
        me.callParent(arguments);
    },
    erpBeforeModify: function () {
        var me = this;
        if (me.actionParams.modifyCheckCreateBy == '1') {
            var stateCmp = me.view.getHeader().getCmp(me.getWfStateField());
            if (Scdp.ObjUtil.isNotEmpty(stateCmp)) {
                var state = stateCmp.gotValue();
                if (Scdp.ObjUtil.isEmpty(state) || '0' == state || '5' == state) {
                    if (Scdp.getCurrentUserId() !== me.view.createBy) {
                        Scdp.MsgUtil.info("只有创建人可以修改该数据！");
                        return false;
                    }
                }
            }
        }
        return true;
    },
    getDeleteObj: function () {
        var me = this;
        var uuids = this.callParent(arguments);
        var obj = {};
        obj.uuids = uuids;
        obj.workFlowDefinitionKey = me.view.workFlowDefinitionKey;
        return obj;
    },
    getWfStateField: function () {
        return "state";
    },
    getFixedStates: function () {
        return ['2','4','8','3'];
    },
    getWfFixedExt: function () {
        var me = this;
        var view = me.view;
        var headerForm = view.getHeader();
        if (headerForm) {
            var state = headerForm.getCmp(me.getWfStateField()).gotValue();
            return Ext.Array.contains(me.getFixedStates(), state);
        }
        return false;
    },
    afterCompelteTask: function (result) {
        var me = this;
        me.refreshViewAfterHandleWF(result);
    },
    afterRejectTask: function (result) {
        var me = this;
        me.refreshViewAfterHandleWF(result);
    },
    afterCancelTask: function (result) {
        var me = this;
        me.refreshViewAfterHandleWF(result);
    },
    refreshViewAfterHandleWF: function (result) {
        var me = this;
        var view = me.view;
        if (result && Scdp.ObjUtil.isNotEmpty(result.state)) {
            var headerForm = view.getHeader();
            if (headerForm) {
                headerForm.getCmp(me.getWfStateField()).sotValue(result.state + '');
            }
        }
        me.afterLoadItem();
    }
});