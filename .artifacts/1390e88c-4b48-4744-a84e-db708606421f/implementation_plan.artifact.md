# Plan de Sincronización de Historial de Ventas

Este plan aborda el problema donde las ventas de la semana y el mes desaparecen al cerrar y volver a iniciar sesión. Implementaremos la descarga automática del historial desde el servidor MySQL para que la aplicación refleje siempre los datos reales del usuario.

## User Review Required

> [!IMPORTANT]
> Necesitamos confirmar la ruta exacta del backend para obtener el historial. He asumido que es `GET jornada/historial/{usuario_id}` siguiendo el patrón del proyecto. Si es diferente, por favor corrígeme.

> [!WARNING]
> Al descargar el historial, los datos locales actuales (si existen) serán reemplazados por los datos del servidor para asegurar la integridad de la información.

## Proposed Changes

### 1. API y Modelos de Red
#### [MODIFY] [RetrofitClient.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
- Definir `JornadaHistorialResponse` y `JornadaRecordResponse` para mapear los datos del backend.
- Agregar el método `obtenerHistorialJornadas(@Path("usuario_id") usuarioId: Int)` a la interfaz `FlowPayApiService`.

### 2. Lógica de Sincronización en MainActivity
#### [MODIFY] [MainActivity.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
- Implementar la función `fetchUserHistoryFromBackend(userId: Int)` que descarga las jornadas cerradas del servidor.
- Llamar a esta función dentro de la pantalla de `Login` cuando el inicio de sesión sea exitoso.
- Asegurar que los datos descargados se formateen correctamente para coincidir con el formato de fecha esperado en el historial (`dd 'de' MMMM`).

### 3. Mejora en la Visualización del Historial
#### [MODIFY] [HistoryScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/HistoryScreen.kt)
- Modificar la lógica de filtrado para que las vistas de "Semana" y "Mes" muestren los registros disponibles, incluso si son menos de 7 o 30 días, en lugar de mostrar una pantalla vacía.

## Verification Plan

### Manual Verification
1. **Login**: Iniciar sesión y verificar en los logs (`FlowPayTest`) que se está solicitando el historial.
2. **Historial**: Navegar a la pantalla de Historial y comprobar que aparezcan las ventas de días anteriores (si existen en la BD).
3. **Filtros**: Cambiar entre Día, Semana y Mes para verificar que los datos se filtren correctamente sin importar la cantidad de registros.
