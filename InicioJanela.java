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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InicioJanela extends JFrame implements ActionListener, MouseListener {

    JPanel painelLista;
    List<Tarefa> tarefas = new ArrayList<>();
    Label minhasTarefas;
    List<JLabel> labels = new ArrayList<>();
    List<Button[]> buttons = new ArrayList<>();
    Panel northPanel;
    Button salvarTarefa;
    JScrollPane scrollPane;
    TextField tf_Titulo = new TextField();;
    TextField tf_Gravidade = new TextField();;
    TextField tf_ValorDeNegocio = new TextField();;
    JComboBox<Tipo> cb;
    Label lbl_Gravidade = new Label("Gravidade", Label.RIGHT);;
    Label lbl_Gravidade_space = new Label();
    Label lbl_ValorDeNegocio = new Label("Valor de Negócio", Label.RIGHT);;
    Label lbl_ValorDeNegocio_space = new Label();
    Panel topButtons = new Panel(new GridLayout(1, 2, 15, 15));
    Label lbl_buttonsSpace = new Label();
    Panel southPanel;
    Label mensagem = new Label();

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
        northPanel.setLayout(new GridLayout(5, 3, 5, 2));
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = LocalDateTime.now().format(myFormatObj);
        minhasTarefas = new Label();

        northPanel.add(minhasTarefas);
        northPanel.add(new Label(formattedDate, Label.CENTER));
        northPanel.add(new Label());

        northPanel.add(new Label("Tipo", Label.RIGHT));
        Tipo[] choices = { Tipo.PBI, Tipo.BUG };
        cb = new JComboBox<>(choices);
        cb.setVisible(true);
        cb.addActionListener(this);
        northPanel.add(cb);
        northPanel.add(new Label());

        northPanel.add(new Label("Titulo", Label.RIGHT));
        tf_Titulo = new TextField();
        northPanel.add(tf_Titulo);
        northPanel.add(new Label());

        lbl_ValorDeNegocio = new Label("Valor de negócio", Label.RIGHT);
        northPanel.add(lbl_ValorDeNegocio);
        tf_ValorDeNegocio = new TextField();
        northPanel.add(tf_ValorDeNegocio);
        northPanel.add(lbl_ValorDeNegocio_space);

        salvarTarefa = new Button("Salvar");
        salvarTarefa.addActionListener(this);
        topButtons.add(salvarTarefa);

        northPanel.add(lbl_buttonsSpace);
        northPanel.add(topButtons);
        add(BorderLayout.NORTH, northPanel);

        southPanel = new Panel();
        southPanel.add(mensagem);
        add(BorderLayout.SOUTH, southPanel);

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

        labels = new ArrayList<>();
        for (int i = 0; i < tarefas.size(); i++) {
            Tarefa t = tarefas.get(i);
            JLabel descricao = alternateBackgroundLabel(new JLabel(t.toString(), JLabel.CENTER), i);
            descricao.addMouseListener(this);
            labels.add(descricao);
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
            Button remover = new Button("Remover");
            Button[] botoesTarefa = { iniciar, parar, remover };
            remover.addActionListener(this);
            buttonPane.add(remover);
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

    private void exibirMensagem(String msg) {
        southPanel.remove(mensagem);
        mensagem = new Label(msg, Label.CENTER);
        southPanel.add(mensagem);
        southPanel.revalidate();
        southPanel.repaint();
    }

    public JLabel alternateBackgroundLabel(JLabel label, int linha) {
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
            if (e.getSource() == labels.get(i)) {
                cb.setSelectedItem(tarefas.get(i).getTipo());
                tf_Titulo.setText(tarefas.get(i).getTitulo());
                if (tarefas.get(i).getTipo().equals(Tipo.PBI)) {
                    ProductBacklogItem pbi = (ProductBacklogItem) tarefas.get(i);
                    tf_ValorDeNegocio.setText(pbi.getValorDeNegocio().toString());
                }
                if (tarefas.get(i).getTipo().equals(Tipo.BUG)) {
                    Bug pbi = (Bug) tarefas.get(i);
                    tf_Gravidade.setText(pbi.getGravidade().toString());
                }
            }
            if (e.getSource() == buttons.get(i)[0]) {
                if (isAlgumaTarefaEmAndamento()) {
                    exibirMensagem("Já existe tarefa em andamento.");
                } else {
                    exibirMensagem("Tarefa iniciada:" + tarefas.get(i).getTitulo());
                    try {
                        tarefas.get(i).iniciar();
                        timer = new Timer();
                        task = new MyCustomTask(i);
                        timer.scheduleAtFixedRate(task, 0, 1000);
                        atualizarTela();
                    } catch (Exception e1) {
                        exibirMensagem(e1.getMessage());
                    }
                }
            }
            if (e.getSource() == buttons.get(i)[1]) {
                try {
                    tarefas.get(i).parar();
                    timer.cancel();
                    atualizarTela();
                } catch (Exception exc) {
                    exibirMensagem(exc.getMessage());
                }
                exibirMensagem("Tarefa interrompida: " + tarefas.get(i).getTitulo());
            }
            if (e.getSource() == buttons.get(i)[2]) {
                if (tarefas.get(i).isTrabalhando()) {
                    exibirMensagem("Tarefa em andamento: " + tarefas.get(i).getTitulo());
                } else {
                    exibirMensagem("Tarefa removida: " + tarefas.get(i).getTitulo());
                    tarefas.remove(i);
                    atualizarTela();
                }
            }
        }
        if (e.getSource() == salvarTarefa) {
            int tarefaIndice = existeTarefa(tf_Titulo.getText());
            if (tarefaIndice >= 0) {
                if (tarefas.get(tarefaIndice).isTrabalhando()) {
                    exibirMensagem("Tarefa em andamento. Pare para editar.");
                } else {
                    if (cb.getSelectedItem() == Tipo.PBI) {
                        ProductBacklogItem pbi = new ProductBacklogItem();
                        pbi.setTipo(Tipo.PBI);
                        pbi.setTitulo(tf_Titulo.getText());
                        pbi.setValorDeNegocio(Integer.valueOf(tf_ValorDeNegocio.getText()));
                        pbi.setRegistros(tarefas.get(tarefaIndice).getRegistros());
                        tarefas.set(tarefaIndice, pbi);
                        exibirMensagem("Tarefa alterada: " + pbi.toString());
                    } else if (cb.getSelectedItem() == Tipo.BUG) {
                        Bug bug = new Bug();
                        bug.setTipo(Tipo.BUG);
                        bug.setTitulo(tf_Titulo.getText());
                        bug.setGravidade(Integer.valueOf(tf_Gravidade.getText()));
                        bug.setRegistros(tarefas.get(tarefaIndice).getRegistros());
                        tarefas.set(tarefaIndice, bug);
                        exibirMensagem("Tarefa alterada: " + bug.toString());
                    }
                    //cb.setSelectedItem(Tipo.PBI);
                    tf_Titulo.setText("");
                    tf_Gravidade.setText("");
                    tf_ValorDeNegocio.setText("");
                    northPanel.revalidate();
                    northPanel.repaint();
                }
            } else {
                if (cb.getSelectedItem() == Tipo.PBI) {
                    ProductBacklogItem t = new ProductBacklogItem();
                    t.setTipo(Tipo.valueOf(cb.getSelectedItem().toString()));
                    t.setTitulo(tf_Titulo.getText());
                    t.setValorDeNegocio(Integer.valueOf(tf_ValorDeNegocio.getText()));
                    tarefas.add(t);
                    exibirMensagem("Tarefa criada: " + t.toString());
                } else {
                    Bug t = new Bug();
                    t.setTipo(Tipo.valueOf(cb.getSelectedItem().toString()));
                    t.setTitulo(tf_Titulo.getText());
                    t.setGravidade(Integer.valueOf(tf_Gravidade.getText()));
                    tarefas.add(t);
                    exibirMensagem("Tarefa criada: " + t.toString());
                }
                //cb.setSelectedItem(Tipo.PBI);
                tf_Titulo.setText("");
                tf_Gravidade.setText("");
                tf_ValorDeNegocio.setText("");
                northPanel.revalidate();
                northPanel.repaint();
            }
            atualizarTela();
        }
        if (e.getSource() == this.cb) {
            northPanel.remove(lbl_buttonsSpace);
            northPanel.remove(topButtons);
            if (cb.getSelectedItem() == Tipo.BUG) {
                northPanel.remove(lbl_ValorDeNegocio);
                northPanel.remove(tf_ValorDeNegocio);
                northPanel.remove(lbl_ValorDeNegocio_space);
                northPanel.add(lbl_Gravidade);
                northPanel.add(tf_Gravidade);
                northPanel.add(lbl_Gravidade_space);
            } else if (cb.getSelectedItem() == Tipo.PBI) {
                northPanel.remove(lbl_Gravidade);
                northPanel.remove(tf_Gravidade);
                northPanel.remove(lbl_Gravidade_space);
                northPanel.add(lbl_ValorDeNegocio);
                northPanel.add(tf_ValorDeNegocio);
                northPanel.add(lbl_ValorDeNegocio_space);
            }
            northPanel.add(lbl_buttonsSpace);
            northPanel.add(topButtons);
            northPanel.revalidate();
            northPanel.repaint();
        }
    }

    private int existeTarefa(String titulo) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getTitulo().equals(titulo)) {
                return i;
            }
        }
        return -1;
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
        private final LocalDateTime inicio = LocalDateTime.now();
        int task;

        public MyCustomTask(int task) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (e.getSource() == labels.get(i)) {
                cb.setSelectedItem(tarefas.get(i).getTipo());
                tf_Titulo.setText(tarefas.get(i).getTitulo());
                if (tarefas.get(i).getTipo().equals(Tipo.PBI)) {
                    ProductBacklogItem pbi = (ProductBacklogItem) tarefas.get(i);
                    tf_ValorDeNegocio.setText(pbi.getValorDeNegocio().toString());
                    exibirMensagem("Consultando: " + pbi.toString());
                }
                if (tarefas.get(i).getTipo().equals(Tipo.BUG)) {
                    Bug bug = (Bug) tarefas.get(i);
                    tf_Gravidade.setText(bug.getGravidade().toString());
                    exibirMensagem("Consultando: " + bug.toString());
                }
            }
        }
        northPanel.revalidate();
        northPanel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // do nothing
    }

}
