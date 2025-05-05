package com.Prata.usuario.business;

import com.Prata.usuario.business.converter.UsuarioConverter;
import com.Prata.usuario.business.dto.UsuarioDTO;
import com.Prata.usuario.infrastructure.entity.Usuario;
import com.Prata.usuario.infrastructure.exception.ConflictException;
import com.Prata.usuario.infrastructure.exception.ResourceNotFoundExeption;
import com.Prata.usuario.infrastructure.repository.UsuarioRepository;
import com.Prata.usuario.infrastructure.security.JwtUtil;
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

    public UsuarioDTO salavaUsuario(UsuarioDTO usuarioDTO) {
        emaiExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
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

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto) {
        // Busca email do usuario atraves do token (tira a obrigatoriedade de email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));


        // Busca os dados so usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundExeption("Email não localizado"));
        // Criptografia de senha
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // Mesclou os dados que recebemos na requisicao DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);


        // Salvou os dados convertido e depois pegou o retorno e converteu para Usuario DTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

}
