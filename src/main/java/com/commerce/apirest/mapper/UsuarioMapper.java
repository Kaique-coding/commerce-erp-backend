
package com.commerce.apirest.mapper;

import org.springframework.stereotype.Component;

import com.commerce.apirest.dto.UsuarioRequestDto;
import com.commerce.apirest.dto.UsuarioResponseDto;
import com.commerce.apirest.models.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioResponseDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();

        dto.setId(usuario.getId());
        dto.setUsuario(usuario.getUsuario());
        dto.setNome(usuario.getNome());
        dto.setSobrenome(usuario.getSobrenome());
        dto.setEmailPrincipal(usuario.getEmailPrincipal());
        dto.setEmailSecundario(usuario.getEmailSecundario());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setCpf(usuario.getCpf());
        dto.setRg(usuario.getRg());
        dto.setTelefonePrincipal(usuario.getTelefonePrincipal());
        dto.setTelefoneSecundario(usuario.getTelefoneSecundario());
        dto.setPerfilUsuario(usuario.getPerfilUsuario());
        dto.setGenero(usuario.getGenero());
        dto.setAtivo(usuario.getAtivo());
        dto.setCriadoPor(usuario.getCriadoPor());
        dto.setCriadoEm(usuario.getCriadoEm());

        return dto;
    }

    public Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setUsuario(dto.getUsuario());
        usuario.setNome(dto.getNome());
        usuario.setSobrenome(dto.getSobrenome());
        usuario.setEmailPrincipal(dto.getEmailPrincipal());
        usuario.setEmailSecundario(dto.getEmailSecundario());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setCpf(dto.getCpf());
        usuario.setRg(dto.getRg());
        usuario.setSenha(dto.getSenha());
        usuario.setTelefonePrincipal(dto.getTelefonePrincipal());
        usuario.setTelefoneSecundario(dto.getTelefoneSecundario());
        usuario.setPerfilUsuario(dto.getPerfilUsuario());
        usuario.setGenero(dto.getGenero());
        usuario.setAtivo(dto.getAtivo());

        return usuario;
    }
}
