package com.luanProject.usuario.business;

import com.luanProject.usuario.business.converter.UsuarioConverter;
import com.luanProject.usuario.business.dto.EnderecoDTO;
import com.luanProject.usuario.business.dto.TelefoneDTO;
import com.luanProject.usuario.business.dto.UsuarioDTO;
import com.luanProject.usuario.infrastructure.entity.Endereco;
import com.luanProject.usuario.infrastructure.entity.Telefone;
import com.luanProject.usuario.infrastructure.entity.Usuario;
import com.luanProject.usuario.infrastructure.exceptions.ConflictException;
import com.luanProject.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.luanProject.usuario.infrastructure.repository.EnderecoRepository;
import com.luanProject.usuario.infrastructure.repository.TelefoneRepository;
import com.luanProject.usuario.infrastructure.repository.UsuarioRepository;
import com.luanProject.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emailExiste(String email) {
        if (verificaEmailExistente(email)) {
            throw new ConflictException("E-mail já cadastrado" + email);
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado " + email)));
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        //Aqui extraimos o email do token
        String email = jwtUtil.extractEmail(token.substring(7));

        //Aqui é criptografada a denha caso ela n seja nula
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        //Aqui buscamos os dados do usuario atravez do email no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado"));

        //Aqui mesclamos dados do UsuarioDTO, e UsuarioEntity
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        //Aqui salvamos salvamos os dados do usuario no banco de dados, e convertemos para usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long enderecoId, EnderecoDTO dto) {
        Endereco enderecoEntity = enderecoRepository.findById(enderecoId).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado " + enderecoId));

        Endereco endereco = usuarioConverter.updateEndereco(dto, enderecoEntity);

        return usuarioConverter.paraEndercoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long telefoneId, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(telefoneId).orElseThrow(
                () -> new ResourceNotFoundException("Id não encontrado" + telefoneId));

        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    public EnderecoDTO cadastroEndereco(String token, EnderecoDTO enderecoDTO){
        String email = jwtUtil.extractEmail(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado"));

        Endereco enderecoEntity = usuarioConverter.paraEndercoEntity(enderecoDTO, usuario.getId());

        return usuarioConverter.paraEndercoDTO(enderecoRepository.save(enderecoEntity));
    }

    public TelefoneDTO cadastroTelefone(String token, TelefoneDTO telefoneDTO){

        String email = jwtUtil.extractEmail(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado"));

        Telefone telefoneEntity = usuarioConverter.paraTelefoneEntity(telefoneDTO, usuario.getId());

        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefoneEntity));
    }
}
