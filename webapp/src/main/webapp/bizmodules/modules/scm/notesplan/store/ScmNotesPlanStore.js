Ext.define("Notesplan.store.ScmNotesPlanStore", {
    extend: 'Ext.data.Store',
    model: 'Notesplan.model.ScmNotesPlanModel',
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