Ext.define("Notesplan.store.ScmNotesPlanDetailStore", {
    extend: 'Ext.data.Store',
    model: 'Notesplan.model.ScmNotesPlanDetailModel',
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