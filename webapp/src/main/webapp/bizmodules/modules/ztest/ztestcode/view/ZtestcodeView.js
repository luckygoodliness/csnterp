Ext.define('Ztestcode.view.ZtestcodeView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/ztest/ztestcode',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'ztestcode-query-layout.xml',
	editLayoutFile: 'ztestcode-edit-layout.xml',
	//extraLayoutFile: 'ztestcode-extra-layout.xml',
	bindings: ['ztrainMainDto','ztrainMainDto.ztrainMainContentDto'],
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
		//M3_C23_F1_归档明细导入
		var editToolbar = me.getEditToolbar();
		editToolbar.add(
			{
				text: '打印申请单',
				cid: 'excelSubmit',
				iconCls: 'printer_icon_16'
			});

	}


});