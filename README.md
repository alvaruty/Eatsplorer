# Eatsplorer

<img src="https://github.com/alvaruty/Eatsplorer/assets/131477177/3e1e07f5-e099-493c-a5d7-a642e49c996e" alt="Eatsplorer Logo" width="200"/>

## Descripción

Eatsplorer es una aplicación móvil para Android diseñada para ayudar a los usuarios a buscar recetas de comida basadas en ingredientes específicos y permitirles añadir y guardar sus propias recetas. La aplicación utiliza la API de Edaman para recuperar recetas, asegurando una base de datos completa y dinámica. Además, permite la publicación de recetas personales para que otros usuarios las vean.

## Funcionalidades

- **Búsqueda de recetas**: Encuentra recetas introduciendo los ingredientes que tienes a mano.
- **Añadir recetas**: Permite a los usuarios crear, guardar, publicar y borrar sus propias recetas.
- **Gestión de usuarios**: Los usuarios pueden crear cuentas, iniciar sesión, cambiar la contraseña y eliminar sus cuentas.
- **Interfaz amigable**: Diseño intuitivo y minimalista para una fácil navegación.
- **Recetas recomendadas**: Sección con recetas sugeridas para los usuarios.

## Instalación

### Descargar APK

Puedes descargar la última versión de la APK de Eatsplorer desde el siguiente enlace:

[Descargar Eatsplorer APK](./eatsplorer.apk)

### Requisitos Previos

- [Android Studio](https://developer.android.com/studio)
- [Firebase Account](https://firebase.google.com/)
- [Edaman API Key](https://developer.edamam.com/)

### Pasos

1. **Clonar el repositorio**:
    ```bash
    git clone https://github.com/tuusuario/eatsplorer.git
    ```

2. **Abrir el proyecto en Android Studio**:
   - Abrir Android Studio.
   - Seleccionar "Open an existing Android Studio project".
   - Navegar al directorio donde se clonó el repositorio y seleccionarlo.

3. **Configurar Firebase**:
   - Crear un proyecto en Firebase y configurar la autenticación y Firestore.
   - Descargar el archivo `google-services.json` y colocarlo en la carpeta `app` del proyecto.

4. **Configurar la API de Edaman**:
   - Regístrate en [Edaman](https://developer.edamam.com/) y obtén tus claves API.
   - Añade las claves API en el archivo `local.properties`:
     ```
     edaman_app_id=YOUR_APP_ID
     edaman_app_key=YOUR_APP_KEY
     ```

5. **Ejecutar la aplicación**:
   - Conecta un dispositivo Android o utiliza un emulador.
   - Haz clic en el botón "Run" en Android Studio.

## Uso

### Pantallas de la Aplicación

1. **Pantalla de Registro/Inicio de sesión**:
   - **Funcionalidad**: Permite a los usuarios registrarse y acceder a la aplicación usando Firebase Authentication.
   - **Descripción**: Los métodos de registro e inicio de sesión son llamados desde las propias pantallas de la aplicación.

2. **Pantalla Principal**:
   - **Funcionalidad**: Es la pantalla de inicio que permite la búsqueda de recetas por ingredientes.
   - **Descripción**: Contiene un buscador de recetas, botones para filtros rápidos por ingredientes comunes y una sección de recetas recomendadas. Utiliza Retrofit para realizar consultas a la API de Edaman.

3. **Pantalla de Añadir Receta**:
   - **Funcionalidad**: Permite a los usuarios crear, guardar, publicar y borrar sus propias recetas.
   - **Descripción**: Incluye un formulario para introducir el nombre de la receta, ingredientes, instrucciones y fotos. Las recetas pueden ser almacenadas localmente y/o publicadas para que otros usuarios las vean.

4. **Pantalla de Perfil de Usuario**:
   - **Funcionalidad**: Permite a los usuarios gestionar su cuenta.
   - **Descripción**: Incluye opciones para cambiar la contraseña, cerrar sesión y eliminar la cuenta.

## Tecnologías Utilizadas

- **Lenguaje de Programación**: Kotlin
- **Framework de UI**: Jetpack Compose
- **Base de Datos**: Firebase Firestore
- **Autenticación**: Firebase Authentication
- **API de Recetas**: Edaman
- **HTTP Client**: Retrofit
