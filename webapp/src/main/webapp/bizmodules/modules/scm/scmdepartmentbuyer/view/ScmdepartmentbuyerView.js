Ext.define('Scmdepartmentbuyer.view.ScmdepartmentbuyerView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmdepartmentbuyer',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 150,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmdepartmentbuyer-query-layout.xml',
    editLayoutFile: 'scmdepartmentbuyer-edit-layout.xml',
    //extraLayoutFile: 'scmdepartmentbuyer-extra-layout.xml',
    bindings: ['scmDepartmentBuyerDto'],
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
        //var principalCmp = me.getCmp("scmDepartmentBuyerDto->buyerName");
        //principalCmp.filterConditions = {orgfilter: " office_code='" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "' "};
    }
});