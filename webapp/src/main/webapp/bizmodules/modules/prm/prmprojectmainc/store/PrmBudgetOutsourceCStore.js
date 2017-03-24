Ext.define("Prmprojectmainc.store.PrmBudgetOutsourceCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmBudgetOutsourceCModel',
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