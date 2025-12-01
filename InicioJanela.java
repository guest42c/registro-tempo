import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InicioJanela extends JFrame implements ActionListener {

    JPanel painelLista;
    List<Tarefa> tarefas = new ArrayList<>();
    Label minhasTarefas;
    List<Button[]> buttons = new ArrayList<>();
    Panel northPanel;
    Button novaTarefa;
    JScrollPane scrollPane;
    TextField tf_Titulo;

    Timer timer;
    TimerTask task;

    public InicioJanela() throws HeadlessException {
        super("Registros de tempo");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void criaJanela() {

        setLayout(new BorderLayout());
        northPanel = new Panel();
        northPanel.setLayout(new GridLayout(5, 3,5,2));
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDateTime.now().format(myFormatObj);
        minhasTarefas = new Label();
        
        northPanel.add(minhasTarefas);
        northPanel.add(new Label(formattedDate, Label.CENTER));
        northPanel.add(new Label());
        
        northPanel.add(new Label("Tipo",Label.RIGHT));
        Tipo[] choices = {Tipo.PBI,Tipo.BUG};
        final JComboBox<Tipo> cb = new JComboBox<>(choices);
        cb.setVisible(true);
        northPanel.add(cb);
        northPanel.add(new Label());
        
        northPanel.add(new Label("Titulo",Label.RIGHT));
        tf_Titulo = new TextField();
        northPanel.add(tf_Titulo);
        northPanel.add(new Label());
        northPanel.add(new Label());

        Panel topButtons = new Panel(new GridLayout(1, 2, 15, 15));
        novaTarefa = new Button("Nova");
        topButtons.add(novaTarefa);
        topButtons.add(new Button("Consultar"));
        northPanel.add(topButtons);
        northPanel.add(new Label());

        add(BorderLayout.NORTH, northPanel);

        Tarefa t1 = new Tarefa();
        t1.setTitulo("US05 - Listar tarefas");
        t1.setTipo(Tipo.PBI);
        this.tarefas.add(t1);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("US03 - Nova tarefa");
        t2.setTipo(Tipo.PBI);
        this.tarefas.add(t2);

        Tarefa t3 = new Tarefa();
        t3.setTitulo("Exibe botão parar com o tempo parado.");
        t3.setTipo(Tipo.BUG);
        this.tarefas.add(t3);

        atualizarTela();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setVisible(true);
    }

    public void atualizarTela() {
        if (this.scrollPane != null) {
            remove(this.scrollPane);
        }
        painelLista = new JPanel();

        painelLista.setLayout(new GridLayout(tarefas.size(), 2));

        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            Label descricao = alternateBackgroundLabel(new Label(t.toString(), Label.CENTER),i);
            painelLista.add(descricao);
            Panel buttonPane = new Panel(new GridLayout(1, 2));
            Button parar = new Button("Parar");
            Button iniciar = new Button("Iniciar");
            if (t.isTrabalhando()) {
                parar.addActionListener(this);
                buttonPane.add(parar);
            } else {
                iniciar.addActionListener(this);
                buttonPane.add(iniciar);
            }
            Button editar = new Button("Editar");
            Button[] botoesTarefa = { iniciar, parar, editar };
            editar.addActionListener(this);
            buttonPane.add(editar);
            buttons.add(i, botoesTarefa);
            painelLista.add(buttonPane);
            painelLista.setBackground(Color.WHITE);
        }

        this.scrollPane = new JScrollPane(painelLista, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(BorderLayout.CENTER, scrollPane);
        revalidate();
        repaint();
    }

    public Label alternateBackgroundLabel(Label label,int linha) {
        if ((linha % 2 == 0)) {
            label.setBackground(Color.LIGHT_GRAY);
        } else {
            label.setBackground(Color.WHITE);
        }
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (e.getSource() == buttons.get(i)[0]) {
                if (isAlgumaTarefaEmAndamento()) {
                    System.out.println("Já existe tarefa em andamento.");
                } else {
                    System.out.println("Iniciar tarefa " + tarefas.get(i).getTitulo());
                    try {
                        tarefas.get(i).iniciar();
                        timer = new Timer();
                        task = new MyCustomTask(tarefas.get(i).getRegistros(),i);
                        timer.scheduleAtFixedRate(task, 0, 1000);
                        atualizarTela();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
            if (e.getSource() == buttons.get(i)[1]) {
                try {
                    tarefas.get(i).parar();
                    timer.cancel();
                    atualizarTela();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                System.out.println("Parar tarefa " + tarefas.get(i).getTitulo());
            }
            if (e.getSource() == buttons.get(i)[2]) {
                System.out.println("Editar tarefa " + tarefas.get(i).getTitulo());
            }
        }
    }

    private boolean isAlgumaTarefaEmAndamento() {
        for (Tarefa t : tarefas) {
            if (t.isTrabalhando()) {
                return true;
            }
        }
        return false;
    }

    class MyCustomTask extends TimerTask {
    private List<Registro> registros;
    private LocalDateTime inicio = LocalDateTime.now();
    int task;

    public MyCustomTask(List<Registro> registros, int task) {
        this.registros = registros;
        this.task = task;
    }

    @Override
    public void run() {
        Duration duration = Duration.between(this.inicio, LocalDateTime.now());
        long hours = duration.toMillis() / (1000 * 60 * 60);
        long remainingMillisAfterHours = duration.toMillis() % (1000 * 60 * 60);
        long minutes = remainingMillisAfterHours / (1000 * 60);
        long remainingMillisAfterMinutes = duration.toMillis() % (1000 * 60);
        long seconds = remainingMillisAfterMinutes / 1000;
        buttons.get(task)[1].setLabel(String.format("Parar (%dh:%dmin:%dseg)", hours, minutes, seconds));
        buttons.get(task)[1].revalidate();
        buttons.get(task)[1].repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }
}

}
