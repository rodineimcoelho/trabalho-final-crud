package model;

public class Funcionario extends Pessoa{
    private String cargo;

    public Funcionario(String nome, Endereco endereco, String cargo){
        setNome(nome);
        setEndereco(endereco);
        setCargo(cargo);
    }

    public Funcionario(int id, String nome, Endereco endereco, String cargo){
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setCargo(cargo);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
