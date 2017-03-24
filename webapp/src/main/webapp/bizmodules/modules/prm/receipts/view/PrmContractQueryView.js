Ext.define('Receipts.view.PrmContractQueryView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/receipts',
    aHeight: 480,  //指定内容面板高度
    aWidth: 900,  //指定内容面板宽度
    cpHeight: 100,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'contract-query-layout.xml',
    hideScroll: true,
    canEdit: false,
    afterInit: function () {
        var view = this;
        view.up('window').dockedItems.items[1].items.items[3].setText("关闭");
        var conditionPanel = view.getConditionPanel();
        var extend = " 1=1 "
        if ( !Scdp.ObjUtil.isEmpty(view.controller.actionParams) ) {
            if(!Scdp.ObjUtil.isEmpty(view.controller.actionParams.selectId))
                extend += " and SCES.uuid not in ("+view.controller.actionParams.selectId+") "
            if(!Scdp.ObjUtil.isEmpty(view.controller.actionParams.payer))
                extend += " and SCES.SUPPLIER_NAME = '"+view.controller.actionParams.payer+"' "
            conditionPanel.queryExtraParams = {extend: extend};
        }else{
            conditionPanel.queryExtraParams = {extend: " 1=1 "};
        }
    }
});