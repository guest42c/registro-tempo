
import java.time.LocalDateTime;

public class Registro {

    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Registro() {
    }

    public void iniciar() {
        this.inicio = LocalDateTime.now();
    }

    public void parar() {
        this.fim = LocalDateTime.now();
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

}
