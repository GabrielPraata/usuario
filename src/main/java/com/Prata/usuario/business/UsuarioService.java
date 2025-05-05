package com.Prata.usuario.business;

import com.Prata.usuario.business.converter.UsuarioConverter;
import com.Prata.usuario.business.dto.UsuarioDTO;
import com.Prata.usuario.infrastructure.entity.Usuario;
import com.Prata.usuario.infrastructure.exception.ConflictException;
import com.Prata.usuario.infrastructure.exception.ResourceNotFoundExeption;
import com.Prata.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;

    public UsuarioDTO salavaUsuario(UsuarioDTO usuarioDTO) {
        emaiExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void emaiExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        }catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundExeption("Email nao encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

}
