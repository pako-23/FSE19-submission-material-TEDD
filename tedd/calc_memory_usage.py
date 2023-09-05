import re
import os
import sys
import statistics

def select_files(directory, substring):
    files = os.listdir(directory)
    selected_files = [f for f in files if substring in f]
    return selected_files

def calculate_average_mem_usage(filename):
    mem_usage = {}
    counts = {}

    with open(filename, 'r') as f:
        for line in f:
            # Usa una regex per estrarre il PID e l'uso della memoria
            match = re.search(r'PID (\d+) - MEM_USAGE (\d+)', line)
            if match:
                pid = match.group(1)
                mem = int(match.group(2))


                if pid not in mem_usage:
                    mem_usage[pid] = []
                    counts[pid] = 0
                mem_usage[pid].append(mem)
                counts[pid] += 1
                
    
    count_max = 0
    pid_max = 0
    for pid in counts:
        if counts[pid] > count_max:
            count_max = counts[pid]
            pid_max = pid
    print( f'RUN: {filename} AVG_MEM_USAGE {int(sum(mem_usage[pid_max])/counts[pid_max])} kB - MDN_MEM_USAGE {statistics.median(mem_usage[pid_max])} kb - MIN_MEM_USAGE {min(mem_usage[pid_max])} - MAX_MEM_USAGE {max(mem_usage[pid_max])}' )
    # for pid in mem_usage:
    #     averages[pid] = int( mem_usage[pid] / counts[pid] )
    
    # name = filename.replace('results/', '')
    # name = name.replace('_memory_usage.log', '')
    # print(f'RUN: {name} PID {pid_max}: average MEM_USAGE {averages[pid_max]} kB COUNTS {counts[pid_max]}')


if __name__ == '__main__':
    
    selected_files = select_files('results', 'memory_usage.log')

    for file in selected_files:
        calculate_average_mem_usage(f'results/{file}')