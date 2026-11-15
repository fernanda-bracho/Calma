# CALMA+ PYMES (CalmaFinal)

App Android (Java) de bienestar emocional para colaboradores de PYMES y usuarios particulares:
respiración 4-7-8, sonidos relajantes, contacto con psicólogo, citas, contactos de emergencia y SOS.

El código está organizado según el entregable *Actividad 3: Modelo Orientado a Objetos y Diseño de Solución*.

## Arquitectura en capas

```
com.example.calmafinal/
├── domain/            Java puro. No importa android.* ni androidx.* (ni Room).
│   ├── model/         Usuario (abstracta), Empleado, UsuarioParticular, Empresa,
│   │                  ContactoEmergencia, Ubicacion, RegistroSintoma, Cita
│   ├── strategy/      EstrategiaCalculoCosto, CalculoCostoCorporativo, CalculoCostoParticular
│   └── service/       ServicioHashContrasena, ControladorRespiracion
├── data/              Persistencia con Room
│   ├── database/      BaseDatos (singleton)
│   ├── entities/      UsuarioEntity, ContactoEmergenciaEntity
│   ├── dao/           UsuarioDao, ContactoDao
│   └── mapper/        UsuarioMapper, ContactoMapper (entidad <-> dominio)
├── infrastructure/
│   └── audio/         FuenteAudio, ServicioAudioPixabay, ServicioAudioLocal,
│                      FuenteAudioConRespaldo, PistaAudio
└── ui/                Activities: solo presentación y navegación
```

Regla de dependencias: `ui → data / domain / infrastructure`, `data → domain`. El dominio no depende de nadie.

## Principios POO aplicados

- **Encapsulamiento:** atributos privados; el acceso es por métodos de negocio (`generarEnlaceMaps()`, `construirMensajePanico()`).
- **Herencia:** `Empleado` y `UsuarioParticular` extienden `Usuario`.
- **Polimorfismo / Strategy (RF04):** `Cita.getCostoFinal()` delega en la estrategia del usuario; no hay `instanceof` en la lógica de cobro.
- **Polimorfismo en audio (RF06):** `MusicaActivity` dependerá solo de `FuenteAudio`; `FuenteAudioConRespaldo` cae a `ServicioAudioLocal` si falla la red.
- **Composición:** `Usuario` crea y posee sus `ContactoEmergencia`; en SQL, clave foránea con `ON DELETE CASCADE`.
- **Agregación:** `Usuario` acumula `RegistroSintoma`.

## Cambios respecto al código anterior

- `loginActivity` y `homeActivity` pasaron a `LoginActivity` y `HomeActivity` (PascalCase) dentro de `ui/`.
- **Contraseñas con hash** (PBKDF2 + sal por usuario) vía `ServicioHashContrasena`. El login ya no consulta por contraseña: carga al usuario por correo y verifica el hash.
- `RespiracionActivity` ya no lleva la lógica de tiempos: la coordina `ControladorRespiracion` y la Activity solo anima el círculo.
- Base de datos **versión 2**. Cambió el esquema y se usa migración destructiva: **las cuentas creadas con la versión anterior se borran** (y no podrían iniciar sesión, porque guardaban la contraseña en texto plano).
- El registro detecta duplicados por **correo** (antes comparaba también el nombre).

## Decisiones que se apartan del documento

| Tema | Documento | Aquí | Motivo |
|---|---|---|---|
| Hashing | BCrypt/PBKDF2 | PBKDF2 | No requiere librerías externas. En Android 7.x (API 24-25) usa SHA-1 porque PBKDF2-SHA256 existe desde API 26; el algoritmo queda guardado en el hash. |
| `ui/` | 7 Activities | 10 | Se conservan `EmergencyActivity`, `AppointmentActivity` y `ContactosEmergenciaActivity`, que ya existían. |
| `data/` | Solo `UsuarioDao` / `UsuarioEntity` | + `ContactoDao`, `ContactoEmergenciaEntity`, mappers | Los contactos ya se guardaban en Room. |
| `ControladorRespiracion` | `CountDownTimer`, animaciones y `TextToSpeech` | Solo temporización | El dominio no puede usar el SDK de Android. Animación y voz van en la `ui`, escuchando al controlador. |
| `Usuario` | `tieneAccesoCorporativo()` | + `estrategiaCosto()` | Cada subclase elige su estrategia, y así `Cita` no pregunta por el tipo. |
| Audio | `FuenteAudio` + 2 servicios | + `FuenteAudioConRespaldo`, `PistaAudio` | Es el mecanismo que alterna online/offline sin tocar la vista. |

## Pruebas

Las pruebas del dominio y del audio son JUnit 4 puro (se ejecutan sin emulador):

```
./gradlew testDebugUnitTest
```

Cubren estrategia de costos (RF04), composición/agregación, validaciones, hashing, ubicación y mensaje de pánico, el controlador 4-7-8 y el respaldo de audio.

## Pendientes para seguir desarrollando

1. **Compilar en Android Studio** y correr `testDebugUnitTest` (esta refactorización se verificó compilando y probando el dominio por separado).
2. **SOS real:** conectar `EmergencyActivity` con `ContactosEmergenciaActivity.dispararAlertaSos`, pedir `ACCESS_FINE_LOCATION`, obtener una `Ubicacion` y pasarla a `construirMensajePanico()`. Hoy el botón solo muestra un Toast.
3. **Navegación a Contactos de emergencia:** no hay botón que abra la pantalla; debe recibir `HomeActivity.EXTRA_USUARIO_ID`.
4. **Empleado B2B:** agregar "código de empresa" al registro, `EmpresaEntity`/`EmpresaDao` y validar el convenio. Hoy todo registro nuevo es `UsuarioParticular`.
5. **Citas:** persistir `Cita` y mostrar el costo con `getCostoFinal()`. `AppointmentActivity` sigue siendo una maqueta.
6. **Seguimiento:** persistencia y pantalla de `RegistroSintoma`.
7. **Audio:** cliente real de `ServicioAudioPixabay` (confirmar que la API ofrezca audio, clave y permiso `INTERNET`), audios en `res/raw` para `ServicioAudioLocal`, y que `MusicaActivity` use `FuenteAudio` en vez de abrir el navegador.
8. **Seguridad:** `RecuperarActivity` permite cambiar la contraseña de cualquier correo registrado sin verificar identidad (falta código por correo o enlace). Revisar también `allowBackup="true"`.
9. Reemplazar `fallbackToDestructiveMigration` por migraciones de Room antes de publicar.
