Ext.define('Goodsarrival.view.NewgoodsarrivalView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/goodsarrival',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 225,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'newgoodsarrival-query-layout.xml',
    editLayoutFile: 'newgoodsarrival-edit-layout.xml',
    //extraLayoutFile: 'goodsarrival-extra-layout.xml',
    bindings: ['scmContractDetailDto', 'scmContractDetailDto.prmGoodsArrivalDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
        var params = me.controller.actionParams;
        var conditionPanel = me.getConditionPanel();
        if(Scdp.ObjUtil.isNotEmpty(params) && Scdp.ObjUtil.isNotEmpty(params.isProject)){
            conditionPanel.queryExtraParams = {projectfilter:" C.is_project = " + params.isProject+" "}
        }
    },
    afterInit: function () {
        var me = this;
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '确认到货',
            cid: 'goodsArrival',
            iconCls: 'temp_icon_16'
        });
        editToolbar.add({
            text: '批量确认到货',
            cid: 'batchGoodsArrival',
            iconCls: 'temp_icon_16'
        });
        me.resetEditToolbar();
    },
    resetEditToolbar: function () {
        var me = this;
        this.getCmp('scmContractDetailDto');
        me.getCmp('editToolbar->addNew2Btn').setVisible(false);
        me.getCmp('editToolbar->copyAddBtn').setVisible(false);
//        me.getCmp('editToolbar->modifyBtn').setVisible(false);
        me.getCmp('editToolbar->deleteBtn').setVisible(false);
//        me.getCmp('editToolbar->cancelBtn').setVisible(false);
//        me.getCmp('editToolbar->saveBtn').setVisible(false);
        var prmGoodsArrivalGrid = me.getCmp("prmGoodsArrivalDto");
        prmGoodsArrivalGrid.getCmp("toolbar->addRowBtn").hide();
//        prmGoodsArrivalGrid.getCmp("toolbar->delRowBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        prmGoodsArrivalGrid.getCmp("toolbar->copyAddRowBtn").hide();
    }
});