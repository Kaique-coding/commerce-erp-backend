package com.commerce.apirest.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.apirest.dto.LoginRequestDto;
import com.commerce.apirest.dto.LoginResponseDto;
import com.commerce.apirest.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Login", description = "Acesso ao sistema")
public class LoginResource {

	@Autowired
	LoginService loginService;

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Acesso ao sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Login realizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados do acesso inválidos.", content = @Content) })
	public LoginResponseDto save(@RequestBody LoginRequestDto login) {
		return loginService.login(login);
	}
}
