Ext.define("Monitory.store.NonBudgetMonitorYStore", {
    extend: 'Ext.data.Store',
    model: 'Monitory.model.NonBudgetMonitorYModel',
    autoLoad: false,
    proxy: {
        type: 'memory',
        reader: {
            type: 'json'
        }
    }
});