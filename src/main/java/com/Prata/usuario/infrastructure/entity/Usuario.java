package com.Prata.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter // Lombok Gera Getter e Setter atutomático
@Setter
@AllArgsConstructor // Lombok cria Contrutor sem argumentos e com argumentos automático
@NoArgsConstructor
@Entity   // Aponta para o Spring que essa é uma tabela do banco de dados
@Table(name = "usuario") // Caso nao coloque o nome da tabela por padrao ele coloca o nome da classe
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gera Id automático
    private Long id;
    @Column(name = "nome", length = 100) // Cria coluna no banco de dados
    private String nome;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "senha")
    private String senha;

    @OneToMany(cascade = CascadeType.ALL) // Um usuario para muitos ou OneToOne para um
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Endereco> enderecos;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private List<Telefone> telefones;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
