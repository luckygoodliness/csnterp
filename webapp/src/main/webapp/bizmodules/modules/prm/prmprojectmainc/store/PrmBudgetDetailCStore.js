Ext.define("Prmprojectmainc.store.PrmBudgetDetailCStore", {
    extend: 'Ext.data.Store',
    model: 'Prmprojectmainc.model.PrmBudgetDetailCModel',
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