from github import Github
import sys, time
import os
from dotenv import load_dotenv
load_dotenv()
# Crea un'istanza Github senza autenticazione
#

token = os.environ.get('GITHUB_TOKEN')
g = Github(token)
# Cerca i repository
repositories = g.search_repositories(query='language:java content:selenium')

# Stampa l'URL di ciascun repository nei risultati
for repo in repositories:
    print(repo.html_url)
    input()

    # sys.exit()