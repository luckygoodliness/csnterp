Ext.define('Goodsarrival.view.NewgoodsarrivalView2', {
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
    },
    afterInit: function () {
        var me = this;
        me.getCmp("addNew1Btn").setVisible(false);
        me.getCmp("addNew2Btn").setVisible(false);
        me.getCmp("copyAddBtn").setVisible(false);
        me.getCmp("modifyBtn").setVisible(false);
        me.getCmp("deleteBtn").setVisible(false);
        me.getCmp("cancelBtn").setVisible(false);
        me.getCmp("saveBtn").setVisible(false);
    }
});