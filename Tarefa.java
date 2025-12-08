import java.time.Duration;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Tarefa {
    private Tipo tipo;
    private String titulo = "Título";
    private boolean trabalhando = false;
    private Registro atual;

    private List<Registro> registros = new ArrayList<>();

    public Tarefa() {
    }

    public void iniciar() throws Exception{
        if (isTrabalhando()) {
            throw new Exception("Tarefa já esta em andamento");
        } else {
            setTrabalhando(true);
            this.atual = new Registro();
            this.atual.iniciar();
        }
    }

    public void parar() throws Exception {
        if (isTrabalhando()) {
            setTrabalhando(false);
            this.atual.parar();
            registros.add(this.atual);
        } else {
            System.out.println(toString());
            throw new Exception("Tarefa não esta em andamento");
        }
    }

    @Override
    public String toString() {
        return getTipo().toString().concat(" ").concat(getTitulo()).concat(" (").concat(getTempoTotal()).concat(")");
    }
    
    public String getTempoTotal() {
        long tempoTotalMillis = 0;
        for (Registro r:registros) {
            Duration duration = Duration.between(r.getInicio(), r.getFim());
            tempoTotalMillis =+ tempoTotalMillis + duration.toMillis(); 
        }
        long hours = tempoTotalMillis / (1000 * 60 * 60);
        long remainingMillisAfterHours = tempoTotalMillis % (1000 * 60 * 60);
        long minutes = remainingMillisAfterHours / (1000 * 60);
        long remainingMillisAfterMinutes = tempoTotalMillis % (1000 * 60);
        long seconds = remainingMillisAfterMinutes / 1000;
        return String.format("%dh:%dmin:%dseg", hours, minutes, seconds);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new InputMismatchException("Titulo não pode ser vazio.");
        }
        this.titulo = titulo;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    private void setTrabalhando(boolean trabalhando) {
        this.trabalhando = trabalhando;
    }

    public boolean isTrabalhando() {
        return trabalhando;
    }
    
}
