Ext.define('Certificatesetrule.view.NonProjectSetRuleView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificatesetrule',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 110,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'nonprojectsetrule-query-layout.xml',
    editLayoutFile: 'nonprojectsetrule-edit-layout.xml',
    //extraLayoutFile: 'certificatesetrule-extra-layout.xml',
    bindings: ['fadNonProjectSetRuleDto'],
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