import matplotlib.pyplot as plt
import numpy as np
import sys 

algos = ['pradet', 'ex-linear', 'big-table']
ts_time = {
    'ppma': {
        'label': 'ppma (23 tests)',
        'data': [261058,163018,153926],
    },
    'claroline': {
        'label': 'claroline (40 tests)',
        'data': [489300,179013,842070], #big-table out of memory
    },
    'addressbook': {
        'label': 'addressbook (28 tests)',
        'data': [251784, 168729, 522703],
    },
    'mantisbt': {
        'label': 'mantisbt (41 tests)',
        'data': [272704,179787,0],
    },
    'mrbs': {
        'label': 'mrbs (24 tests)',
        'data': [232646, 164015, 0],
    }
}

for ts in ts_time:
    print( ts, ts_time[ts] )
    plt.figure()
    plt.bar(range(len(ts_time[ts]['data'])), ts_time[ts]['data'], tick_label=algos, edgecolor='black')
    plt.title(ts_time[ts]['label'])
    plt.xlabel("Algorithm")
    plt.ylabel("Memory (kb)")
    plt.savefig(f"pdf/hist_mem_{ts}.pdf")
    
