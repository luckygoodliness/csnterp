Ext.define('Prmprojectmainc.view.SquaremainpcView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmprojectmainc',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 255,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'prm-projectmainc2-query-layout.xml',
	editLayoutFile: 'prm-squaremainpc-edit-layout.xml',
	//extraLayoutFile: 'squaremainpc-extra-layout.xml',
	bindings: ['prmProjectMainCDto', 'prmProjectMainCDto.prmSquareDetailPCDto'],
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
		me.getCmp("conditionPanel->detailType").sotValue("PRM_SQUARE_DETAIL");

		var squareGrid = me.getCmp("prmSquareDetailPCDto");
		squareGrid.afterEditGrid = me.changeSquareDetail;
	},
	UIStatusChanged: function (view, newStatus) {
		view.getCmp("prmProjectMainCDto").sotEditable(false);
	},
	changeSquareDetail: function (eventObj, isChanged) {
		if (!isChanged) {
			return;
		}

		var grid = eventObj.grid;
		var record = eventObj.record;
		var field = eventObj.field;

		if (!(field == "schemingSquareMoney" || field == "schemingSquareCost")) {
			return;
		}
		var amount1 = record.get("schemingSquareMoney");
		if (Scdp.ObjUtil.isEmpty(amount1)) {
			amount1 = 0;
		}
		var amount2 = record.get("schemingSquareCost");
		if (Scdp.ObjUtil.isEmpty(amount2)) {
			amount2 = 0;
		}
		record.set("schemingSquareProfits", amount1 - amount2);
	}
});