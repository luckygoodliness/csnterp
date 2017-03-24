Ext.define('Finalestimate.view.FinalestimateView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/finalestimate',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 75,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'finalestimate-query-layout.xml',
    editLayoutFile: 'finalestimate-edit-layout.xml',
    //extraLayoutFile: 'finalestimate-extra-layout.xml',
    bindings: ['prmFinalEstimateDto', 'prmFinalEstimateDto.prmFinalProjectInfoDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Final_Estimates',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initToolbar();
    },
    initToolbar: function () {
        var me = this;
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.getEditToolbar().add(
            {
                text: '项目情况明细表',
                cid: 'btnProjectReport',
                iconCls: 'temp_icon_16'
            });
        me.getEditToolbar().add(
            {
                text: '修正税金',
                cid: 'btnModifyTax',
                iconCls: 'modify_btn',
                disabled: true
            });
        me.getEditToolbar().add(
            {
                text: '修正已发生成本',
                cid: 'btnModifyCost',
                iconCls: 'modify_btn',
                disabled: true
            });
        me.getEditToolbar().add(
            {
                text: '考核时间修正',
                cid: 'btnExamDate',
                iconCls: 'erp-examDate'
            });
        me.getEditToolbar().add(
            {
                text: '税收修正考核时间修正',
                cid: 'btnrExamDate',
                iconCls: 'erp-rExamDate'
            });
    },
    UIStatusChanged: function (view, uistatus) {
        view.afterChangeUIStatus();
    },
    afterChangeUIStatus: function () {
        var view = this;
        var squareType = view.getCmp("prmFinalEstimateDto->squareType").gotValue();
        var status = view.getCmp("prmFinalEstimateDto->state").gotValue();
        var taxCorrection = view.getCmp("prmFinalEstimateDto->taxCorrection").gotValue();
        var btnModifyTax = view.getCmp("btnModifyTax");
        var btnModifyCost = view.getCmp("btnModifyCost");
        var squareProfitCmp = view.getCmp("prmFinalEstimateDto->squareGrossProfit");
        if (btnModifyTax) {
            var wfData = view.controller.workFlowFormData;
            var enableTaxBtn = view.getUIStatus() == Scdp.Const.UI_INFO_STATUS_VIEW
                && (squareType == "1" || squareType == "3")
                && ((status == "1" && Scdp.ObjUtil.isNotEmpty(wfData)
                && wfData.indexOf("wf_enable_btnModifyTax=1") != -1) || status == "2");
            var enableCostBtn = view.getUIStatus() == Scdp.Const.UI_INFO_STATUS_VIEW
                && (squareType == "1" || squareType == "3") && (status == "1");

            btnModifyTax.setDisabled(!enableTaxBtn);
            btnModifyCost.setDisabled(!enableCostBtn);
        }
        var enableSquareProfit = view.getUIStatus() == Scdp.Const.UI_INFO_STATUS_MODIFY
            && squareType == '0'
            && (Scdp.ObjUtil.isNotEmpty(wfData) && wfData.indexOf("wf_enable_squareProfit=1") != -1);
        squareProfitCmp.sotEditable(enableSquareProfit);
    },
    collectMoreWorkFlowParamOnLoadAssignee: function () {
        var param = {};
        param.assignLoadUserMethod='default';
        return param;
    }
});