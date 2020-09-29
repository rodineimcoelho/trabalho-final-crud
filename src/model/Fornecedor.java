package model;

public class Fornecedor extends Pessoa{
    private String cnpj;


    public Fornecedor(String nome, Endereco endereco, String cnpj){
        setNome(nome);
        setEndereco(endereco);
        setCnpj(cnpj);
    }

    public Fornecedor(int id, String nome, Endereco endereco, String cnpj){
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setCnpj(cnpj);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFormattedCnpj() {
        String cnpj = getCnpj();

        return cnpj.substring(0, 2) +
                ". " + cnpj.substring(2, 5) +
                ". " + cnpj.substring(5, 8) +
                "/" + cnpj.substring(8, 12) +
                "-" + cnpj.substring(12, 14);
    }
}
