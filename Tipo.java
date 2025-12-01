public enum Tipo {
    PBI("PBI"), BUG("Bug");

    private final String descricao;

    Tipo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return this.descricao;
    }
};