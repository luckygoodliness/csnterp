Ext.define('Prmbilling.view.PrmbillingDialogView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmbilling',
    //aHeight: 1500,  //指定内容面板高度
    aWidth: 1000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 0,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmbilling-query-layout.xml',
    bindings: ['prmBillingDto'],
    canEdit: false,
    enableColumnMove: true,
    showHeaderCheckbox: false,
    needSplitPage: true,
    selMode: "SINGLE",
    withSelMode: false,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }

});