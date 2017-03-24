Ext.define('Prmunknownreceipts.view.PrmunknownreceiptsView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmunknownreceipts',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 180,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmunknownreceipts-query-layout.xml',
    editLayoutFile: 'prmunknownreceipts-edit-layout.xml',
    //extraLayoutFile: 'prmunknownreceipts-extra-layout.xml',
    bindings: ['prmUnknownReceiptsDto', 'prmUnknownReceiptsDto.prmReceiptsDto'],
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
    }
});