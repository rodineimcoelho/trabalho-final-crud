package model;

public class Endereco {
    private int id;
    private String rua;
    private String uf;
    private String cidade;
    private String cep;

    public Endereco(String rua, String uf, String cidade, String cep){
        setRua(rua);
        setUf(uf);
        setCidade(cidade);
        setCep(cep);
    }

    public Endereco(int id, String rua, String uf, String cidade, String cep){
        setId(id);
        setRua(rua);
        setUf(uf);
        setCidade(cidade);
        setCep(cep);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String toString(){
        return rua +", " +cidade +" - " +uf +" - " +cep;
    }
}
