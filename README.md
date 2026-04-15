#### Новый запуск
Для сборки и запуска используется docker compose. Не надо всё собирать и запускать отдельно.

Как запускать:
1. Перейти в infrastructure
2. Вызвать в консоли (запуск проекта без тестов): docker compose up --build
3. Для остановки с очисткой всего: docker compose down -v

Порты на localhost

- 9000 - авторизация
- 9001 (вместо 8080) - gateway
- 8080 (вместо 3000) - фронт

БД:
- 3306 - БД авторизации
- 3307 - БД музея (пока пустая, потом тут будут данные GateWay)

WEB-тесты находятся:
- rococo-e2e-test/src/test/java/test/web

REST-тесты находятся:
- rococo-e2e-test/src/test/java/test/rest