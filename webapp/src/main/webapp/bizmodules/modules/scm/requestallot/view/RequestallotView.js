Ext.define('Requestallot.view.RequestallotView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/requestallot',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'requestallot-query-layout.xml',
    editLayoutFile: 'requestallot-edit-layout.xml',
    //extraLayoutFile: 'requestallot-extra-layout.xml',
    bindings: ['scmPurchaseReqDto', 'scmPurchaseReqDto.prmPurchaseReqDetailDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        //去除新增，复制新增，删除按钮,批量删除按钮
        me.getCmp("addNew2Btn").setVisible(false);
        me.getCmp("copyAddBtn").setVisible(false);
        me.getCmp("deleteBtn").setVisible(false);
        me.getCmp("batchDelBtn").setVisible(false);
        me.getCmp("scmPurchaseReqDto->purchaseReqNo").inputEl.on('click', me.gotoPrmPurchaseReq);
        var prmPurchaseReqDetailGrid = me.getCmp("prmPurchaseReqDetailDto");
        prmPurchaseReqDetailGrid.getCmp("toolbar->addRowBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->delRowBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        prmPurchaseReqDetailGrid.getCmp("toolbar->copyAddRowBtn").hide();
        //2.添加查询区的工具按钮
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '批量设置',
            cid: 'batchSet',
            iconCls: 'setting_btn'
            //disabled: "true"
        });
    },

    gotoPrmPurchaseReq: function () {
        var me = this;
        var prmPurchaseReqIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
        var prmPurchaseReqId = Ext.getCmp(prmPurchaseReqIdPlusId).up("IView").getCmp("scmPurchaseReqDto->prmPurchaseReqId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmPurchaseReqId)) {
            var param = {uuid: prmPurchaseReqId};
            var isProject = Ext.getCmp(prmPurchaseReqIdPlusId).up("IView").getCmp("scmPurchaseReqDto->isProject").gotValue();
            var menuCode = '';
            if(isProject=="1"){
                menuCode = 'PRMPURCHASEREQ';
            }else{
                menuCode = 'NONPRMPURCHASEREQ';
            }
            Scdp.openNewModuleByMenuCode(menuCode, {erp_msg_business_obj: param}, "MENU_ITEM_CTL", true);
        }
    }
});