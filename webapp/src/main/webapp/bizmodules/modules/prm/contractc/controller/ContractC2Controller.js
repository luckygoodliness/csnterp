Ext.define("ContractC.controller.ContractC2Controller", {
    extend: 'ContractC.controller.ContractCController',
    viewClass: 'ContractC.view.ContractC2View',
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
    },
    doAdd: function () {
        var me = this;
        var view = me.view;
        var callBack = function (subView) {
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length == 1) {
                view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                view.getCmp("query\x26edit").setActiveTab(me.view.getEditPanel());
                var uuid = selectedRecords[0].get("uuid");
                var postData = {};
                postData.uuid = uuid;
                Scdp.doAction("wrapper-contract-to-revise", postData, function (result) {
                    view.sotValue(result, true);
                }, null, true);
                return true;
            } else if (selectedRecords.length > 1) {
                Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
                return false;
            } else {
                return true;
            }
        };
        var queryController = Ext.create("Contract.controller.ContractQueryController");
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
        queryController.view.getConditionPanel().queryExtraParams = {pickModule: 'pickForContractC'};
    },

    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
    },

    afterSave: function () {
        var me = this;
        me.callParent(arguments);
    },

    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
    },

    doCopyAdd: function () {

    }
});