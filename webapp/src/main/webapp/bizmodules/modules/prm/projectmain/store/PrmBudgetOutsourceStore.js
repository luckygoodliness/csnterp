Ext.define("Projectmain.store.PrmBudgetOutsourceStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.PrmBudgetOutsourceModel',
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