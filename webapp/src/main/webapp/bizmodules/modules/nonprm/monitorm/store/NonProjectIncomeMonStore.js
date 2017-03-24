Ext.define("Monitorm.store.NonProjectIncomeMonStore", {
    extend: 'Ext.data.Store',
    model: 'Monitorm.model.NonProjectIncomeMonModel',
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