Ext.define("Projectmain.store.PrmBudgetPrincipalStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmBudgetPrincipalModel',
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