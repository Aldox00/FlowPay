# Walkthrough - Sincronización de Encuestas

Se ha corregido el conflicto de campos entre el frontend y el backend en el flujo de encuestas. Ahora la aplicación envía exactamente lo que el servidor Node.js espera.

## Cambios realizados

### [RetrofitClient.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
Se actualizó el modelo `EncuestaRequest` para incluir los campos específicos del backend:
- `id_usuario`
- `pregunta_1`, `pregunta_2`, `pregunta_3`
- Se mantuvieron `puntuacion_app` y `comentarios` como opcionales para compatibilidad.

### [MainActivity.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
- Se actualizó el NavHost para pasar `usuarioIdSesion` y `jornadaIdSesion` a las pantallas correspondientes.
- Se ajustó la encuesta automática al cerrar jornada para enviar valores por defecto (5) en los nuevos campos.

### [CloseDayScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/CloseDayScreen.kt)
- La pantalla ahora recibe el `usuarioId`.
- Al guardar manualmente la jornada, la calificación del usuario se mapea a las 3 preguntas requeridas por el backend.

### [SurveyScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/SurveyScreen.kt)
- Se implementó la llamada real a la API usando `RetrofitClient.apiService.registrarEncuesta`.
- Se envían las calificaciones individuales de cada estrella (`ratingQ1`, `ratingQ2`, `ratingQ3`).

## Verificación

> [!TIP]
> Puedes verificar el envío exitoso en el **Logcat** de Android Studio filtrando por el tag `FlowPayTest`.

### Resultados esperados en Logs:
- ✅ `Encuesta registrada correctamente.` (Cierre automático)
- ✅ `Encuesta guardada con éxito en la BD` (Cierre manual)
- ✅ `Encuesta de historial enviada con éxito.` (Pantalla de encuesta)

render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/CloseDayScreen.kt)
render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/SurveyScreen.kt)
