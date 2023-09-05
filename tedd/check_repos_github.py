from github import Github
import sys, time
import os
from dotenv import load_dotenv
load_dotenv()

token = os.environ.get('GITHUB_TOKEN')
g = Github(token)
# Cerca i repository
repositories = g.search_repositories(query='language:java selenium docker in:file')

if not os.path.exists('repos'):
    os.makedirs('repos')

count = repositories.totalCount
print(f"Number of repositories containing both 'selenium' and 'docker': {count}")
i =0
for repo in repositories:
    i += 1
    print(f"{i}/{count} {repo.full_name}")
    owner_name = repo.full_name.split('/')[0]  # Extract owner name from full name
    
    # Create directory for the owner if it doesn't exist
    target_dir = os.path.join('repos', owner_name)
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)
    else:
        continue  # Skip if the directory already exists
    
    os.system(f"git clone {repo.clone_url} {target_dir}/{repo.name}")
    time.sleep(1)  # Sleep for 1 second to avoid abuse detection
    # input("Press Enter to continue...")