Ext.define('Prmorgrolefilter.view.PrmorgrolefilterView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmorgrolefilter',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 50,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prmorgrolefilter-query-layout.xml',
	editLayoutFile: 'prmorgrolefilter-edit-layout.xml',
	//extraLayoutFile: 'prmorgrolefilter-extra-layout.xml',
	bindings: ['scdpOrgDto', 'scdpOrgDto.prmOrgRoleFilterDto'],
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
        me.hideGridToolBar(me.getCmp("prmOrgRoleFilterDto"));

        me.getEditToolbar().items.items[0].hide();
        me.getEditToolbar().items.items[1].hide();
        me.getEditToolbar().items.items[3].hide();
	},

    hideGridToolBar: function (Grid) {
        Grid.getCmp("toolbar->rowMoveDownBtn").hide();
        Grid.getCmp("toolbar->rowMoveUpBtn").hide();
        Grid.getCmp("toolbar->rowMoveTopBtn").hide();
        Grid.getCmp("toolbar->rowMoveBottomBtn").hide();
        Grid.getCmp("toolbar->copyAddRowBtn").hide();
//        Grid.getCmp("toolbar->addRowBtn").hide();
    }
});