Ext.define("Cashreq.store.FadCashReqBudgetStore", {
    extend: 'Ext.data.Store',
    model: 'Cashreq.model.FadCashReqBudgetModel',
    autoLoad: false,
    sorters: [
        {
            property: 'seqNo',
            direction: 'ASC'
        }
    ],
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});