#Check if a folder contains a subfolder named "test" recursively
# If it does, then print the path of the folder and the subfolder

import os

if __name__ == '__main__':
    for root, dirs, files in os.walk('repos'):
        if 'test' in dirs:
            print(root)
            print(dirs)
            print(files)
            print()
    