// ============================================================
// LOGIN.JS — Gestió del Cognito Sign-In a la pàgina de login
// ============================================================

// Usamos el CONFIG centralizado que ya tenemos en js/config.js
const API_BASE = CONFIG.API_BASE;
const COGNITO_DOMAIN = CONFIG.COGNITO.DOMAIN; // Lo añadiré ahora al config.js
const COGNITO_CLIENT_ID = CONFIG.COGNITO.APP_CLIENT_ID;
const REDIRECT_URI = CONFIG.COGNITO.REDIRECT_URI;

// ---------- GESTIÓ DEL TOKEN ----------
function getToken()        { return localStorage.getItem('agenda_token'); }
function setToken(token)   { localStorage.setItem('agenda_token', token); }


// PKCE (Proof Key for Code Exchange) és necessari per clients públics (SPA sense backend secret)
function generateCodeVerifier() {
    const array = new Uint8Array(32);
    crypto.getRandomValues(array);
    return btoa(String.fromCharCode(...array))
        .replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
}

async function generateCodeChallenge(verifier) {
    const encoded = new TextEncoder().encode(verifier);
    const hash    = await crypto.subtle.digest('SHA-256', encoded);
    return btoa(String.fromCharCode(...new Uint8Array(hash)))
        .replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '');
}

// ---------- REDIRIGIR A COGNITO ----------
async function loginWithCognito() {
    const verifier   = generateCodeVerifier();
    const challenge  = await generateCodeChallenge(verifier);

    // Guardem el verifier per quan torni el callback
    sessionStorage.setItem('pkce_verifier', verifier);
    console.log('REDIRECT_URI:', REDIRECT_URI);

    const url = new URL(`https://${COGNITO_DOMAIN}/oauth2/authorize`);
    url.searchParams.set('client_id',             COGNITO_CLIENT_ID);
    url.searchParams.set('response_type',         'code');
    url.searchParams.set('scope',                 'openid email profile');
    url.searchParams.set('redirect_uri',          REDIRECT_URI);
    url.searchParams.set('code_challenge',        challenge);
    url.searchParams.set('code_challenge_method', 'S256');
    
    // Forzamos el uso de Google como Identity Provider
    url.searchParams.set('identity_provider',     'Google');

    window.location.href = url.toString();
}

// ---------- GESTIÓ DEL CALLBACK DE COGNITO ----------
async function handleCallback() {
    const params = new URLSearchParams(window.location.search);
    const code   = params.get('code');
    const error  = params.get('error');

    if (error) {
        document.getElementById('login-error').style.display = 'block';
        window.history.replaceState({}, '', window.location.pathname);
        return false; 
    }
    if (!code) return false; 

    const errorEl   = document.getElementById('login-error');
    const loadingEl = document.getElementById('login-loading');
    errorEl.style.display   = 'none';
    loadingEl.style.display = 'flex';

    const verifier = sessionStorage.getItem('pkce_verifier');
    sessionStorage.removeItem('pkce_verifier');

    try {
        console.log("Iniciant intercanvi de codi per token...");
        // 1. Intercanviar el codi pel token de Cognito
        const tokenRes = await fetch(`https://${COGNITO_DOMAIN}/oauth2/token`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body:    new URLSearchParams({
                grant_type:    'authorization_code',
                client_id:     COGNITO_CLIENT_ID,
                code,
                redirect_uri:  REDIRECT_URI,
                code_verifier: verifier   
            })
        });

        if (!tokenRes.ok) {
            const errData = await tokenRes.text();
            console.error("Error Cognito:", errData);
            throw new Error('Error Cognito: ' + tokenRes.status);
        }

        const tokens  = await tokenRes.json();
        const idToken = tokens.id_token; 
        console.log("Token obtingut de Cognito. Cridant a l'API...");

        // 2. Registrar/actualitzar l'usuari a l'API con el token de Cognito
        const apiRes = await fetch(`${API_BASE}/usuaris/token`, {
            method:  'POST',
            headers: { 'Authorization': `Bearer ${idToken}` }
        });

        if (!apiRes.ok) {
            const errData = await apiRes.text();
            console.error("Error API:", apiRes.status, errData);
            throw new Error('Error API: ' + apiRes.status);
        }

        console.log("API acceptada. Guardant token...");
        // 3. Guardar el token i redirigir
        setToken(idToken);
        window.history.replaceState({}, '', window.location.pathname); 
        window.location.href = 'admin.html';

    } catch (e) {
        console.error("Error d'autenticació:", e);
        loadingEl.style.display = 'none';
        errorEl.style.display   = 'block';
        errorEl.innerHTML = `⚠ ${e.message}. Contacta amb el suport.`;
    }

    return true;
}

// ---------- ARRENCADA ----------
document.addEventListener('DOMContentLoaded', async function () {
    if (getToken()) {
        window.location.href = 'admin.html';
        return;
    }

    const handled = await handleCallback();
    if (handled) return;

    const btn = document.getElementById('cognito-signin-btn');
    if (btn) {
        btn.addEventListener('click', loginWithCognito);
    }
});