package com.classdoor.backend.config;

import com.classdoor.backend.model.Course;
import com.classdoor.backend.model.Professor;
import com.classdoor.backend.repository.CourseRepository;
import com.classdoor.backend.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public DataInitializer(ProfessorRepository professorRepository, CourseRepository courseRepository) {
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (professorRepository.count() == 0) {
            professorRepository.save(new Professor(
                    "Dr. Carlos Eduardo Santos",
                    "Ciência da Computação",
                    "carlos.santos@university.edu",
                    "Especialista em Inteligência Artificial e Engenharia de Software.",
                    "https://randomuser.me/api/portraits/men/32.jpg",
                    4.8,
                    45,
                    95.0
            ));
            professorRepository.save(new Professor(
                    "Dra. Ana Paula Mendes",
                    "Matemática",
                    "ana.mendes@university.edu",
                    "Pesquisadora em Álgebra Linear Avançada e Métodos Numéricos.",
                    "https://randomuser.me/api/portraits/women/44.jpg",
                    4.6,
                    30,
                    90.0
            ));
            professorRepository.save(new Professor(
                    "Prof. Marcos Vinicius Lima",
                    "Engenharia de Software",
                    "marcos.lima@university.edu",
                    "Especialista em Arquitetura de Microsserviços e Sistemas Distribuídos.",
                    "https://randomuser.me/api/portraits/men/65.jpg",
                    4.9,
                    60,
                    98.0
            ));
        }

        if (courseRepository.count() == 0) {
            courseRepository.save(new Course(
                    "CIC0100",
                    "Estruturas de Dados e Algoritmos",
                    "Ciência da Computação",
                    "Estudo de estruturas avançadas de dados, árvores, grafos e algoritmos de ordenação.",
                    4.7,
                    50
            ));
            courseRepository.save(new Course(
                    "MAT0025",
                    "Cálculo Diferencial e Integral II",
                    "Matemática",
                    "Funções de várias variáveis, integrais múltiplas e cálculo vetorial.",
                    4.2,
                    40
            ));
            courseRepository.save(new Course(
                    "ENG0350",
                    "Arquitetura de Software",
                    "Engenharia de Software",
                    "Padrões de projeto, Clean Architecture, microsserviços e governança de APIs.",
                    4.9,
                    75
            ));
        }
    }
}
