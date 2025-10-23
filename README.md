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

<img width="650" height="630" alt="image" src="https://github.com/user-attachments/assets/c3e70b12-3e17-4633-a25d-0799247ba34b" />

Контейнерный уровень

<img width="529" height="525" alt="image" src="https://github.com/user-attachments/assets/58d5a339-f354-4a2c-8f0d-71ec4b23e3a6" />

Компонентный уровень

<img width="875" height="850" alt="image" src="https://github.com/user-attachments/assets/94edb613-1e81-4852-8ecd-798d44a16c96" />

### Схема данных

<img width="1573" height="1188" alt="шеоперщшпо2" src="https://github.com/user-attachments/assets/5fbd51bc-2f59-478b-ba82-5398ab2b84f9" />


Скрипт для БД:

CREATE DATABASE insurance_portfolio_reporting;
USE insurance_portfolio_reporting;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'EMPLOYEE', 'ADMIN') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE personal_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    passport_series VARCHAR(10),
    passport_number VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_personal_data (
    user_id BIGINT PRIMARY KEY,
    personal_data_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (personal_data_id) REFERENCES personal_data(id) ON DELETE CASCADE
);

CREATE TABLE insurance_product_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    base_rate DECIMAL(5,4) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE policy_statuses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    insurant_personal_data_id BIGINT NOT NULL, -- ссылка на personal_data
    product_type_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    sum_insured DECIMAL(15,2) NOT NULL,
    premium DECIMAL(15,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (insurant_personal_data_id) REFERENCES personal_data(id),
    FOREIGN KEY (product_type_id) REFERENCES insurance_product_types(id),
    FOREIGN KEY (status_id) REFERENCES policy_statuses(id)
);

CREATE TABLE insured_objects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    object_type VARCHAR(100) NOT NULL, -- 'VEHICLE', 'PROPERTY', 'PERSON'
    description TEXT
);

CREATE TABLE policy_details (
    policy_id BIGINT PRIMARY KEY,
    insured_object_id BIGINT NOT NULL,
    additional_conditions TEXT,
    risk_factors JSON,
    FOREIGN KEY (policy_id) REFERENCES policies(id) ON DELETE CASCADE,
    FOREIGN KEY (insured_object_id) REFERENCES insured_objects(id)
);

CREATE TABLE payment_statuses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE payment_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    policy_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    payment_date DATE NOT NULL,
    status_id BIGINT NOT NULL,
    payment_method_id BIGINT NOT NULL,
    transaction_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (policy_id) REFERENCES policies(id),
    FOREIGN KEY (status_id) REFERENCES payment_statuses(id),
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

CREATE TABLE claim_statuses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_number VARCHAR(50) UNIQUE NOT NULL,
    policy_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    incident_date DATE NOT NULL,
    description TEXT NOT NULL,
    claimed_amount DECIMAL(15,2) NOT NULL,
    approved_amount DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (policy_id) REFERENCES policies(id),
    FOREIGN KEY (status_id) REFERENCES claim_statuses(id)
);

CREATE TABLE document_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE claim_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    claim_id BIGINT NOT NULL,
    document_type_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (claim_id) REFERENCES claims(id) ON DELETE CASCADE,
    FOREIGN KEY (document_type_id) REFERENCES document_types(id)
);

CREATE TABLE report_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE report_formats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    file_extension VARCHAR(10) NOT NULL
);


CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    report_type_id BIGINT NOT NULL,
    format_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    generated_by BIGINT NOT NULL,
    generation_parameters JSON,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_type_id) REFERENCES report_types(id),
    FOREIGN KEY (format_id) REFERENCES report_formats(id),
    FOREIGN KEY (generated_by) REFERENCES users(id)
);


INSERT INTO insurance_product_types (code, name, description, base_rate) VALUES 
('CAR_INSURANCE', 'Автострахование', 'Страхование автомобилей', 0.05),
('HEALTH_INSURANCE', 'Медицинское страхование', 'Страхование здоровья', 0.03),
('PROPERTY_INSURANCE', 'Имущественное страхование', 'Страхование недвижимости', 0.02),
('LIFE_INSURANCE', 'Страхование жизни', 'Жизненное страхование', 0.015);

