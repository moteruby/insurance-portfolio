# **Программное средство ведения отчетности страхового портфеля**

Программное средство предназначено для автоматизации процессов учета и анализа страхового портфеля компании. Система обеспечивает централизованное хранение данных о страховых договорах, клиентах и выплатах, а также формирование отчетности по ключевым показателям.

Основные цели:
-Повышение эффективности управления страховым портфелем.
-Минимизация ошибок и трудозатрат при подготовке отчетности.
-Обеспечение прозрачности и доступности данных для анализа и принятия решений.
-Поддержка требований внутреннего и внешнего аудита.

Возможности:
-Ведение базы данных страховых договоров и клиентов.
-Автоматическое формирование отчетов (финансовых, статистических, аналитических).
-Мониторинг динамики страхового портфеля (премии, выплаты, убытки).
-Интеграция с другими корпоративными системами и внешними источниками данных.
-Гибкая настройка форм отчетности под потребности компании.
-Визуализация показателей в виде графиков и диаграмм.

---

## **Содержание**

1. [Архитектура](#Архитектура)
	1. [C4-модель](#C4-модель)
	2. [Схема данных](#Схема_данных)
2. [Функциональные возможности](#Функциональные_возможности)
	1. [Диаграмма вариантов использования(#Диаграмма_вариантов_использования)]
	2. [User-flow диаграммы](#User-flow_диаграммы)
3. Пользоательский интерфейс(#Пользовательский_интерфейс)
4. [Детали реализации](#Детали_реализации)
	1. [UML-диаграммы](#UML-диаграммы)
	2. [Спецификация API](#Спецификация_API)
	3. [Безопасность](#Безопасность)
	4. [Оценка качества кода](#Оценка_качества_кода)
5. [Тестирование](#Тестирование)
	1. [Unit-тесты](#Unit-тесты)
	2. [Интеграционные тесты](#Интеграционные_тесты)
6. [Установка и  запуск](#installation)
	1. [Манифесты для сборки docker образов](#Манифесты_для_сборки_docker_образов)
	2. [Манифесты для развертывания k8s кластера](#Манифесты_для_развертывания_k8s_кластера)
7. [Лицензия](#Лицензия)
8. [Контакты](#Контакты)

---
## **Архитектура**

### C4-модель

Иллюстрация и описание архитектура ПС

Контекстный уровень

<img width="611" height="572" alt="image" src="https://github.com/user-attachments/assets/c9a1e26e-23ab-4f9f-b92b-ab13f126a694" />

Контейнерный уровень

<img width="537" height="633" alt="image" src="https://github.com/user-attachments/assets/d869ac01-83d6-4f7c-97ef-e7a6c0d4f945" />

Компонентный уровень

<img width="875" height="526" alt="image" src="https://github.com/user-attachments/assets/6540f7d5-9d81-44f3-8018-2afd6a40e2d2" />

Кодовый уровень

<img width="963" height="487" alt="image" src="https://github.com/user-attachments/assets/06c4f0b2-86ab-4cea-9867-09f90946fc3f" />

### Схема данных

<img width="559" height="736" alt="image" src="https://github.com/user-attachments/assets/3c234e9c-a005-4b1e-8a39-99983d9f891c" />

Скрипт для БД:

CREATE DATABASE IF NOT EXISTS insurance_portfolio
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE insurance_portfolio;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE policies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_number VARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE premiums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    due_date DATE NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (policy_id) REFERENCES policies(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_premiums_policy_id ON premiums(policy_id);

CREATE TABLE payouts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    payout_date DATE NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (policy_id) REFERENCES policies(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_payouts_policy_id ON payouts(policy_id);

CREATE TABLE reports (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_type VARCHAR(100) NOT NULL,
    created_by BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_json JSON,

    FOREIGN KEY (created_by) REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE TABLE stored_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (report_id) REFERENCES reports(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_file_report_id ON stored_files(report_id);

---

## **Функциональные возможности**

### Диаграмма вариантов использования

Диаграмма вариантов использования и ее описание

### User-flow диаграмм

User-flow для аналитика

<img width="657" height="427" alt="image" src="https://github.com/user-attachments/assets/fe024c5f-d689-466b-8223-b4da2deeea43" />

User-flow для сотрудника страховой компании

<img width="875" height="651" alt="image" src="https://github.com/user-attachments/assets/b5e271e4-6254-4a9a-9c59-0282fce9edc3" />

User-flow для администратора

<img width="903" height="603" alt="image" src="https://github.com/user-attachments/assets/ab8e49a4-d8a5-47fd-9a56-d46b2165c7c5" />

---

## **Пользовательский_интерфейс**

### Примеры экранов UI

Клиентская часть ПС

Главная страница

<img width="976" height="909" alt="image" src="https://github.com/user-attachments/assets/32625aa3-293c-4409-950c-62b1dc873543" />

Регистрация

<img width="974" height="1047" alt="image" src="https://github.com/user-attachments/assets/8063c6e1-8c69-4dd2-b24a-edd8bb15db61" />

Авторизация

<img width="974" height="912" alt="image" src="https://github.com/user-attachments/assets/f3e3a2cf-876d-49a0-b8ae-b11577581171" />

Личный кабинет клиента

<img width="974" height="726" alt="image" src="https://github.com/user-attachments/assets/a44c438a-70c3-4f5e-9d4e-abe5e423a687" />

Старница страхового портфеля клиента

<img width="974" height="643" alt="image" src="https://github.com/user-attachments/assets/8f9947d7-71c0-47d5-8812-08ac9b3a3b94" />

Договоры страхования клиента

<img width="974" height="760" alt="image" src="https://github.com/user-attachments/assets/6397d71e-5d1a-481c-a8e1-c6d99f25c965" />

Регистрация страхового случая

<img width="974" height="918" alt="image" src="https://github.com/user-attachments/assets/66494446-11bc-41ae-90d4-c15ccfe1c2e1" />

Процессс страховых выплат

<img width="974" height="894" alt="image" src="https://github.com/user-attachments/assets/a54bca4a-d85e-477c-ba56-3b0d52fdac71" />

Формирование отчетов клиента

<img width="974" height="902" alt="image" src="https://github.com/user-attachments/assets/91d8ea0d-87c5-440f-8f5a-af3a2db23f8e" />

Аналитика портфеля клиента

<img width="974" height="912" alt="image" src="https://github.com/user-attachments/assets/8fa5982f-480e-496a-98ca-0bb267db5b5d" />

Экспорт документов

<img width="974" height="896" alt="image" src="https://github.com/user-attachments/assets/6ff85f80-3b1b-40fe-8ce5-93f5fada2cb6" />

Сотрудник страховой компании

Личный кабинет сотрудника 

<img width="974" height="897" alt="image" src="https://github.com/user-attachments/assets/ecf5170e-bb2a-4ca9-a30f-219632f46c42" />

Клиенты сотрудника

<img width="974" height="820" alt="image" src="https://github.com/user-attachments/assets/4d2bdd0a-d956-4a4d-b20a-28cb38f232e0" />

Добавление новых клиентов

<img width="974" height="893" alt="image" src="https://github.com/user-attachments/assets/0550302b-b12e-4fc7-9f90-c5c4990bbe73" />

Договоры сотрудника

<img width="974" height="898" alt="image" src="https://github.com/user-attachments/assets/b9d62198-2166-4ab7-89a5-4e0457e6e69d" />

Оформление новых договоров сотрудника

<img width="974" height="914" alt="image" src="https://github.com/user-attachments/assets/8b8e8abd-9278-4cef-abe9-24f7483ea38d" />

Страховые случаи

<img width="974" height="900" alt="image" src="https://github.com/user-attachments/assets/fca40225-2be2-426e-aa14-cd5b4e7f4d42" />

Отчетность 

<img width="974" height="907" alt="image" src="https://github.com/user-attachments/assets/da0d451a-233f-452a-a625-cf240ef924c9" />

Коммуникации

<img width="974" height="801" alt="image" src="https://github.com/user-attachments/assets/48608eba-ea00-43ac-bbb8-0a9a259a9fa7" />

Администратор

Панель администратора

<img width="974" height="920" alt="image" src="https://github.com/user-attachments/assets/99fc19c3-4c8d-482a-8d5a-d9d519e44a6e" />

Одобрение договоров

<img width="974" height="907" alt="image" src="https://github.com/user-attachments/assets/5dcb3b37-05c1-4889-8d19-4cc325a2f327" />

Управление сотрудниками

<img width="974" height="718" alt="image" src="https://github.com/user-attachments/assets/070d2982-fdaf-4b31-8a91-fcf977eea1e7" />

Управление клиентами

<img width="974" height="885" alt="image" src="https://github.com/user-attachments/assets/0f2471c8-18a6-4e3b-b2f3-62eef3e770be" />

Управление доступом

<img width="974" height="689" alt="image" src="https://github.com/user-attachments/assets/09ec1229-00f6-4a67-b148-bf5dc10a5ba9" />

Управление ролями

<img width="974" height="908" alt="image" src="https://github.com/user-attachments/assets/31d201e4-7572-4e9e-a4d2-a10d262ea27d" />

Управление тарифами

<img width="974" height="896" alt="image" src="https://github.com/user-attachments/assets/a2d61e5e-8b99-4560-b5ff-3373ae3f9085" />

Аналитика системы

<img width="974" height="895" alt="image" src="https://github.com/user-attachments/assets/d9e484c2-5993-498a-a73f-a7d71a4df53f" />

Код клиентской части в папке UI

---

## **Детали реализации**

### UML-диаграммы

 <img width="974" height="310" alt="image" src="https://github.com/user-attachments/assets/330a8da1-fe17-4dbe-b6e3-e6fc7ed17e33" />

Диаграмма развертывания

<img width="755" height="690" alt="image" src="https://github.com/user-attachments/assets/cc595a40-feb8-46cc-ab9e-c75e5a1843e2" />

Диаграмма пакетов insurance-portfolio

<img width="975" height="642" alt="image" src="https://github.com/user-attachments/assets/0801a92a-5874-4e1e-86de-86b1734ed350" />

Диаграмма вариантов использования

<img width="765" height="910" alt="image" src="https://github.com/user-attachments/assets/c162c63b-22aa-42f4-946b-e945174c5321" />

Диаграмма состояний отчета

<img width="623" height="676" alt="image" src="https://github.com/user-attachments/assets/03de332f-fa66-4736-85b3-24c83bab0693" />

Диаграмма последовательности

<img width="897" height="741" alt="image" src="https://github.com/user-attachments/assets/80d1f0aa-aaeb-43cd-9c6c-384f70250ee1" />

Диаграмма деятельности 

### Спецификация API

Была разработана документация к разработанному API с использованием Swagger. 
На рисунке 1 представлена спецификация для системы ведения отчетности страхового портфеля. 

 <img width="975" height="681" alt="image" src="https://github.com/user-attachments/assets/62a9a991-2f43-42ac-8e96-2a57b6d3b7b5" />

Рисунок 1 – Спецификация для страховых агентов и аутентификации

На рисунке 2 представлена спецификация для клиентов и страховых отчетов. 

<img width="852" height="623" alt="image" src="https://github.com/user-attachments/assets/0056d4f1-45b4-426c-9ebc-eea5f7a5c611" />

Рисунок 2 – Спецификация для клиентов и страховых отчетов

На рисунке 3 представлена спецификация для платежей и полисов. 

 <img width="975" height="487" alt="image" src="https://github.com/user-attachments/assets/faf9a073-d087-4f71-b751-29699f06947a" />

Рисунок 3 – Спецификация для платежей и полисов

На рисунке 4 представлена спецификация для пользователей и страховых случаев. 

 <img width="904" height="521" alt="image" src="https://github.com/user-attachments/assets/da8861c7-1709-4c01-bedf-0d5e497fc8d2" />

Рисунок 4 – Спецификация для пользователей и страховых случаев

В итоге была разработана документация.

### Безопасность

Результаты внедрения системы безопасности

Spring Security Framework как основа защиты:
– все запросы проходят через Security Filter Chain;
– JWT валидация на уровне фильтров;
– маршрутизация на основе ролей пользователей.
Security Config как центр аутентификации:
– централизованное управление пользователями;
– генерация и валидация JWT токенов;
– управление ролями и правами доступа.
Многоуровневая система авторизации:
– проверка прав доступа на уровне URL;
– проверка на уровне методов с @PreAuthorize;
– проверка на уровне бизнес-логики.
Распределенная проверка прав доступа:
– каждый сервис проверяет права доступа к данным;
– использование PermissionService для проверки прав;
– передача контекста пользователя через SecurityContext.
Внедрение этих компонентов безопасности трансформировало архитектуру в единую, защищенную систему. Централизация аутентификации и использование многоуровневой авторизации являются фундаментальными изменениями, повышающими безопасность и управляемость страховых данных.
BCrypt – это криптографическая хеш-функция для паролей, разработанная на основе шифра Blowfish. Основные преимущества:
1 Адаптивность: можно увеличивать сложность вычисления.
2 Salt: автоматически генерируется уникальная соль для каждого пароля.
3 Защита от rainbow tables: невозможно использовать предвычисленные таблицы.

Компоненты системы безопасности

5.2.1 Security Framework (Spring Security). Функции:
– единая точка входа для всех запросов;
– валидация JWT токенов;
– управление доступом на основе ролей;
– CORS configuration;
– Protection against CSRF.
Технологии:
– Spring Security;
– Servlet Filters;
– JWT validation;
– BCrypt password encoding.

5.2.2 Authentication Service (Port: 8080). Функции:
– аутентификация пользователей (логин);
– генерация JWT токенов;
– управление пользователями;
– регистрация новых пользователей (для администраторов);
– ведение аудита действий.
Технологии:
– Spring Boot;
– Spring Security;
– Spring Data JPA;
– PostgreSQL;
– JWT (JJWT);
– BCrypt.

5.2.3 Database Layer (PostgreSQL Port: 5432)
Функции:
– хранение пользователей и их ролей;
– хранение данных клиентов и полисов;
– хранение событий аудита;
– хранение филиалов и агентов.
Таблицы безопасности:

Код реализации представлен в папке src.

### Оценка качества кода

Анализ результатов:
1 Model и DTO классы демонстрируют высокое покрытие (79-80%), что свидетельствует о тщательном тестировании бизнес-логики и структур данных.
2 Controller слой имеет покрытие 76%, что является хорошим показателем для REST API. Написано 46 integration тестов, проверяющих доступность endpoints и правильность настройки Spring Security.
3 Config классы имеют 100% покрытие благодаря тесту загрузки контекста Spring.
4 Общее покрытие 80% значительно превышает минимальный целевой показатель и соответствует уровню хорошо по индустриальным стандартам.
В итоге было оценено качество кода.


---

## **Тестирование**

<img width="883" height="177" alt="image" src="https://github.com/user-attachments/assets/31f13feb-e487-483e-afbc-06112a55b9d1" />

Тестирование системы ведения отчетности страхового портфеля

<img width="835" height="304" alt="image" src="https://github.com/user-attachments/assets/7bf5fd25-96d0-4390-9b13-f76f1ff41e00" />

Тестирование моделей системы ведения отчетности страхового портфеля

<img width="846" height="112" alt="image" src="https://github.com/user-attachments/assets/e7ed66e2-c67f-4f67-a3d3-ec2172548f0d" />

Тестирование конфигураций системы ведения отчетности страхового портфеля

<img width="845" height="229" alt="image" src="https://github.com/user-attachments/assets/b113f5b9-60a8-49f6-a45a-3d103316232d" />

Тестирование контроллеров системы ведения отчетности страхового портфеля

---

## **Установка и  запуск**

### Манифесты для сборки docker образов

services:
  # База данных PostgreSQL
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: insurance_db
      POSTGRES_USER: insurance_user
      POSTGRES_PASSWORD: insurance_password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./sql-scripts/init.sql:/docker-entrypoint-initdb.d/init.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U insurance_user -d insurance_db"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Backend приложение (Spring Boot)
  insurance-backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/insurance_db
      - SPRING_DATASOURCE_USERNAME=insurance_user
      - SPRING_DATASOURCE_PASSWORD=insurance_password
      - JWT_SECRET=your-super-secret-jwt-key-for-docker
      - ENCRYPTION_SECRET=your-encryption-secret-key-here
    depends_on:
      postgres:
        condition: service_healthy
    volumes:
      - ./logs/backend:/app/logs

  # Frontend приложение (React)
  insurance-frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    environment:
      - REACT_APP_API_URL=http://localhost:8080/api
      - REACT_APP_WS_URL=ws://localhost:8080/ws
    depends_on:
      - insurance-backend
    volumes:
      - ./logs/frontend:/app/logs

  # Админка для PostgreSQL (опционально)
  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@insurance.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "8081:80"
    depends_on:
      - postgres

networks:
  default:
    driver: bridge

volumes:
  postgres_data:
    driver: local


### Манифесты для развертывания k8s кластера

Namespace (00-namespace.yaml):

apiVersion: v1
kind: Namespace
metadata:
  name: insurance-system
  labels:
    name: insurance-system
    environment: production

ConfigMaps и Secrets (01-configmaps-secrets.yaml):

apiVersion: v1
kind: ConfigMap
metadata:
  name: backend-config
  namespace: insurance-system
data:
  SPRING_PROFILES_ACTIVE: "k8s"
  SPRING_DATASOURCE_URL: "jdbc:postgresql://postgres-service:5432/insurance_db"
  LOGGING_LEVEL: "INFO"
  SERVER_PORT: "8080"
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: frontend-config
  namespace: insurance-system
data:
  REACT_APP_API_URL: "http://backend-service:8080/api"
  REACT_APP_WS_URL: "ws://backend-service:8080/ws"
---
apiVersion: v1
kind: Secret
metadata:
  name: postgres-secret
  namespace: insurance-system
type: Opaque
stringData:
  postgres-password: "insurance_password"
  postgres-user: "insurance_user"
  postgres-db: "insurance_db"
---
apiVersion: v1
kind: Secret
metadata:
  name: jwt-secret
  namespace: insurance-system
type: Opaque
stringData:
  jwt-secret: "your-super-secret-jwt-key-for-production"
---
apiVersion: v1
kind: Secret
metadata:
  name: encryption-secret
  namespace: insurance-system
type: Opaque
stringData:
  encryption-key: "your-encryption-secret-key-for-production"

База данных PostgreSQL (02-postgresql.yaml):

apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: postgres
  namespace: insurance-system
spec:
  serviceName: postgres-service
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
      - name: postgres
        image: postgres:15
        env:
        - name: POSTGRES_DB
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: postgres-db
        - name: POSTGRES_USER
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: postgres-user
        - name: POSTGRES_PASSWORD
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: postgres-password
        ports:
        - containerPort: 5432
        volumeMounts:
        - name: postgres-data
          mountPath: /var/lib/postgresql/data
        - name: init-script
          mountPath: /docker-entrypoint-initdb.d
        livenessProbe:
          exec:
            command:
            - pg_isready
            - -U
            - insurance_user
            - -d
            - insurance_db
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          exec:
            command:
            - pg_isready
            - -U
            - insurance_user
            - -d
            - insurance_db
          initialDelaySeconds: 5
          periodSeconds: 5
      volumes:
      - name: init-script
        configMap:
          name: postgres-init
  volumeClaimTemplates:
  - metadata:
      name: postgres-data
    spec:
      accessModes: [ "ReadWriteOnce" ]
      resources:
        requests:
          storage: 10Gi
---
apiVersion: v1
kind: Service
metadata:
  name: postgres-service
  namespace: insurance-system
spec:
  selector:
    app: postgres
  ports:
  - port: 5432
    targetPort: 5432
  type: ClusterIP

Backend приложение (03-backend-app.yaml):

apiVersion: apps/v1
kind: Deployment
metadata:
  name: insurance-backend
  namespace: insurance-system
spec:
  replicas: 2
  selector:
    matchLabels:
      app: insurance-backend
  template:
    metadata:
      labels:
        app: insurance-backend
    spec:
      containers:
      - name: insurance-backend
        image: insurance-system/insurance-backend:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          valueFrom:
            configMapKeyRef:
              name: backend-config
              key: SPRING_PROFILES_ACTIVE
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            configMapKeyRef:
              name: backend-config
              key: SPRING_DATASOURCE_URL
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: postgres-user
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: postgres-secret
              key: postgres-password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: jwt-secret
        - name: ENCRYPTION_SECRET
          valueFrom:
            secretKeyRef:
              name: encryption-secret
              key: encryption-key
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /api/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /api/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: backend-service
  namespace: insurance-system
spec:
  selector:
    app: insurance-backend
  ports:
  - port: 8080
    targetPort: 8080
    nodePort: 30081
  type: NodePort

Frontend приложение (04-frontend-app.yaml):

apiVersion: apps/v1
kind: Deployment
metadata:
  name: insurance-frontend
  namespace: insurance-system
spec:
  replicas: 2
  selector:
    matchLabels:
      app: insurance-frontend
  template:
    metadata:
      labels:
        app: insurance-frontend
    spec:
      containers:
      - name: insurance-frontend
        image: insurance-system/insurance-frontend:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 3000
        env:
        - name: REACT_APP_API_URL
          valueFrom:
            configMapKeyRef:
              name: frontend-config
              key: REACT_APP_API_URL
        - name: REACT_APP_WS_URL
          valueFrom:
            configMapKeyRef:
              name: frontend-config
              key: REACT_APP_WS_URL
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "250m"
        livenessProbe:
          httpGet:
            path: /
            port: 3000
          initialDelaySeconds: 30
          periodSeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  name: frontend-service
  namespace: insurance-system
spec:
  selector:
    app: insurance-frontend
  ports:
  - port: 3000
    targetPort: 3000
    nodePort: 30080
  type: NodePort

Ingress (05-ingress.yaml):

apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: insurance-ingress
  namespace: insurance-system
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: insurance.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend-service
            port:
              number: 3000
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: backend-service
            port:
              number: 8080


---

## **Лицензия**

Этот проект лицензирован по лицензии MIT - подробности представлены в файле [[License.md|LICENSE.md]]

---

## **Контакты**

Автор: romakirik0@gmail.com
