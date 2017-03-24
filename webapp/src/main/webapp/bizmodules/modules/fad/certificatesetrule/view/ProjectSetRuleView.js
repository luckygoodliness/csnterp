Ext.define('Certificatesetrule.view.ProjectSetRuleView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificatesetrule',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 120,
    epHeight: 150,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'projectsetrule-query-layout.xml',
    editLayoutFile: 'projectsetrule-edit-layout.xml',
    //extraLayoutFile: 'certificatesetrule-extra-layout.xml',
    bindings: ['fadProjectSetRuleDto'],
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