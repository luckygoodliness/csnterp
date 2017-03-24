Ext.define('Card.view.AssetCardView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/asset/card',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'card-query-layout.xml',
    editLayoutFile: 'card-edit-layout.xml',
    bindings: ['assetCardDto',
        'assetCardDto.cdmFileRelationDto',
        'assetCardDto.assetAnnualCheckDto',
        'assetCardDto.assetMaintainDto',
        'assetCardDto.assetTransferDto'
    ],

    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    scroll: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var editToolbar = me.getEditToolbar();

        editToolbar.add({
            text: '设备转移',
            cid: 'btnCardTransfer',
            iconCls: 'add_btn'
        });
        me.getCmp("editPanel->editToolbar->btnCardTransfer").setDisabled(true);

        editToolbar.add({
            text: '年检提醒',
            cid: 'btnYearCheckRemind',
            iconCls: 'add_btn'
        });
        me.getCmp("editPanel->editToolbar->btnYearCheckRemind").setDisabled(true);

        editToolbar.add({
            text: '保险到期',
            cid: 'btnInsuranceMaturity',
            iconCls: 'add_btn'
        });
        me.getCmp("editPanel->editToolbar->btnInsuranceMaturity").setDisabled(true);

        editToolbar.add({
            text: '检定提醒',
            cid: 'btnMustCheckRemind',
            iconCls: 'add_btn'
        });
        me.getCmp("editPanel->editToolbar->btnMustCheckRemind").setDisabled(true);

        me.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;
    },
    rowColorConfigFn: function (record) {
        if ((record.data.state == '已审核') && (record.data.status == '在用')) {
            var currentDate = new Date();
            currentDate.setDate(currentDate.getDate() + 30);

            if (record.data.annualCheckExpiredDate) {
                var annualCheckExpiredDate = new Date(record.data.annualCheckExpiredDate.substr(0, 10));
                if (currentDate.getTime() >= annualCheckExpiredDate.getTime()) {
                    return 'erp-grid-font-color-red';
                }
            }

            if (record.data.insuranceExpiredDate) {
                var insuranceExpiredDate = new Date(record.data.insuranceExpiredDate.substr(0, 10));
                if (currentDate.getTime() >= insuranceExpiredDate.getTime()) {
                    return 'erp-grid-font-color-red';
                }
            }

            if (record.data.checkedDate) {
                var checkedDate = new Date(record.data.checkedDate.substr(0, 10));
                if (currentDate.getTime() >= checkedDate.getTime()) {
                    return 'erp-grid-font-color-red';
                }
            }
        }
        return null;
    },
    UIStatusChanged: function (view, uistatus) {
        var me = this;
        var btnCardTransfer = view.getCmp("editPanel->editToolbar->btnCardTransfer");
        var state = view.getCmp("assetCardDto->state").gotValue();

        if (btnCardTransfer) {
            btnCardTransfer.setDisabled(true);
            if (state == '2') {
                if (
                    !(
                        uistatus == Scdp.Const.UI_INFO_STATUS_NEW
                        || uistatus == Scdp.Const.UI_INFO_STATUS_NULL
                        || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
                    )
                ) {
                    btnCardTransfer.setDisabled(false);
                }
            }
        }
    }
});