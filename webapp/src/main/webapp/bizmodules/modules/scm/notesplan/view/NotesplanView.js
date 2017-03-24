Ext.define('Notesplan.view.NotesplanView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/notesplan',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'notesplan-query-layout.xml',
    editLayoutFile: 'notesplan-edit-layout.xml',
    //extraLayoutFile: 'notesplan-extra-layout.xml',
    bindings: ['scmNotesPlanDto', 'scmNotesPlanDto.scmNotesPlanDetailDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        //1.编辑区工具条
        var editToolbar = me.getEditToolbar();
        //增加“记录时间按钮”
        editToolbar.add({
            text: '已催办',
            cid: 'markTime',
            iconCls: 'ok_btn'
        });
        //隐藏“新增”，“复制新增”，“保存”，“删除”按钮
        me.getCmp("addNew2Btn").hide();
        me.getCmp("copyAddBtn").hide();
        //me.getCmp("saveBtn").hide();
        me.getCmp("deleteBtn").hide();
        //2.查询区工具条
        //增加“新增下一期”按钮
        var queryToolbar = me.getQueryToolbar();
        queryToolbar.add({
            text: '新增下一期',
            cid: 'addLastIssue',
            iconCls: 'add_btn'
        });
        queryToolbar.add({
            text: '锁定',
            cid: 'lockIssue',
            iconCls: 'add_btn'
        });
        //隐藏“新增”，“删除”按钮
        me.getCmp("queryPanel->addNew1Btn").hide();
        me.getCmp("queryPanel->batchDelBtn").hide();
        //me.initDetailGrid();
    }
    //initDetailGrid: function () {
    //    var me = this;
    //    var detailGrid = me.getCmp("scmNotesPlanDetailDto");
    //    detailGrid.afterEditGrid = me.changeDetail;
    //},
    //changeDetail: function (eventObj, isChanged) {
    //    if (!isChanged) {
    //        return;
    //    }
    //    var field = eventObj.field;
    //    var record = eventObj.record;
    //    if (field == "conclusionLineOutName" || field == "conclusionLineFinancialName") {
    //        record.set("scmContractCode", record.get("scmContractCodeName"));
    //    }
    //}
});