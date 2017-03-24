Ext.define('Collectionmeasure.view.CollectionmeasureView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/prm/collectionmeasure',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 80,
	epHeight: 160,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'collectionmeasure-query-layout.xml',
	editLayoutFile: 'collectionmeasure-edit-layout.xml',
	//extraLayoutFile: 'collectionmeasure-extra-layout.xml',
	bindings: ['prmCollectionMeasureDto'],
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