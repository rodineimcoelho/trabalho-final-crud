package model;

import java.math.BigInteger;

public class Fornecedor extends Pessoa{
    private long cnpj;


    public Fornecedor(String nome, Endereco endereco, long cnpj){
        setNome(nome);
        setEndereco(endereco);
        setCnpj(cnpj);
    }

    public Fornecedor(int id, String nome, Endereco endereco, long cnpj){
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setCnpj(cnpj);
    }

    public long getCnpj() {
        return cnpj;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }

    public String getFormattedCnpj() {
        String cnpj = Long.toString(getCnpj());

        return cnpj.substring(0, 2) +
                ". " + cnpj.substring(2, 5) +
                ". " + cnpj.substring(5, 8) +
                "/" + cnpj.substring(8, 12) +
                "-" + cnpj.substring(12, 14);
    }
}
