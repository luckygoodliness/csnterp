Ext.define("Projectmain.store.PrmBudgetRunStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmBudgetRunModel',
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