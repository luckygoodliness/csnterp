Ext.define("Prmprojectmainc.store.PrmBudgetAccessoryCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmBudgetAccessoryCModel',
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