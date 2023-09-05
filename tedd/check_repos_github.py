from github import Github
import sys, time

# Crea un'istanza Github senza autenticazione
g = Github('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')

# Cerca i repository
repositories = g.search_repositories(query='symbol:find_element_by_ language:java')

# Stampa l'URL di ciascun repository nei risultati
for repo in repositories:
    print(repo.html_url)
    sys.exit()