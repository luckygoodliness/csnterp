Ext.define('WorkflowDelete.view.WorkflowDeleteView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/workflow/delete',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'workflowdelete-query-layout.xml',
    editLayoutFile: 'workflowdelete-edit-layout.xml',
    //extraLayoutFile: 'workflowdelete-extra-layout.xml',
    bindings: ['workFlowDeleteDto'],
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
		me.initEditToolbar();
    },

    initEditToolbar: function () {
        var me = this;
        me.getCmp("editPanel->modifyBtn").setDisabled(true);
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.getCmp("editPanel->deleteBtn").setVisible(false);
    },
});