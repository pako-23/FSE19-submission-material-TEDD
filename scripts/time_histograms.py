import matplotlib.pyplot as plt
import numpy as np
import sys 

algos = ['pradet', 'ex-linear', 'big-table']
ts_time = {
    'ppma': {
        'label': 'ppma (23 tests)',
        'data': [6957, 2805, 6778],
    },
    'claroline': {
        'label': 'claroline (40 tests)',
        'data': [27685,9341,-1], #big-table out of memory
    },
    'addressbook': {
        'label': 'addressbook (28 tests)',
        'data': [6296,2320,4800],
    },
    'mantisbt': {
        'label': 'mantisbt (41 tests)',
        'data': [37766,12817,27067],
    },
    'mrbs': {
        'label': 'mrbs (24 tests)',
        'data': [6034,3143,2382],
    }
}

for ts in ts_time:
    print( ts, ts_time[ts] )
    plt.figure()
    plt.bar(range(len(ts_time[ts]['data'])), ts_time[ts]['data'], tick_label=algos, edgecolor='black')
    plt.title(ts_time[ts]['label'])
    plt.xlabel("Algorithm")
    plt.ylabel("Time (s)")
    plt.savefig(f"pdf/hist_{ts}.pdf")
    
