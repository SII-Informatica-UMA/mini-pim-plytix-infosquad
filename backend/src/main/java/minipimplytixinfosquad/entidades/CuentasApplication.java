package minipimplytixinfosquad.entidades;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CuentasApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CuentasApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("🔧 Aplicación iniciada correctamente.");
        System.out.println("📄 El esquema de la base de datos se ha generado automáticamente (DDL).");
    }
}
