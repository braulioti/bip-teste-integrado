#!/usr/bin/env bash
set -euo pipefail

cd /workspaces/bip-teste-integrado

mvn -q -DskipTests dependency:go-offline
npm ci --prefix frontend
