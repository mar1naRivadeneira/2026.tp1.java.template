package com.bibliotech;

import com.bibliotech.exception.BibliotecaException;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // Repositories
        MemoriaSocioRepository socioRepo = new MemoriaSocioRepository();
        MemoriaRecursoRepository recursoRepo = new MemoriaRecursoRepository();
        MemoriaPrestamoRepository prestamoRepo = new MemoriaPrestamoRepository();
        // Services
        SocioService socioService = new SocioService(socioRepo);
        RecursoService recursoService = new RecursoService(recursoRepo);
        PrestamoService prestamoService =
                new PrestamoService(recursoRepo, socioRepo, prestamoRepo);

        cargarDatosPrueba(socioService, recursoService);

        boolean salir = false;

        while (!salir) {

            System.out.println("\n========== BIBLIOTECH CORE ==========");
            System.out.println("1. Gestión de Recursos");
            System.out.println("2. Gestión de Socios");
            System.out.println("3. Gestión de Préstamos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {

                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> menuRecursos(recursoService);
                    case 2 -> menuSocios(socioService, socioRepo);
                    case 3 -> menuPrestamos(prestamoService, prestamoRepo);
                    case 0 -> {
                        salir = true;
                        System.out.println("📚 Cerrando BiblioTech...");
                    }
                    default -> System.out.println("⚠️ Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número.");
            }
        }
    }

    // =========================================================
    // RECURSOS
    // =========================================================

    private static void menuRecursos(RecursoService service) {
        boolean volver = false;
        while (!volver) {

            System.out.println("\n--- GESTIÓN DE RECURSOS ---");
            System.out.println("1. Buscar por Título");
            System.out.println("2. Buscar por Autor");
            System.out.println("3. Buscar por Categoría");
            System.out.println("4. Registrar Nuevo Recurso");
            System.out.println("5. Ver Catálogo Completo");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> buscarPorTitulo(service);
                    case 2 -> buscarPorAutor(service);
                    case 3 -> buscarPorCategoria(service);
                    case 4 -> registrarRecurso(service);
                    case 5 -> mostrarResultados(service.buscarTodos());
                    case 0 -> volver = true;
                    default -> System.out.println("⚠️ Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número.");
            }
        }
    }

    private static void buscarPorTitulo(RecursoService service) {
        System.out.print("Título a buscar: ");
        String titulo = scanner.nextLine();
        mostrarResultados(service.buscarPorTitulo(titulo));
    }

    private static void buscarPorAutor(RecursoService service) {
        System.out.print("Autor a buscar: ");
        String autor = scanner.nextLine();
        mostrarResultados(service.buscarPorAutor(autor));
    }

    private static void buscarPorCategoria(RecursoService service) {
        System.out.println("\nCategorías:");
        for (int i = 0; i < Categoria.values().length; i++) {
            System.out.println((i + 1) + ". " + Categoria.values()[i]);
        }

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion < 1 || opcion > Categoria.values().length) {
                throw new IllegalArgumentException("Categoría inválida.");
            }

            Categoria categoria = Categoria.values()[opcion - 1];
            mostrarResultados(service.buscarPorCategoria(categoria));

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Debe ingresar un número.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void registrarRecurso(RecursoService service) {
        try {
            System.out.println("\nTipo de recurso:");
            System.out.println("1. Libro Físico");
            System.out.println("2. Ebook");
            System.out.print("Seleccione: ");
            String tipo = scanner.nextLine();
            if (!tipo.equals("1") && !tipo.equals("2")) {
                throw new IllegalArgumentException("Tipo inválido.");
            }

            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("Título: ");
            String titulo = scanner.nextLine();

            System.out.print("Autor: ");
            String autor = scanner.nextLine();

            System.out.print("Año: ");
            int anio = Integer.parseInt(scanner.nextLine());

            System.out.println("\nCategorías:");

            for (int i = 0; i < Categoria.values().length; i++) {
                System.out.println((i + 1) + ". " + Categoria.values()[i]);
            }

            System.out.print("Seleccione categoría: ");
            int categoriaSeleccionada = Integer.parseInt(scanner.nextLine());
            if (categoriaSeleccionada < 1 ||
                    categoriaSeleccionada > Categoria.values().length) {

                throw new IllegalArgumentException("Categoría inválida.");
            }

            Categoria categoria =
                    Categoria.values()[categoriaSeleccionada - 1];

            if (tipo.equals("1")) {

                LibroFisico libro = new LibroFisico(
                        isbn,
                        titulo,
                        autor,
                        anio,
                        categoria
                );

                service.registrar(libro);

            } else {

                System.out.print("Formato archivo (PDF/EPUB): ");
                String formato = scanner.nextLine();

                System.out.print("Tamaño MB: ");
                double tamanio = Double.parseDouble(scanner.nextLine());

                Ebook ebook = new Ebook(
                        isbn,
                        titulo,
                        autor,
                        anio,
                        categoria,
                        formato,
                        tamanio
                );

                service.registrar(ebook);
            }

            System.out.println("✅ Recurso registrado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("⚠️ Formato numérico inválido.");
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }

    private static void mostrarResultados(List<Recurso> recursos) {

        if (recursos.isEmpty()) {
            System.out.println("No se encontraron resultados.");
            return;
        }

        System.out.println("\n--- RESULTADOS ---");

        for (Recurso r : recursos) {

            String tipo = (r instanceof Ebook)
                    ? "EBOOK"
                    : "FÍSICO";

            System.out.println(
                    "[" + tipo + "] "
                            + r.titulo()
                            + " | "
                            + r.autor()
                            + " | "
                            + r.categoria()
                            + " | ISBN: "
                            + r.isbn()
            );
        }
    }

    // =========================================================
    // SOCIOS
    // =========================================================

    private static void menuSocios(
            SocioService service,
            MemoriaSocioRepository repo
    ) {

        boolean volver = false;

        while (!volver) {

            System.out.println("\n--- GESTIÓN DE SOCIOS ---");
            System.out.println("1. Registrar Socio");
            System.out.println("2. Ver Socios");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {

                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {

                    case 1 -> registrarSocio(service);

                    case 2 -> listarSocios(repo);

                    case 0 -> volver = true;

                    default -> System.out.println("⚠️ Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número.");
            }
        }
    }

    private static void registrarSocio(SocioService service) {

        try {

            System.out.print("ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("DNI: ");
            String dni = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.println("Tipo:");
            System.out.println("1. Estudiante");
            System.out.println("2. Docente");
            System.out.print("Seleccione: ");

            String tipo = scanner.nextLine();

            if (!tipo.equals("1") && !tipo.equals("2")) {
                throw new IllegalArgumentException("Tipo inválido.");
            }

            TipoSocio tipoSocio =
                    tipo.equals("1")
                            ? TipoSocio.ESTUDIANTE
                            : TipoSocio.DOCENTE;

            Socio socio = new Socio(
                    id,
                    nombre,
                    dni,
                    email,
                    tipoSocio
            );

            service.registrarSocio(socio);

            System.out.println(
                    "✅ Socio registrado. Límite: "
                            + tipoSocio.getLimiteLibros()
                            + " libros."
            );

        } catch (BibliotecaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void listarSocios(MemoriaSocioRepository repo) {

        List<Socio> socios = repo.buscarTodos();

        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
            return;
        }

        System.out.println("\n--- SOCIOS REGISTRADOS ---");

        for (Socio s : socios) {

            System.out.println(
                    s.nombre()
                            + " | "
                            + s.tipoSocio()
                            + " | DNI: "
                            + s.dni()
                            + " | Límite: "
                            + s.tipoSocio().getLimiteLibros()
            );
        }
    }

    // =========================================================
    // PRÉSTAMOS
    // =========================================================

    private static void menuPrestamos(
            PrestamoService service,
            MemoriaPrestamoRepository repo
    ) {

        boolean volver = false;

        while (!volver) {

            System.out.println("\n--- GESTIÓN DE PRÉSTAMOS ---");
            System.out.println("1. Realizar Préstamo");
            System.out.println("2. Registrar Devolución");
            System.out.println("3. Ver Historial");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {

                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {

                    case 1 -> realizarPrestamo(service);

                    case 2 -> registrarDevolucion(service);

                    case 3 -> mostrarHistorial(repo);

                    case 0 -> volver = true;

                    default -> System.out.println("⚠️ Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número.");
            }
        }
    }

    private static void realizarPrestamo(PrestamoService service) {

        try {

            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();

            System.out.print("ID Socio: ");
            int socioId = Integer.parseInt(scanner.nextLine());

            service.realizarPrestamo(isbn, socioId);

            System.out.println(
                    "✅ Préstamo exitoso. Fecha límite: "
                            + LocalDate.now().plusDays(7)
            );

        } catch (BibliotecaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ ID inválido.");
        }
    }

    private static void registrarDevolucion(PrestamoService service) {

        try {

            System.out.print("ISBN a devolver: ");
            String isbn = scanner.nextLine();

            service.devolverPrestamo(isbn);

        } catch (BibliotecaException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }

    private static void mostrarHistorial(MemoriaPrestamoRepository repo) {

        List<Prestamo> prestamos = repo.buscarTodos();

        if (prestamos.isEmpty()) {
            System.out.println("No existen transacciones.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE TRANSACCIONES ---");

        for (Prestamo p : prestamos) {

            String tipo = (p.recurso() instanceof Ebook)
                    ? "EBOOK"
                    : "FÍSICO";

            System.out.println(
                    "\nRecurso: "
                            + p.recurso().titulo()
                            + " ["
                            + tipo
                            + "]"
            );

            System.out.println(
                    "Socio: "
                            + p.socio().nombre()
            );

            System.out.println(
                    "Fecha salida: "
                            + p.fechaSalida()
            );

            if (p.getFechaDevolucionReal().isPresent()) {

                LocalDate fechaReal =
                        p.getFechaDevolucionReal().get();

                System.out.print(
                        "Estado: ✅ DEVUELTO el "
                                + fechaReal
                );

                if (fechaReal.isAfter(p.fechaDevolucionPrevista())) {

                    long dias =
                            ChronoUnit.DAYS.between(
                                    p.fechaDevolucionPrevista(),
                                    fechaReal
                            );

                    System.out.println(
                            " ⚠️ Retraso de "
                                    + dias
                                    + " días."
                    );

                } else {
                    System.out.println(" (A tiempo)");
                }

            } else {

                System.out.println(
                        "Estado: ⏳ ACTIVO | Fecha límite: "
                                + p.fechaDevolucionPrevista()
                );
            }

            System.out.println("--------------------------------");
        }
    }


    // DATOS INICIALES

    private static void cargarDatosPrueba(
            SocioService socioService,
            RecursoService recursoService
    ) {

        try {

            socioService.registrarSocio(
                    new Socio(
                            1,
                            "Marina",
                            "20444555",
                            "marina@it.edu.ar",
                            TipoSocio.ESTUDIANTE
                    )
            );

            recursoService.registrar(
                    new LibroFisico(
                            "123",
                            "Clean Code",
                            "Robert Martin",
                            2008,
                            Categoria.PROGRAMACION
                    )
            );

            recursoService.registrar(
                    new Ebook(
                            "456",
                            "Effective Java",
                            "Joshua Bloch",
                            2017,
                            Categoria.PROGRAMACION,
                            "PDF",
                            5.4
                    )
            );

            recursoService.registrar(
                    new LibroFisico(
                            "789",
                            "Sapiens",
                            "Yuval Noah Harari",
                            2011,
                            Categoria.HISTORIA
                    )
            );

            recursoService.registrar(
                    new Ebook(
                            "999",
                            "Cien Años de Soledad",
                            "Gabriel García Márquez",
                            1967,
                            Categoria.FICCION,
                            "EPUB",
                            2.1
                    )
            );

        } catch (BibliotecaException e) {

            System.out.println(
                    "⚠️ Error cargando datos iniciales: "
                            + e.getMessage()
            );
        }
    }
}