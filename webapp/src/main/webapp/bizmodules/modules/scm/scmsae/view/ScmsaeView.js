Ext.define('Scmsae.view.ScmsaeView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsae',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'scmsae-query-layout.xml',
	editLayoutFile: 'scmsae-edit-layout.xml',
	//extraLayoutFile: 'scmsae-extra-layout.xml',
	bindings: ['scmSaeDto','scmSaeDto.scmSaeObjectDto'],
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

		var editToolbar = me.getEditToolbar();
		editToolbar.add({
			text:'生成评价人员列表',
			cid:'createScmSaeForm',
			iconCls:'temp_icon_16',
			disabled:'true'
		});
		editToolbar.add({
			text:'生成考评任务',
			cid:'createScmSaeTask',
			iconCls:'temp_icon_16',
			disabled:'true'
		});

		editToolbar.add({
			text:'计算考评结果',
			cid:'computeSaeResult',
			iconCls:'temp_icon_16',
			disabled:'true'
		});

		//考评类型过滤
		var sCaseQueryCmp=me.getCmp("scmSaeDto->saiCase");
		 sCaseQueryCmp.filterConditions={selfconditions:" case_type='0' and isactive = 1"};

		 var wCaseQueryCmp=me.getCmp("scmSaeDto->waiCase");
		 wCaseQueryCmp.filterConditions={selfconditions:" case_type='1' and isactive = 1"};

		//复制新增按钮隐藏
		me.getCmp("editPanel->copyAddBtn").hide();
		me.resetDetailToolbar();
	},
	resetDetailToolbar: function () {
		var me = this;
		var scmSaeObjectGrid = me.getCmp("scmSaeObjectDto");
		var scmSaeFormGrid = me.getCmp("scmSaeFormDto");
		scmSaeObjectGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		scmSaeObjectGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		scmSaeObjectGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		scmSaeObjectGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		scmSaeObjectGrid.getCmp("toolbar->copyAddRowBtn").hide();
		scmSaeObjectGrid.doAddRow = function (model) {
			var scmSaeObjectController = Scdp.getActiveModule().controller;
			scmSaeObjectController.pickSaeObject();
		};
		scmSaeObjectGrid.beforeDeleteRow = function(model) {
			var flag = true;
			Ext.each(scmSaeFormGrid.getStore().data.items, function (item) {
				if("未发放" != item.data.state) {
					flag = false;
					return false;
				}
			});
			if(!flag) {
				Scdp.MsgUtil.info("存在不是未发放的数据,不允许删除！");
				return false;
			}
		};

		scmSaeObjectGrid.getCmp("toolbar").add({
			cid: 'addBySupplier',
			tooltip: '智能添加',
			iconCls: 'two_row_add_btn',
			disabled: "true"
		});
		scmSaeFormGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		scmSaeFormGrid.getCmp("toolbar->copyAddRowBtn").hide();
		scmSaeFormGrid.beforeAddRow = function (model) {
			model.state = "未发放";
		};

		scmSaeFormGrid.beforeDeleteRow = function(model) {
			if (model[0].data.state == "已发放" || model[0].data.state == "已评价") {
				Scdp.MsgUtil.info("考评任务已发放,不允许删除！");
				return false;
			}
		};

	}
});