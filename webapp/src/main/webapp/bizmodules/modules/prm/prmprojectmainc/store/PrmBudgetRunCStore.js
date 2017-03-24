Ext.define("Prmprojectmainc.store.PrmBudgetRunCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmBudgetRunCModel',
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