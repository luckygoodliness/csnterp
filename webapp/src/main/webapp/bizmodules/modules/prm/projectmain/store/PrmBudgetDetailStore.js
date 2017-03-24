Ext.define("Projectmain.store.PrmBudgetDetailStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmBudgetDetailModel',
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