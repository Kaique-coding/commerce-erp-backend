package com.commerce.apirest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.apirest.dto.LoginRequestDto;
import com.commerce.apirest.dto.LoginResponseDto;
import com.commerce.apirest.enums.PerfilUsuario;
import com.commerce.apirest.repository.UsuarioRepository;
import com.commerce.apirest.util.HashUtil;

@Service
public class LoginService {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	HashUtil hashUtil;

	@Autowired
	TokenService tokenService;

	public LoginResponseDto login(LoginRequestDto dto) {
		var email = dto.getEmail();
		var senha = dto.getSenha();

		if (email == null || senha == null) {
			return new LoginResponseDto(false, null, "Email e senha são obrigatórios.", false, null);
		}

		var usuario = usuarioRepository.findByEmailPrincipal(email);

		if (usuario.isPresent()) {
			var senhaCriptografada = hashUtil.calculateHash(senha, "SHA256");
			var senhaDoUsuario = usuario.get().getSenha();

			if (senhaDoUsuario.equals(senhaCriptografada)) {
				var conta = usuario.get();
				if (!Boolean.TRUE.equals(conta.getAtivo())) {
					return new LoginResponseDto(false, null, "Esta conta está desativada.", false, conta.getPerfilUsuario());
				}
				boolean notificacaoBoasVindas;
				if (conta.getPerfilUsuario() == PerfilUsuario.CLIENTE) {
					// O campo novo permite entregar uma única boas-vindas também a clientes antigos.
					notificacaoBoasVindas = !Boolean.TRUE.equals(conta.getNotificacaoBoasVindasExibida());
					if (notificacaoBoasVindas) {
						conta.setNotificacaoBoasVindasExibida(true);
						conta.setNotificacaoBoasVindasPendente(false);
						usuarioRepository.save(conta);
					}
					var tokenCliente = tokenService.generateToken(email);
					return new LoginResponseDto(true, tokenCliente, "Login realizado com sucesso.", notificacaoBoasVindas, PerfilUsuario.CLIENTE);
				}

				notificacaoBoasVindas = Boolean.TRUE.equals(conta.getNotificacaoBoasVindasPendente());
				if (notificacaoBoasVindas) {
					conta.setNotificacaoBoasVindasPendente(false);
					usuarioRepository.save(conta);
				}

				var token = tokenService.generateToken(email);
				return new LoginResponseDto(true, token, "Login realizado com sucesso.", notificacaoBoasVindas, conta.getPerfilUsuario());
			} else {
				return new LoginResponseDto(false, null, "Senha inválida.", false, null);
			}

		} else {
			return new LoginResponseDto(false, null, "Usuário não encontrado.", false, null);
		}

	}

}
