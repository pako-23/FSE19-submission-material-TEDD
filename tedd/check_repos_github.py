from github import Github
import sys, time
import os
from dotenv import load_dotenv
load_dotenv()

token = os.environ.get('GITHUB_TOKEN')
g = Github(token)
# Cerca i repository
repositories = g.search_repositories(query='language:java content:selenium')

if not os.path.exists('repos'):
    os.makedirs('repos')

for repo in repositories:
    owner_name = repo.full_name.split('/')[0]  # Extract owner name from full name
    
    # Create directory for the owner if it doesn't exist
    target_dir = os.path.join('repos', owner_name)
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)
    else:
        continue  # Skip if the directory already exists
    
    os.system(f"git clone {repo.clone_url} {target_dir}/{repo.name}")
    time.sleep(1)  # Sleep for 1 second to avoid abuse detection