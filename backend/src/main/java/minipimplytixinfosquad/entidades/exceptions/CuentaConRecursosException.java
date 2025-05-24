package minipimplytixinfosquad.entidades.exceptions;

public class CuentaConRecursosException extends RuntimeException {
    public CuentaConRecursosException() {
        super("La cuenta tiene productos, categorías, relaciones o activos asociados");
    }

    public CuentaConRecursosException(String mensaje) {
        super(mensaje);
    }
}
