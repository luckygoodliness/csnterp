Ext.define('Prmpurchasereq.view.PrmpurchasereqView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmpurchasereq',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmpurchasereq-query-layout.xml',
    editLayoutFile: 'prmpurchasereq-edit-layout.xml',
    //extraLayoutFile: 'prmpurchasereq-extra-layout.xml',
    //bindings: ['prmPurchaseReqDto','prmPurchasePlanDtoTemp','prmPurchaseReqDto.prmPurchaseReqDetailDto','prmPurchaseReqDto.cdmFileRelationDto'],
    bindings: ['prmPurchaseReqDto', 'prmPurchaseReqDto.prmPurchaseReqDetailDto', 'prmPurchaseReqDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Purchase_Request',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("editPanel->editToolbar->copyAddBtn").hide();
//        1.添加编辑区的工具按钮
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '内委申请作废',
            cid: 'abolishBtn',
            iconCls: 'close_other_modules',
            disabled: "false"
        });
        editToolbar.add({
            text: '标记项目',
            cid: 'signProject',
            iconCls: 'close_other_modules',
            disabled: false
        });
        editToolbar.add({
            text: '取消标记',
            cid: 'abolishSign',
            iconCls: 'close_other_modules',
            disabled: false
        });
        me.hideReqToolBar(me.getCmp("prmPurchaseReqDetailDto"));
        me.resetDetailToolbar(me.getCmp("prmPurchaseReqDetailDto"));
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subUpload',
            tooltip: '上传附件',
            iconCls: 'file_upload_icon',
            disabled: "true"
        });
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subDownload',
            tooltip: '下载附件',
            iconCls: 'file_download_icon'
            //disabled: "true"
        });
        me.getCmp("prmPurchaseReqDetailDto").getCmp("toolbar").add({
            cid: 'subDelfile',
            tooltip: '删除附件',
            iconCls: 'delete_btn',
            disabled: "true"
        });

        me.contractJump(me);

        var prmBudgetPrincipalCDtoGrid = me.getCmp("prmPurchaseReqDetailDto");
        prmBudgetPrincipalCDtoGrid.rowColorConfigFn = me.purchaseDetailRowColorConfigFn;
    },
    //M3_C5_F2_页面调
    //为增加行按钮设置事件,点击后弹出采购包选择界面
    resetDetailToolbar: function (planGrid) {
        planGrid.doAddRow = function (model) {
            var prmpurchasereqController = Scdp.getActiveModule().controller;
            prmpurchasereqController.pickChasereqPack();
        }
    },
    hideReqToolBar: function (reqGrid) {
        reqGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        reqGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        reqGrid.getCmp("toolbar->copyAddRowBtn").hide();
        //reqGrid.getCmp("toolbar->addRowBtn").hide();
    },

    contractJump: function (view) {
        openContract = function (uuid) {
            var postData = {};
            postData.erp_msg_business_obj = {uuid: uuid};
            var menuCode = "CONTRACTESTABLISHMENT";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        var prmPurchaseReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
        var scmContractCodeColumns = prmPurchaseReqDetailGrid.getColumnBydataIndex("scmContractCode");

        scmContractCodeColumns.renderer = function (value, p, record) {
            if (Scdp.ObjUtil.isNotEmpty(value)) {
                return Ext.String.format(
                        '<a href="javascript:openContract(\'' + record.data.scmContractId + '\');" style="color: blue">' + value + '</a>'
                );
            }

        };
    },
    collectMoreWorkFlowParamOnLoadAssignee: function () {
        var param = {};
        param.assignLoadUserMethod = 'default';
        return param;
    },
    purchaseDetailRowColorConfigFn: function (record) {
        if (record.data.isStamp == null) {
            return null;
        } else if (record.data.isStamp == "1" && Scdp.ObjUtil.isNotEmpty(record.data.stampProjectUuid)) {
            return 'erp-grid-font-color-green';
        } else {
            return 'erp-grid-font-color-red';
        }
        return null;
    }
});