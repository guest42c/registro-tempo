public class ProductBacklogItem extends Tarefa {
    
    private Integer prioridade;
    private Integer esforço;
    private Integer valorDeNegocio;

    public ProductBacklogItem() {
        super();
    } 
    
    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }

    public Integer getEsforço() {
        return esforço;
    }

    public void setEsforço(Integer esforço) {
        this.esforço = esforço;
    }

    public Integer getValorDeNegocio() {
        return valorDeNegocio;
    }

    public void setValorDeNegocio(Integer valorDeNegocio) {
        this.valorDeNegocio = valorDeNegocio;
    }


}
