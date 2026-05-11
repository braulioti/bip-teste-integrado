# [Teste BIP v1.0.0](https://github.com/braulioti/Import-Scripts)
### Projeto de teste da BIP Brasil

[![Sobre: brau.io](https://img.shields.io/badge/Contato-Br%C3%A1ulio%20Figueiredo-blue)](https://brau.io)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8d587cef062549b9af40f1d3c1c74c0d)](https://www.codacy.com/app/braulioti/Import-Scripts?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=braulioti/Import-Scripts&amp;utm_campaign=Badge_Grade)
[![Java](https://img.shields.io/badge/JDK-8%2B-green)](https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html)
[![Liquibase](https://img.shields.io/badge/-Liquibase-2962FF?style=flat&logo=liquibase&logoColor=white)](https://www.liquibase.com/)
[![CloudFlare](https://img.shields.io/badge/Cloudflare-F38020?style=flat&logo=Cloudflare&logoColor=white)](https://www.cloudflare.com/pt-br/)
[![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/br/java/technologies/javase/javase8-archive-downloads.html)
[![Docker](https://img.shields.io/badge/docker-257bd6?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

Este é um teste proposto pela BIP Brasil. Neste teste você vai encontrar boas práticas de desenvolvimento de projeto Java.

## Table of Contents

- [Desenvolvimento com Dev Container](#desenvolvimento-com-dev-container)
  - [Pre-requisitos](#pre-requisitos)
  - [Como abrir o projeto no container](#como-abrir-o-projeto-no-container)
  - [Como executar a aplicacao](#como-executar-a-aplicacao)
- [Deploy Github Actions](#deploy-github-actions)
  - [Como o deploy funciona](#como-o-deploy-funciona)
  - [Secrets necessarios no GitHub](#secrets-necessarios-no-github)
  - [Arquivo .env no servidor](#arquivo-env-no-servidor)
  - [Preparo minimo do servidor](#preparo-minimo-do-servidor)
  - [Deploy manual](#deploy-manual)
- [Versionamento](#versionamento)
- [Autor](#autor)

## Desenvolvimento com Dev Container

O projeto agora possui uma configuracao em `.devcontainer/` para padronizar o ambiente de desenvolvimento com Java 17, Maven, Node.js 22, Angular CLI, PostgreSQL e Liquibase.

### Pre-requisitos

- Docker Desktop instalado e em execucao
- Cursor ou VS Code com suporte a Dev Containers

### Como abrir o projeto no container

1. Abra o repositorio no Cursor.
2. Execute o comando `Dev Containers: Reopen in Container`.
3. Aguarde a criacao da imagem e a execucao do `postCreateCommand`.

Quando o container subir:

- o servico `postgres` sera iniciado automaticamente
- o servico `liquibase` aplicara `schema.sql` e `seed.sql`
- as dependencias do Maven e do frontend serao instaladas

### Como executar a aplicacao

Abra dois terminais dentro do Dev Container.

No primeiro terminal, suba o backend:

```bash
mvn -pl backend-module spring-boot:run
```

No segundo terminal, suba o frontend:

```bash
cd frontend
npm start -- --host 0.0.0.0 --port 4200
```

Depois disso:

- frontend: `http://localhost:4200`
- backend: `http://localhost:8080`
- postgres: `localhost:5432`

Dentro do container, o backend ja estara configurado para usar o banco em `postgres:5432`.

## Deploy Github Actions

### Como o deploy funciona

1. O GitHub Actions faz checkout do repositorio.
2. Monta um pacote com `db`, `backend-module`, `frontend` e `docker-compose.prod.yml`.
3. Envia esse pacote para o servidor por SSH.
4. Executa `docker compose` no servidor usando o arquivo `.env` que deve existir no destino.
5. Roda o Liquibase apos o deploy para aplicar o changelog de schema em `db/changelog/db.changelog-master.yaml`.

### Secrets necessarios no GitHub

Configure estes secrets no repositorio:

- `DEPLOY_HOST`: IP ou dominio do servidor
- `DEPLOY_PORT`: porta SSH, normalmente `22`
- `DEPLOY_USERNAME`: usuario usado no SSH
- `DEPLOY_SSH_KEY`: chave privada do usuario em formato OpenSSH
- `DEPLOY_PATH`: pasta no servidor onde a aplicacao sera publicada, por exemplo `/opt/bip-teste-integrado`

### Arquivo .env no servidor

Use `.env.production.example` como base e crie um arquivo `.env` dentro de `DEPLOY_PATH` com pelo menos:

```env
POSTGRES_DB=bip_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=troque-esta-senha
POSTGRES_PORT=5432
BACKEND_PORT=8080
FRONTEND_PORT=80
```

### Preparo minimo do servidor

O servidor precisa ter:

- Docker instalado
- plugin `docker compose` instalado
- usuario de deploy com permissao para executar Docker
- porta da API liberada apenas se voce realmente quiser acesso externo ao backend via `BACKEND_PORT`
- porta HTTP liberada para o valor configurado em `FRONTEND_PORT`
- porta do banco liberada apenas se voce realmente quiser acesso externo ao PostgreSQL via `POSTGRES_PORT`

### Deploy manual

Depois de configurar os secrets e o `.env` no servidor, voce pode:

- fazer `push` na `main`, ou
- abrir a aba `Actions` no GitHub e executar o workflow manualmente


## Versionamento

O Import Scripts será mantido, tanto quanto possível, seguindo as diretrizes de Versionamento Semântico.
As releases serão numeradas no seguinte formato:

`<major>.<minor>.<patch>`

E serão construídas com as seguintes diretrizes:

* Quebras de compatibilidade com versões anteriores incrementam a versão major (e redefinem minor e patch)
* Novas adições, incluindo novos ícones, sem quebrar a compatibilidade com versões anteriores, incrementam a versão minor (e redefinem o patch)
* Correções de bugs e mudanças diversas incrementam o patch

Para mais informações sobre SemVer, visite http://semver.org.

## Autor
- Email: braulio@braulioti.com.br
- Twitter: http://twitter.com/braulio_info
- GitHub: https://github.com/braulioti
- Website: https://brau.io
