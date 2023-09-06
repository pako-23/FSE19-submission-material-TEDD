#Check if a folder contains a subfolder named "test" recursively
# If it does, then print the path of the folder and the subfolder

import os

if __name__ == '__main__':
    repos = []
    for root, dirs, files in os.walk('repos'):
        if 'test' in dirs:
            repo = root.replace('repos/', '').split('/')[0]+'/'+root.replace('repos/', '').split('/')[1]
            if repo not in repos:
                repos.append(repo)
                print( repo )
    