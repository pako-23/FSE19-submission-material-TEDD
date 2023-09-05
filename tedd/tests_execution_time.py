import os

algos = ['ex-linear', 'pradet', 'big-table']
testsuites = ['addressbook', 'claroline', 'mantisbt', 'mrbs']

for ts in testsuites:
    for algo in algos:
        try:
            print(f"Running {algo}-{ts}")
            os.system(f"./run-app.sh -a {algo} -t {ts} > results/{algo}-{ts}-results.txt 2>&1")
        except:
            print(f"Error: {algo}-{ts}")


