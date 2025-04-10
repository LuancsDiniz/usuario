package com.luanProject.usuario.business.converter;

import com.luanProject.usuario.business.dto.EnderecoDTO;
import com.luanProject.usuario.business.dto.TelefoneDTO;
import com.luanProject.usuario.business.dto.UsuarioDTO;
import com.luanProject.usuario.infrastructure.entity.Endereco;
import com.luanProject.usuario.infrastructure.entity.Telefone;
import com.luanProject.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){
        return Usuario.builder().
                nome(usuarioDTO.getNome()).
                email(usuarioDTO.getEmail()).
                senha(usuarioDTO.getSenha()).
                enderecos(paraListaEndereco(usuarioDTO.getEnderecos())).
                telefones(paraListaTelefone(usuarioDTO.getTelefones())).
                build();

    }

    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){
        return enderecoDTOS.stream().map(this::paraEnderco).toList();
    }

    public Endereco paraEnderco(EnderecoDTO enderecoDTO) {
        return Endereco.builder().
                rua(enderecoDTO.getRua()).
                numero(enderecoDTO.getNumero()).
                complemento(enderecoDTO.getComplemento()).
                cidade(enderecoDTO.getCidade()).
                estado(enderecoDTO.getEstado()).
                cep(enderecoDTO.getCep()).
                build();
    }

    public List<Telefone> paraListaTelefone(List<TelefoneDTO> telefoneDTO){
        return telefoneDTO.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return  Telefone.builder().
                numero(telefoneDTO.getNumero()).
                ddd(telefoneDTO.getDdd()).
                build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder().
                nome(usuario.getNome()).
                email(usuario.getEmail()).
                senha(usuario.getSenha()).
                enderecos(paraListaEnderecoDTO(usuario.getEnderecos())).
                telefones(paraListaTelefoneDTO(usuario.getTelefones())).
                build();

    }

    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> endereco){
        return endereco.stream().map(this::paraEndercoDTO).toList();
    }

    public EnderecoDTO paraEndercoDTO(Endereco endereco) {
        return EnderecoDTO.builder().
                rua(endereco.getRua()).
                numero(endereco.getNumero()).
                complemento(endereco.getComplemento()).
                cidade(endereco.getCidade()).
                estado(endereco.getEstado()).
                cep(endereco.getCep()).
                build();
    }

    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone> telefone){
        return telefone.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder().
                numero(telefone.getNumero()).
                ddd(telefone.getDdd()).
                build();
    }
}
