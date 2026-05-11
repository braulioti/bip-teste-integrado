FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build && mkdir -p /app/dist-output && if [ -d /app/dist/frontend/browser ]; then cp -R /app/dist/frontend/browser/. /app/dist-output/; else cp -R /app/dist/frontend/. /app/dist-output/; fi

FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY docker-entrypoint.sh /docker-entrypoint.d/40-env.sh
COPY --from=build /app/dist-output/ /usr/share/nginx/html/

RUN chmod +x /docker-entrypoint.d/40-env.sh

EXPOSE 80
