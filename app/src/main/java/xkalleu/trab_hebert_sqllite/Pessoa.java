package xkalleu.trab_hebert_sqllite;

/**
 * Created by The_H on 15/10/2017.
 */

class Pessoa {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String cpf;
    private int codigo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setCodigo(int codigo){
        this.codigo=codigo;
    }

    public int getCodigo(){
        return this.codigo;
    }

    @Override
    public String toString() {
        return  nome;
    }
}
