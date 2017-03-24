Ext.define("Prmrolefilter.store.ScdpQueryFilterStore", {
    extend: 'Ext.data.Store',
    model: 'Prmrolefilter.model.ScdpQueryFilterModel',
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