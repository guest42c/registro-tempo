public enum Atividade {
    DESENVOLVIMENTO("Desenvolcimento"),
    TESTE("Teste"),
    REQUISITOS("Requisitos");

    private String descricao;

    private Atividade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

};
