import os
import glob

def find_dockerfiles_in_repositories(root_path):
    # List all the directories inside the root_path
    repo_dirs = [d for d in os.listdir(root_path) if os.path.isdir(os.path.join(root_path, d))]
    
    dockerfiles_found = []
    
    # Iterate through each repository directory
    for repo in repo_dirs:
        repo_path = os.path.join(root_path, repo)
        
        # Use glob to find Dockerfile at any depth inside the repository directory
        dockerfiles = glob.glob(os.path.join(repo_path, '**', 'Dockerfile'), recursive=True)
        
        if dockerfiles:
            dockerfiles_found.extend(dockerfiles)
    
    return dockerfiles_found


if __name__ == '__main__':
    repos = []
    for root, dirs, files in os.walk('repos'):
        if root.startswith('repos/archive'):
            continue
        if 'test' in dirs:
            repo = root.replace('repos/', '').split('/')[0]+'/'+root.replace('repos/', '').split('/')[1]
            if repo not in repos:
                root_path = f"repos/{repo}"
                dockerfiles = find_dockerfiles_in_repositories(root_path)
                if len(dockerfiles) > 0:
                    print( repo )
                    repos.append(repo)
                else:
                    #create folder archive if not exists
                    if not os.path.exists('repos/archive'):
                        os.makedirs('repos/archive')
                    #move folder to archive
                    os.system(f"mv repos/{repo} repos/archive/")
    