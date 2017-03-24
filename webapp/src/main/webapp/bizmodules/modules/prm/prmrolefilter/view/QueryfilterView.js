Ext.define('Prmrolefilter.view.QueryfilterView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmrolefilter',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 0,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    isPopup:true,
    queryLayoutFile: 'queryfilter-search-layout.xml',
//    editLayoutFile: 'queryfilter-tmp-layout.xml',
    //extraLayoutFile: 'queryfilter-extra-layout.xml',
    bindings: ['scdpQueryFilterDto'],
    canEdit: false,
    enableColumnMove: false,
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