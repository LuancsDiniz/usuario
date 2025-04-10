package com.luanProject.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//lombok
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//data spring JPA Jakarta Entity indica que essa classe é uma tabela
@Entity
//define o nome da tabela
@Table (name = "usuario")
@Builder
public class Usuario implements UserDetails {

    //anotação do ID
    @Id
    //Gera o ID automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Column cria a coluna, e define suas caracteristicas
    @Column(name = "nome", length = 100)
    private String nome;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "senha")
    private String senha;
    //define a natureza da relação de uma tabela com outra, no caso varios endereços para um cliente
    @OneToMany(cascade = CascadeType.ALL)
    //JoinColumn indica como ocorrerá a identificação de uma tabela com a outra.
    @JoinColumn(name = "usuario.id", referencedColumnName = "id")
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
