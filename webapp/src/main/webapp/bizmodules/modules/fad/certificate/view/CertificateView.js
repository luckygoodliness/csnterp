Ext.define('Certificate.view.CertificateView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificate',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 140,
    //epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'certificate-query-layout.xml',
    editLayoutFile: 'certificate-edit-layout.xml',
    //extraLayoutFile: 'certificate-extra-layout.xml',
    bindings: ['fadCertificateDto', 'fadCertificateDto.fadCertificateDetailDto'],
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

        var queryToolbar = me.getQueryToolbar();
        queryToolbar.add({
            text: '合并凭证',
            cid: 'certificateMerge',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '拆分凭证',
            cid: 'certificateSplit',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '复原合并凭证',
            cid: 'certificateMergeRestore',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '复原拆分凭证',
            cid: 'certificateSplitRestore',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '生成红冲凭证',
            cid: 'certificateDeficit',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        queryToolbar.add({
            text: '发送NC',
            cid: 'batchSendNC',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });

        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '发送NC',
            cid: 'sendNC',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '作废',
            cid: 'toLogicVoid',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '消退',
            cid: 'toLogicVoidAlone',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '业务',
            cid: 'toBusiness',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '【NCCode】设定',
            cid: 'nCCodeSet',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '关联收款',
            cid: 'Collection',
            iconCls: 'temp_icon_16',
            disabled: 'true'
        });
        if (Scdp.ObjUtil.isNotEmpty(me.controller.actionParams)) {
            if (Scdp.ObjUtil.isNotEmpty(me.controller.actionParams.fadCertificateUuid)) {
                me.controller.loadItem(me.controller.actionParams.fadCertificateUuid);
                me.controller.afterLoadItem();
            }
        }
    }
});