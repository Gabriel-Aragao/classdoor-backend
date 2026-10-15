-- =======================================================
-- CLASSDOOR BACKEND - INITIAL SCHEMA MIGRATION (Flyway V1)
-- Target: PostgreSQL 16+
-- =======================================================

-- 1. Table USERS
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- 2. Table PROFESSORS
CREATE TABLE IF NOT EXISTS professors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    bio VARCHAR(1000),
    avatar_url VARCHAR(255),
    average_rating DOUBLE PRECISION DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    recommendation_percentage DOUBLE PRECISION DEFAULT 0.0
);

-- 3. Table COURSES
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    department VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    average_rating DOUBLE PRECISION DEFAULT 0.0,
    total_reviews INT DEFAULT 0
);

-- 4. Table CLASS_ROOMS (Turmas)
CREATE TABLE IF NOT EXISTS class_rooms (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    professor_id BIGINT NOT NULL REFERENCES professors(id) ON DELETE CASCADE,
    semester VARCHAR(20) NOT NULL,
    is_evaluation_open BOOLEAN NOT NULL DEFAULT FALSE,
    policy VARCHAR(50) NOT NULL DEFAULT 'ANONYMOUS_ONLY',
    enrolled_students_count INT NOT NULL DEFAULT 0
);

-- 5. Table CLASS_ENROLLMENTS (Matrículas / Quórum)
CREATE TABLE IF NOT EXISTS class_enrollments (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL REFERENCES class_rooms(id) ON DELETE CASCADE,
    student_email VARCHAR(255) NOT NULL,
    CONSTRAINT uk_class_enrollment_class_email UNIQUE (class_id, student_email)
);

-- 6. Table REVIEWS (Avaliações com HMAC-SHA256 audit_hash)
CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    professor_id BIGINT NOT NULL REFERENCES professors(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    class_id BIGINT NOT NULL REFERENCES class_rooms(id) ON DELETE CASCADE,
    overall_rating DOUBLE PRECISION NOT NULL,
    difficulty_rating DOUBLE PRECISION NOT NULL,
    recommend BOOLEAN NOT NULL,
    comment VARCHAR(1000) NOT NULL,
    is_anonymous BOOLEAN NOT NULL DEFAULT TRUE,
    author_display_name VARCHAR(150) DEFAULT 'Estudante Anônimo',
    useful_count INT DEFAULT 0,
    audit_hash VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_reviews_professor ON reviews(professor_id);
CREATE INDEX IF NOT EXISTS idx_reviews_class ON reviews(class_id);
CREATE INDEX IF NOT EXISTS idx_class_enrollments_email ON class_enrollments(student_email);
