Ext.define('Bidcontractplan.view.BidcontractplanView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/operate/bidcontractplan',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
//    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'bidcontractplan-query-layout.xml',
    editLayoutFile: 'bidcontractplan-edit-layout.xml',
    //extraLayoutFile: 'bidcontractplan-extra-layout.xml',
    bindings: ['operateBusinessBidInfoDto', 'operateBusinessBidInfoDto.cdmFileRelationDto'],
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
        me.getCmp("editPanel->copyAddBtn").setVisible(false);

        var fileGrid = me.getCmp("cdmFileRelationDto");
        me.getCmp("queryPanel->projectType").sotValue("2");
        me.getCmp("queryPanel->countryCode").sotValue("CN");
        me.getCmp("queryPanel->projectType").originalValue = me.getCmp("queryPanel->projectType").gotValue();
        me.getCmp("queryPanel->countryCode").originalValue = me.getCmp("queryPanel->countryCode").gotValue();
        var fileToolbar = fileGrid.getEditToolbar();
        me.getCmp("cdmFileRelationDto->rowMoveTopBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveTopBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveBottomBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveUpBtn").setVisible(false);
        me.getCmp("cdmFileRelationDto->rowMoveDownBtn").setVisible(false);

        fileToolbar.add({
            xtype: 'button',
            cid: 'fileDownload',
            iconCls: 'file_download_icon'
        });

        fileGrid.doAddRow = function () {
            var me = this;
            Erp.FileUtil.erpFileUpload(me);
        };

//        fileGrid.doDeleteRow = function () {
//            var me = this;
//            Erp.FileUtil.erpFileDownLoad(me);
//        }
    },
    UIStatusChanged: function (view, newStatus) {
        var fileDownloadBtn = view.getCmp("cdmFileRelationDto->fileDownload");
        if (fileDownloadBtn) {
            fileDownloadBtn.setDisabled(false);
        }
    }
});