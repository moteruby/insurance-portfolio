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

### Схема данных

Описание отношений и структур данных, используемых в ПС. Также представить скрипт (программный код), который необходим для генерации БД

---

## **Функциональные возможности**

### Диаграмма вариантов использования

Диаграмма вариантов использования и ее описание

### User-flow диаграммы

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
