Ext.define("Finalestimate.model.FinalEstimateHistoryModel", {
        extend: 'Ext.data.Model',
        fields: ['uuid', 'prmProjectMainId', 'totalSquareMoney', 'plannedCost', 'plannedProfit', 'projectActualMoneySum', 'plannedProfitPercent',
            'plannedProfitPercent', 'squareMoneySum', 'squareCostSum', 'squareGrossProfitSum', 'raxSum', 'registeredMoney',
            'registeredCost', 'registeredProfit', 'registeredReceiveMoney', 'lockedBudgetSum', 'seqNo']
    }
);