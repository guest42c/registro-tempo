public class Bug extends Tarefa {
    
    private Integer gravidade;

    public Bug() {
        super();
    }

    public Integer getGravidade() {
        return gravidade;
    }

    public void setGravidade(Integer gravidade) {
        this.gravidade = gravidade;
    } 

    @Override
    public String toString() {
        return super.toString() + " Gravidade:" + getGravidade();
    }    
    
}
