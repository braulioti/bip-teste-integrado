#!/bin/sh
set -eu

cat <<EOF >/usr/share/nginx/html/env.js
window.__env = {
  API_BASE_URL: "${FRONTEND_API_BASE_URL:-/api/v1/beneficios}"
};
EOF
