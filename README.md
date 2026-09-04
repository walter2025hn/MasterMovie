Master Movie - Android (TV + Mobile) app
=======================================

Descripción
- App para ver películas y series desde un servidor (autenticación por credenciales del servidor).
- Compatible con móvil y TV/TV box (UI orientada a D-Pad/foco).
- Reproducción con ExoPlayer, baja/medio/alto calidad, cambio de resolución en reproducción.
- Historial, Favoritos, Resumir reproducción, carga en lotes de 100, sección "Apoya al creador" con PayPal.
- Al añadir cuenta muestra banner: "APP CREADA POR WALTER, FUNDADOR DEL GRUPO CODIGO MASTER..."

Requerimientos
- Android Studio Bumblebee o superior
- JDK 11+
- Android SDK (minSdkVersion 21, targetSdk 33)

Cómo ocultar la URL del servidor (IMPORTANTE)
1. No pongas la URL en el código. Crea (o edita) el archivo local.properties en la raíz del repo y añade:
   SERVER_URL=http://zonacero.lat:8080
2. El archivo local.properties NUNCA debe subirse a GitHub. Añade local.properties a .gitignore (ya lo incluimos).
3. El app lee la URL desde BuildConfig (gradle) mediante la propiedad SERVER_URL - revisa app/build.gradle.

Icono / Logo
- Coloca el archivo original ic_master_movie.png en app/src/main/res/drawable/ic_master_movie.png. También incluimos adaptative icon XMLs que referencian este drawable.

Compilar y generar APK
- Abrir el proyecto en Android Studio -> Build -> Make Project
- Build -> Build Bundle(s) / APK(s) -> Build APK(s)

Notas
- Las llamadas a autenticación y lista de películas esperan endpoints REST JSON en el servidor:
  - POST /api/login  { "user":"...", "pass":"..." } -> { "token":"...", "expires":"2026-12-31" }
  - GET /api/movies?page=X&size=100 (Authorization: Bearer <token>) -> listado de items (id,title,poster,streams[])
  - GET /api/series?page=X&size=100
  - Ajusta los endpoints en ApiService.kt según tu servidor.
- Si quieres que implemente endpoints exactos para tu servidor (ej.: nombres de campos), pásame la respuesta JSON de login y lista de películas.

Licencia
- Tú decides. Este repo es el esqueleto inicial.
