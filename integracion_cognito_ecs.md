# Integración de AWS Cognito con ECS/API

Guía para usar Cognito JWT con la API existente en ECS.

---

## 1. Obtener tokens desde Hosted UI

Tras aplicar Terraform, la salida `cognito_hosted_ui_url` proporciona la URL de login.
El navegador redirige al `callback_url` con un `?code=XXXX`.

### Intercambiar el código por tokens

```bash
curl -X POST \
  "https://<project_name>-auth.auth.us-east-1.amazoncognito.com/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "client_id=<cognito_app_client_id>" \
  -d "code=<AUTHORIZATION_CODE>" \
  -d "redirect_uri=https://localhost:3000/callback"
```

Respuesta:
```json
{
  "id_token": "eyJraWQ...",
  "access_token": "eyJraWQ...",
  "refresh_token": "eyJjd...",
  "expires_in": 3600,
  "token_type": "Bearer"
}
```

---

## 2. Enviar JWT a la API en ECS

Incluir el token en la cabecera `Authorization`:

```bash
curl -H "Authorization: Bearer <access_token>" \
  http://<load_balancer_url>/api/recurso
```

---

## 3. Validar JWT en la API (backend)

La API debe verificar:

| Verificación | Valor esperado |
|---|---|
| **Issuer (`iss`)** | `https://cognito-idp.us-east-1.amazonaws.com/<user_pool_id>` |
| **Audience (`aud`)** | ID del App Client |
| **Expiración (`exp`)** | No expirado |
| **Firma** | Válida contra JWKS |

### URL de JWKS (claves públicas)

```
https://cognito-idp.us-east-1.amazonaws.com/<user_pool_id>/.well-known/jwks.json
```

### Ejemplo de validación (Node.js con `jsonwebtoken` + `jwks-rsa`)

```javascript
const jwt = require('jsonwebtoken');
const jwksClient = require('jwks-rsa');

const client = jwksClient({
  jwksUri: `https://cognito-idp.us-east-1.amazonaws.com/${USER_POOL_ID}/.well-known/jwks.json`
});

function getKey(header, callback) {
  client.getSigningKey(header.kid, (err, key) => {
    callback(null, key.getPublicKey());
  });
}

function verifyToken(token) {
  return new Promise((resolve, reject) => {
    jwt.verify(token, getKey, {
      issuer: `https://cognito-idp.us-east-1.amazonaws.com/${USER_POOL_ID}`,
      audience: APP_CLIENT_ID
    }, (err, decoded) => {
      if (err) reject(err);
      else resolve(decoded);
    });
  });
}
```

---

## 4. Atributos personalizados disponibles

| Atributo | Descripción | Ejemplo |
|---|---|---|
| `custom:user_role` | Rol del usuario | `admin`, `usuario` |
| `custom:room_preference` | Preferencia de sala | `sala-A` |

Accesibles en el `id_token` decodificado.

---

## Nota importante

> **No se ha modificado** ningún recurso existente (ECS Task Definition, Service, ALB, etc.).
> Cognito funciona como servicio independiente; la validación de tokens ocurre dentro del código de la aplicación.
