package com.agenda.itic.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.UsuariResponseDto;
import com.agenda.itic.dto.UsuariTokenDto;
import com.agenda.itic.exception.BadRequestException;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.Rol;
import com.agenda.itic.model.Usuari;
import com.agenda.itic.repository.UsuariRepository;

@Service
public class UsuariService {

    @Autowired
    UsuariRepository usuariRepository;

    @Autowired
    CorreoPermitidoService correoPermitidoService;

    private UsuariResponseDto toDTO(Usuari usuari) {
        return new UsuariResponseDto(
                usuari.getId(),
                usuari.getNom(),
                usuari.getEmail(),
                usuari.getRol().name(),
                usuari.getRol().getPermisos().stream().toList(),
                usuari.getFotoPerfil()
        );
    }

    public List<UsuariResponseDto> getUsuaris() {
        return usuariRepository.findAll().stream().map(user -> toDTO(user)).toList();
    }

    public List<UsuariResponseDto> getUsuarisActius(boolean actiu) {
        return usuariRepository.findByActiu(actiu).stream().map(user -> toDTO(user)).toList();
    }

    public List<UsuariResponseDto> getUsuarisProfes() {
        return usuariRepository.findByRol(Rol.PROFESSOR).stream().map(user -> toDTO(user)).toList();
    }


    // ELIMINAR PARA FINAL
    private Usuari mapToUsuari(UsuariTokenDto usuariRequestDTO) {
        Usuari usuari = new Usuari();
        usuari.setNom(usuariRequestDTO.getNom());
        usuari.setEmail(usuariRequestDTO.getEmail());
        usuari.setRol(getRol(usuariRequestDTO.getEmail()));
        usuari.setActiu(true);
        usuari.setProvider(usuariRequestDTO.getProvider() != null ? usuariRequestDTO.getProvider() : "local");
        usuari.setProviderId(usuariRequestDTO.getProviderId());
        usuari.setFotoPerfil(usuariRequestDTO.getFotoPerfil());
        return usuari;
    }

    private Rol getRol(String email) {
        if (!email.contains("@iticbcn.cat")) {
            throw new BadRequestException("Només s'accepten correus de l'ITIC BCN.");
        }

        try {
            if (correoPermitidoService.getCorreoPermitido(email) != null) {
                return Rol.ADMIN;
            }
        } catch (ResourceNotFoundException e) {
            // No es necesario manejar esta excepción aquí, ya que simplemente significa que el correo no está en la lista blanca.
        }
            
        if (!email.split("@")[0].contains("_")) {
            return Rol.PROFESSOR;
        }

        return Rol.USUARI;
    }   

    // ELIMINAR PARA FINAL
    public UsuariResponseDto createUsuari(UsuariTokenDto usuariRequestDTO) {
        Rol rol = getRol(usuariRequestDTO.getEmail());
        Usuari usuari = mapToUsuari(usuariRequestDTO);
        usuari.setRol(rol);
        return toDTO(usuariRepository.save(usuari));
    }

    // Crearemos un usuario o no, a traves de un token
    public UsuariResponseDto createOrUpdateUsuariFromToken(String token) {
        String email = normalizeEmail(getTokenEmail(token));

        Optional<Usuari> usuariOptional = usuariRepository.findByEmail(email);
        if (usuariOptional.isPresent()) {
            return toDTO(usuariOptional.get());
        }

        Usuari user = new Usuari();
        user.setEmail(email);
        user.setNom(getTokenName(token) == null ? "Desconocido" : getTokenName(token));
        user.setFotoPerfil(getTokenPicture(token));
        user.setRol(getRol(email));
        user.setActiu(true);
        user.setProvider(getTokenProvider(token) == null ? "cognito" : getTokenProvider(token));
        user.setProviderId(getTokenProviderId(token));
        user = usuariRepository.save(user);
        return toDTO(user);
    }

    public UsuariResponseDto updateUsuari(Long id, UsuariTokenDto usuariRequestDTO) {
        Usuari usuari = usuariRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuari no trobat"));
            
        usuari.setNom(usuariRequestDTO.getNom());
        usuari.setEmail(usuariRequestDTO.getEmail());
        usuari.setRol(getRol(usuariRequestDTO.getEmail()));
        usuari.setActiu(true);
        usuari.setProvider(
                usuariRequestDTO.getProvider() != null ? usuariRequestDTO.getProvider() : usuari.getProvider());
        usuari.setProviderId(usuariRequestDTO.getProviderId() != null ? usuariRequestDTO.getProviderId()
                : usuari.getProviderId());
        if (usuariRequestDTO.getFotoPerfil() != null) {
            usuari.setFotoPerfil(usuariRequestDTO.getFotoPerfil());
        }
        return toDTO(usuariRepository.save(usuari));
    }

    public void deleteUsuari(Long id) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuari no trobat"));
        usuariRepository.delete(usuari);
    }

    private String getTokenEmail(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        return extractClaim(token, "email");
    }

    private String getTokenName(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        return extractClaim(token, "name");
    }

    private String getTokenPicture(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        return extractClaim(token, "picture");
    }

    private String getTokenProviderId(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        String payload = decodeTokenPayload(token);
        if (payload == null) {
            return null;
        }

        String userId = extractJsonValue(payload, "userId");
        if (userId != null) {
            return userId;
        }

        String cognitoUsername = extractJsonValue(payload, "cognito:username");
        if (cognitoUsername != null) {
            return cognitoUsername;
        }

        String sub = extractJsonValue(payload, "sub");
        if (sub != null) {
            return sub;
        }

        return null;
    }

    private String getTokenProvider(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        String payload = decodeTokenPayload(token);
        if (payload == null) {
            return null;
        }

        String providerName = extractJsonValue(payload, "providerName");
        if (providerName != null) {
            return providerName.toLowerCase(Locale.ROOT);
        }

        return null;
    }

    private String extractClaim(String token, String claimName) {
        String payload = decodeTokenPayload(token);
        if (payload == null) {
            return null;
        }
        return extractJsonValue(payload, claimName);
    }

    private String decodeTokenPayload(String token) {
        String normalizedToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
        String[] parts = normalizedToken.split("\\.");
        if (parts.length != 3) {
            return null;
        }

        byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private String extractJsonValue(String json, String key) {
        String quotedKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(quotedKey);
        if (keyIndex < 0) {
            return null;
        }

        int colonIndex = json.indexOf(':', keyIndex + quotedKey.length());
        if (colonIndex < 0) {
            return null;
        }

        int startIndex = colonIndex + 1;
        while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
            startIndex++;
        }

        if (startIndex >= json.length()) {
            return null;
        }

        if (json.charAt(startIndex) == '"') {
            int endIndex = json.indexOf('"', startIndex + 1);
            if (endIndex < 0) {
                return null;
            }
            return json.substring(startIndex + 1, endIndex);
        }

        int endIndex = startIndex;
        while (endIndex < json.length()) {
            char current = json.charAt(endIndex);
            if (current == ',' || current == '}' || current == ']') {
                break;
            }
            endIndex++;
        }

        String value = json.substring(startIndex, endIndex).trim();
        return value.isBlank() ? null : value;
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

}
