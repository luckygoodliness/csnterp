Ext.define("Projectmain.store.PrmBudgetAccessoryStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmBudgetAccessoryModel',
    autoLoad: false,
    sorters: [
        {
            property: 'serialNumber',
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