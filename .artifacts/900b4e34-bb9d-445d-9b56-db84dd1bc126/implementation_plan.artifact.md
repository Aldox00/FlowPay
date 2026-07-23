# Plan de Corrección de Relaciones y Lógica en FlowPay

Este plan aborda la pérdida de "relaciones" (sincronización de datos con el backend) identificada en el flujo de ventas por transferencia y en la gestión de productos. También se corrigen errores tipográficos y se mejora la consistencia de la navegación.

## User Review Required

> [!IMPORTANT]
> Se ha detectado que las ventas realizadas mediante **Transferencia** nunca se registraban en la base de datos (MySQL), solo en la memoria local del teléfono. Esto causaba una desconexión entre lo que el usuario veía y lo que el backend almacenaba para la jornada.

> [!WARNING]
> Las ediciones y eliminaciones de productos en "Mis Productos" solo afectaban la lista local. Los cambios no se persistían en el servidor, lo que causaba inconsistencias al reiniciar la aplicación.

## Proposed Changes

### 1. Sincronización de Ventas por Transferencia

#### [MODIFY] [MainActivity.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
- Corregir el typo `Landi   ngScreen`.
- Actualizar la ruta `transfer_proof` para aceptar `productId`.
- Actualizar la navegación desde `select_payment` a `transfer_proof` para pasar el `productId`.
- Asegurar que `onProofValidated` maneje la respuesta tras el registro exitoso en la API (aunque el registro se moverá a la pantalla para mantener el patrón actual).

#### [MODIFY] [TransferProofScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/TransferProofScreen.kt)
- Agregar `productId` y `productPrice` como parámetros.
- Implementar la llamada a `RetrofitClient.apiService.registrarVenta` al validar el comprobante.
- Esto asegura que la venta se vincule correctamente con la `jornada_id` en el backend.

### 2. Gestión de Productos (Persistencia)

#### [MODIFY] [RetrofitClient.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/RetrofitClient.kt)
- Agregar endpoints para actualizar y eliminar productos (si el backend los soporta, de lo contrario se marcará como pendiente de API). *Nota: Investigaré si existen o si debo crearlos en el modelo.*

#### [MODIFY] [MyProductsScreen.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/screens/MyProductsScreen.kt)
- Implementar llamadas a la API para las acciones de edición y eliminación (actualmente solo locales).

### 3. Limpieza de Lógica y Hardcoding

#### [MODIFY] [MainActivity.kt](file:///C:/Users/yuans/OneDrive/Documentos/FlowPaay/app/src/main/java/com/example/flowpay/MainActivity.kt)
- Revisar los valores por defecto como `15` para `usuarioIdSesion` y asegurar que se use el ID real obtenido del login.

## Verification Plan

### Automated Tests
- No se cuenta con tests unitarios robustos para la capa de red en este momento, por lo que se realizará verificación manual y por logs.

### Manual Verification
1. **Flujo de Transferencia**: Realizar una venta por transferencia y verificar en los logs de Android Studio (`Log.d("FlowPayTest", ...)`) que la respuesta de `registrarVenta` sea exitosa.
2. **Edición de Productos**: Editar un producto y verificar que el cambio se envíe a la API.
3. **Navegación**: Verificar que no haya crashes al navegar a la pantalla de comprobante con el nuevo parámetro `productId`.
