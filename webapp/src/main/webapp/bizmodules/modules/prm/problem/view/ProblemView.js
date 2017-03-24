Ext.define('Problem.view.ProblemView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/problem',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 60,
	epHeight: 230,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'problem-query-layout.xml',
	editLayoutFile: 'problem-edit-layout.xml',
	//extraLayoutFile: 'problem-extra-layout.xml',
	bindings: ['prmProblemDto','prmProblemDto.prmProblemFeedbackDto'],
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
            text: '问题处理',
            cid: 'btnProblem',
            iconCls: 'temp_icon_16',
            disabled:true
        });
        editToolbar.add({
            text: '关闭问题',
            cid: 'colseProblem',
            iconCls: 'temp_icon_16',
            disabled:true
        });
	}
});