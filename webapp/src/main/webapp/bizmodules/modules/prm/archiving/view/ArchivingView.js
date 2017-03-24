Ext.define('Archiving.view.ArchivingView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/archiving',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'archiving-query-layout.xml',
	editLayoutFile: 'archiving-edit-layout.xml',
	//extraLayoutFile: 'archiving-extra-layout.xml',
	bindings: ['prmArchivingDto','prmArchivingDto.prmArchivingDetailDto','prmArchivingDto.cdmFileRelationDto'],
	canEdit: true,
	enableColumnMove: true,
	showHeaderCheckbox: true,
	needSplitPage: true,
	workFlowDefinitionKey:'Prm_Archiving_Detail',
	initComponent: function () {
		var me = this;
		this.callParent(arguments);
	},
	afterInit: function () {
		var me = this;

		me.getCmp("prmArchivingDetailDto->rowMoveTopBtn").setVisible(false);
		me.getCmp("prmArchivingDetailDto->rowMoveBottomBtn").setVisible(false);
		me.getCmp("prmArchivingDetailDto->rowMoveUpBtn").setVisible(false);
		me.getCmp("prmArchivingDetailDto->rowMoveDownBtn").setVisible(false);

		//M3_C23_F1_归档明细导入
		me.getCmp("prmArchivingDetailDto").getCmp("toolbar").add({
			cid: 'excelSubmit',
			tooltip: 'excel导入明细',
			iconCls: 'file_upload_icon',
			disabled: "true"
		});

	},
    UIStatusChanged:function(view, uistatus){
        if (uistatus == Scdp.Const.UI_INFO_STATUS_NEW
            || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
            view.getCmp("fileUpload").setDisabled(false);
            view.getCmp("fileDelete").setDisabled(false);
        } else {
            view.getCmp("fileUpload").setDisabled(true);
            view.getCmp("fileDelete").setDisabled(true);
        }
    }
});