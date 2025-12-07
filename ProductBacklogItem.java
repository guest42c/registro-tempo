public class ProductBacklogItem extends Tarefa {
    
    private Integer valorDeNegocio;

    public ProductBacklogItem() {
        super();
    } 
        
    public Integer getValorDeNegocio() {
        return valorDeNegocio;
    }

    public void setValorDeNegocio(Integer valorDeNegocio) {
        this.valorDeNegocio = valorDeNegocio;
    }

    @Override
    public String toString() {
        return super.toString() + " Valor:" + getValorDeNegocio();
    }
}
