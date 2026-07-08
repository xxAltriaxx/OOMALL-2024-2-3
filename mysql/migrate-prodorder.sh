#!/bin/bash
set -euo pipefail

MYSQL_CONTAINER="${MYSQL_CONTAINER:-$(docker container ls -q -f name=mysql.* | head -n 1)}"
MYSQL_USER="${MYSQL_USER:-demouser}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-123456}"
REPO_ROOT="${REPO_ROOT:-$(cd "$(dirname "$0")/.." && pwd)}"

if [ -z "$MYSQL_CONTAINER" ]; then
  echo "未找到 mysql 容器，请设置 MYSQL_CONTAINER 环境变量" >&2
  exit 1
fi

echo "执行 prodorder 增量迁移..."
docker exec -i "$MYSQL_CONTAINER" mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" < "$REPO_ROOT/mysql/sql/migrations/prodorder-001-idempotent.sql"

echo "prodorder 增量迁移完成"
