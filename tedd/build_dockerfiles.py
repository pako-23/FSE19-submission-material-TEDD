import os
import subprocess

BASE_DIR = 'repos'

def build_docker_image(repo_path):
    try:
        result = subprocess.run(['docker', 'build', '-t', 'test_image', repo_path], capture_output=True, check=True, text=True)
        print(f"[SUCCESS] Immagine Docker costruita con successo per {repo_path}")
        return True
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Errore durante la costruzione dell'immagine Docker per {repo_path}: {e.stderr}")
        return False

def main():
    for root, dirs, files in os.walk(BASE_DIR):
        if 'Dockerfile' in files:
            print(f"Verifica Dockerfile in: {root}")
            build_docker_image(root)

if __name__ == "__main__":
    main()
