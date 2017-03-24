/**
 * Created by lijx on 2016/9/7.
 */
Ext.define('Scmsaeapproval.view.ScmsaeapprovalQueryView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmsaeapproval',
    aHeight: 500,  //指定内容面板高度
    aWidth: 900,  //指定内容面板宽度
    cpHeight: 45,
    layoutFile: "scmsaeapprovalquery-layout.xml",
    bindings: ['scmSaeResultDto', 'scmSaeResultDto.scmSaeObjectDto'],
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});