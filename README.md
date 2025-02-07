# **WebChecker - Monitoreo de Páginas Web**

**WebChecker** es una aplicación móvil diseñada para realizar un monitoreo periódico de páginas web, buscando una palabra clave específica dentro de ellas. La aplicación permite a los usuarios configurar dinámicamente una URL y una palabra clave, y luego realiza las búsquedas en segundo plano a intervalos regulares de 15 minutos utilizando **WorkManager**.

## **Características Principales:**

- **Monitoreo Periódico**: Realiza búsquedas automáticas de una palabra clave dentro de una URL a intervalos de 15 minutos.
- **Configuración Dinámica**: Los usuarios pueden configurar la URL y la palabra clave a través de una interfaz sencilla.
- **Trabajo en Segundo Plano**: Utiliza **WorkManager** para ejecutar las búsquedas en segundo plano, sin interrumpir el uso de la aplicación.
- **Interfaz Intuitiva**: Ofrece una interfaz simple con botones para iniciar y detener el proceso de búsqueda.
- **Persistencia de Configuración**: Los datos de la URL y la palabra clave se guardan de manera persistente utilizando **SharedPreferences**.
- **Manejo de Permisos**: La aplicación gestiona los permisos necesarios para notificaciones, garantizando una experiencia fluida y eficiente en segundo plano.
- **Verificación de Campos Vacíos**: Asegura que los campos de URL y palabra clave no estén vacíos antes de permitir la ejecución del monitoreo.
- **Activación/Desactivación de Botones**: Los botones de inicio y detención se habilitan o deshabilitan según el estado de la aplicación, evitando acciones innecesarias.

## **Mejoras Recientes:**

- **Diseño Mejorado**: La interfaz ha sido optimizada para una mejor experiencia de usuario.
- **Comprobación de Campos Vacíos**: Ahora, la aplicación valida que los campos de URL y palabra clave no estén vacíos antes de permitir su uso.
- **Control Dinámico de Botones**: Los botones de encendido y apagado se activan o desactivan según el funcionamiento actual de la aplicación, evitando inconsistencias.

## **Tecnologías Utilizadas:**

- **Android (Kotlin)**
- **WorkManager**
- **SharedPreferences**
- **Gestión de Permisos para Notificaciones**

## **Instrucciones de Uso:**

1. **Instalación**: Descarga e instala la aplicación desde la tienda de aplicaciones de tu dispositivo.
2. **Configuración Inicial**: Ingresa la URL y la palabra clave en los campos correspondientes.
3. **Inicio de Búsqueda**: Presiona el botón **Iniciar** para comenzar el monitoreo de la página web.
4. **Detener Búsqueda**: Presiona el botón **Detener** para finalizar la búsqueda.
5. **Notificaciones**: Las notificaciones aparecerán cada vez que se detecte la palabra clave en la URL configurada.

