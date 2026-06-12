#!/bin/bash
set -e

# Usage: ./deploy.sh <cluster-name>
CLUSTER_NAME=${1:-finance-cluster}

SERVICES=("auth-service" "expense-service" "investment-service" "budget-service")
IMAGES=("auth-api" "expense-api" "investment-api" "budget-api")

# ── 1. Ensure cluster exists ────────────────────────────────────────────────
if ! kind get clusters 2>/dev/null | grep -q "^${CLUSTER_NAME}$"; then
  echo ">>> Creating Kind cluster '${CLUSTER_NAME}'..."
  kind create cluster --name "$CLUSTER_NAME"
else
  echo ">>> Cluster '${CLUSTER_NAME}' already exists, skipping creation."
fi

# ── 2. Build Maven artifacts ─────────────────────────────────────────────────
echo ""
echo ">>> Building all services with Maven..."
for service in "${SERVICES[@]}"; do
  echo "    Building ${service}..."
  (cd "$service" && mvn clean package -q -DskipTests)
done

# ── 3. Build Docker images & load into Kind ──────────────────────────────────
echo ""
echo ">>> Building Docker images and loading into Kind..."
for i in "${!SERVICES[@]}"; do
  service="${SERVICES[$i]}"
  image="${IMAGES[$i]}"
  echo "    ${service} → ${image}:latest"
  docker build -t "${image}:latest" "$service"
  kind load docker-image "${image}:latest" --name "$CLUSTER_NAME"
done

# ── 4. Apply shared DB secrets & Postgres per service ───────────────────────
echo ""
echo ">>> Applying DB secrets and Postgres deployments..."
for service in "${SERVICES[@]}"; do
  echo "    ${service}..."
  kubectl apply -f "${service}/k8s/db-secrets.yaml"
  kubectl apply -f "${service}/k8s/postgres-deployment.yaml"
  kubectl apply -f "${service}/k8s/postgres-service.yaml"
done

# ── 5. Deploy services (budget last — depends on expense + investment) ───────
echo ""
echo ">>> Deploying application services..."
for service in "${SERVICES[@]}"; do
  name="${service%-service}"   # e.g. "expense-service" → "expense"
  echo "    Deploying ${service}..."
  kubectl apply -f "${service}/k8s/${name}-deployment.yaml"
  kubectl apply -f "${service}/k8s/${name}-service.yaml"
done

# ── 6. Verify ────────────────────────────────────────────────────────────────
echo ""
echo ">>> Deployment complete. Current cluster state:"
kubectl get deployments
echo ""
kubectl get pods
echo ""
kubectl get services
echo ""
echo "Port-forward commands:"
echo "  kubectl port-forward service/auth-api-service     8083:80"
echo "  kubectl port-forward service/expense-api-service  8081:80"
echo "  kubectl port-forward service/investment-api-service 8084:80"
echo "  kubectl port-forward service/budget-api-service   8082:80"
