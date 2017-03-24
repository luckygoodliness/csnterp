Ext.define('Operatekeyprojectsinfo.view.QueryOperatekeyprojectsinfoView', {
	extend: 'Scdp.mvc.AbstractCrudView_1',
	modulePath: 'com/csnt/scdp/bizmodules/modules/operate/operatekeyprojectsinfo',
	aHeight: 480,//aHeight: 1500,  //指定内容面板高度
	aWidth: 900,//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 0,
	allowNullConditions: true,
	queryLayoutFile: 'operatekeyprojectsinfo-grid-query-layout.xml',
	hideScroll: true,
	canEdit: false,

});
