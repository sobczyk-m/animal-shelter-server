CREATE SCHEMA IF NOT EXISTS animal_shelter;

SET NAMES 'UTF8MB4';
SET TIME_ZONE = '+02:00';

USE animal_shelter;

DROP TABLE IF EXISTS DogWalks;
DROP TABLE IF EXISTS EmployeeRoles;
DROP TABLE IF EXISTS Employees;
DROP TABLE IF EXISTS Roles;
DROP TABLE IF EXISTS WarehouseLeashes;
DROP TABLE IF EXISTS Leashes;
DROP TABLE IF EXISTS WarehouseCollars;
DROP TABLE IF EXISTS Collars;
DROP TABLE IF EXISTS WarehouseMedicines;
DROP TABLE IF EXISTS WarehouseDogFood;
DROP TABLE IF EXISTS WarehouseVaccines;
DROP TABLE IF EXISTS Warehouses;
DROP TABLE IF EXISTS DogFoodDogMeals;
DROP TABLE IF EXISTS DogMeals;
DROP TABLE IF EXISTS DogFood;
DROP TABLE IF EXISTS DogMeals;
DROP TABLE IF EXISTS DogDiseases;
DROP TABLE IF EXISTS DogExaminationDiseases;
DROP TABLE IF EXISTS DogExaminations;
DROP TABLE IF EXISTS AnimalOwnerDogs;
DROP TABLE IF EXISTS AnimalOwners;
DROP TABLE IF EXISTS Dogs;
DROP TABLE IF EXISTS Cages;
DROP TABLE IF EXISTS Shelters;
DROP TABLE IF EXISTS Diseases;
DROP TABLE IF EXISTS Medicines;
DROP TABLE IF EXISTS Vaccinations;
DROP TABLE IF EXISTS DogVaccinations;
DROP TABLE IF EXISTS Vaccines;

CREATE TABLE Shelters
(
    shelter_id  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL,
    address     VARCHAR(255)    NOT NULL,
    phone       VARCHAR(30)  DEFAULT NULL,
    email       VARCHAR(100) DEFAULT NULL,
    description VARCHAR(255) DEFAULT NULL
);

CREATE TABLE Employees
(
    employee_id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name             VARCHAR(50)     NOT NULL,
    last_name              VARCHAR(50)     NOT NULL,
    email                  VARCHAR(100)    NOT NULL,
    password               VARCHAR(255)    NOT NULL,
    address                VARCHAR(255)    NOT NULL,
    phone                  VARCHAR(30)  DEFAULT NULL,
    title                  VARCHAR(50)     NOT NULL,
    bio                    VARCHAR(255) DEFAULT NULL,
    enabled                BOOLEAN      DEFAULT FALSE,
    non_locked             BOOLEAN      DEFAULT TRUE,
    employment_start       DATE            NOT NULL,
    employment_termination DATE         DEFAULT NULL,
    identity_document      VARCHAR(255)    NOT NULL,
    created_at             DATETIME     DEFAULT CURRENT_TIMESTAMP,
    image_url              VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    shelter_id             BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (shelter_id) REFERENCES Shelters (shelter_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_Employees_Email UNIQUE (email)
);

CREATE TABLE Roles
(
    role_id    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50)     NOT NULL,
    permission VARCHAR(255)    NOT NULL,
    CONSTRAINT UQ_Roles_Name UNIQUE (name)
);

