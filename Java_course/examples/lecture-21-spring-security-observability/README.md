# Лекция 21: spring-security-observability

Требования: JDK 25, Maven 3.9.x, Docker.

```bash
docker compose up -d
mvn spring-boot:run
```

Ожидаемый результат: защищённое приложение и открытый /actuator/health. Проект имеет собственный POM и запускается независимо от остальных.
