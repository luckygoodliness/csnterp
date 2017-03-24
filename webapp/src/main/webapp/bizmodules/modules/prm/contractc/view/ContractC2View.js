Ext.define('ContractC.view.ContractC2View', {
    extend: 'ContractC.view.ContractCView',
    workFlowDefinitionKey: 'Prm_Contract_Revise',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.initToolbar();
    },
    initQueryPanel: function () {
        var me = this;
        me.getCmp("queryPanel->contractStatus").sotValue("REVISE");
    },
    initToolbar: function () {
        var me = this;
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
    },
    UIStatusChanged: function (view, newStatus) {
        view.callParent(arguments);
        if (Scdp.Const.UI_INFO_STATUS_MODIFY == newStatus || Scdp.Const.UI_INFO_STATUS_NEW == newStatus) {
            view.getCmp("prmContractCDto").sotEditable(false);
            //view.getCmp("prmContractCDto->contractSignMoney").sotEditable(false);
            view.getCmp("prmContractCDto->contractNowMoney").sotEditable(true);
            view.getCmp("prmContractCDto->contractDuration").sotEditable(true);
            view.getCmp("prmContractCDto->preoperation").sotEditable(true);
        }
    },
    initContractNowMoney:function(){
        var view=this;
        var form = view.getCmp("prmContractCDto");
        var contractNowMoneyCmp = form.getCmp("contractNowMoney");
        contractNowMoneyCmp.setVisible(true);
        contractNowMoneyCmp.afterSotValue = function () {
            var contractNowMoneyValue = contractNowMoneyCmp.gotValue();
            var deptCode = form.getCmp("contractorOffice").gotValue();
            var deptMajorMoney = Erp.Util.getDeptMajorProjectMoney(deptCode);
            var isMajorProject = form.getCmp("isMajorProject");
            if (Scdp.ObjUtil.isNotEmpty(deptMajorMoney) && contractNowMoneyValue >= deptMajorMoney) {
                isMajorProject.sotValue(1);
            } else {
                isMajorProject.sotValue(0);
            }
        }
    }
});