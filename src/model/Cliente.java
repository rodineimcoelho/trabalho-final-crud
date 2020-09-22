package model;

public class Cliente extends Pessoa{
    private String nomeUsuario;
    private String senha;

    public Cliente (int id, String nome, Endereco endereco, String nomeUsuario, String senha){
        setId(id);
        setNome(nome);
        setEndereco(endereco);
        setNomeUsuario(nomeUsuario);
        setSenha(senha);
    }


    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
