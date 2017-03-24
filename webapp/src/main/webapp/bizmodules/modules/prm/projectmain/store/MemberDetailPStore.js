Ext.define("Projectmain.store.MemberDetailPStore", {
    extend: 'Ext.data.Store',
    model: 'Projectmain.model.MemberDetailPModel',
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