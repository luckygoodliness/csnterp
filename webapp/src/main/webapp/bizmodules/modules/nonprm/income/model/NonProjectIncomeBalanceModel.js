Ext.define("Income.model.NonProjectIncomeBalanceModel", {
    extend: 'Ext.data.Model',
    fields: [
        'tit',
        'uuid',
        'year',
        'lastAppliedValue',
        'firstInstance',
        'appliedValue',
        'assignedValue',
        'occurredValue'
    ]});