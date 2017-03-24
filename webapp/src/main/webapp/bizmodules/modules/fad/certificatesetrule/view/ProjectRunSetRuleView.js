Ext.define('Certificatesetrule.view.ProjectRunSetRuleView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificatesetrule',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 140,
    epHeight: 170,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'projectrunsetrule-query-layout.xml',
    editLayoutFile: 'projectrunsetrule-edit-layout.xml',
    //extraLayoutFile: 'certificatesetrule-extra-layout.xml',
    bindings: ['fadProjectRunSetRuleDto'],
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