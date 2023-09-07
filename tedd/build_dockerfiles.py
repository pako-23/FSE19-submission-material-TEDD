import os
import subprocess

BASE_DIR = 'repos'

def stop_and_remove_container(image_name):
    try:
        # Stop container
        subprocess.run(['docker', 'stop', image_name], check=True)
    except subprocess.CalledProcessError:
        pass

    try:
        # Remove container
        subprocess.run(['docker', 'rm', image_name], check=True)
    except subprocess.CalledProcessError:
        pass

def build_docker_image(repo_path):
    image_name = 'test_image'
    
    # Stop and remove any containers from previous builds
    stop_and_remove_container(image_name)
    
    try:
        result = subprocess.run(['docker', 'build', '-t', image_name, repo_path], capture_output=True, check=True, text=True)
        print(f"[SUCCESS] Docker image built successfully for {repo_path}")
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Error building Docker image for {repo_path}: {e.stderr}")

def delete_docker_images():
    try:
        result = subprocess.run(['docker', 'rmi', '-f', '$(docker images -q)'], capture_output=True, check=True, text=True)
        print(f"Docker images deleted successfully")
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Error deleting Docker images: {e.stderr}")

def docker_system_prune():
    try:
        result = subprocess.run(['docker', 'system', 'prune', '-f'], capture_output=True, check=True, text=True)
        print(f"Docker system pruned successfully")
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Error pruning Docker system: {e.stderr}")
def main():
    for root, dirs, files in os.walk(BASE_DIR):
        if 'Dockerfile' in files:
            print(f"Verifying Dockerfile in: {root}")
            build_docker_image(root)
            delete_docker_images()
            docker_system_prune()

if __name__ == "__main__":
    main()
