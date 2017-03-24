Ext.define("Prmprojectmainc.store.PrmBudgetPrincipalCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmBudgetPrincipalCModel',
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