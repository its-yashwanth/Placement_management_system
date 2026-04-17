CREATE TABLE user_accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    university_roll_no VARCHAR(255) NOT NULL UNIQUE,
    branch VARCHAR(255) NOT NULL,
    cgpa DOUBLE NOT NULL,
    skills VARCHAR(1000) NOT NULL,
    resume_url VARCHAR(255),
    placement_status VARCHAR(50) NOT NULL,
    user_account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_student_user FOREIGN KEY (user_account_id) REFERENCES user_accounts(id)
);

CREATE TABLE companies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    website VARCHAR(255),
    approved BOOLEAN NOT NULL,
    user_account_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_company_user FOREIGN KEY (user_account_id) REFERENCES user_accounts(id)
);

CREATE TABLE placement_drives (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    eligible_branch VARCHAR(255) NOT NULL,
    minimum_cgpa DOUBLE NOT NULL,
    location VARCHAR(255) NOT NULL,
    drive_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    company_id BIGINT NOT NULL,
    CONSTRAINT fk_drive_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    drive_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    applied_at DATETIME NOT NULL,
    interview_at DATETIME NULL,
    CONSTRAINT fk_application_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_application_drive FOREIGN KEY (drive_id) REFERENCES placement_drives(id)
);
