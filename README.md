## Homework 1

### How to run

- Option 1: Run DB in docker and service locally

    ```bash
    docker compose up postgres -d 
    ```

    Service will be available at `localhost:8080`

- Option 2: Run DB and service in docker

    ```bash
    docker compose up -d
    ```

    Service will be available at `localhost:8080`

- Option 3: Run in minikube

    ```bash
    minikube start
    kubectl apply -f ./kube/
    ```

    Run this to get service url:
    ```bash
    minikube service hw1-s2
    ```