INSERT INTO policy_statuses (code, name, description) VALUES 
('ACTIVE', 'Активен', 'Полис действует'),
('EXPIRED', 'Истек', 'Срок действия полиса истек'),
('CANCELLED', 'Аннулирован', 'Полис отменен'),
('PENDING_PAYMENT', 'Ожидает оплаты', 'Ожидается оплата по полису');

INSERT INTO payment_statuses (code, name) VALUES 
('PENDING', 'Ожидает'),
('COMPLETED', 'Завершен'),
('FAILED', 'Неудачный'),
('REFUNDED', 'Возвращен');

INSERT INTO payment_methods (code, name) VALUES 
('BANK_CARD', 'Банковская карта'),
('BANK_TRANSFER', 'Банковский перевод'),
('ELECTRONIC_WALLET', 'Электронный кошелек');

INSERT INTO claim_statuses (code, name, description) VALUES 
('NEW', 'Новый', 'Новое заявление'),
('UNDER_REVIEW', 'На рассмотрении', 'Заявление рассматривается'),
('APPROVED', 'Одобрен', 'Выплата одобрена'),
('REJECTED', 'Отклонен', 'Выплата отклонена'),
('PAID', 'Выплачен', 'Выплата произведена');

INSERT INTO document_types (code, name, description) VALUES 
('CLAIM_FORM', 'Заявление', 'Заявление о страховом случае'),
('DAMAGE_ACT', 'Акт осмотра', 'Акт осмотра повреждений'),
('POLICE_REPORT', 'Протокол', 'Протокол ГИБДД'),
('EXPERT_REPORT', 'Заключение эксперта', 'Заключение независимого эксперта');

INSERT INTO report_types (code, name, description) VALUES 
('FINANCIAL', 'Финансовый', 'Финансовая отчетность'),
('PORTFOLIO', 'Портфельный', 'Анализ страхового портфеля'),
('CLAIMS', 'Убыточность', 'Отчет по убыткам'),
('OPERATIONAL', 'Операционный', 'Операционная отчетность');

INSERT INTO report_formats (code, name, file_extension) VALUES 
('PDF', 'PDF документ', '.pdf'),
('EXCEL', 'Excel файл', '.xlsx'),
('HTML', 'HTML отчет', '.html');

CREATE INDEX idx_policies_user_id ON policies(user_id);
CREATE INDEX idx_policies_insurant_id ON policies(insurant_personal_data_id);
CREATE INDEX idx_policies_status ON policies(status_id);
CREATE INDEX idx_payments_policy_id ON payments(policy_id);
CREATE INDEX idx_claims_policy_id ON claims(policy_id);
CREATE INDEX idx_claims_status ON claims(status_id);


---

## **Функциональные возможности**

### Диаграмма вариантов использования

Диаграмма вариантов использования и ее описание

### User-flow диаграмм

User-flow для клиента

<img width="657" height="773" alt="image" src="https://github.com/user-attachments/assets/a7908171-9529-461e-8da2-4d402648f2d1" />

User-flow для сотрудника страховой компании

<img width="875" height="651" alt="image" src="https://github.com/user-attachments/assets/b5e271e4-6254-4a9a-9c59-0282fce9edc3" />

User-flow для администратора

<img width="975" height="603" alt="image" src="https://github.com/user-attachments/assets/7a164c07-e349-4204-9377-b41dcf062550" />

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

Представить все UML-диаграммы , которые позволят более точно понять структуру и детали реализации ПС

### Спецификация API

Представить описание реализованных функциональных возможностей ПС с использованием Open API (можно представить либо полный файл спецификации, либо ссылку на него)

### Безопасность

Описать подходы, использованные для обеспечения безопасности, включая описание процессов аутентификации и авторизации с примерами кода из репозитория сервера

### Оценка качества кода

Используя показатели качества и метрики кода, оценить его качество

---

## **Тестирование**

### Unit-тесты

Представить код тестов для пяти методов и его пояснение

### Интеграционные тесты

Представить код тестов и его пояснение

---

## **Установка и  запуск**

### Манифесты для сборки docker образов

Представить весь код манифестов или ссылки на файлы с ними (при необходимости снабдить комментариями)

### Манифесты для развертывания k8s кластера

Представить весь код манифестов или ссылки на файлы с ними (при необходимости снабдить комментариями)

---

## **Лицензия**

Этот проект лицензирован по лицензии MIT - подробности представлены в файле [[License.md|LICENSE.md]]

---

## **Контакты**

Автор: romakirik0@gmail.com
