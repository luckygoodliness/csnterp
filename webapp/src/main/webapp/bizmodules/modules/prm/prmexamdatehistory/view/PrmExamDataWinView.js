Ext.define('PrmExamdateHistory.view.PrmExamDataWinView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmexamdatehistory',
    aHeight: 100,  //指定内容面板高度
    aWidth: 500,  //指定内容面板宽度
    layoutFile: 'wind-prmexamdate-edit-layout.xml',
    hideScroll: true,
    afterInit: function () {
        var me = this;
    }
});