CREATE TABLE EmployeeRoles
(
    employee_role_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    employee_id      BIGINT UNSIGNED NOT NULL,
    role_id          BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employees (employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_EmployeeRoles_Employee_Id UNIQUE (employee_id)
);

CREATE TABLE Warehouses
(
    warehouse_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(50)     NOT NULL,
    address      VARCHAR(255)    NOT NULL,
    phone        VARCHAR(30)     DEFAULT NULL,
    email        VARCHAR(100)    DEFAULT NULL,
    description  VARCHAR(255)    DEFAULT NULL,
    shelter_id   BIGINT UNSIGNED DEFAULT NULL,
    FOREIGN KEY (shelter_id) REFERENCES Shelters (shelter_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Leashes
(
    leash_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brand    VARCHAR(50) DEFAULT NULL,
    length   INT             NOT NULL,
    width    INT             NOT NULL
);

CREATE TABLE WarehouseLeashes
(
    warehouse_leash_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    in_stock           INT             NOT NULL,
    leash_id           BIGINT UNSIGNED NOT NULL,
    warehouse_id       BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (leash_id) REFERENCES Leashes (leash_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses (warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Collars
(
    collar_id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brand             VARCHAR(50) DEFAULT NULL,
    min_circumference FLOAT(4, 1)     NOT NULL,
    max_circumference FLOAT(4, 1)     NOT NULL,
    width             INT             NOT NULL
);

CREATE TABLE WarehouseCollars
(
    warehouse_collar_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    in_stock            INT             NOT NULL,
    collar_id           BIGINT UNSIGNED NOT NULL,
    warehouse_id        BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (collar_id) REFERENCES Collars (collar_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses (warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE DogFood
(
    dog_food_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    brand       VARCHAR(50)     NOT NULL,
    name        VARCHAR(50)     NOT NULL,
    type        VARCHAR(20)     NOT NULL CHECK ( type IN ('DRY_FOOD', 'WET_FOOD', 'TREATS')),
    description VARCHAR(255) DEFAULT NULL,
    CONSTRAINT UQ_DogFood_Name UNIQUE (name)
);

CREATE TABLE WarehouseDogFood
(
    warehouse_dog_food_id BIGINT UNSIGNED        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    in_stock              DECIMAL(9, 3) UNSIGNED NOT NULL,
    warehouse_id          BIGINT UNSIGNED        NOT NULL,
    dog_food_id           BIGINT UNSIGNED        NOT NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses (warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (dog_food_id) REFERENCES DogFood (dog_food_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Cages
(
    cage_id     BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    number      INT             NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
    shelter_id  BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (shelter_id) REFERENCES Shelters (shelter_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Dogs
(
    dog_id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    reference_number   VARCHAR(255)    NOT NULL,
    species            VARCHAR(30)     NOT NULL CHECK ( species IN ('DOG', 'CAT')),
    breed              VARCHAR(50)  DEFAULT NULL,
    gender             VARCHAR(10)     NOT NULL CHECK ( gender IN ('MALE', 'FEMALE')),
    age                DATE         DEFAULT NULL,
    ointment           VARCHAR(40)     NOT NULL,
    height             INT             NOT NULL,
    weight             FLOAT(4, 1)     NOT NULL,
    neck_circumference FLOAT(4, 1)     NOT NULL,
    image_url          VARCHAR(255) DEFAULT 'https://previews.123rf.com/images/cundrawan703/cundrawan7031207/cundrawan703120700008/14519717-dog-avatar-cartoon-character-icon.jpg',
    chipped            BOOLEAN      DEFAULT NULL,
    additional_info    VARCHAR(255) DEFAULT NULL,
    registration_date  DATE            NOT NULL,
    adopted_at         DATE         DEFAULT NULL,
    cage_id            BIGINT UNSIGNED NOT NULL,
    shelter_id         BIGINT UNSIGNED,
    FOREIGN KEY (cage_id) REFERENCES Cages (cage_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (shelter_id) REFERENCES Shelters (shelter_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_Dogs_Reference_Number UNIQUE (reference_number)
);

CREATE TABLE AnimalOwners
(
    animal_owner_id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name                     VARCHAR(50)     NOT NULL,
    last_name                      VARCHAR(50)     NOT NULL,
    address                        VARCHAR(255)    NOT NULL,
    email                          VARCHAR(100) DEFAULT NULL,
    phone                          VARCHAR(30)  DEFAULT NULL,
    identification_document_type   VARCHAR(30)     NOT NULL CHECK ( identification_document_type IN ('IDENTITY_CARD', 'PASSPORT')),
    identification_document_number VARCHAR(30)     NOT NULL,
    additional_info                VARCHAR(255) DEFAULT NULL
);

CREATE TABLE AnimalOwnerDogs
(
    animal_owner_dog_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    adoption_date       DATE            NOT NULL,
    animal_owner_id     BIGINT UNSIGNED NOT NULL,
    dog_id              BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (animal_owner_id) REFERENCES AnimalOwners (animal_owner_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (dog_id) REFERENCES Dogs (dog_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AnimalOwnerDogs_Dog_Id UNIQUE (dog_id)
);

CREATE TABLE DogMeals
(
    dog_meal_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    served_at   DATETIME        NOT NULL,
    dog_id      BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (dog_id) REFERENCES Dogs (dog_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE DogFoodDogMeals
(
    dog_food_dog_meal_id BIGINT UNSIGNED        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    portion              DECIMAL(9, 3) UNSIGNED NOT NULL,
    dog_food_id          BIGINT UNSIGNED        NOT NULL,
    dog_meal_id          BIGINT UNSIGNED        NOT NULL,
    FOREIGN KEY (dog_food_id) REFERENCES DogFood (dog_food_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (dog_meal_id) REFERENCES DogMeals (dog_meal_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE DogWalks
(
    dog_walk_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    started_at  DATETIME        NOT NULL,
    duration    INT             NOT NULL,
    dog_id      BIGINT UNSIGNED NOT NULL,
    employee_id BIGINT UNSIGNED,
    FOREIGN KEY (dog_id) REFERENCES Dogs (dog_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES Employees (employee_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE Diseases
(
    disease_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)    NOT NULL,
    CONSTRAINT UQ_Diseases_Name UNIQUE (name)
);

CREATE TABLE DogDiseases
(
    dog_disease_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    dog_id         BIGINT UNSIGNED NOT NULL,
    disease_id     BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (dog_id) REFERENCES Dogs (dog_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (disease_id) REFERENCES Diseases (disease_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Medicines
(
    medicine_id  BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)    NOT NULL,
    manufacturer VARCHAR(100)    NOT NULL,
    dose         VARCHAR(255)    NOT NULL,
    composition  VARCHAR(255)    NOT NULL,
    description  VARCHAR(255) DEFAULT NULL
);

CREATE TABLE WarehouseMedicines
(
    warehouse_medicine_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    in_stock              INT             NOT NULL,
    warehouse_id          BIGINT UNSIGNED NOT NULL,
    medicine_id           BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses (warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES Medicines (medicine_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE DogExaminations
(
    dog_examination_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    dog_weight         FLOAT(4, 1)  DEFAULT NULL,
    description        VARCHAR(255) DEFAULT NULL,
    examination_date   DATE            NOT NULL,
    dog_id             BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (dog_id) REFERENCES Dogs (dog_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE DogExaminationDiseases
(
    dog_examination_disease_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    dog_examination_id         BIGINT UNSIGNED NOT NULL,
    disease_id                 BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (dog_examination_id) REFERENCES DogExaminations (dog_examination_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (disease_id) REFERENCES Diseases (disease_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Vaccines
(
    vaccine_id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100)    NOT NULL,
    manufacturer VARCHAR(100)    NOT NULL,
    dose         VARCHAR(255)    NOT NULL,
    composition  VARCHAR(255)    NOT NULL,
    description  VARCHAR(255) DEFAULT NULL
);

CREATE TABLE WarehouseVaccines
(
    warehouse_vaccine_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    in_stock             INT             NOT NULL,
    warehouse_id         BIGINT UNSIGNED NOT NULL,
    vaccine_id           BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (warehouse_id) REFERENCES Warehouses (warehouse_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (vaccine_id) REFERENCES Vaccines (vaccine_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE DogVaccinations
(
    dog_vaccination_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    applied_at         DATE            NOT NULL,
    vaccine_id         BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (vaccine_id) REFERENCES Vaccines (vaccine_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
