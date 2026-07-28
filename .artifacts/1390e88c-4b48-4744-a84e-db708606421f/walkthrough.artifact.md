# Walkthrough - Restauración de Bloqueo por Encuesta

Se ha restaurado y mejorado la lógica de las encuestas para que el historial semanal y mensual se bloquee correctamente si el usuario no ha completado su encuesta de satisfacción.

## Cambios Realizados

### [RetrofitClient.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
- Se añadió el campo `encuesta_contestada` al modelo `JornadaRecordResponse`. Esto permite que la app sepa si una jornada pasada ya fue evaluada.

### [MainActivity.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
- Se actualizó la función `fetchUserHistoryFromBackend` para sincronizar los flags `hasSurveyedThisWeek` y `hasSurveyedThisMonth`. Si el servidor devuelve al menos una jornada cerrada con encuesta, el historial se desbloquea automáticamente.
- Se pasan estos estados a la pantalla de Historial.

### [HistoryScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/HistoryScreen.kt)
- **Integración de Bloqueo**: Se añadió un overlay de bloqueo (Lock Card) que aparece cuando el usuario selecciona "Semana" o "Mes" sin haber hecho la encuesta.
- **Efecto Visual**: El fondo del historial se difumina (blur) cuando está bloqueado, manteniendo la estética de la aplicación.
- **Flujo Corregido**: El botón "Ir a la encuesta" en el overlay es ahora el encargado de llevar al usuario al cuestionario.

## Verificación

### Pruebas realizadas:
1. **Sincronización**: Al iniciar sesión, la app detecta si ya contestaste encuestas anteriormente y desbloquea el historial si es necesario.
2. **Bloqueo Visual**: Al hacer clic en "Semana", se muestra el candado y el texto explicativo si no hay registro de encuesta.
3. **Navegación**: El flujo `Historial -> Semana -> Bloqueo -> Encuesta -> Desbloqueo` funciona correctamente sin perder la posición del usuario.

render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
render_diffs(file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/HistoryScreen.kt)
